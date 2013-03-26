package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;

/**
 * Class represents the sex of sportsmen in competitions.
 * @author Podkopaev Anton
 */

public class Sex implements Serializable, CustomSerializable {
	public Sex() {
	}

	public Sex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int newSex) {
		sex = newSex;
	}

	public String serialize(boolean withBeansHead) {
		String result = "<object class=\"beans.Sex\">";

		//Tags for fields
		//May be need to be in alphabetical order

		result += Utils.intToBeanField("sex", sex);

		result += "</object>";
		if (withBeansHead) {
			result = Utils.encoderWrap(result);
		}

		return result;
	}

	public static final int male      = 1;
	public static final int female    = 0;
	public static final int undefined = 2;

	private int sex;
}
