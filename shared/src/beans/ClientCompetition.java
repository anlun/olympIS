package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientCompetition implements Serializable, CustomSerializable {
	public ClientCompetition() {
		this.competition = "";
		this.athleteCompetitionList = new ArrayList<Athlete>();
		this.maxAthleteNumber = 0;
	}

	public String serialize() {
		String result = "<object class=\"beans.ClientCompetition\">";

		result += Utils.arrayListToBeanField("athleteCompetitionList", athleteCompetitionList);
		result += Utils.stringToBeanField("competition", competition);
		result += Utils.intToBeanField("maxAthleteNumber", maxAthleteNumber);

		result += "</object>";

		return result;
	}

	public ClientCompetition(String competition, int maxAthleteNumber) {
		this.competition = competition;
		this.athleteCompetitionList = new ArrayList<Athlete>();
		this.maxAthleteNumber = maxAthleteNumber;
	}

	public Athlete getAthlete(String name) {
		for (Athlete athlete : athleteCompetitionList) {
			if (athlete.getName().equals(name)) {
				return athlete;
			}
		}
		return null;
	}

	public int getAthleteListIndex(String name) {
		for (int i = 0; i < this.athleteCompetitionList.size(); i++) {
			if (this.athleteCompetitionList.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public void addAthlete(int index, Athlete athlete) {
		this.athleteCompetitionList.add(index, athlete);
	}

	public void deleteAthlete(String name) {
		for (Athlete athlete : this.athleteCompetitionList) {
			if (athlete.getName().equals(name)) {
				this.athleteCompetitionList.remove(athlete);
				return;
			}
		}
	}
	public int getAthleteNumber() {
		return this.athleteCompetitionList.size();
	}


	//JavaBeans methods
	public void setAthleteCompetitionList(ArrayList<Athlete> athleteCompetitionList) {
		this.athleteCompetitionList = athleteCompetitionList;
	}

	public ArrayList<Athlete> getAthleteCompetitionList() {
		return this.athleteCompetitionList;
	}

	public String getCompetition() {
		return this.competition;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public int getMaxAthleteNumber() {
		return this.maxAthleteNumber;
	}

	public void setMaxAthleteNumber(int newMaxAthleteNumber) {
		maxAthleteNumber = newMaxAthleteNumber;
	}

	private ArrayList<Athlete> athleteCompetitionList;
	private String             competition;
	// Количество атлетов, которое страна пожет подать на данное соревнование.
	private int                maxAthleteNumber;
}
