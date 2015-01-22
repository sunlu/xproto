package test;

import com.gavin.ProtoStream;
import com.gavin.Message;
import com.gavin.ProtoType;

public class PhoneNumber extends Message {

	public int PhoneType;
	public String Number;

	@Override
	public String toString() {
		return "PhoneNumber [PhoneType=" + PhoneType + ", Number=" + Number
				+ "]";
	}

	@Override
	public void decode(ProtoStream stream) {
		super.decode(stream);

		int tag = 0;
		while (--fieldCount >= 0) {
			tag = stream.readInt();

			switch ((tag >> ProtoType.TAG_TYPE_BITS)) {
			case 1: {
				PhoneType = stream.readInt();
				break;
			}
			case 2: {
				Number = stream.readString();
				break;
			}
			}
		}
	}

	@Override
	public void encode(ProtoStream buffer) {
		super.encode(buffer);

		fieldCount += buffer.write(1, PhoneType);
		fieldCount += buffer.write(2, Number);

		super.close(buffer);
	}
}