package communication.client;


import util.SocketUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class SocketClientBody {
	private Socket client;
	//报头标识字段
	public static Integer RequestID=88;//长度为4字节
	public static Integer SequenceID=99;
	public static Integer PacketLength=0;
	
	public SocketClientBody() throws UnknownHostException, IOException {
		client = new Socket("127.0.0.1", 7777);
		BufferedOutputStream bout=new BufferedOutputStream(client.getOutputStream()); 

		//将报体字符串转为字节流发送                   
		String message="hello java";
		byte[] messages = message.getBytes("utf-8");   
		
		PacketLength=4+4+4+messages.length;
		//字段转换
		byte[] bRequestID=SocketUtil.intToByte(RequestID);
		byte[] bSequenceID=SocketUtil.intToByte(SequenceID);
		byte[] bPacketLength=SocketUtil.intToByte(PacketLength);
		System.out.println("data length:"+PacketLength);
		//合并字节数组
		List<byte[]> header = new ArrayList<byte[]>();
		header.add(bPacketLength);
		header.add(bRequestID);
		header.add(bSequenceID);
		byte[] headerByte=SocketUtil.sysCopy(header);
		List<byte[]> body = new ArrayList<byte[]>();
		body.add(headerByte);
		body.add(messages);
		byte[] bytes=SocketUtil.sysCopy(body);
		
		System.out.println("client read start");
		bout.write(bytes);      
		bout.flush();    
		bout.close();
		client.close();
		System.out.println("client ebd read");
	}

	public static void main(String[] args) {
		try {
			new SocketClientBody();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
