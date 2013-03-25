package utils;

import com.googlecode.openbeans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;

public class Utils {
	public static String beanToString(Object objectToSerialize) {
		ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(byteArr));
		e.writeObject(objectToSerialize);
		e.close();
		return byteArr.toString();
	}
}
