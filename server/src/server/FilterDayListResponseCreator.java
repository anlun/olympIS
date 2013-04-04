package server;

import beans.DayList;
import beans.Filter;
import beans.FilterListForDayList;
import utils.Utils;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

public class FilterDayListResponseCreator extends ResponseCreator {
	public FilterDayListResponseCreator(String filterListXML) {
		this.filterListXML = filterListXML;
	}

	public String createResponse() {
		try {
			XMLDecoder decoder = new XMLDecoder(
					new ByteArrayInputStream(filterListXML.getBytes("UTF-8"))
			);
			FilterListForDayList filterList = (FilterListForDayList) decoder.readObject();
			if (filterList == null) {
				return Utils.beanToString(new DayList());
			}

			ArrayList<Filter> filters = filterList.getFilters();

			try {
				Database db = Database.createDatabase();
				DayList result = db.getDayList(filters);
				db.closeConnection();
				return Utils.beanToString(result);
			} catch (SQLException e) {
				//TODO: норм комментарий
				System.err.println("Проблема с базой при DayList!");
			}

			return Utils.beanToString(new DayList());
		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported encoding error!");
		}

		return Utils.beanToString(new DayList());
	}

	private String filterListXML;
}
