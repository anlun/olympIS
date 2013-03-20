import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Владимир
 * Date: 18.03.13
 * Time: 0:25
 * To change this template use File | Settings | File Templates.
 */
public class Filters {
    private final static int MAX_COUNT_DAYS = 21;   //Max count days in olimp


    //this method for filters with name "sport-array"
    public static int[] getFilterSportArray(ArrayList<String> sports) {
        sports.remove(0);
        int[] res = new int[MAX_COUNT_DAYS];
        try {
            Database db = new Database();
            for (int i = 0; i < MAX_COUNT_DAYS; ++i) {
                res[i] = 0;
            }
            for (String sport : sports) {
                int competitionId = db.getIdCompetition(sport);
                res[(int) db.getDayCompetition(competitionId)] = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return res;
    }


    public static int[] getFilters(ArrayList<ArrayList<String>> filters) {
        int[] res = new int[MAX_COUNT_DAYS];
        for (int i = 0; i < MAX_COUNT_DAYS; ++i) {
            res[i] = 1;
        }
        for (ArrayList filt : filters) {
            int[] temp = new int[21];
            //in this "if" we iterate all filters/
            if (filt.get(0).equals("sport_array")) {
                temp = getFilterSportArray(filt);
            }
            for (int i = 0; i < MAX_COUNT_DAYS; ++i) {
                if (temp[i] == 0) {
                    res[i] = 0;
                }
            }
        }
        return res;

    }

}
