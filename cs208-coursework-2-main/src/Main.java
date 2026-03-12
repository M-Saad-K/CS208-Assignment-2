import algorithms.SchedulingAlgorithm;
import algorithms.studentAlgorithms.*;
import utilities.AlgorithmEvaluator;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        AlgorithmEvaluator algorithmEvaluator = new AlgorithmEvaluator();
        SchedulingAlgorithm algorithm = new SaadKhan2Algorithm();

        if (algorithm.getName().equals("Please Enter Your Name Here")) {
            System.out.println("Invalid Name: Please enter your name into the YourAlgorithm file, " +
                    "and change the name of that file to FirstNameLastNameAlgorithm");
            System.out.println("You will not be able to submit your code until you do this.");
        } else {

            // If you would like to see a chart of your progress over time, set this to true
            boolean viewChart = true;

            double grade = 0.0;

            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_c_hihi.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_c_hilo.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_c_lohi.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_c_lolo.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_i_hihi.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_i_hilo.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_i_lohi.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_i_lolo.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_s_hihi.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_s_hilo.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_s_lohi.0");
            grade += algorithmEvaluator.evaluateAlgorithm(algorithm, "resources/u_s_lolo.0");

            grade = Math.round(grade / 12);

            System.out.println("Grade = " + grade);

            algorithmEvaluator.evaluateSolution(grade, algorithm);

            if (viewChart) {
                displayChart(algorithm);
            }

        }
    }

    private static void displayChart(SchedulingAlgorithm algorithm) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new GradeChartViewer(algorithm.getName() + "Results.txt").setVisible(true);
        });
    }
}