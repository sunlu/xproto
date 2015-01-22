package com.gavin;

/*
 * 数据类型
 */
public enum ProtoType {
	VARINT(0),
	VARLONG(1),
	STRING(2), 
	OBJECT(3), 
	VARINTLIST(4),
	VARLONGLIST(5),
	STRINGLIST(6), 
	OBJECTLIST(7);

	private static final int TAG_TYPE_MASK = 0x7;
	public static final int TAG_TYPE_BITS = 3;

	private final int value;

	private ProtoType(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public static ProtoType valueOf(int tagAndType) {
		switch (tagAndType & TAG_TYPE_MASK) {
		case 0:
			return VARINT;
		case 1:
			return VARLONG;
		case 2:
			return STRING;
		case 3:
			return OBJECT;
		case 4:
			return VARINTLIST;
		case 5:
			return VARLONGLIST;
		case 6:
			return STRINGLIST;
		case 7:
			return OBJECTLIST;
		default:
			return VARINT;
		}
	}

	public static String getName(ProtoType type) {
		switch (type) {
		case VARINT:
			return "VarInt";
		case VARLONG:
			return "VarLong";
		case STRING:
			return "String";
		case OBJECT:
			return "Object";
		case VARINTLIST:
			return "VarInt List";
		case VARLONGLIST:
			return "VarLong List";
		case STRINGLIST:
			return "String List";
		case OBJECTLIST:
			return "Object List";

		default:
			return "UnKnow";
		}
	}
}
