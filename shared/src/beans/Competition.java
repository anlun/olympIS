package beans;

/**
 * Created with IntelliJ IDEA.
 * User: Владимир
 * Date: 17.03.13
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 */

import beans.ApplicationConstrain.Sex;

import java.util.ArrayList;

public class Competition {
    private int id;
    private int duration;
    private Sex sexParticipants;
    private ArrayList<Integer> idRequiredSportObjects;

    public Competition(int id, int duration, Sex sexParticipants, ArrayList<Integer> idRequiredSportObjects) {
        this.id = id;
        this.duration = duration;
        this.sexParticipants = sexParticipants;
        this.idRequiredSportObjects = idRequiredSportObjects;
    }

}
