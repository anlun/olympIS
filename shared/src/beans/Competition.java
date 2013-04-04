package beans;

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

	public int getidTypeRequiredSportObject() {
		return idTypeRequiredSportObject;
	}

	public void setidTypeRequiredSportObject(int idTypeRequiredSportObject) {
		this.idTypeRequiredSportObject = idTypeRequiredSportObject;
	}

	private int id;
	private int duration;
	private Sex athleteSex;
	private int idTypeRequiredSportObject;
}
