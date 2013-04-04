package server;

import beans.DayList;
import beans.FilterListForDayList;
import utils.Utils;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

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

			//TODO: чтение из базы DayList по filterList
			return Utils.beanToString(new DayList());

		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported encoding error!");
		}

		return Utils.beanToString(new DayList());
	}

	private String filterListXML;
}
