package proj.ethicalengine;

import java.util.ArrayList;
import java.util.Random;

/**
 * @description:ScenarioGenerator
 * @author: Fan Jia
 */
public class ScenarioGenerator {
    private Random random;
    private int pedestrianCountMinimum = 1;
    private int passengerCountMinimum = 1;
    private int passengerCountMaximum = 5;
    private int pedestrianCountMaximum = 5;

    public ScenarioGenerator() {
        this.random = new Random();
    }

    public ScenarioGenerator(long seed) {
        this.random = new Random(seed);
    }

    public ScenarioGenerator(long seed, int passengerCountMinimum, int passengerCountMaximum, int pedestrianCountMinimum, int pedestrianCountMaximum) {
        this.random = new Random(seed);
        this.passengerCountMinimum = passengerCountMinimum;
        this.passengerCountMaximum = passengerCountMaximum;
        this.pedestrianCountMinimum = pedestrianCountMinimum;
        this.pedestrianCountMaximum = pedestrianCountMaximum;
    }


    public int getPassengerCountMinimum() {
        return passengerCountMinimum;
    }

    public void setPassengerCountMinimum(int passengerCountMinimum) {
        this.passengerCountMinimum = passengerCountMinimum;
    }

    public int getPassengerCountMaximum() {
        return passengerCountMaximum;
    }

    public void setPassengerCountMaximum(int passengerCountMaximum) {
        this.passengerCountMaximum = passengerCountMaximum;
    }

    public int getPedestrianCountMinimum() {
        return pedestrianCountMinimum;
    }

    public void setPedestrianCountMinimum(int pedestrianCountMinimum) {
        this.pedestrianCountMinimum = pedestrianCountMinimum;
    }

    public int getPedestrianCountMaximum() {
        return pedestrianCountMaximum;
    }

    public void setPedestrianCountMaximum(int pedestrianCountMaximum) {
        this.pedestrianCountMaximum = pedestrianCountMaximum;
    }


    public Person getRandomPerson() {
        Character.Gender gender;
        boolean isPregnant;
        Character.BodyType bodyType;
        int age;
        Person.Profession profession;

        Character.Gender[] genders = Character.Gender.values();
        Character.BodyType[] bodyTypes = Character.BodyType.values();
        Person.Profession[] professions = Person.Profession.values();

        gender = genders[random.nextInt(genders.length)];
        if (gender.equals(Character.Gender.FEMALE)) {
            isPregnant = random.nextBoolean();
        } else {
            isPregnant = false;
        }
        bodyType = bodyTypes[random.nextInt(bodyTypes.length)];
        age = random.nextInt(100);
        if (age <= 68 && age >= 17) {   //adult
            profession = professions[random.nextInt(professions.length)];
        } else {
            profession = Person.Profession.NONE;
        }

        return new Person(gender, bodyType, age, isPregnant, profession);
    }

    public Animal getRandomAnimal() {
        int age;
        Character.Gender gender;
        Character.BodyType bodyType;
        String specie;
        boolean isPet;

        Character.Gender[] genders = Character.Gender.values();
        Character.BodyType[] bodyTypes = Character.BodyType.values();
        String[] species = new String[Animal.specie.size()];                         //species
        for (int i = 0; i < Animal.specie.size(); i++) {
            species[i] = Animal.specie.get(i);
        }

        age = random.nextInt(20);
        gender = genders[random.nextInt(genders.length)];
        bodyType = bodyTypes[random.nextInt(bodyTypes.length)];
        isPet = random.nextBoolean();
        specie = species[random.nextInt(species.length)];

        return new Animal(gender, bodyType, age, specie, isPet);
    }


    public Scenario generate() {

        int numberOfPassenger = passengerCountMinimum + random.nextInt(passengerCountMaximum);
        int numberOfPedestrian = pedestrianCountMinimum + random.nextInt(pedestrianCountMaximum);
        boolean isLegal = random.nextBoolean();

        //in passengerArrayList
        ArrayList<Character> passengers = generateArrayList(numberOfPassenger);
        //in pedestrianArrayList
        ArrayList<Character> pedestrians = generateArrayList(numberOfPedestrian);

        int myCase = random.nextInt(3);
        if (myCase == 1) {//I am a passenger
            Person me = getRandomPerson();
            me.setAsYou(true);
            passengers.remove(0);
            passengers.add(me);
        } else if (myCase == 2) {//I am a pedestrian
            Person me = getRandomPerson();
            me.setAsYou(true);
            pedestrians.remove(0);
            pedestrians.add(me);
        }
        // Neither passenger nor pedestrian
        Character[] passengerArray = passengers.toArray(new Character[0]);
        Character[] pedestrianArray = pedestrians.toArray(new Character[0]);

        return new Scenario(passengerArray, pedestrianArray, isLegal);


    }

    private ArrayList<Character> generateArrayList(int range) {

        ArrayList<Character> list = new ArrayList<>();
        int numberOfAnimal;
        numberOfAnimal = random.nextInt(range);
        for (int i = 0; i < numberOfAnimal; i++) {
            list.add(getRandomAnimal());
        }
        for (int i = 0; i < range - numberOfAnimal; i++) {
            list.add(getRandomPerson());
        }
        return list;
    }
}
