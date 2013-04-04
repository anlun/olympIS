package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class CompetitionList implements Serializable, CustomSerializable {
	public CompetitionList() {
		this.competitionList = new ArrayList<ClientCompetition>();

		listTest = new ArrayList<Integer>();
		listTest.add(5);
		listTest.add(6);
	}

	public CompetitionList(String[] competitionNameList, int[] athleteNumberList) {
		this();
		for (int i = 0; i < competitionNameList.length; i++) {
			this.competitionList.add(new ClientCompetition(competitionNameList[i], athleteNumberList[i]));
		}
	}

	public int getAthleteListIndex(String name, String competition) {
		for (ClientCompetition competitionIterator : competitionList) {
			if (competitionIterator.getCompetition().equals(competition)) {
				return competitionIterator.getAthleteListIndex(name);
			}
		}
		return -1;
	}

	public void addAthlete(int index, String competition, Athlete athlete) {
		for (ClientCompetition competitionIterator : competitionList) {
			if (competitionIterator.getCompetition().equals(competition)) {
				competitionIterator.addAthlete(index, athlete);
			}
		}
	}

	public void deleteAthlete(String name, String competition) {
		for (ClientCompetition competitionIterator : competitionList) {
			if (competitionIterator.getCompetition().equals(competition)) {
				competitionIterator.deleteAthlete(name);
				return;
			}
		}
	}

	public int getAthleteNumber(String competitionName) {
		for (ClientCompetition competition : this.competitionList) {
			if (competition.getCompetition().equals(competitionName)) {
				return competition.getAthleteNumber();
			}
		}
		return -1;
	}

	public int getMaxAthleteNumber(String competitionName) {
		for (ClientCompetition competition : this.competitionList) {
			if (competition.getCompetition().equals(competitionName)) {
				return competition.getMaxAthleteNumber();
			}
		}
		return -1;
	}

	public Athlete getAthlete(String name, String competitionName){
		for (ClientCompetition competition : this.competitionList) {
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
	public ArrayList<ClientCompetition> getCompetitionList() {
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

	private ArrayList<ClientCompetition> competitionList; // Список соревнований и атлетов.
	private ArrayList<Integer>     listTest;
}
