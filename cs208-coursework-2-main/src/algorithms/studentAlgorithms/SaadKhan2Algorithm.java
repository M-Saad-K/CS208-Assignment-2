package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

import java.util.Random;

public class SaadKhan2Algorithm extends SchedulingAlgorithm {

    private String studentName = "Muhammad Saad Khan";

    Random rand = new Random();

    @Override
    public double[] runAlgorithm(double[][] etcMatrix) {
        int numberOfProcessors = etcMatrix.length; // Number of processors
        int numberOfTasks = etcMatrix[0].length; // Number of tasks
        double[] processorTimes = new double[numberOfProcessors]; // This is the duration of the processor

        int[] bestLocalProcesses = new int[numberOfTasks]; // This is the array that will store all best local processes
        // for each job
        int[] scheduled = new int[numberOfTasks]; // This will hold scheduled jobs, at their index number

        scheduled = new int[numberOfTasks];
        bestLocalProcesses = new int[numberOfTasks];
        processorTimes = new double[numberOfProcessors];

        for (int t = 0; t < numberOfTasks; t++) { // Iterate through the number of jobs

            ///  Weird behavior from here!
            // This will develop the bestLocalProcesses array
            getBestLocalProcess(etcMatrix, numberOfProcessors, numberOfTasks, processorTimes, bestLocalProcesses);

            int[] result = findGlobalMinJob(bestLocalProcesses, scheduled, etcMatrix, numberOfTasks, processorTimes);

            int globalMinProcessor = result[0];
            int globalMinJob = result[1];

            processorTimes[globalMinProcessor] += etcMatrix[globalMinProcessor][globalMinJob]; // Add onto global processTimes
            scheduled[globalMinJob] = 1; // Show the job as scheduled!
        }

        return processorTimes;
    }

    @Override
    public String getName() {
        return "";
    }

    void getBestLocalProcess(double[][] etcMatrix, int numberOfProcessors, int numberOfTasks, double[] processorTimes, int[] bestLocalProcesses) {

        // This will iterate through each task to develop the array of processors
        for (int i = 0; i < numberOfTasks; i++) {

            int startPos = rand.nextInt(numberOfProcessors); // Get rand. starting position

            double minJobDur = processorTimes[startPos] + etcMatrix[startPos][i]; // Intialise minimum job's completion time

            bestLocalProcesses[i] = startPos;

            for (int j = 1; j < 5; j++) { // Iterate for 2 neighbours

                /*
                 *               B <- B <- starting position - > F -> F
                 *               1    2        3                 4    5
                 * */

                int forward = (startPos + j) % numberOfProcessors; // Forward neighbour
                int backward = Math.floorMod(startPos - j, numberOfProcessors); // Back neighbour

                double forMinJobDur = processorTimes[forward] + etcMatrix[forward][i];
                double backMinJobDur = processorTimes[backward] + etcMatrix[backward][i];

                if (minJobDur > forMinJobDur) {
                    minJobDur = forMinJobDur;
                    bestLocalProcesses[i] = forward;
                }

                if (minJobDur > backMinJobDur) {
                    minJobDur = backMinJobDur;
                    bestLocalProcesses[i] = backward;
                }
            }
        } // By the end of the for loop - we would've gotten a nice array of best local processors for each job
    }

    int[] findGlobalMinJob(int[] bestLocalProcesses, int[] scheduled, double[][] etcMatrix, int numberofTasks, double[] processorTimes) {

        /*double minJobDur = Double.MAX_VALUE;
        int globalLocalMinProcessor = 0;
        int globalLocalMinJob = 0;

        for (int i = 0; i < numberofTasks / 2; i++) {// Iterate for 2 neighbours
            int startPos = rand.nextInt(numberofTasks);

            int forward = (startPos + i) % numberofTasks; // Forward neighbour
            int backward = Math.floorMod(startPos - i, numberofTasks); // Back neighbour

            double forMinJobDur = processorTimes[forward] + etcMatrix[forward][i];
            double backMinJobDur = processorTimes[backward] + etcMatrix[backward][i];

            if (minJobDur > forMinJobDur) {
                minJobDur = forMinJobDur;
                bestLocalProcesses[i] = forward;
            }

            if (minJobDur > backMinJobDur) {
                minJobDur = backMinJobDur;
                bestLocalProcesses[i] = backward;
            }
        } */

        double minJobDur = Double.MAX_VALUE;
        int globalLocalMinProcessor = 0; // This will hold the minimum global processor
        int globalLocalMinJob = 0; // This will hold the minimum global job number

        for (int i = 0; i < numberofTasks; i++) { // Iterate through the number of tasks

            if (scheduled[i] != 1) { // Check if the task isn't scheduled already

                int p = bestLocalProcesses[i]; // Get the best local process for that task

                double completionTime = processorTimes[p] + etcMatrix[p][i]; // Get completion time of ETC

                if (completionTime < minJobDur) { // min completion time globally
                    minJobDur = completionTime;
                    globalLocalMinProcessor = p;
                    globalLocalMinJob = i;
                }
            }
        }

        return new int[]{globalLocalMinProcessor, globalLocalMinJob};// Found this neat trick of returning two statements as an array
    }

}
