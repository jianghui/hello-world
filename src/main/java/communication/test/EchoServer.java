package communication.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EchoServer {
	private int port = 8000;
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private final int POOL_SIZE = 4;
	private final int portForShutdown = 8001;
	private ServerSocket serverSocketForShutdown;
	private boolean isShutdown = false;
	private Thread shutdownThread = new Thread(){
		public void start() {
			this.isDaemon();
			super.start();
		};
		public void run(){
			while(!isShutdown){
				Socket socketShutdown = null;
				try {
					socketShutdown = serverSocketForShutdown.accept();
					BufferedReader buffReader = new BufferedReader(new InputStreamReader(socketShutdown.getInputStream()));
					String commond = buffReader.readLine();
					if(commond.equals("shutdown")){
						socketShutdown.getOutputStream().write("服务器正在关闭".getBytes());
						isShutdown = true;
						executorService.shutdown();
						while (!executorService.isTerminated()) {
							executorService.awaitTermination(30, TimeUnit.SECONDS);
						}
						serverSocket.close();
						socketShutdown.getOutputStream().write("服务器已经关闭".getBytes());
						socketShutdown.close();
						serverSocketForShutdown.close();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
	};
	
	public EchoServer()throws Exception{
		serverSocket = new ServerSocket(port);
		serverSocketForShutdown = new ServerSocket(portForShutdown);
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
		shutdownThread.start();
		System.out.println("服务器开始启动");
	}
	
	public void service(){
		while (!isShutdown) {
			try {
				Socket socket = serverSocket.accept();
				executorService.execute(new Handler(socket));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}

	public static void main(String[] args) throws Exception {
		new EchoServer().service();

	}
	
	class Handler implements Runnable{
		private Socket socket;
		public Handler(Socket socket){
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				InputStream in = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				byte[] buff = new byte[9];
				in.read(buff);
				System.out.println("客户端发过来的消息："+new String(buff));
				os.write("我收到你的消息".getBytes());
				in.close();
				os.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
	}

}
