package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class FilterListForTimetable implements Serializable, CustomSerializable {
	public FilterListForTimetable() {
		filters = new ArrayList<Filter>();
	}

	public FilterListForTimetable(ArrayList<Filter> filters, int dayNumber) {
		this.filters = filters;
		this.dayNumber = dayNumber;
	}

	public String serialize() {
		String result = "<object class=\"beans.FilterListForTimetable\">";

		result += Utils.arrayListToBeanField("filters", filters);
		result += Utils.intToBeanField("dayNumber", dayNumber);

		result += "</object>";

		return result;
	}

	public ArrayList<Filter> getFilters() {
		return filters;
	}

	public void setFilters(ArrayList<Filter> filters) {
		this.filters = filters;
	}

	public int getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(int newDayNumber) {
		dayNumber = newDayNumber;
	}

	private ArrayList<Filter> filters;
	private int               dayNumber;
}
