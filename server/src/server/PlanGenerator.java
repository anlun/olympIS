package server;

import beans.Sex;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class server.PlanGenerator for generating competition plan.
 * @author Oderov Roman
 */
public class PlanGenerator {

    public PlanGenerator() {
        try{
            this.db = Database.createDatabase();

            this.nonPlannedCompetitions = db.competitions();
            this.allCompetitionCount = this.nonPlannedCompetitions.size();
            this.sportObjectCount = db.sportObjectNumber();

            //spObjs by type:
            this.spObjByType = db.allSportObjectsByType();

            this.plan = new int[this.sportObjectCount + 1][this.allCompetitionCount*DAY_LENGTH + 1];
            this.plannedCompetitions = new ArrayList<Competition>();

            this.currHour = new int[this.sportObjectCount+1];
            this.hoursLeft = new int[this.sportObjectCount+1];
            this.currDay = new int[this.sportObjectCount+1];
            for (int i = 1; i <= sportObjectCount; i++) {
                this.hoursLeft[i] = DAY_LENGTH;
                this.currDay[i] = 1;
                this.currHour[i] = 1;
            }
            //get all collisions between competitions into array:
            this.athleteCollisions = new boolean[allCompetitionCount + 1][allCompetitionCount + 1];
            for (int i = 1; i <= allCompetitionCount; i++) {//zero-row and zero-column are empty: there's no competition with ID = 0
                this.athleteCollisions[i][i] = false;//no collision with itself
                for (int j = i + 1; j <= allCompetitionCount; j++) {
                    this.athleteCollisions[i][j] = db.athleteCollision(i,j);
                    this.athleteCollisions[j][i] = this.athleteCollisions[i][j];//Array is symmetrical about the main diagonal.
                }
            }
        }
        catch (Exception e){
            System.err.print("\nCan't create DB connection!");
        }

        //close db connection:
        try {
            this.db.closeConnection();
        } catch (SQLException e) {
            System.err.print("\nCan't close database connection!");
            e.printStackTrace();
        }
    }

//======================================
    /**
     * Function to generate the plan. Implemented with recursion.
     * @return TRUE - plan was generated. FALSE - on the contrary.
     */
    private boolean generatePlan() {
        boolean flagCheck = false;
        boolean flagGen = false;

        //Check restriction on total length of the Games
        if (getMaxCurrHour() > MAX_DAYS * DAY_LENGTH + 1) {
            return false;
        }
        //if there's no competitions - ok. Everything's been planned
        if (nonPlannedCompetitions.size() == 0)
            return true;

        //exhaustive search for competitions
        Competition cmptn;
        for (int i = 0; i < nonPlannedCompetitions.size(); i++){
            cmptn = nonPlannedCompetitions.get(i);
            nonPlannedCompetitions.remove(cmptn);

            //trying all possible sportObjects to hold current competition:
            int cmptnType = cmptn.getIdTypeRequiredSportObject();
            for (int spObj : spObjByType[cmptnType]) {
                flagCheck = checkPlanRestrictions(cmptn, spObj);
                if (flagCheck) {
                    pushIntoPlan(cmptn,spObj);
                    flagGen = generatePlan();
                    if (flagGen) {
                        cmptn.setSportObjectId(spObj);
                        return true;
                    } else {
                        popFromPlan(cmptn,spObj);
                    }
                }
            }

            /*
            Pop first element from the respective spObjByTest list and push it at the end.
            It's necessary for more uniform a priory competition distribution.
            //TODO: запилить, чтоб работала оптимизация! Сейчас выдает ConcurrentModificationException
            */
            /*
            if (spObjByType[cmptnType].size() > 1) {
                int temp = spObjByType[cmptnType].get(0);
                spObjByType[cmptnType].add(temp);
                spObjByType[cmptnType].remove(0);
            }
            */

            nonPlannedCompetitions.add(i,cmptn);
        }

        /*Вставляем пустое соревнование в качестве одночасовой паузы на объектах с минимальным текущим временем.
         И попробуем еще разок сгенерить расписание. Если не прокатит - откатим эти изменения и попробуем по-другому.
        * */
        int SpObjWithMinCurrHour = getSpObjWithMinCurrHour();
        pushIntoPlan(null,SpObjWithMinCurrHour);
        flagGen = generatePlan();
        if (flagGen) {
            return true;
        } else {
            popFromPlan(null,SpObjWithMinCurrHour);
        }

        //if nothing was planned - return FALSE: generation with current parameters isn't possible
        return false;
    }


