package beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class contains a selected day timetable.
 *  @author danya
 */
public class DayTimetable implements Serializable {

	public DayTimetable() {
		this.dayTimetable = new ArrayList<DaySportElement>();
	}

	public DayTimetable(ArrayList<DaySportElement> dayTimetable) {
		this.dayTimetable = dayTimetable;
	}

	public void setDayTimetable(ArrayList<DaySportElement> dayTimetable) {
		this.dayTimetable = dayTimetable;
	}

	public ArrayList<DaySportElement> getDayTimetable() {
		return this.dayTimetable;
	}

	/**
	 * Class contains day element timetable.
	 *  @author danya
	 */
	public static class DaySportElement implements Serializable {

		public DaySportElement(){
			this.satisfyFilter = false;
			this.sportElement = "";
		}

		public DaySportElement(String sportElement, boolean satisfyFilter){
			this.satisfyFilter = satisfyFilter;
			this.sportElement = sportElement;
		}

		public void setSatisfyFilter(boolean satisfyFilter) {
			this.satisfyFilter = satisfyFilter;
		}

		public boolean getSatisfyFilter() {
			return this.satisfyFilter;
		}

		public void setSportElement(String sportElement) {
			this.sportElement = sportElement;
		}

		public String getSportElement() {
			return this.sportElement;
		}

		private boolean satisfyFilter; // Удовлетворяет ли этот элемент выбранным фильрам.
		private String sportElement; // Это время + название меропрятия и т.д.
	}

	private ArrayList<DaySportElement> dayTimetable;
}
