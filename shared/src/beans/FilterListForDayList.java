package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class FilterListForDayList implements Serializable, CustomSerializable {
	public FilterListForDayList() {
		filters = new ArrayList<Filter>();
	}

	public FilterListForDayList(ArrayList<Filter> filters) {
		this.filters = filters;
	}

	public String serialize() {
		String result = "<object class=\"beans.FilterListForDayList\">";

		result += Utils.arrayListToBeanField("filters", filters);

		result += "</object>";

		return result;
	}


	public ArrayList<Filter> getFilters() {
		return filters;
	}

	public void setFilters(ArrayList<Filter> filters) {
		this.filters = filters;
	}

	private ArrayList<Filter> filters;
}
