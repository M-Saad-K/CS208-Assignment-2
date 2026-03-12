package utilities;

import algorithms.SchedulingAlgorithm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;


public class AlgorithmEvaluator {

    private final static SchedulingResults RESULTS = new SchedulingResults();

    private final JobReader jobReader;
    private String pathToFile;

    public AlgorithmEvaluator() {
        jobReader = new JobReader();
        pathToFile = "resources/u_c_hihi.0";
    }

    public double evaluateAlgorithm(SchedulingAlgorithm algorithm, String pathToFile) {
        this.pathToFile = pathToFile;
        jobReader.readFileIntoETCMatrix(pathToFile);
        double grade = 0.0;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<double[]> result = executorService.submit(() ->
                algorithm.runAlgorithm(jobReader.getEtcMatrix()));

        try {
            double[] processorTimes = result.get(270, TimeUnit.SECONDS);

            double makespan = calculateMakespan(processorTimes);

            grade = calculateGrade(makespan);
        } catch (TimeoutException e) {
            System.out.println("Algorithm time exceeded!");
            result.cancel(true);
            grade = 0.0;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        printGradeEvaluation(grade, algorithm);
        return grade;
    }

    private double calculateMakespan(double[] processorTimes) {
        double makespan = 0;
        for (double processorTime : processorTimes) {
            makespan = Math.max(makespan, processorTime);
        }
        return makespan;
    }

    private double calculateGrade(double makespan) {
        double grade;
        double baselineMakeSpan = RESULTS.getResults().get(pathToFile).get(0);
        double lowestMakeSpan = RESULTS.getResults().get(pathToFile).get(1);
        double highestMakeSpan = RESULTS.getResults().get(pathToFile).get(2);
        double optimalMakeSpan = RESULTS.getResults().get(pathToFile).get(3);

        System.out.println("Makespan is : " + makespan);

        if (makespan < optimalMakeSpan) {
            grade = 0;
            System.out.println("Your makespan is lower than the optimal solution. " +
                    "This likely means that your solution isn't correct. Make sure all jobs are being added to the processors");
            return grade;
        }

        if (makespan > baselineMakeSpan) {
            return Math.round((highestMakeSpan - makespan) / (highestMakeSpan - baselineMakeSpan) * 40);
        } else if (makespan > lowestMakeSpan) {
            grade = 100 - ((makespan - lowestMakeSpan) / (baselineMakeSpan - lowestMakeSpan)) * 60;
            return Math.round(grade);
        }
        grade = 100;
        return Math.round(grade);
    }

    private void printGradeEvaluation(double grade, SchedulingAlgorithm algorithm) {
        System.out.println("------------------------------");
        System.out.println("Hello " + algorithm.getName() + "!");
        System.out.println("File selected: " + pathToFile);
        System.out.println("Your algorithm would currently achieve a grade of " + grade + "%");
        System.out.println("NOTE: This is not an official grade, only an estimate.");
        System.out.println("------------------------------");
    }

    public void evaluateSolution(double grade, SchedulingAlgorithm algorithm) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timestamp = now.format(formatter);
        String moduleCode = "CS208";

        String line = moduleCode + ", " + timestamp + ", " + grade + "\n";

        try {
            Files.write(Paths.get(algorithm.getName() + "Results.txt"), line.getBytes(), StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

            // TODO: Add in SSH call to remote server

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
