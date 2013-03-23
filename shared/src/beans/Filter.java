package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Filter implements Serializable {

	public Filter(String filterName, ArrayList<String> filter) {
		this.filter = filter;
		this.filterName = filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterName() {
		return this.filterName;
	}

	public void setFilter(ArrayList<String> filter) {
		this.filter = filter;
	}

	public ArrayList<String> getFilter() {
		return this.filter;
	}

	private ArrayList<String> filter;
	private String filterName;
}
