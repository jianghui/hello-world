package communication.client;

import util.SocketUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class SocketServerBody {

	private Socket socket;
	private ServerSocket ss;
	
	public  void setServer() throws Exception {
			ss = new ServerSocket(7777);
			System.out.println("server start");
			
			socket = ss.accept();
			System.out.println("accept:"+socket.getRemoteSocketAddress());
			
			byte[] bytes=readXmlBytesFromSocket(socket);

			byte[] bHeadr1 = SocketUtil.getSubByte(bytes, 0, 3);
			System.out.println("header 1:"+SocketUtil.byteToInt(bHeadr1));
			byte[] bHeadr2=SocketUtil.getSubByte(bytes, 4, 7);
			System.out.println("header 2:"+SocketUtil.byteToInt(bHeadr2));

			byte[] body=SocketUtil.getSubByte(bytes, 8, bytes.length);
			System.out.println("body:"+new String(body,"utf-8"));
		
	}
	 private byte[] readXmlBytesFromSocket(Socket socket) throws Exception{
		 InputStream bis = socket.getInputStream();
		 System.out.println("server read byte data");
		 byte[] lengthBytes = new byte[4];
		 if (bis.read(lengthBytes, 0, 4) == 4) {
			 int length = SocketUtil.byteToInt(lengthBytes);
			 int rest = length;
			 byte[] messageBytes = new byte[length-4];
			 int read = 0;
			 //while (rest > 0) {
				 byte[] buffer = new byte[rest];
				 read = bis.read(buffer);
				 if(read > length || rest < 0){
					 throw new Exception("exception 1");
				 }
				 else {
					 System.arraycopy(buffer, 0, messageBytes, 0, read);
					 //rest -= read;
				 }
			// }
			 
			 return messageBytes;
    	} else {
        throw new Exception("exception 2");
    	}
	 }

	public static void main(String[] args) throws Exception {
		try {
			SocketServerBody s=new SocketServerBody();
			s.setServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}}
