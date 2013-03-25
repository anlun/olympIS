package beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class represents users filter.
 *  @author danya
 */
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

	private ArrayList<String> filter; // Список выбранных элдементов фильтра.
				// Этот список содержит имея соответствующих элементов. (Например: Франция, Англия, Испания).
	private String filterName; // Тип фильтра. По этому типу определяем какой фильтр выбран.
				// Например: Страна.
}
