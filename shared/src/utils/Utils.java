package utils;

import com.googlecode.openbeans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Utils {
	public static final  int maxCountDays = 21;
	public static final String openOlimp ="2013-02-01 00:00:00";

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
				+ "<int>" + String.valueOf(value) + "</int>"
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
		result += arrayListToBeanXML(list);
		result += "</void>";

		return result;
	}

	public static String arrayListToBeanXML(ArrayList<? extends CustomSerializable> list) {
		String result = "<object class=\"java.util.ArrayList\">";

		for (CustomSerializable el : list) {
			result += "<void method=\"add\">" + el.serialize() + "</void>";
		}

		result += "</object>";

		return result;
	}

	public static String stringArrayListToBeanField(
			String fieldName, ArrayList<String> list
	) {
		String result = "<void property=\"" + fieldName + "\">";
		result += "<object class=\"java.util.ArrayList\">";

		for (String el : list) {
			result += "<void method=\"add\">" + "<string>" + el + "</string>" + "</void>";
		}

		result += "</object>";
		result += "</void>";

		return result;
	}


	public static String objectToBeanField(String fieldName, CustomSerializable value) {
		return "<void property=\"" + fieldName + "\">"
				+ value.serialize()
				+ "</void>";
	}

	public static String encoderWrap(String strToWrap) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
				+ "<java version=\"1.7.0_09\" class=\"com.googlecode.openbeans.XMLDecoder\">"
				+ strToWrap
				+ "</java>";
	}
}
