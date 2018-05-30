package communication.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(9912);
			System.out.println("server start");
			int count = 0;
			while(true){
				final Socket socket =server.accept();
				count++;
				System.out.println("server accept num:"+count);
				new Thread(new Runnable() {
					public void run() {
						try {
							System.out.println("thread:" + Thread.currentThread().getName());
							InputStream in1 = socket.getInputStream();
							BufferedReader  br = new BufferedReader(new InputStreamReader(in1));
							String line = br.readLine();
							System.out.println("server jieshou data1:"+line);
							PrintWriter  pw = new PrintWriter(socket.getOutputStream(),true);
							String s = line + ",world";
							pw.println(s);

							InputStream in2 = socket.getInputStream();
							System.out.println(in1 == in2);
							BufferedReader  br2 = new BufferedReader(new InputStreamReader(in2));
							String line2 = br2.readLine();
							System.out.println("server jieshou data2:"+line2);
							PrintWriter  pw2 = new PrintWriter(socket.getOutputStream(),true);
							String s2 = line2 + ",world";
							pw2.println(s2);
//							br.close();
//							pw.close();
//							socket.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
