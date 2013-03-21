package beans;

import java.util.ArrayList;

/**
 * Class represents sport competition.
 * It's JavaBean that transfers from server to client.
 * @author Podkopaev Anton
 */
public class Competition {
	public Competition(int id, int duration, Sex athleteSex, ArrayList<Integer> idRequiredSportObjects) {
		this.id         = id;
		this.duration   = duration;
		this.athleteSex = athleteSex;
		this.idRequiredSportObjects = idRequiredSportObjects;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Sex getAthleteSex() {
		return athleteSex;
	}
	public void setAthleteSex(Sex athleteSex) {
		this.athleteSex = athleteSex;
	}

	public ArrayList<Integer> getIdRequiredSportObjects() {
		return idRequiredSportObjects;
	}
	public void setIdRequiredSportObjects(ArrayList<Integer> idRequiredSportObjects) {
		this.idRequiredSportObjects = idRequiredSportObjects;
	}

	private int id;
	private int duration;
	private Sex athleteSex;
	private ArrayList<Integer> idRequiredSportObjects;
}
