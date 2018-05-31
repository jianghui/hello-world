package communication.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class ChannelSocketClient {
	private SocketChannel socketChannel;

	public ChannelSocketClient() throws Exception {
		socketChannel = SocketChannel.open();
		InetAddress address = InetAddress.getLocalHost();
		InetSocketAddress isa = new InetSocketAddress(address, 8000);
		socketChannel.connect(isa);
		System.out.println("与服务器建立连接成功");
	}
	
	public void tall()throws Exception{
		BufferedReader br = getReader(socketChannel.socket());
		PrintWriter pw = getWriter(socketChannel.socket());
		BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
		String msg = null;
		while((msg = localReader.readLine()) != null){
			pw.println(msg);
			System.out.println(br.readLine());
			if(msg.equals("bye")){
				break;
			}
		}
	}

	public static void main(String[] args)throws Exception {
		new ChannelSocketClient().tall();

	}

	private PrintWriter getWriter(Socket socket) throws IOException {
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
	}

	private BufferedReader getReader(Socket socket) throws IOException {
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}

}
