package com.jhui.communication.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws Exception {
		monitor();
	}
	
	public static void monitor(){
		try {
			Socket socket = new Socket("localhost",8001);
			OutputStream os = socket.getOutputStream();
			String text = "shutdown\r\n";
			os.write(text.getBytes());
			InputStream in = socket.getInputStream();
			byte[] buff = new byte[21];
			int count = 0;
			while ((count = in.read(buff)) != -1) {
				System.out.println(new String(buff,0,count));
			}
			os.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void testSend(){
		try {
			Socket socket = new Socket("localhost",8000);
			OutputStream os = socket.getOutputStream();
            String text = "你好啊";
			os.write(text.getBytes());
			InputStream in = socket.getInputStream();
			byte[] buff = new byte[21];
			in.read(buff);
			System.out.println(new String(buff));
			os.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void multithread(){
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Socket socket = new Socket("localhost",8001);
						OutputStream os = socket.getOutputStream();
						String text = "shutdown\r\n";
						os.write(text.getBytes());
						InputStream in = socket.getInputStream();
						byte[] buff = new byte[12];
						in.read(buff);
						System.out.println(new String(buff));
						os.close();
						in.close();
						socket.close();
					} catch (Exception e) {
					}
					
				}
			}).start();
		}
	}

}
