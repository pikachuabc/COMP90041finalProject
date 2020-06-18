package ethicalengine;


import java.util.ArrayList;

/**
 * @description: Animal
 * @author: Fan Jia
 */
public class Animal extends Character {
    private String species;
    private boolean isPet;
    public static ArrayList<String> specie = new ArrayList<String>(){{
        add("TURTLE");
        add("BIRD");
        add("DOG");
        add("CAT");
    }};


    public Animal(Character.Gender gender, Character.BodyType bodyType, int age, String species, boolean isPet) {
        super(gender, bodyType, age);
        this.species = species;
        this.isPet = isPet;
    }

    public Animal(Animal animal) {
        this.species = animal.species;
        this.isPet = animal.isPet();
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

    /**
     * Gain additional mark for animal based on if it is a pet
     *
     * @author Fan Jia
     * @methodName getMark
     * @return double
     */
    @Override
    public double getMark() {
        double mark = super.getMark();
        if (isPet) {
            mark+=1;
        }
        return mark;
    }

    @Override
    public String toString() {
        if (isPet()) {
            return getSpecies().toLowerCase() + " is pet";
        } else {
            return getSpecies().toLowerCase();
        }
    }
}
