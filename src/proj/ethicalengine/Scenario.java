package proj.ethicalengine;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @description:Scenario
 * @author: Fan Jia
 */
public class Scenario {
    private ArrayList<Character> passengers = new ArrayList<>();
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

    public ArrayList<Character> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<Character> passengers) {
        this.passengers = passengers;
    }

    public ArrayList<Character> getPedestrians() {
        return pedestrians;
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

    public int getPassengerCount() {
        return passengers.size();
    }

    public int getPedestrianCount() {
        return pedestrians.size();
    }

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
