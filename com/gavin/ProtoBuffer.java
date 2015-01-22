package com.gavin;

/*
 * 协议缓冲池
 */
public class ProtoBuffer {
	static final int SIZE = 256;
	protected byte[] data;
	protected int size;
	protected int position;

	public ProtoBuffer(byte[] bytes) {
		position = 0;
		size = bytes.length;
		data = bytes;
	}

	public ProtoBuffer() {
		position = 0;
		size = SIZE;
		data = new byte[SIZE];
	}
  
	public int position() {
		return position;
	}
	
	public void position(int pos) {
		 position = pos;
	}
	
	public int available(){
		return size - position;
	}

	private void expand(int sz) {
		if (available() < sz) {
			sz = (int) Math.ceil((double) sz / SIZE) * SIZE;
			size += sz;
			byte[] temp = new byte[size];
			System.arraycopy(data, 0, temp, 0, position);
			data = temp;
		}
	}

	public void put(byte b) {
		expand(1);
		data[position] = b;
		position++;
	}
	public void put(byte b,int pos) { 
		data[pos] = b; 
	}

	public void put(byte[] bytes) {
		if (bytes == null || bytes.length == 0)
			return;

		expand(bytes.length);
		System.arraycopy(bytes, 0, data, position, bytes.length);
		position += bytes.length;
	}
	
	public void put(byte[] bytes,int pos){
		System.arraycopy(bytes, 0, data, pos, bytes.length);
	}
	
	public byte get() {
		return get(position++);
	}
	
	public byte get(int pos){
		if (pos >= data.length)
			throw new IllegalArgumentException("Buffer is null");

		return data[pos];
	}
	
	public byte[] toByteArray() {
		byte[] temp = new byte[position];
		System.arraycopy(data, 0, temp, 0, temp.length);
		return temp;
	}

	public String hex(){
		return ProtoUtils.hex(this.toByteArray());
	}
	
}
