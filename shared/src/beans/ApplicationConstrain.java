package beans;

import java.io.Serializable;
import java.util.Vector;

public class ApplicationConstrain implements Serializable {
	public ApplicationConstrain() {
	}

	public ApplicationConstrain(Vector<SportConstrain> sportConstrains) {
		this.sportConstrains = sportConstrains;
	}

	public Vector<SportConstrain> getSportConstrains() {
		return sportConstrains;
	}
	public void setSportConstrains(Vector<SportConstrain> sportConstrains) {
		this.sportConstrains = sportConstrains;
	}

	public static class SportConstrain {
		public SportConstrain() {
		}

		public SportConstrain(String sportName, int athleteCount, Sex athleteSex) {
			this.sportName    = sportName;
			this.athleteCount = athleteCount;
			this.athleteSex   = athleteSex;
		}

		public String getSportName() {
			return sportName;
		}
		public void setSportName(String sportName) {
			this.sportName = sportName;
		}

		public int getAthleteCount() {
			return athleteCount;
		}
		public void setAthleteCount(int athleteCount) {
			this.athleteCount = athleteCount;
		}

		public Sex getAthleteSex() {
			return athleteSex;
		}
		public void setAthleteSex(Sex athleteSex) {
			this.athleteSex = athleteSex;
		}

		private String  sportName;
		private int     athleteCount;
		private Sex     athleteSex;
	}

	public enum Sex {
		Male, Female, Undefined
	}

	private Vector<SportConstrain> sportConstrains;
}
