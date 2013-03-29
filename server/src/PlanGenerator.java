import beans.Competition;
import beans.Sex;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

//TODO: сейчас все работает только локально (без БД).
//Когда появится БД, я немного переделаю некоторые методы.

/**
 * Class PlanGenerator for generating competition plan.
 * @author Oderov Roman
 */
public class PlanGenerator {

    public PlanGenerator() {
        try{
            ArrayList<Competition> result = new ArrayList<Competition>();
//1
            ArrayList<Integer> spObjs = new ArrayList<Integer>();
            spObjs.add(2);spObjs.add(1);
            result.add(new Competition(1,3,null,spObjs));
//2
            spObjs = new ArrayList<Integer>();spObjs.add(1);spObjs.add(2);
            result.add(new Competition(2,4,null,spObjs));
//3
            spObjs = new ArrayList<Integer>();spObjs.add(2);spObjs.add(1);
            result.add(new Competition(3,3,null,spObjs));
//4
            spObjs = new ArrayList<Integer>();spObjs.add(1);spObjs.add(2);
            result.add(new Competition(4,2,null,spObjs));
//5
            spObjs = new ArrayList<Integer>();spObjs.add(3);
            result.add(new Competition(5,2,null,spObjs));
//6
            spObjs = new ArrayList<Integer>();spObjs.add(3);
            result.add(new Competition(6,5,null,spObjs));
//7
            spObjs = new ArrayList<Integer>();spObjs.add(3);
            result.add(new Competition(7,2,null,spObjs));
//8
            spObjs = new ArrayList<Integer>();spObjs.add(3);
            result.add(new Competition(8,2,null,spObjs));
//9
            spObjs = new ArrayList<Integer>();spObjs.add(4);
            result.add(new Competition(9,6,null,spObjs));
//10
            spObjs = new ArrayList<Integer>();spObjs.add(4);
            result.add(new Competition(10,1,null,spObjs));
//11
            spObjs = new ArrayList<Integer>();spObjs.add(4);
            result.add(new Competition(11,8,null,spObjs));
//12
            spObjs = new ArrayList<Integer>();spObjs.add(2);spObjs.add(1);
            result.add(new Competition(12,3,null,spObjs));
//13
            spObjs = new ArrayList<Integer>();spObjs.add(1);spObjs.add(2);
            result.add(new Competition(13,5,null,spObjs));

            this.nonPlannedCompetitions = result;
            //Database db = Database.createDatabase();
            //this.nonPlannedCompetitions = db.competitions();
            this.allCompetitionCount = this.nonPlannedCompetitions.size();
            //this.sportObjectCount = db.sportObjectNumber();
            this.sportObjectCount = 4;

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
            /*try {
                db.closeConnection();
            } catch (SQLException e) {
                System.err.println("Can't close database connection!");
                e.printStackTrace();
            } */
        }
        catch (Exception e){
            System.err.print("Can't create DB connection!");
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
        if (getMaxCurrHour() > MAX_DAYS * DAY_LENGTH + 1){  //getMaxCurrDay() > MAX_DAYS){
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

            /*TODO: попросить Вову дать мне тип у каждого соревнования. И списки id объектов под каждый тип.
            и после цикла сделать так: (чтобы в след. раз перебирали с другого спорт.объекта и равномернее распределялись)
            Это нужно для большей оптимальности раскладки.
                for (int spObj : Types[cmptn.getTypeSpObj()])
                {
                 ...
                }
                 int temp = Types[cmptn.getTypeSpObj()].get(0);
                 Types[cmptn.getTypeSpObj()].add(temp);
                 Types[cmptn.getTypeSpObj()].remove(0);
            */
            //trying all possible sportObjects to hold current competition:
            for (int spObj : cmptn.getIdRequiredSportObjects()) {
                flagCheck = checkPlanRestrictions(cmptn, spObj);
                if (flagCheck){
                    pushIntoPlan(cmptn,spObj);
                    flagGen = generatePlan();
                    if (flagGen) {
                        return true;
                    } else {
                        popFromPlan(cmptn,spObj);
                    }
                }
            }
            nonPlannedCompetitions.add(i,cmptn);
        }

        // делаем сдвиг времени(currDay & hoursLeft & currHour) на 1 час для объектов c минимальным currHour.
        // И попробуем еще разок сгенерить расписание. Если не прокатит - откатим эти изменения и попробуем по-другому.
        int SpObjWithMinCurrHour = getSpObjWithMinCurrHour();
        pushIntoPlan(null,SpObjWithMinCurrHour); // изменить как описано в комменте выше.
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
            plan[sportObject][currHour[sportObject]] = -1;
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
            for (int sO = 1; sO <= sportObjectCount; sO++){
                //if it's not itself and if there's a collision return false;
                if ((sO != sportObject) && (athleteCollision(competition.getId(),plan[sO][currHour[sportObject] + d]))){
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
     * Get maximum element from currDay[]
     * @return max element of currDay
     */
    private int getMaxCurrDay(){
        int max = currDay[1];
        for (int i = 2; i <= sportObjectCount; i++){
            if (currDay[i] > max){
                max = currDay[i];
            }
        }
        return max;
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
     * Function checking if there're athlete collisions between two competitions
     * i.e. same athlete participates in two concurrent competitions.
     * @param cmptnID1 first competition's ID
     * @param cmptnID2 second competition's ID
     * @return TRUE if there's a collision. FALSE - on the contrary
     */
    private boolean athleteCollision(int cmptnID1, int cmptnID2) {
        //TODO: ask Vova to implement it
        if (
            (cmptnID1 == 1 && cmptnID2 == 6)||(cmptnID1 == 1 && cmptnID2 == 6)||
            (cmptnID1 == 2 && cmptnID2 == 5)||(cmptnID1 == 5 && cmptnID2 == 2)||
            (cmptnID1 == 2 && cmptnID2 == 12)||(cmptnID1 == 12 && cmptnID2 == 2)||
            (cmptnID1 == 11 && cmptnID2 == 12)||(cmptnID1 == 12 && cmptnID2 == 11)||
            (cmptnID1 == 13 && cmptnID2 == 8)||(cmptnID1 == 8 && cmptnID2 == 13)||
            (cmptnID1 == 3 && cmptnID2 == 4)||(cmptnID1 == 4 && cmptnID2 == 3)||
            (cmptnID1 == 3 && cmptnID2 == 8)||(cmptnID1 == 8 && cmptnID2 == 3)
                ) {
            return true;
        }
        return false;
    }

    /**
     * Export renewed competition info back to the Database
     * Now it writes plan in the console
     */
    private void exportPlanToDatabase() {
        //TODO: export competitions back to the Database. Ask Vova to implement it.
        System.out.print("\t\t");
        for (int i = 1; i <= DAY_LENGTH * MAX_DAYS; i++) {
            System.out.print(" "+i+"\t");
        }
        System.out.print("\n\n");
        for (int obj = 1; obj <= sportObjectCount; obj++) {
            System.out.print(obj+"\t\t");
            for (int i = 1; i <= MAX_DAYS * DAY_LENGTH; i++){
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
        if (success) {
            planGen.exportPlanToDatabase();
        } else {
            System.out.print("ERROR! Plan generation failed. There's no suitable plan.");
        }
        try {
            System.out.print("\n\n=========\nPRESS ENTER!");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

//=======================================================
    private ArrayList<Competition> nonPlannedCompetitions; // competitions not planned yet
    private int allCompetitionCount;   //count of existing competitions
    private int sportObjectCount;   //count of existing sportObjects

    private Competition[] competitionIDs;    //used to set bijection between Competitions and their IDs.

    private int[][] plan;           //main structure for the plan
    private ArrayList<Competition> plannedCompetitions;    // competitions added to plan

    private int[] currHour; //NOT NECESSARY!// current hour at each sportObject (when the last competition finishes)
    private int[] hoursLeft; // Hours left from current time to next day // maybe set this field for easier way to write code
    private int[] currDay;   // current day of the Championship/Games at each sportObject

    private final static int DAY_LENGTH = 8;
    private final static int MAX_DAYS = 2;//21;
}