    /**
     * Push competition into the plan.
     * Renew plan-table; add the competition to plannedCompetitions and set them beginning and ending; renew currHour, hoursLeft & currDay correctly.
     * @param competition competition to be pushed into the plan
     * @param sportObject sport object where the chosen competition will be held
     */
    private void pushIntoPlan(Competition competition, int sportObject) {
        if (competition != null) {
            for (int i = 0; i < competition.getDuration(); i++) {
                plan[sportObject][currHour[sportObject] + i] = competition.getId();
            }
            plannedCompetitions.add(competition);
            competition.setBeginHour(currHour[sportObject]);
            competition.setEndHour(currHour[sportObject]+competition.getDuration());
            currHour[sportObject] += competition.getDuration();
        } else {
            plan[sportObject][currHour[sportObject]] = 0;//plan[sportObject][currHour[sportObject]] = -1; Just to avoid ArrayIndexOutOfRange exception
            currHour[sportObject] += 1;
        }
        currDay[sportObject] = (currHour[sportObject] - 1) / DAY_LENGTH + 1;
        hoursLeft[sportObject] = DAY_LENGTH - (currHour[sportObject] - 1) % DAY_LENGTH;
    }
    /**
     *  Pop competition from the plan.
     *  Renew plan, pop the competition from plannedCompetitions, roll back currHour, hoursLeft & currDay correctly.
     * @param competition competition to be popped from the plan
     * @param sportObject sport object where the chosen competition would be held
     */
    private void popFromPlan(Competition competition, int sportObject) {
        if (competition != null) {
            for (int i = 0; i < competition.getDuration(); i++) {
                plan[sportObject][currHour[sportObject] - i - 1] = 0;
            }
            plannedCompetitions.remove(competition);
            competition.setBeginHour(0);
            competition.setEndHour(0);
            currHour[sportObject] -= competition.getDuration();
        } else {
            plan[sportObject][currHour[sportObject] - 1] = 0;
            currHour[sportObject] -= 1;
        }
        currDay[sportObject] = (currHour[sportObject] - 1) / DAY_LENGTH + 1;
        hoursLeft[sportObject] = DAY_LENGTH - (currHour[sportObject] - 1) % DAY_LENGTH;
    }


    /**
     * Check if the competition could be held at the sportObject.
     * @param competition competition to be checked
     * @param sportObject potential sportObject
     * @return success or not, i.e. TRUE - if all Restrictions complied with requirements. FALSE - on the contrary
     */
    private boolean checkPlanRestrictions(Competition competition, int sportObject) {
        //check if the competition doesn't fit supposed vacant hours return false
        if (hoursLeft[sportObject] < competition.getDuration()) {
            return false;
        }
        //check if there's any collisions during the supposed competition
        for (int d = 0; d < competition.getDuration(); d++) {
            for (int sO = 1; sO <= sportObjectCount; sO++) {
                //if it's not itself and if there's a collision return false;
                if ((sO != sportObject) && (athleteCollisions[competition.getId()][plan[sO][currHour[sportObject] + d]])) {
                    return false;
                }
            }
        }

        //if there's no collision return true
        return true;
    }


    /**
     * Get Sport Object with minimal currHour
     * @return sport object id
     */
    private int getSpObjWithMinCurrHour() {
        int min = currHour[1];
        int result = 1;
        for (int i = 2; i <= sportObjectCount; i++) {
            if (currHour[i] < min) {
                min = currHour[i];
                result = i;
            }
        }
        return result;
    }
    /**
     * Get maximum element from currHour[]
     * @return max element of currHour
     */
    private int getMaxCurrHour(){
        int max = currHour[1];
        for (int i = 2; i <= sportObjectCount; i++){
            if (currHour[i] > max){
                max = currHour[i];
            }
        }
        return max;
    }


    /**
     * Export renewed competition info back to the server.Database
     * Now it writes plan in the console
     */
    private void exportPlanToDatabase() {
        //TODO: export competitions back to the server.Database. Ask Vova to implement it.
        System.out.print("\t\t");
        for (int i = 1; i <= DAY_LENGTH * MAX_DAYS; i++) {
            System.out.print(" "+i+"\t");
        }
        System.out.print("\n\n");
        for (int obj = 1; obj <= sportObjectCount; obj++) {
            System.out.print(obj+"\t\t");
            for (int i = 1; i <= MAX_DAYS * DAY_LENGTH; i++) {
                System.out.print(" "+plan[obj][i]+"\t");
            }
            System.out.print("\n");
        }

    }


