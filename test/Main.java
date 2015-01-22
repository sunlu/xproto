package test;

import java.util.ArrayList;

import com.gavin.ProtoStream;
import com.gavin.ProtoUtils;

public class Main {

	public static void main(String[] args) {

		// byte[] data = new byte[]{0x11,0,0,0,0x56,0,0x32,0x43};
		// byte[] temp = Packet.packed(data);
		// byte[] daa2 = Packet.unpacked(temp);

		Person person = new Person();
		person.Id = 1;
		person.Name = "中文";
		person.Email = "sunlu@foxmail.com";
		person.Money = 1000L;
		person.Mobile = new PhoneNumber();
		person.Mobile.PhoneType = 1;
		person.Mobile.Number = "13800138000";

		person.Phones = new ArrayList<>();

		PhoneNumber num = new PhoneNumber();
		num.PhoneType = 2;
		num.Number = "123456";
		person.Phones.add(num);

		num = new PhoneNumber();
		num.PhoneType = 3;
		num.Number = "6543dfsdf21";
		person.Phones.add(num);

		ProtoStream buffer = new ProtoStream();
		person.encode(buffer);

		byte[] data = buffer.toByteArray();

		System.out.println("Person pack size=" + data.length + "，Hex="
				+ buffer.hex());

		ProtoStream buffer2 = new ProtoStream(data);
		Person person2 = new Person();
		person2.decode(buffer2);

		System.out.println("Person is =" + person2.toString());

		Person2 person3 = new Person2();
		buffer2.position(0);
		person3.decode(buffer2);

		System.out.println("person2 is =" + person3.toString());

		System.out.println("----------- MESSAGE DUMP -----------");
		buffer2.position(0);		
		ProtoUtils.dump(buffer2);

	}
}
