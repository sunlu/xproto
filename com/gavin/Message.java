package com.gavin;

/*
 * 消息基类
 */
public abstract class Message {

	/*
	 * 消息头，用于存储消息字段数，2 byte
	 */
	static final byte[] HEADER = new byte[] { 0, 0 };

	/*
	 * 消息中字段数
	 */
	protected short fieldCount = 0;

	/*
	 * 消息在字节组中位置
	 */
	private int startPos = 0;

	/*
	 * 装包
	 */
	public void encode(ProtoStream stream) {
		startPos = stream.position();
		stream.put(HEADER);
	}

	/*
	 * 解包
	 */
	public void decode(ProtoStream stream) {
		fieldCount = stream.readFixedShort();
	}

	/*
	 * 完成消息装包
	 */
	protected void close(ProtoStream stream) {
		if (fieldCount > 0) {
			stream.writeFixedShort(fieldCount, startPos);
		}
	}
}
