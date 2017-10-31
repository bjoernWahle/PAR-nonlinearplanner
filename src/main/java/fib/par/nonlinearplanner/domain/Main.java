package fib.par.nonlinearplanner.domain;

import fib.par.nonlinearplanner.Plan;
import fib.par.nonlinearplanner.Planner;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        BlocksWorldInputParser parser = new BlocksWorldInputParser();

        String inputFileName = "input";

        URL resource = BlocksWorld.class.getResource("/"+inputFileName);

        File file = null;
        try {
            file = Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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
}
