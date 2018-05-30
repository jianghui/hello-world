package communication.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 9912);
			OutputStream o1 = socket.getOutputStream();
			System.out.println(o1);
			PrintWriter pw = new PrintWriter(o1,true);
			BufferedReader  br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw.println("hello");
			String line = br.readLine();
			System.out.println("client jieshou1:"+line);

			OutputStream o2 = socket.getOutputStream();
			System.out.println(o2);
			System.out.println(o2==o1);
			PrintWriter pw2 = new PrintWriter(o2,true);
			BufferedReader  br2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw2.println("hello2");
			String line2 = br2.readLine();
			System.out.println("client jieshou2:"+line2);

			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
