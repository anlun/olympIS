package utils;

import com.googlecode.openbeans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Utils {
	public static String beanToString(Object objectToSerialize) {
		ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(byteArr));
		e.writeObject(objectToSerialize);
		e.close();
		return byteArr.toString();
	}

	//TODO: ускорить с помощью StringBuilder
	public static String intToBeanField(String fieldName, int value) {
		return "<void property=\"" + fieldName + "\">"
				+ "<string>" + String.valueOf(value) + "</string>"
				+ "</void>";
	}

	public static String stringToBeanField(String fieldName, String value) {
		return "<void property=\"" + fieldName + "\">"
		       + "<string>" + value + "</string>"
		       + "</void>";
	}

	public static String arrayListToBeanField(
			String fieldName, ArrayList<? extends CustomSerializable> list
	) {
		String result = "<void property=\"" + fieldName + "\">";
		result += "<object class=\"java.util.ArrayList\">";

		for (CustomSerializable el : list) {
			result += "<void method=\"add\">" + el.serialize(false) + "</void>";
		}

		result += "</object>";
		result += "</void>";

		return result;
	}

	public static String objectToBeanField(String fieldName, CustomSerializable value) {
		return "<void property=\"" + fieldName + "\">"
				+ value.serialize(false)
				+ "</void>";
	}
}
