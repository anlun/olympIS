package beans;

import java.io.Serializable;

public class Athlete implements Serializable {
	public Athlete(String name, String sex, String weight, String height, String competition){
		this.name = name;
		this.sex = sex;
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

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getWeight() {
		return this.weight;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getHeight() {
		return this.height;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public String getCompetition() {
		return this.competition;
	}

	private String name;
	private String sex;
	private String weight;
	private String height;
	private String competition;
}

