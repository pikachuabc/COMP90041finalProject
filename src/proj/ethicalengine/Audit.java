package proj.ethicalengine;
import proj.EthicalEngine;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;


/**
 * @description: run given/generated scenarios by program/user and record statistic
 * @author: Fan Jia
 */
public class Audit {

    private String auditType = "Unspecified";
    int totalRuns = 0;
    private int surviveTotalAge;
    private int personSurvivor;

    public ArrayList<Scenario> scenarios = new ArrayList<>();
    ArrayList<CharacteristicStatistic> characteristicStatistics = allCCharacteristic();

    public Audit() {
    }

    public Audit(Scenario[] scenariosArray) {
        Collections.addAll(this.scenarios, scenariosArray);
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }


    /**
     * @author: Fan Jia
     * @methodName: run
     * @description: run audit using given scenarios, judging by program or user
     *               depending on EthicalEngine.interactiveMode. In interactive
     *               mode, print statistic every 3 scenarios and ask if continue,
     *               thus [int]totalRuns is also used for detecting if it hs been 3
     *               scenarios, and record current scenario index in the array for
     *               next round playing.
     * @param: []
     * @return: void
     * @throw:
     */
    public void run() {

        if (EthicalEngine.interactiveMode) {        //user make choice
            for ( int i = this.totalRuns; i < scenarios.size(); i++) {  //resume last
                System.out.println(scenarios.get(i));
                boolean done = false;
                while (!done) {
                    try {
                        System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
                        String decision = EthicalEngine.sc.nextLine();
                        if (decision.equals("passenger") || decision.equals("passengers") || decision.equals("1")) {
                            recordThisScenario(scenarios.get(i), EthicalEngine.Decision.PASSENGERS);
                        } else if (decision.equals("pedestrian") || decision.equals("pedestrians") || decision.equals("2")) {
                            recordThisScenario(scenarios.get(i), EthicalEngine.Decision.PEDESTRIANS);
                        } else {
                            throw new InvalidInputException();
                        }
                        done = true;
                    } catch (InvalidInputException e) {
                        System.out.print(e.getMessage());
                    }
                }
                if (totalRuns !=0 && totalRuns%3==0) {          //if it has been 3 scenarios
                    printStatistic();
                    return;
                }
            }
        } else {        //program make choice
            for (Scenario scenario : scenarios) {
                EthicalEngine.Decision decision = EthicalEngine.decide(scenario);
                recordThisScenario(scenario, decision);
            }
        }
    }

    /**
     * @author: Fan Jia
     * @methodName: run
     * @description: run audit using self generated scenarios.
     * @param: [runs]
     * @return: void
     * @throw:
     */
    public void run(int runs) {

        ScenarioGenerator scenarioGenerator = new ScenarioGenerator();
        for (int i = 0; i < runs; i++) {        //generate [runs] scenarios
            scenarios.add(scenarioGenerator.generate());
        }
        run();

    }
    /**
     *@author: Fan Jia
     *@methodName: recordThisScenario
     *@description: for recording each scenario's each character's each characteristic in given decision
     *@param: [scenario, decision]
     *@return: void
     *@throw:
     */
    public void recordThisScenario(Scenario scenario, EthicalEngine.Decision decision) {

        this.totalRuns++;

        CharacteristicStatistic green = findCharacter("green");
        CharacteristicStatistic red = findCharacter("red");
        green.setTotalCase(scenarios.size());                   //scenarios.size() might change
        red.setTotalCase(scenarios.size());

        ArrayList<Character> pedestriansList = scenario.getPedestrians();
        ArrayList<Character> passengersList = scenario.getPassengers();

        if (scenario.isLegalCrossing()) {
            green.setTotalSurvive(green.getTotalSurvive() + 1);
        } else {
            red.setTotalSurvive(red.getTotalSurvive() + 1);
        }


        if (decision.equals(EthicalEngine.Decision.PASSENGERS)) { //save passengers
            count(passengersList, true);
            count(pedestriansList, false);
        } else {        //save pedestrians
            count(pedestriansList, true);
            count(passengersList, false);
        }
        characteristicStatistics.sort((o1, o2) -> {
            double o1Ratio = Double.parseDouble(o1.ratio());
            double o2Ratio = Double.parseDouble(o2.ratio());
            if (o1Ratio > o2Ratio) {
                return -1;
            } else if (o1Ratio < o2Ratio) {
                return 1;
            } else {
                return o1.getCharacteristicName().compareTo(o2.getCharacteristicName());
            }
        });
    }

