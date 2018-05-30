package communication.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient2 {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 9912);
			OutputStream os = socket.getOutputStream();
			InputStream in = socket.getInputStream();
			os.write("hello".getBytes());
			os.flush();
			os.write("word".getBytes());
			os.flush();

			int len = in.available();
			byte[] buff = new byte[len];
			while ((in.read(buff)) != len) {
				System.out.println(new String(buff));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
