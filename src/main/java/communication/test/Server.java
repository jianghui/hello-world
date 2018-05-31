package communication.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	static int port = 8091;
	public static void main(String[] args) throws Exception {
		manyThread();
	}
	
	
	public static void manyThread()throws Exception{
		ServerSocket serverSocket = new ServerSocket(port);
		while (true) {
			final Socket socket = serverSocket.accept();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						InputStream in = socket.getInputStream();
						byte[] buff = new byte[12];
						in.read(buff);
						System.out.println(new String(buff));
						OutputStream os = socket.getOutputStream();
						os.write(buff);
						in.close();
						os.close();
					} catch (Exception e) {
					}finally {
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}).start();
		}
	}

}
