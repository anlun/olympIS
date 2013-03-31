package beans;

import java.io.Serializable;

/**
 * Class represents the sex of sportsmen in competitions.
 * @author Podkopaev Anton
 */

public class Sex implements Serializable {
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

	public static final int male      = 0;
	public static final int female    = 1;
	public static final int undefined = 2;

	private int sex;
}
