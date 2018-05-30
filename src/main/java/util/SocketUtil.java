package com.jhui.util;


import java.util.List;
public class SocketUtil {
	 public static byte[] sysCopy(List<byte[]> srcArrays) {
	  int len = 0;
	  for (byte[] srcArray:srcArrays) {
	   len+= srcArray.length;
	  }
	     byte[] destArray = new byte[len];   
	     int destLen = 0;
	     for (byte[] srcArray:srcArrays) {
	         System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);   
	         destLen += srcArray.length;   
	     }   
	     return destArray;
	 }  

	 public static byte[] getSubByte(byte[] byteArray, int startIndex,int endIndex) {
			byte[] newArray = new byte[endIndex - startIndex + 1];
			for (int index = 0; index < (endIndex - startIndex); index++) {
				newArray[index] = byteArray[startIndex + index];
			}
			return newArray;
		}

	 public static byte[] intToByte(int i) {
	        byte[] abyte0 = new byte[4];
	        abyte0[0] = (byte) (0xff & i);
	        abyte0[1] = (byte) ((0xff00 & i) >> 8);
	        abyte0[2] = (byte) ((0xff0000 & i) >> 16);
	        abyte0[3] = (byte) ((0xff000000 & i) >> 24);
	        return abyte0;
	    }

	 public static int byteToInt(byte[] bytes) {
	        int addr = bytes[0] & 0xFF;
	        addr |= ((bytes[1] << 8) & 0xFF00);
	        addr |= ((bytes[2] << 16) & 0xFF0000);
	        addr |= ((bytes[3] << 24) & 0xFF000000);
	        return addr;
	    }

	 
}
