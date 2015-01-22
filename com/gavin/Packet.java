package com.gavin;

/**
 * 数据包压缩解压
 * 
 * @author Gavin
 *
 */
public class Packet {
	 

	/**
	 * 压缩
	 * 
	 * @param data
	 */
	public static byte[] packed(byte[] data) {
		
		if(data.length % 8 != 0){
			int size = data.length + 8 - data.length % 8;
			byte[] temp = new byte[size];
			System.arraycopy(data, 0, temp, 0, data.length);
			data = temp;
		} 
		
		ProtoBuffer read = new ProtoBuffer(data);
		ProtoBuffer write = new ProtoBuffer();
		  
		
		byte mask = 0;
		int pos = 0;
		byte b = 0;

		for (int j = 0; j < data.length/8; j ++) {
			mask = 0;
			pos = write.position();
			write.put(mask);
			
			for (byte i = 7; i >= 0; i--) {

				b = read.get();

				if (b != 0) {
					mask |= (1 << i);
					write.put(b);
				}
			}
			write.put(mask, pos);
		}
		
		System.out.println("packed write="+write.hex());

		return write.toByteArray();
	}

	/**
	 * 解压
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] unpacked(byte[] data) {
		ProtoBuffer read = new ProtoBuffer(data);
		ProtoBuffer write = new ProtoBuffer();

		byte mask = 0;
		while (read.available() > 0) {
			mask = read.get();

			for (byte i = 7; i >= 0; i--) {
				if ((mask >> i & 1) == 1) {
					write.put(read.get());
				} else {
					write.put((byte) 0);
				}
			}
		} 

		// 去除最后几位的0
		
		System.out.println("unpacked write="+write.hex());

		return write.toByteArray();
	}

}
