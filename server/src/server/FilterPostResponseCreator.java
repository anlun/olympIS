package server;


import beans.Filter;
import utils.Utils;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

public class FilterPostResponseCreator extends ResponseCreator {
	public String createResponse() {
		ArrayList<Filter> filters = new ArrayList<Filter>();
		try {
			Database db = Database.createDatabase();
			filters = db.getFilters();
			db.closeConnection();
		} catch (SQLException e) {
			//TODO: норм комментарий
			System.err.println("Проблема с базой при Filter!");
		}

		//TODO: сделать чтение из БД набора фильтров
		//Сложить в filters

		return Utils.arrayListToBeanXML(filters);
	}
}