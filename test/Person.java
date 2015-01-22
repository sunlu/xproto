package test;

import java.util.List;

import com.gavin.Message;
import com.gavin.ProtoStream;
import com.gavin.ProtoType;

public class Person extends Message {

	public int Id;
	public String Name;
	public String Email;
	public List<PhoneNumber> Phones;

	public long Money;
	public PhoneNumber Mobile;
	public List<Integer> Codes;
	public List<String> Cards;
	public List<Long> Datas;

	@Override
	public String toString() {
		return "Person [Id=" + Id + ", Name=" + Name + ", Email=" + Email
				+ ", Phones=" + Phones + ", Money=" + Money + ", Mobile="
				+ Mobile + ", Codes=" + Codes + ", Cards=" + Cards + ", Datas="
				+ Datas + "]";
	}

	@Override
	public void decode(ProtoStream stream) {

		super.decode(stream);

		int tag = 0;
		
		//System.out.println("Person unpack fn=" + fieldCount);

		while (fieldCount-- > 0) {
			tag = stream.readInt();
			
			switch ((tag >> ProtoType.TAG_TYPE_BITS)) {
			case 1: {
				Id = stream.readInt();
				break;
			}
			case 2: {
				Name = stream.readString();
				break;
			}
			case 3: {
				Email = stream.readString();
				break;
			}
			case 4: {
				Phones = stream.readObjectList(PhoneNumber.class);
				break;
			}
			case 10: {
				Money = stream.readLong();
				break;
			}
			case 11: {
				Mobile = stream.readObject(PhoneNumber.class);
				break;
			}
			case 12: {
				Codes = stream.readIntList();
				break;
			}
			case 13: {
				Cards = stream.readStringList();
				break;
			}
			case 14: {
				Datas = stream.readLongList();
				break;
			}
			default:{
				stream.readUnknow(ProtoType.valueOf(tag));
				break;
			}
			}
		}
	}

	@Override
	public void encode(ProtoStream stream) {
		super.encode(stream);

		fieldCount += stream.write(1, Id);
		fieldCount += stream.write(2, Name);
		fieldCount += stream.write(3, Email); 
		fieldCount += stream.write(10, Money);
		fieldCount += stream.write(11, Mobile);
		
		fieldCount += stream.writeIntList(12, Codes);
		fieldCount += stream.writeStringList(13, Cards);
		fieldCount += stream.writeLongList(14, Datas);
		fieldCount += stream.writeObjectList(4, Phones);

		close(stream);
	}
}
