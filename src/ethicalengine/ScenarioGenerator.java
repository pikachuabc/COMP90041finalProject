package ethicalengine;

import java.util.ArrayList;
import java.util.Collections;
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
        if (passengerCountMinimum > passengerCountMaximum) {
            System.out.println("invalid minimum number");
            return;
        }
        this.passengerCountMinimum = passengerCountMinimum;
    }

    public int getPassengerCountMaximum() {
        return passengerCountMaximum;
    }

    public void setPassengerCountMaximum(int passengerCountMaximum) {
        if (passengerCountMaximum < passengerCountMinimum) {
            System.out.println("invalid maximum number");
            return;
        }
        this.passengerCountMaximum = passengerCountMaximum;
    }

    public int getPedestrianCountMinimum() {
        return pedestrianCountMinimum;
    }

    public void setPedestrianCountMinimum(int pedestrianCountMinimum) {
        if (pedestrianCountMinimum < pedestrianCountMaximum) {
            System.out.println("invalid minimum number");
            return;
        }
        this.pedestrianCountMinimum = pedestrianCountMinimum;
    }

    public int getPedestrianCountMaximum() {
        return pedestrianCountMaximum;
    }

    public void setPedestrianCountMaximum(int pedestrianCountMaximum) {
        if (pedestrianCountMaximum < pedestrianCountMinimum) {
            System.out.println("invalid maximum number");
            return;
        }
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
//        String[] species = new String[Animal.specie.size()];                         //species
//        for (int i = 0; i < Animal.specie.size(); i++) {
//            species[i] = Animal.specie.get(i);
//        }
        String[] species = Animal.specie.toArray(new String[0]);

        age = random.nextInt(20);
        gender = genders[random.nextInt(genders.length)];
        bodyType = bodyTypes[random.nextInt(bodyTypes.length)];
        isPet = random.nextBoolean();
        specie = species[random.nextInt(species.length)];

        return new Animal(gender, bodyType, age, specie, isPet);
    }

    /**
     * generate scenarios randomly based on {@code passengerCountMinimum,
     * passengerCountMaximum, pedestrianCountMinimum, pedestrianCountMaximum}
     *
     * @author Fan Jia
     * @methodName generate
     * @return ethicalengine.Scenario
     */
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

        return new Scenario(passengers.toArray(new Character[0]), pedestrians.toArray(new Character[0]), isLegal);


    }

    /**
     * generate scenarios based on given index (from config file)
     *
     * @author Fan Jia
     * @methodName generate
     * @param scenarioInfo : several rows representing for a scenario
     * @param isLegal : if this scenario is legal
     * @param baseLineNumber : track number from the config
     * @return ethicalengine.Scenario
     *
     */
    public Scenario generate(ArrayList<String[]> scenarioInfo, boolean isLegal, int baseLineNumber) {
        ArrayList<Character> passenger = new ArrayList<>();
        ArrayList<Character> pedestrian = new ArrayList<>();

        for (String[] strings : scenarioInfo) {             //for each Character(row)

            boolean isPerson = strings[0].equals("person");

            Character.Gender gender;                        //common characteristics gender & age
            int age;
            Character.BodyType bodyType;                    //for Person
            Person.Profession profession;
            boolean pregnant;
            boolean isYou;
            String specie;                                  //for Animal
            boolean isPet;

            try {
                if (strings.length != 10) {
                    throw new InvalidDataFormatException(baseLineNumber);
                }

                try {
                    gender = Character.Gender.contains(strings[1]);
                } catch (InvalidCharacteristicException e) {
                    System.out.println(e.getMessage() + baseLineNumber);
                    gender = Character.Gender.UNKNOWN;
                }

                try {
                    age = Integer.parseInt(strings[2]);
                } catch (NumberFormatException e) {
                    System.out.println("WARNING: invalid number format in config file in line ");
                    age = Character.DEFAULT_AGE;

                }

                if (isPerson) {
                    try {
                        bodyType = Character.BodyType.contains(strings[3]);
                    } catch (InvalidCharacteristicException e) {
                        System.out.println(e.getMessage() + baseLineNumber);
                        bodyType = Character.BodyType.UNSPECIFIED;
                    }

                    if (strings[4].equals("")) {
                        profession = Person.Profession.NONE;
                    } else {
                        try {
                            profession = Person.Profession.contains(strings[4]);
                        } catch (InvalidCharacteristicException e) {
                            System.out.println(e.getMessage() + baseLineNumber);
                            profession = Person.Profession.UNKNOWN;
                        }
                    }

                    pregnant = strings[5].equals("yes") || strings[5].equals("true");
                    isYou = strings[5].equals("yes") || strings[5].equals("true");

                    Person person = new Person(gender, bodyType, age, pregnant, profession);
                    if (isYou) {
                        person.setAsYou(true);
                    }
                    if (strings[9].equals("passenger")) {       //as passenger
                        passenger.add(person);
                    } else {
                        pedestrian.add(person);
                    }

                } else {       //animal
                    specie = strings[7];
                    if (!Animal.specie.contains(strings[7])) {
                        Animal.specie.add(strings[7]);          //add new specie, assume also an animal name
                    }
                    isPet = strings[8].equals("yes") || strings[8].equals("true");
                    Animal animal = new Animal(specie);
                    if (isPet) {
                        animal.setPet(true);
                    }
                    if (strings[9].equals("passenger")) {      //as passenger
                        passenger.add(animal);
                    } else {
                        pedestrian.add(animal);
                    }
                }
            } catch (InvalidDataFormatException e) {
                System.out.println(e.getMessage() + baseLineNumber);
            }

        }

        Character[] passengerArray = passenger.toArray(new Character[0]);
        Character[] pedestrianArray = pedestrian.toArray(new Character[0]);

        return new Scenario(passengerArray, pedestrianArray, isLegal);

    }
    /**
     * support function, generate passenger and pedestrian list
     * for a random scenario
     *
     * @author Fan Jia
     * @methodName generateArrayList
     * @param range : upper bound of the group
     * @return java.util.ArrayList<ethicalengine.Character>
     * @see #generate()
     */
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
