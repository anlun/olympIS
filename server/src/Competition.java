package server;

import beans.Sex;

/**
 * Class represents sport competition.
 * It's JavaBean that transfers from server to client.
 *
 * @author Podkopaev Anton
 */
public class Competition {
	public Competition(int id, int duration, Sex athleteSex, int idTypeRequiredSportObject) {
		this.id = id;
		this.duration = duration;
		this.athleteSex = athleteSex;
		this.idTypeRequiredSportObject = idTypeRequiredSportObject;
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
    //=================
    public int getBeginHour() {
        return beginHour;
    }
    public void setBeginHour(int newBeginning) {
        this.beginHour = newBeginning;
    }
    public int getEndHour() {
        return endHour;
    }
    public void setEndHour(int newEnd) {
        this.endHour = newEnd;
    }
    public int getIdTypeRequiredSportObject() {
        return idTypeRequiredSportObject;
    }
    public void setIdTypeRequiredSportObject(int newSportType) {
        this.idTypeRequiredSportObject = newSportType;
    }
    //add begin and end of the competition
    private int beginHour;
    private int endHour;
    //=================
	private int id;
	private int duration;
	private Sex athleteSex;
	private int idTypeRequiredSportObject;
}
