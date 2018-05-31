package communication.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChannelSocketServer {
	private int port = 8000;
	private ServerSocketChannel serverSocketChannel = null;
	private ExecutorService executorService;
	private final static int POOL_MULTIPLE = 4;
	
	public ChannelSocketServer()throws Exception{
		executorService= Executors.newFixedThreadPool(
			    Runtime.getRuntime().availableProcessors() * POOL_MULTIPLE);
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		System.out.println("服务器启动！");
	}
	
	public void service(){
		while(true){
			try {
				SocketChannel socketChannel = serverSocketChannel.accept();
				executorService.execute(new Handler(socketChannel));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	class Handler implements Runnable{
		private SocketChannel socketChannel;
		public Handler(SocketChannel socketChannel){
			this.socketChannel = socketChannel;
		}
		@Override
		public void run() {
			try {
				Socket socket = socketChannel.socket();
				BufferedReader br = getReader(socket);
				PrintWriter pw = getWrite(socket);
				String msg = null;
				while((msg = br.readLine())!= null){
					System.out.println("客户端发过来的消息：" + msg);
					pw.println("同样返回给你：" + msg);
					if(msg.equals("bye")){
						break;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}
	
	public PrintWriter getWrite(Socket socket)throws Exception{
		return new PrintWriter(socket.getOutputStream(),true);
	}	
	
	public BufferedReader getReader(Socket socket)throws Exception{
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	

	public static void main(String[] args) throws Exception {
		new ChannelSocketServer().service();

	}

}
