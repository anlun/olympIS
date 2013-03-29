package beans;

import android.util.Log;

import java.util.ArrayList;

public class CompetitionList {
	public CompetitionList(String[] competitionNameList, int[] athleteNumberList) {
		this.competitionList = new ArrayList<Competition>();
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
				Log.d("DAN", "enter to Competition");
				competitionIterator.deleteAthlete(name);
				return;
			}
		}
	}

	public void setCompetitionList(ArrayList<Competition> competitionList) {
		this.competitionList = competitionList;
	}

	public ArrayList<Competition> getCompetitionList() {
		return this.competitionList;
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

	private ArrayList<Competition> competitionList; // Список соревнований и атлетов.

	private class Competition {

		private Competition() {
			this.competition = "";
			this.athleteCompetitionList = new ArrayList<Athlete>();
			this.athleteNumber = 0;
		}

		private Competition(String competition, int maxAthleteNumber) {
			this.competition = competition;
			this.athleteCompetitionList = new ArrayList<Athlete>();
			this.athleteNumber = maxAthleteNumber;
		}

		private Athlete getAthlete(String name) {
			for (Athlete athlete : athleteCompetitionList) {
				if (athlete.getName().equals(name)) {
					return athlete;
				}
			}
			return null;
		}

		private int getAthleteListIndex(String name) {
			for (int i = 0; i < this.athleteCompetitionList.size(); i++) {
				if (this.athleteCompetitionList.get(i).getName().equals(name)) {
					return i;
				}
			}
			return -1;
		}

		private void addAthlete(int index, Athlete athlete) {
			this.athleteCompetitionList.add(index, athlete);
		}

		private void deleteAthlete(String name) {
			for (Athlete athlete : this.athleteCompetitionList) {
				if (athlete.getName().equals(name)) {
					Log.d("DAN", "delete athlete ");
					this.athleteCompetitionList.remove(athlete);
					Log.d("DAN", "deleted");
					return;
				}
			}
		}

		private void setAthleteCompetitionList(ArrayList<Athlete> athleteCompetitionList) {
			this.athleteCompetitionList = athleteCompetitionList;
		}

		private ArrayList<Athlete> getAthleteCompetitionList() {
			return this.athleteCompetitionList;
		}

		private void setCompetition(String competition) {
			this.competition = competition;
		}

		private String getCompetition() {
			return this.competition;
		}

		private int getAthleteNumber() {
			return this.athleteCompetitionList.size();
		}

		private int getMaxAthleteNumber() {
			return this.athleteNumber;
		}

		private ArrayList<Athlete> athleteCompetitionList;
		private String competition;
		private int athleteNumber; // Количество атлетов, которое страна пожет подать на данное соревнование.
	}
}