    /**
     * Main function to start generating from:
     */
    public static void main(String[] args) {
        PlanGenerator planGen = new PlanGenerator();

        boolean success = planGen.generatePlan();

        //export calculated data and
        if (success) {
            planGen.exportPlanToDatabase();
        } else {
            System.out.print("ERROR! Plan generation failed. There's no suitable plan.");
        }

        //exit:
        try {
            System.out.print("\n\n=========\nPRESS ENTER!");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//=======================================================
    private Database db; //database entity

    private ArrayList<Competition> nonPlannedCompetitions; // competitions not planned yet
    private int allCompetitionCount;   //count of existing competitions
    private int sportObjectCount;   //count of existing sportObjects

    private ArrayList<Integer>[] spObjByType;   //type <-> spObjects

    private int[][] plan;           //main structure for the plan
    private ArrayList<Competition> plannedCompetitions;    // competitions added to plan

    private int[] currHour;  // current hour at each sportObject (when the last competition finishes)
    private int[] hoursLeft; // Hours left from current time to next day // maybe set this field for easier way to write code
    private int[] currDay;   // current day of the Championship/Games at each sportObject

    private boolean[][] athleteCollisions;   //array [allCompetitionCount][allCompetitionCount] to store collisions from db
    private final static int DAY_LENGTH = 8;
    private final static int MAX_DAYS = 2;//21
}




/** -- FOR TEST -- -- FOR TEST -- -- FOR TEST -- -- FOR TEST -- -- FOR TEST --
 *============================================================================

/**
 * Class Constructor with manually created data
 */
/*
public PlanGenerator() {
    ArrayList<Competition> result = new ArrayList<Competition>();
    result.add(new Competition(1,3,new Sex(Sex.undefined),1));
    result.add(new Competition(2,4,new Sex(Sex.undefined),1));
    result.add(new Competition(3,3,new Sex(Sex.undefined),1));
    result.add(new Competition(4,2,new Sex(Sex.undefined),1));
    result.add(new Competition(5,2,new Sex(Sex.undefined),2));
    result.add(new Competition(6,5,new Sex(Sex.undefined),2));
    result.add(new Competition(7,2,new Sex(Sex.undefined),2));
    result.add(new Competition(8,2,new Sex(Sex.undefined),2));
    result.add(new Competition(9,6,new Sex(Sex.undefined),3));
    result.add(new Competition(10,1,new Sex(Sex.undefined),3));
    result.add(new Competition(11,8,new Sex(Sex.undefined),3));
    result.add(new Competition(12,3,new Sex(Sex.undefined),1));
    result.add(new Competition(13,5,new Sex(Sex.undefined),1));

    this.nonPlannedCompetitions = result;
    this.allCompetitionCount = this.nonPlannedCompetitions.size();
    this.sportObjectCount = 4;

    //spObjs by type:
    spObjByType = new ArrayList[4];
    spObjByType[0] = null;
    spObjByType[1] = new ArrayList<Integer>(Arrays.asList(1,2));
    spObjByType[2] = new ArrayList<Integer>(Arrays.asList(3));
    spObjByType[3] = new ArrayList<Integer>(Arrays.asList(4));

    this.plan = new int[this.sportObjectCount + 1][this.allCompetitionCount*DAY_LENGTH + 1];
    this.plannedCompetitions = new ArrayList<Competition>();

    this.currHour = new int[this.sportObjectCount+1];
    this.hoursLeft = new int[this.sportObjectCount+1];
    this.currDay = new int[this.sportObjectCount+1];
    for (int i = 1; i <= sportObjectCount; i++) {
        this.hoursLeft[i] = DAY_LENGTH;
        this.currDay[i] = 1;
        this.currHour[i] = 1;
    }

    this.athleteCollisions = new boolean[allCompetitionCount + 1][allCompetitionCount + 1];
    for (int i = 1; i <= allCompetitionCount; i++) {//zero-row and zero-column are empty: there's no competition with ID = 0
        for (int j = 1; j <= allCompetitionCount; j++) {
            this.athleteCollisions[i][j] = false;
        }
    }
    this.athleteCollisions[1][6] = true;    this.athleteCollisions[6][1] = true;
    this.athleteCollisions[2][5] = true;    this.athleteCollisions[5][2] = true;
    this.athleteCollisions[2][12] = true;   this.athleteCollisions[12][2] = true;
    this.athleteCollisions[11][12] = true;  this.athleteCollisions[12][11] = true;
    this.athleteCollisions[8][13] = true;   this.athleteCollisions[13][8] = true;
    this.athleteCollisions[3][4] = true;    this.athleteCollisions[4][3] = true;
    this.athleteCollisions[3][8] = true;    this.athleteCollisions[8][3] = true;
}

*/