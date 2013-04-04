package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class CompetitionList implements Serializable, CustomSerializable {
	public CompetitionList() {
		this.competitionList = new ArrayList<Competition>();

		listTest = new ArrayList<Integer>();
		listTest.add(5);
		listTest.add(6);
	}

	public CompetitionList(String[] competitionNameList, int[] athleteNumberList) {
		this();
		for (int i = 0; i < competitionNameList.length; i++) {
			this.competitionList.add(new Competition(competitionNameList[i], athleteNumberList[i]));
		}
	}

	public int getAthleteListIndex(String name, String competition) {
		for (Competition competitionIterator : competitionList) {
			if (competitionIterator.getCompetition().equals(competition)) {
				return competitionIterator.getAthleteListIndex(name);
			}
		}
		return -1;
	}

	public void addAthlete(int index, String competition, Athlete athlete) {
		for (Competition competitionIterator : competitionList) {
			if (competitionIterator.getCompetition().equals(competition)) {
				competitionIterator.addAthlete(index, athlete);
			}
		}
	}

	public void deleteAthlete(String name, String competition) {
		for (Competition competitionIterator : competitionList) {
			if (competitionIterator.getCompetition().equals(competition)) {
				competitionIterator.deleteAthlete(name);
				return;
			}
		}
	}

	public int getAthleteNumber(String competitionName) {
		for (Competition competition : this.competitionList) {
			if (competition.getCompetition().equals(competitionName)) {
				return competition.getAthleteNumber();
			}
		}
		return -1;
	}

	public int getMaxAthleteNumber(String competitionName) {
		for (Competition competition : this.competitionList) {
			if (competition.getCompetition().equals(competitionName)) {
				return competition.getMaxAthleteNumber();
			}
		}
		return -1;
	}

	public Athlete getAthlete(String name, String competitionName){
		for (Competition competition : this.competitionList) {
			if (competition.getCompetition().equals(competitionName)) {
				return competition.getAthlete(name);
			}
		}
		return null;
	}

	public String serialize() {
		String result = "<object class=\"beans.CompetitionList\">";

		//competitionList
		result += Utils.arrayListToBeanField("competitionList", competitionList);

		result += "</object>";

		return result;
	}

	//JavaBeans methods
	public ArrayList<Competition> getCompetitionList() {
		return this.competitionList;
	}
    /*
	public void setCompetitionList(ArrayList<Competition> competitionList) {
		this.competitionList = competitionList;
	}*/

	public ArrayList<Integer> getListTest() {
		return listTest;
	}

	public void setListTest(ArrayList<Integer> arr) {
		this.listTest = arr;
	}

	public class Competition implements Serializable, CustomSerializable {
		public Competition() {
			this.competition = "";
			this.athleteCompetitionList = new ArrayList<Athlete>();
			this.maxAthleteNumber = 0;
		}

		public String serialize() {
			String result = "<object class=\"beans.CompetitionList.Competition\">";

			result += Utils.arrayListToBeanField("athleteCompetitionList", athleteCompetitionList);
			result += Utils.stringToBeanField("competition", competition);
			result += Utils.intToBeanField("maxAthleteNumber", maxAthleteNumber);

			result += "</object>";

			return result;
		}

		private Competition(String competition, int maxAthleteNumber) {
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

	private ArrayList<Competition> competitionList; // Список соревнований и атлетов.
	private ArrayList<Integer>     listTest;
}
