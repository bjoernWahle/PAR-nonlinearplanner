package fib.par.nonlinearplanner.domain;

import fib.par.nonlinearplanner.Plan;
import fib.par.nonlinearplanner.Planner;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BlocksWorldInputParser parser = new BlocksWorldInputParser();
        String inputFileName = args[0];
        File file = openFile(inputFileName);
        Planner myPlanner = parser.readInputFile(file);
        myPlanner.findBestPlanWithRegression(100);
        if(myPlanner.planWasFound()) {
            Plan plan = myPlanner.bestPlan;
            boolean validPlan = myPlanner.verifyPlan(plan);
            System.out.println("Best plan is valid: "+validPlan);

            BlocksWorld.writeToOutputFile(myPlanner.generateOutput(), "output_"+inputFileName);
        } else {
            System.out.println("No plan could be found.");
        }
    }

    private static File openFile(String fileName) {
        URL resource = BlocksWorld.class.getResource("/"+ fileName);

        File file = null;
        try {
            file = Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static void analyzeRuntimeAndUsedOperators() {
        BlocksWorldInputParser parser = new BlocksWorldInputParser();
        List<String> problems = new LinkedList<>();
        problems.add("input");
        problems.add("input1");
        for (String problem : problems) {
            for(int i = 6; i >=3; i--) {
                String filename = "analysis/"+problem+"_"+i;
                File file = openFile(filename);
                Planner myPlanner = parser.readInputFile(file);
                long timeBefore = System.currentTimeMillis();
                myPlanner.findBestPlanWithRegression(100);
                long timeAfter = System.currentTimeMillis();
                if(myPlanner.verifyPlan(myPlanner.bestPlan)) {
                    long timeNeeded = timeAfter - timeBefore;
                    int numberOfOperators = myPlanner.bestPlan.operators.size();
                    System.out.println(problem+" with " +i +" columns took "+ timeNeeded + " ms and the optimal plan needs " + numberOfOperators+" operators.");
                }
            }
        }
        for(int i = 7; i >=4; i--) {
            String filename = "analysis/input2";
            filename = filename+"_"+i;
            File file = openFile(filename);
            Planner myPlanner = parser.readInputFile(file);
            long timeBefore = System.currentTimeMillis();
            myPlanner.findBestPlanWithRegression(100);
            long timeAfter = System.currentTimeMillis();
            if(myPlanner.verifyPlan(myPlanner.bestPlan)) {
                long timeNeeded = timeAfter - timeBefore;
                int numberOfOperators = myPlanner.bestPlan.operators.size();
                System.out.println("input2 with " +i +" columns took "+ timeNeeded + " ms and the optimal plan needs " + numberOfOperators+" operators.");
            }
        }
    }
}
