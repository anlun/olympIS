package beans;

import utils.CustomSerializable;
import utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class represents users filter.
 *  @author danya
 */
public class Filter implements Serializable, CustomSerializable {

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

	public String serialize() {
		String result = "<object class=\"beans.Filter\">";

		//Tags for fields
		//May be need to be in alphabetical order

		result += Utils.stringArrayListToBeanField("filter", filter);
		result += Utils.stringToBeanField("filterName", filterName);

		result += "</object>";

		return result;

	}

	private ArrayList<String> filter; // Список выбранных элдементов фильтра.
				// Этот список содержит имея соответствующих элементов. (Например: Франция, Англия, Испания).
	private String filterName; // Тип фильтра. По этому типу определяем какой фильтр выбран.
				// Например: Страна.
}
