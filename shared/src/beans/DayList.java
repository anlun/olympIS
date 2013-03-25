package beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class contains list of days, with satisfy filters.
 *  @author danya
 */
public class DayList implements Serializable {

	public DayList() {
		this.listOfDays = new ArrayList<Integer>();
	}

	public DayList(ArrayList<Integer> listOfDays) {
		this.listOfDays = listOfDays;
	}

	public void setListOfDays(ArrayList<Integer> listOfDays) {
		this.listOfDays = listOfDays;
	}

	public ArrayList<Integer> getListOfDays() {
		return this.listOfDays;
	}

	public void addDay(Integer day) {
		if (!listOfDays.contains(day)) {
			listOfDays.add(day);
		}
	}

	public void setEmpty() {
		this.listOfDays = new ArrayList<Integer>();
	}

	private ArrayList<Integer> listOfDays;
}
