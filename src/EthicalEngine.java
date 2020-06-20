
import ethicalengine.*;
import ethicalengine.Character;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @description: EthicalEngine
 * @author: Fan Jia
 */
public class EthicalEngine {

    public static class InvalidInputException extends Exception {
        public InvalidInputException() {
            super("Invalid response. ");
        }
    }

    String configPath = null;
    String resultPath = null;
    public static boolean interactiveMode = false;
    public static Scanner sc = new Scanner(System.in);
    boolean collectDate = false;
    Scenario[] scenarios = null;        //store scenarios that from config file


    public enum Decision {
        PEDESTRIANS,
        PASSENGERS;
    }

    public static void main(String[] args) throws IOException {

        EthicalEngine ethicalEngine = new EthicalEngine();
        ethicalEngine.mainProcess(args);


    }


    /**
     * Initiate whole process depending on user input parameters
     *
     * @param args : system input
     * @return void
     * @throws IOException when something goes wrong while reading file
     * @author Fan Jia
     * @methodName mainProcess
     */
    public void mainProcess(String[] args) throws IOException {

        for (int i = 0; i < args.length; i++) {             //parse input args
            switch (args[i]) {
                case "--config":
                case "-c":
                    if (i != args.length - 1) {
                        configPath = args[i + 1];
                        scenarios = readConfig();           //read scenarios from config file
                    } else {
                        help(args);
                    }
                    break;
                case "--help":
                case "-h":
                    help(args);
                    break;
                case "--results":
                case "-r":
                    if (i != args.length - 1) {
                        resultPath = args[i + 1];
                    } else {
                        help(args);
                    }
                    break;
                case "--interactive":
                case "-i":
                    interactiveMode = true;
                    break;
            }
        }

        if (interactiveMode) {
            interActive(configPath != null);
        }

    }

    /**
     * Read scenarios data from .csv file and store in field {@link EthicalEngine#scenarios}
     *
     * @return ethicalengine.Scenario[]
     * @author Fan Jia
     * @methodName readConfig
     * @see ScenarioGenerator#generate(ArrayList, boolean, int)
     */
    public Scenario[] readConfig() {
        File file = new File(configPath);
        FileReader fr;
        BufferedReader reader;


        ArrayList<Scenario> scenarios = new ArrayList<>();
        ScenarioGenerator scenarioGenerator = new ScenarioGenerator();

        try {
            fr = new FileReader(file);
            reader = new BufferedReader(fr);
            int lineNumber = 1;
            reader.readLine();  //skip caption row
            lineNumber++;

            String line = reader.readLine();



            while (line != null) {

                String[] info = line.split(",", -1);
                if (info[0].contains("scenario:")) {
                    int baselineNumber = lineNumber;
                    lineNumber++;
                    ArrayList<String[]> scenarioInfo = new ArrayList<>();        //each character's information in this scenario
                    boolean isLegal = info[0].split(":")[1].contains("green");

                    while ((line = reader.readLine()) != null &&
                            !line.split(",", -1)[0].contains("scenario:")) {
                        info = line.split(",", -1);
                        scenarioInfo.add(info);
                        lineNumber++;
                    }

                    Scenario scenario = scenarioGenerator.generate(scenarioInfo, isLegal, baselineNumber);
                    scenarios.add(scenario);
                }
            }

            reader.close();
            fr.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: could not find config file.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scenarios.toArray(new Scenario[0]);
    }


    /**
     * @param configDate : decide if the program should generate scenarios for user
     * @return void
     * @throws IOException when something wrong while reading welcome file
     * @author Fan Jia
     * @methodName interActive
     */
    public void interActive(boolean configDate) throws IOException {

        File f = new File("welcome.ascii");
        FileInputStream fis = new FileInputStream(f);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.US_ASCII);
        BufferedReader reader = new BufferedReader(isr);
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        boolean done = false;
        while (!done) {
            try {
                System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
                String respond = sc.nextLine();
                if (respond.equals("yes")) {
                    collectDate = true;
                } else if (respond.equals("no")) {
                    collectDate = false;
                } else {
                    throw new InvalidInputException();
                }
                done = true;
            } catch (InvalidInputException e) {
                System.out.print(e.getMessage());
            }
        }

        Audit audit = new Audit();
        audit.setAuditType("User");
        ScenarioGenerator generator = new ScenarioGenerator();

        if (!configDate) {                     //add scenario from self-generated or config file
            for (int i = 0; i < 3; i++) {
                audit.scenarios.add(generator.generate());
            }
        } else {
            audit.scenarios.addAll(Arrays.asList(scenarios));
        }

        while (true) {
            if (!configDate) {                     //add scenario from self-generated or config file
                for (int i = 0; i < 3; i++) {
                    audit.scenarios.add(generator.generate());
                }
            }
            audit.run();                            //main process of making choice

            //ask user if continue after 3 times
            done = configDate && audit.totalRuns == audit.scenarios.size();
            while (!done) {
                try {
                    System.out.println("Would you like to continue? (yes/no)");
                    String respond = sc.nextLine();
                    if (respond.equals("no")) {
                        if (collectDate) {
                            audit.printToFile(resultPath);
                        }
                        System.exit(0);
                    } else if (!respond.equals("yes")) {
                        throw new InvalidInputException();
                    }
                    done = true;
                } catch (InvalidInputException e) {
                    System.out.print(e.getMessage());
                }
            }


            if (configDate && audit.totalRuns==audit.scenarios.size()) {                   //when user want to continue and no scenarios from config
                //audit.printStatistic();
                if (collectDate) {
                    audit.printToFile(resultPath);
                }
                System.out.println("That's all. Press Enter to quit.");
                sc.nextLine();
                System.exit(0);

            }
        }

    }

    /**
     * help info
     *
     * @author Fan Jia
     * @methodName help
     * @param args :
     * @return void
     */
    public void help(String[] args) {
        String help = "Usage: java EthicalEngine";
        for (String arg : args) {
            help += " " + arg;
        }
        help += "\n" + "Arguments:" + "\n" +
                " -c or --config      Optional: path to config file" + "\n" +
                " -h or --help        Print help (this message) and exit" + "\n" +
                " -r or --results     Optional: path to result log file" + "\n" +
                " -i or --interactive Optional: launches interactive mode";
        System.out.println(help);
        System.exit(0);
    }
    /**
     * let program decide which group to save depending on sum mark of each
     * group, for marking detail see {@link Character#getMark()}
     *
     * @author Fan Jia
     * @methodName decide
     * @param scenario : make decision for this scenario
     * @return EthicalEngine.Decision
     * @see Character#getMark()
     */
    public static Decision decide(Scenario scenario) {
        double passengerMark=0;
        double pedestrianMark=0;
        Character[] passengers = scenario.getPassengers();
        Character[] pedestrians = scenario.getPedestrians();

        for (Character passenger : passengers) {
            passengerMark += passenger.getMark();
        }
        for (Character pedestrian : pedestrians) {
            pedestrianMark += pedestrian.getMark();
        }


        if (scenario.isLegalCrossing()) {
            pedestrianMark += 5;
        } else {
            passengerMark+= 5;
        }


        if (passengerMark >= pedestrianMark) {
            return Decision.PASSENGERS;
        }
        return Decision.PEDESTRIANS;
    }


}
