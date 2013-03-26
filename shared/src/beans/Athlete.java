package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;

public class Athlete implements Serializable, CustomSerializable {
	public Athlete() {
	}

	public Athlete(String name, Sex sex, int weight, int height, String competition){
		this.name   = name;
		this.sex    = sex;
		this.weight = weight;
		this.height = height;
		this.competition = competition;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Sex getSex() {
		return this.sex;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return this.weight;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return this.height;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public String getCompetition() {
		return this.competition;
	}

	public String serialize(boolean withBeansHead) {
		String result = "<object class=\"beans.Athlete\">";

		//Tags for fields
		//May be need to be in alphabetical order

		result += Utils.stringToBeanField("competition", competition);
		result += Utils.intToBeanField("height", height);
		result += Utils.stringToBeanField("name", name);
		result += Utils.objectToBeanField("sex", sex);
		result += Utils.intToBeanField("weight", weight);

		result += "</object>";
		if (withBeansHead) {
			result = Utils.encoderWrap(result);
		}

		return result;
	}

	private String name;
	private Sex sex;
	private int weight;
	private int height;
	private String competition;
}

