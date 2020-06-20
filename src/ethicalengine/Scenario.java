package ethicalengine;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @description: Scenario
 * @author: Fan Jia
 */
public class Scenario {

    private ArrayList<Character> passengers = new ArrayList<>();              //more flexible
    private ArrayList<Character> pedestrians = new ArrayList<>();
    private boolean isLegalCrossing;

    public Scenario(boolean isLegalCrossing) {
        this.isLegalCrossing = isLegalCrossing;
    }

    public Scenario(Character[] passengersArray, Character[] pedestriansArray, boolean isLegalCrossing) {
        Collections.addAll(this.passengers, passengersArray);
        Collections.addAll(this.pedestrians, pedestriansArray);
        this.isLegalCrossing = isLegalCrossing;

    }

    public Character[] getPassengers() {
        return passengers.toArray(new Character[0]);
    }

    public void setPassengers(ArrayList<Character> passengers) {
        this.passengers = passengers;
    }

    public Character[] getPedestrians() {
        return pedestrians.toArray(new Character[0]);
    }

    public void setPedestrians(ArrayList<Character> pedestrians) {
        this.pedestrians = pedestrians;
    }

    public boolean isLegalCrossing() {
        return isLegalCrossing;
    }

    public void setLegalCrossing(boolean legalCrossing) {
        isLegalCrossing = legalCrossing;
    }

    public int getPassengerCount() {
        return passengers.size();
    }

    public int getPedestrianCount() {
        return pedestrians.size();
    }

    /**
     * If usr in the care
     *
     * @author Fan Jia
     * @methodName hasYouInCar
     * @return boolean
     */
    public boolean hasYouInCar() {
        for (Character passenger : passengers) {
            if (passenger.getClass().equals(Person.class)) {
                if (((Person) passenger).isYou()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * If usr in the lane
     *
     * @author Fan Jia
     * @methodName hasYouInLane
     * @return boolean
     */
    public boolean hasYouInLane() {
        for (Character pedestrian : pedestrians) {
            if (pedestrian.getClass().equals(Person.class)) {
                if (((Person) pedestrian).isYou()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * format display
     *
     * @author Fan Jia
     * @methodName toString
     * @return java.lang.String
     */
    @Override
    public String toString() {
        String display = "";
        display += "======================================" + "\n";
        display += "# Scenario" + "\n";
        display += "======================================" + "\n";
        display += "Legal Crossing:";
        if (isLegalCrossing()) {
            display += " yes" + "\n";
        } else {
            display += " no" + "\n";
        }
        display += "Passengers (" + getPassengerCount() + ")" + "\n";
        for (Character passenger : passengers) {
            display += "- " + passenger.toString() + "\n";
        }
        display += "Pedestrians (" + getPedestrianCount() + ")" + "\n";
        for (Character pedestrian : pedestrians) {
            display += "- " + pedestrian.toString() + "\n";
        }
        return display;
    }
}
