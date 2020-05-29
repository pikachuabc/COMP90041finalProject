package proj.ethicalengine;


import java.util.ArrayList;

/**
 * @description:Animal
 * @author: Fan Jia
 */
public class Animal extends Character {
    private String species;
    private boolean isPet;
    public static ArrayList<String> specie = new ArrayList<>(){{
        add("turtle");
        add("bird");
        add("dog");
        add("cat");
    }};


    public Animal(Character.Gender gender, Character.BodyType bodyType, int age, String species, boolean isPet) {
        super(gender, bodyType, age);
        this.species = species;
        this.isPet = isPet;
    }

    public Animal(String species) {
        super();
        this.species = species;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isPet() {
        return isPet;
    }

    public void setPet(boolean pet) {
        isPet = pet;
    }



    @Override
    public String toString() {
        if (isPet()) {
            return getSpecies() + " is pet";
        } else {
            return getSpecies();
        }
    }
}
