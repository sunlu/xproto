package com.gavin;

public class ProtoUtils {
	static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String hex(byte[] bytes) {

		char[] result = new char[bytes.length * 2];

		for (int i = 0; i < bytes.length; i++) {
			result[i * 2] = HEX_DIGITS[(bytes[i] >> 4) & 0xf];
			result[i * 2 + 1] = HEX_DIGITS[bytes[i] & 0xf];
		}
		return new String(result);
	}

	public static ProtoBuffer decodeHex(String hex) {
		if (hex == null)
			throw new IllegalArgumentException("hex == null");
		if (hex.length() % 2 != 0)
			throw new IllegalArgumentException("Unexpected hex string: " + hex);

		byte[] result = new byte[hex.length() / 2];
		for (int i = 0; i < result.length; i++) {
			int d1 = decodeHexDigit(hex.charAt(i * 2)) << 4;
			int d2 = decodeHexDigit(hex.charAt(i * 2 + 1));
			result[i] = (byte) (d1 + d2);
		}
		return new ProtoBuffer(result);
	}

	public static int decodeHexDigit(char c) {
		if (c >= '0' && c <= '9')
			return c - '0';
		if (c >= 'a' && c <= 'f')
			return c - 'a' + 10;
		if (c >= 'A' && c <= 'F')
			return c - 'A' + 10;
		throw new IllegalArgumentException("Unexpected hex digit: " + c);
	}
	
	public static void dump(ProtoStream stream) {
		dump(stream,0);
	}

	public static void dump(ProtoStream stream, int level) {
		System.out.println();
		short fieldCount = stream.readFixedShort(); 
		
		while (fieldCount-- > 0) {
			dumpField(stream, level);
		}
	}

	private static void dumpField(ProtoStream stream, int level) {
		int tagAndType = stream.readInt();
		int tag = (tagAndType >> ProtoType.TAG_TYPE_BITS);
		ProtoType type = ProtoType.valueOf(tagAndType);
		
		for(int i=0; i < level; i++){
			System.out.print("\t");
		}

		System.out.print("T:"+tag +", \tTYPE:" + ProtoType.getName(type) + " = ");
		
		switch (type) {
		case VARINT: {
			System.out.print(stream.readInt());
			break;
		}
		case VARLONG:{
			System.out.print(stream.readLong());
			break;
		}
		case STRING: {
			System.out.print(stream.readString());
			break;
		}
		case OBJECT: {
			
			dump(stream,level+1);
			break;
		}
		
		case VARINTLIST: {
			int count = stream.readInt();
			System.out.print(" COUNT:" + count + "[");
			
			for(int i=0; i < count; i++){
				System.out.print(stream.readInt());
				if(i < count - 1)
					System.out.print(",");
			}
			
			System.out.print("]");
			
			break;
		}
		case VARLONGLIST: {
			int count = stream.readInt();
			System.out.print(" COUNT:" + count + "[");
			
			for(int i=0; i < count; i++){
				System.out.print(stream.readLong());
				if(i < count - 1)
					System.out.print(",");
			}
			
			System.out.print("]");
			
			break;
		}
		case STRINGLIST: {
			int count = stream.readInt();
			System.out.print(" COUNT:" + count + "[");
			
			for(int i=0; i < count; i++){
				System.out.print(stream.readString());
				if(i < count - 1)
					System.out.print(",");
			}
			
			System.out.print("]");
			
			break;
		}
		case OBJECTLIST: {
			int count = stream.readInt();
			System.out.print(" COUNT:" + count + "[");
			
			for(int i=0; i < count; i++){
				dump(stream,level+1);
			}
			
			System.out.print("\t]");
			
			break;
		} 
		default:
			break;
		}
		System.out.println();
	}
}
