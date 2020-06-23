import ethicalengine.*;
import ethicalengine.Character;


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

    public ArrayList<Scenario> scenarios = new ArrayList<>();   //all scenarios in this audit stored here
    ArrayList<CharaStatistic> charaStatistics = allCCharacteristic();

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
     * run audit using given scenarios(from randomly generate or config file),
     * judging by program or user depending on EthicalEngine.interactiveMode.
     * In interactive mode, print statistic every 3 scenarios and ask if continue,
     * thus [int]totalRuns is also used for detecting if it hs been 3 scenarios,
     * and record current scenario index in the array for next round playing.
     * use {@link #recordThisScenario(Scenario, EthicalEngine.Decision)}
     * to record statistic
     *
     * @author Fan Jia
     * @methodName run
     * @return void
     * @see #recordThisScenario(Scenario, EthicalEngine.Decision)
     */
    public void run() {

        if (EthicalEngine.interactiveMode) {        //user make choice
            for ( int i = this.totalRuns; i < scenarios.size(); i++) {  //resume last
                System.out.print(scenarios.get(i));
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
                            throw new EthicalEngine.InvalidInputException();
                        }
                        done = true;
                    } catch (EthicalEngine.InvalidInputException e) {
                        System.out.print(e.getMessage());
                    }
                }
                if ((totalRuns !=0 && totalRuns%3==0) || totalRuns==scenarios.size()) {          //if it has been 3 scenarios
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
     * generate {@code runs} random scenarios and run
     *
     * @author Fan Jia
     * @methodName run
     * @param runs : this audit will run {@code runs} random scenarios
     * @return void
     * @see Audit#run()
     * @see ScenarioGenerator#generate()
     */
    public void run(int runs) {

        ScenarioGenerator scenarioGenerator = new ScenarioGenerator();
        for (int i = 0; i < runs; i++) {        //generate [runs] scenarios
            scenarios.add(scenarioGenerator.generate());
        }
        run();

    }
    /**
     *  for recording each scenario's each character's each characteristic in given decision
     *
     * @author Fan Jia
     * @methodName recordThisScenario
     * @param scenario : scenario
     * @param decision : decision made by usr or program
     * @return void
     */
    public void recordThisScenario(Scenario scenario, EthicalEngine.Decision decision) {

        this.totalRuns++;

        CharaStatistic green = CharaStatistic.findCharacter("green", charaStatistics);
        CharaStatistic red = CharaStatistic.findCharacter("red", charaStatistics);
        assert green != null;
        green.setTotalCase(scenarios.size());                   //scenarios.size() might change
        assert red != null;
        red.setTotalCase(scenarios.size());

        ArrayList<ethicalengine.Character> pedestriansList = new ArrayList<>();
        ArrayList<ethicalengine.Character> passengersList = new ArrayList<>();
        Collections.addAll(pedestriansList, scenario.getPedestrians());
        Collections.addAll(passengersList, scenario.getPassengers());


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

    }

    /**
     * this is a support function for recording statistic for each character's each characteristic
     *
     * @author Fan Jia
     * @methodName count
     * @param List : pedestrian or passenger
     * @param isSurvive : if survive
     * @return void
     */
    private void count(ArrayList<ethicalengine.Character> List, boolean isSurvive) {
        for (ethicalengine.Character character1 : List) {
            if (character1.getClass().equals(Person.class)) {    //count person
                Person person = (Person) character1;
                String[] characters = person.toString().toLowerCase().split(" ");
                for (String character : characters) {           //for every person count their character
                    for (CharaStatistic charaStatistic : charaStatistics) {
                        if (charaStatistic.getCharacteristicName().equals(character)
                                || charaStatistic.getCharacteristicName().equals("person")) {
                            charaStatistic.survive(isSurvive);
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
                    for (CharaStatistic charaStatistic : charaStatistics) {
                        if (charaStatistic.getCharacteristicName().equals(character)
                                || charaStatistic.getCharacteristicName().equals("animal")) {
                            charaStatistic.survive(isSurvive);
                        }
                    }
                }

            }
        }
    }

    /**
     * this is a support function for generating all characteristic in one audit.
     * due to characteristic may changed (add or delete), each audit should call
     * this function to acquire current characteristic
     *
     * @author Fan Jia
     * @methodName allCCharacteristic
     * @return java.util.ArrayList<ethicalengine.CharaStatistic>
     */
    private ArrayList<CharaStatistic> allCCharacteristic() {
        ArrayList<CharaStatistic> characteristics = new ArrayList<>();
        //for Character
        ethicalengine.Character.Gender[] genders = ethicalengine.Character.Gender.values();               //gender
        for (ethicalengine.Character.Gender gender : genders) {
            if (!gender.equals(ethicalengine.Character.Gender.UNKNOWN)) {
                characteristics.add(new CharaStatistic(gender.toString().toLowerCase()));
            }
        }
        ethicalengine.Character.BodyType[] bodyTypes = ethicalengine.Character.BodyType.values();         //body type
        for (Character.BodyType bodyType : bodyTypes) {
            characteristics.add(new CharaStatistic(bodyType.toString().toLowerCase()));
        }
        //for person
        Person.AgeCategory[] ageCategories = Person.AgeCategory.values();                                 //age category
        for (Person.AgeCategory ageCategory : ageCategories) {
            characteristics.add(new CharaStatistic(ageCategory.toString().toLowerCase()));
        }
        Person.Profession[] professions = Person.Profession.values();                                     //profession
        for (Person.Profession profession : professions) {
            if (!profession.equals(Person.Profession.UNKNOWN) && !profession.equals(Person.Profession.NONE))
                characteristics.add(new CharaStatistic(profession.toString().toLowerCase()));
        }
        characteristics.add(new CharaStatistic("pregnant"));                              //pregnant


        //for animal
        String[] species = new String[Animal.specie.size()];                  //species
        for (int i = 0; i < Animal.specie.size(); i++) {
            species[i] = Animal.specie.get(i);
        }
        for (String specie : species) {
            characteristics.add(new CharaStatistic(specie.toLowerCase()));
        }
        characteristics.add(new CharaStatistic("pet"));     //isPet

        //for scenario
        characteristics.add(new CharaStatistic("person"));   //class type
        characteristics.add(new CharaStatistic("animal"));
        characteristics.add(new CharaStatistic("red"));      //isLegal
        characteristics.add(new CharaStatistic("green"));
        characteristics.add(new CharaStatistic("you"));      //is you
        characteristics.add(new CharaStatistic("age"));      //age
        return characteristics;
    }


    /**
     * format output
     *
     * @author Fan Jia
     * @methodName toString
     * @return java.lang.String
     */
    @Override
    public String toString() {
        if (this.totalRuns == 0) {
            return "no audit available";
        }
        charaStatistics.sort((o1, o2) -> {
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

        String summary = "";
        summary += "======================================" + "\n";
        summary += "# " + getAuditType() + " Audit" + "\n";
        summary += "======================================" + "\n";
        summary += "- % SAVED AFTER " + this.totalRuns + " RUNS" + "\n";
        for (CharaStatistic charaStatistic : charaStatistics) {
            if (charaStatistic.getTotalCase() != 0) {
                summary += charaStatistic.toString();
            }
        }
        summary += "--" + "\n";
        String avgAge = String.format("%.1f",(double)surviveTotalAge / personSurvivor);
        summary += "average age: " + avgAge;
        return summary;
    }

    /**
     * command line output
     *
     * @author Fan Jia
     * @methodName printStatistic
     * @return void
     * @see #toString()
     */
    public void printStatistic() {
        System.out.println(toString());
    }

    /**
     * print statistic to file
     *
     * @author Fan Jia
     * @methodName printToFile
     * @param filepath : path to save the log file
     * @return void
     */
    public void printToFile(String filepath) {

        try {
            if (filepath != null) {

                OutputStreamWriter writer = new OutputStreamWriter(
                        new FileOutputStream(filepath,true),
                        StandardCharsets.US_ASCII);
                writer.write(toString());
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("ERROR: could not print results. Target directory does not exist.");
        }

    }
}