    /**
     * @author: Fan Jia
     * @methodName: count
     * @description: used to record statistic index for each character's each characteristic
     * @param: [List, isSurvive]
     * @return: void
     * @throw:
     */
    private void count(ArrayList<Character> List, boolean isSurvive) {
        for (Character character1 : List) {
            if (character1.getClass().equals(Person.class)) {    //count person
                Person person = (Person) character1;
                String[] characters = person.toString().toLowerCase().split(" ");
                for (String character : characters) {           //for every person count their character
                    for (CharacteristicStatistic characteristicStatistic : characteristicStatistics) {
                        if (characteristicStatistic.getCharacteristicName().equals(character)
                                || characteristicStatistic.getCharacteristicName().equals("person")) {
                            characteristicStatistic.survive(isSurvive);
                        }
                    }
                }
                if (isSurvive) {
                    surviveTotalAge += character1.getAge();
                    personSurvivor++;
                }
            } else {                                //count animal
                Animal animal = (Animal) character1;
                String[] characters = animal.toString().toLowerCase().split(" ");
                for (String character : characters) {
                    for (CharacteristicStatistic characteristicStatistic : characteristicStatistics) {
                        if (characteristicStatistic.getCharacteristicName().equals(character)
                                || characteristicStatistic.getCharacteristicName().equals("animal")) {
                            characteristicStatistic.survive(isSurvive);
                        }
                    }
                }

            }
        }
    }

    /**
     *@author: Fan Jia
     *@methodName: allCCharacteristic
     *@description: this is a support function for generating all characteristic in one audit
     *@param: []
     *@return: java.util.ArrayList<proj.ethicalengine.CharacteristicStatistic>
     *@throw:
     */
    private ArrayList<CharacteristicStatistic> allCCharacteristic() {
        ArrayList<CharacteristicStatistic> characteristics = new ArrayList<>();
        //for Character
        Character.Gender[] genders = Character.Gender.values();                     //gender
        for (Character.Gender gender : genders) {
            if (!gender.equals(Character.Gender.UNKNOWN)) {
                characteristics.add(new CharacteristicStatistic(gender.toString().toLowerCase()));
            }
        }
        Character.BodyType[] bodyTypes = Character.BodyType.values();               //body type
        for (Character.BodyType bodyType : bodyTypes) {
            characteristics.add(new CharacteristicStatistic(bodyType.toString().toLowerCase()));
        }
        //for person
        Person.AgeCategory[] ageCategories = Person.AgeCategory.values();           //age category
        for (Person.AgeCategory ageCategory : ageCategories) {
            characteristics.add(new CharacteristicStatistic(ageCategory.toString().toLowerCase()));
        }
        Person.Profession[] professions = Person.Profession.values();               //profession
        for (Person.Profession profession : professions) {
            if (!profession.equals(Person.Profession.UNKNOWN) && !profession.equals(Person.Profession.NONE))
                characteristics.add(new CharacteristicStatistic(profession.toString().toLowerCase()));
        }
        characteristics.add(new CharacteristicStatistic("pregnant")); //pregnant

        characteristics.add(new CharacteristicStatistic("person"));   //class type
        characteristics.add(new CharacteristicStatistic("animal"));
        //for animal

        String[] species = new String[Animal.specie.size()];                         //species
        for (int i = 0; i < Animal.specie.size(); i++) {
            species[i] = Animal.specie.get(i);
        }
        for (String specie : species) {
            characteristics.add(new CharacteristicStatistic(specie.toLowerCase()));
        }
        characteristics.add(new CharacteristicStatistic("pets"));     //isPet
        //for scenario
        characteristics.add(new CharacteristicStatistic("red"));      //isLegal
        characteristics.add(new CharacteristicStatistic("green"));

        characteristics.add(new CharacteristicStatistic("you"));      //is you
        characteristics.add(new CharacteristicStatistic("age"));
        return characteristics;
    }

    private CharacteristicStatistic findCharacter(String name) {
        for (CharacteristicStatistic characteristicStatistic : characteristicStatistics) {
            if (characteristicStatistic.getCharacteristicName().equals(name)) {
                return characteristicStatistic;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (this.totalRuns == 0) {
            return "no audit available";
        }
        String summary = "";
        summary += "======================================" + "\n";
        summary += "# " + getAuditType() + " Audit" + "\n";
        summary += "======================================" + "\n";
        summary += "- % SAVED AFTER " + this.totalRuns + " RUNS" + "\n";
        for (CharacteristicStatistic characteristicStatistic : characteristicStatistics) {
            if (characteristicStatistic.getTotalCase() != 0) {
                summary += characteristicStatistic.toString();
            }
        }
        summary += "--" + "\n";
        summary += "average age: " + surviveTotalAge / personSurvivor + "\n";
        return summary;
    }

    public void printStatistic() {
        System.out.println(toString());
        System.out.println();
    }

    public void printToFile(String filepath) throws IOException {

        try {
            File myFile = new File(filepath);
            FileOutputStream fos = new FileOutputStream(myFile, true);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.US_ASCII);
            writer.write(toString());
            writer.close();
            fos.close();

        } catch (IOException e) {
            System.out.println("ERROR: could not print results. Target directory does not exist.");
        }

    }
}
