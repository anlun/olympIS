package server;

import beans.*;
import utils.Utils;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class FilterDayTimetableResponseCreator extends ResponseCreator {
	public FilterDayTimetableResponseCreator(String filterListXML) {
		this.filterListXML = filterListXML;
	}

	public String createResponse() {
		try {
			XMLDecoder decoder = new XMLDecoder(
					new ByteArrayInputStream(filterListXML.getBytes("UTF-8"))
			);
			FilterListForTimetable filterList = (FilterListForTimetable) decoder.readObject();

			//TODO: For Vova
			ArrayList<Filter> filters = filterList.getFilters();
			int dayNumber = filterList.getDayNumber();

			if (filterList == null) {
				return Utils.beanToString(new DayTimetable());
			}

			//TODO: чтение из базы DayTimetable по filterList
			return Utils.beanToString(new DayTimetable());

		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported encoding error!");
		}

		return Utils.beanToString(new DayTimetable());
	}

	private String filterListXML;
}
