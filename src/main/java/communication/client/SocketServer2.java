package communication.client;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer2 {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(9912);
			System.out.println("server start");
			int count = 0;
			while(true){
				final Socket socket =server.accept();
				count++;
				System.out.println("accept count:"+count);
				new Thread(new Runnable() {
					public void run() {
						try {
							OutputStream os = socket.getOutputStream();
							InputStream in = socket.getInputStream();
							
							int len = in.available();
							System.out.println("len:" + len);
							byte[] buff = new byte[len];
							while ((in.read(buff)) != len) {
								System.out.println(new String(buff));
							}
							System.out.println(new String(buff));
							os.write("hello".getBytes());
							os.flush();
							os.write("word".getBytes());
							os.flush();
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
