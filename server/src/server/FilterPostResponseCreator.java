package server;


import beans.Filter;
import utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class FilterPostResponseCreator extends ResponseCreator {
	public String createResponse() {
		ArrayList<Filter> filters = new ArrayList<Filter>();

		//TODO: сделать чтение из БД набора фильтров
		//Сложить в filters

		return Utils.arrayListToBeanXML(filters);
	}
}