package com.gavin;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ProtoStream extends ProtoBuffer {

	static final Charset UTF8 = Charset.forName("utf-8");

	public ProtoStream(byte[] bytes) {
		super(bytes);
	}

	public ProtoStream() {
		super();
	}

	public void writeFixedShort(short value, int pos) {
		put((byte) (value & 0xff), pos++);
		put((byte) ((value >> 8) & 0xff), pos);
	}

	public short readFixedShort() {
		short value = (short) (get() & 0xff);
		value += (get() & 0xff) << 8;
		return value;
	}

	private void writeVarInt32(int value) {
		while ((value & 0xFFFFFF80) != 0L) {
			put((byte) (value & 0x7F | 0x80));
			value >>>= 7;
		}
		put((byte) (value & 0x7F));
	}

	private void writeVarInt64(long value) {
		while ((value & 0xFFFFFFFFFFFFFF80L) != 0L) {
			put((byte) (value & 0x7F | 0x80));
			value >>>= 7;
		}
		put((byte) (value & 0x7F));
	}

	private void writeString(String val) {
		if (val == null || val.equals("")) {
			writeVarInt32(0);
			return;
		}
		byte[] bytes = val.getBytes(UTF8);
		writeVarInt32(bytes.length);
		put(bytes);
	}

	public int readInt() {
		int value = 0;
		int i = 0;
		int b;
		while (((b = get()) & 0x80) != 0) {
			value |= (b & 0x7F) << i;
			i += 7;
			if (i > 35) {
				throw new IllegalArgumentException(
						"Variable length quantity is too long");
			}
		}
		return value | (b << i);
	}

	public Long readLong() {
		long value = 0L;
		int i = 0;
		long b;
		while (((b = get()) & 0x80) != 0) {
			value |= (b & 0x7F) << i;
			i += 7;
			if (i > 63) {
				throw new IllegalArgumentException(
						"Variable length quantity is too long");
			}
		}
		return value | (b << i);
	}

	public String readString() {
		int len = readInt();

		if (len > 0) {
			byte[] temp = new byte[len];
			System.arraycopy(data, position, temp, 0, temp.length);
			position += temp.length;
			return new String(temp, UTF8);
		}
		return null;
	}

	public List<Integer> readIntList() {
		int count = readInt();
		List<Integer> list = new ArrayList<>(count);

		for (int i = 0; i < count; i++) {
			list.add(readInt());
		}

		return list;
	}

	public List<Long> readLongList() {
		int count = readInt();
		List<Long> list = new ArrayList<>(count);

		for (int i = 0; i < count; i++) {
			list.add(readLong());
		}

		return list;
	}

	public List<String> readStringList() {
		int count = readInt();
		List<String> list = new ArrayList<>(count);

		for (int i = 0; i < count; i++) {
			list.add(readString());
		}

		return list;
	}

	public <T extends Message> T readObject(Class<T> clazz) {
		T item = null;
		try {
			item = clazz.newInstance();
			item.decode(this);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return item;
	}

	public <T extends Message> List<T> readObjectList(Class<T> clazz) {
		int size = readInt();
		if (size <= 0)
			return null;

		List<T> list = new ArrayList<T>(size);

		for (int i = 0; i < size; i++) {

			list.add(readObject(clazz));
		}

		return list;
	}

	public int write(int tag, int val) {
		if (val != 0) {

			tag = ((tag << ProtoType.TAG_TYPE_BITS) + ProtoType.VARINT.value());

			System.out.println("wirte int tag2=" + tag);

			writeVarInt32(tag);
			writeVarInt32(val);
			return 1;
		}
		return 0;
	}

	public int write(int tag, String val) {
		if (val != null && !val.equals("")) {

			tag = ((tag << ProtoType.TAG_TYPE_BITS) + ProtoType.STRING.value());

			writeVarInt32(tag);
			writeString(val);
			return 1;
		}
		return 0;
	}

	public int write(int tag, Long val) {
		if (val != 0) {
			tag = ((tag << ProtoType.TAG_TYPE_BITS) + ProtoType.VARINT.value());

			writeVarInt32(tag);
			writeVarInt64(val);
			return 1;
		}
		return 0;
	}

	public <T extends Message> int write(int tag, T val) {
		if (val != null) {

			tag = ((tag << ProtoType.TAG_TYPE_BITS) + ProtoType.OBJECT.value());

			writeVarInt32(tag);
			val.encode(this);
			return 1;
		}
		return 0;
	}

	public int writeLongList(int tag, List<Long> list) {
		if (list != null && list.size() > 0) {

			tag = ((tag << ProtoType.TAG_TYPE_BITS) + ProtoType.VARINTLIST
					.value());

			writeVarInt32(tag);
			writeVarInt32(list.size());

			for (Long i : list) {
				writeVarInt64(i);
			}
			return 1;
		}
		return 0;
	}

	public int writeIntList(int tag, List<Integer> list) {
		if (list != null && list.size() > 0) {
			tag = ((tag << ProtoType.TAG_TYPE_BITS) + ProtoType.VARINTLIST
					.value());

			writeVarInt32(tag);
			writeVarInt32(list.size());

			for (int i : list) {
				writeVarInt32(i);
			}
			return 1;
		}
		return 0;
	}

	public int writeStringList(int tag, List<String> list) {
		if (list != null && list.size() > 0) {

			tag = ((tag << ProtoType.TAG_TYPE_BITS) + ProtoType.STRINGLIST
					.value());

			writeVarInt32(tag);
			writeVarInt32(list.size());

			for (String i : list) {
				writeString(i);
			}
			return 1;
		}
		return 0;
	}

	public <T extends Message> int writeObjectList(int tag, List<T> list) {

		if (list != null && list.size() > 0) {

			tag = ((tag << ProtoType.TAG_TYPE_BITS) + ProtoType.OBJECTLIST
					.value());

			writeVarInt32(tag);
			writeVarInt32(list.size());

			for (T i : list) {
				i.encode(this);
			}
			return 1;
		}
		return 0;
	}

	public void readUnknow(ProtoType type) {

		switch (type) {

		case VARINT: {
			readInt();
			break;
		}
		case VARLONG: {
			readLong();
			break;
		}
		case STRING: {
			readString();
			break;
		}
		case VARINTLIST: {
			readIntList();
			break;
		}
		case VARLONGLIST: {
			readLongList();
			break;
		}
		case STRINGLIST: {
			readStringList();
			break;
		}
		case OBJECT: {
			readUnknowObject();
			break;
		}
		case OBJECTLIST: {
			readUnknowObjectList();
			break;
		}
		default:

			break;
		}
	}

	private void readUnknowObject() {
		int fields = get() & 0xff;
		fields += (get() & 0xff) << 8;

		int tag = 0;
		while (fields-- > 0) {
			tag = readInt();
			readUnknow(ProtoType.valueOf(tag));
		}
	}

	private void readUnknowObjectList() {
		int count = readInt();

		for (int i = 0; i < count; i++) {
			readUnknowObject();
		}
	}
}
