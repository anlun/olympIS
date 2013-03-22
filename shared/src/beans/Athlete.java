package beans;

import java.io.Serializable;

public class Athlete implements Serializable {
	public Athlete(String name, String competition){
		this.name = name;
		this.competition = competition;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public String getCompetition() {
		return this.competition;
	}

	private String name;
	private String competition;
}

