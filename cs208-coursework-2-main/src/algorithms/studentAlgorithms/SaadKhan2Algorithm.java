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

        for(int t = 0; t < numberOfTasks; t++){ // Iterate through the number of jobs

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

    void getBestLocalProcess(double[][] etcMatrix, int numberOfProcessors, int numberOfTasks, double[] processorTimes, int[] bestLocalProcesses) {

        // This will iterate through each task to develop the array of processors
        for(int i = 0; i < numberOfTasks; i++){

            int startPos = rand.nextInt(numberOfProcessors); // Get rand. starting position

            double minJobDur = processorTimes[startPos] + etcMatrix[startPos][i]; // Intialise minimum job's completion time

            bestLocalProcesses[i] = startPos;

            for(int j = 1; j < 5; j++){ // Iterate for 2 neighbours

                /*
                *               B <- B <- starting position - > F -> F
                *               1    2        3                 4    5
                * */

                int forward = (startPos + j) % numberOfProcessors; // Forward neighbour
                int backward = Math.floorMod(startPos - j, numberOfProcessors); // Back neighbour

                double forMinJobDur = processorTimes[forward] + etcMatrix[forward][i];
                double backMinJobDur = processorTimes[backward] + etcMatrix[backward][i];

                if(minJobDur > forMinJobDur){
                    minJobDur = forMinJobDur;
                    bestLocalProcesses[i] = forward;
                }

                if(minJobDur > backMinJobDur){
                    minJobDur = backMinJobDur;
                    bestLocalProcesses[i] = backward;
                }
            }
        } // By the end of the for loop - we would've gotten a nice array of best local processors for each job
    }

    int[] findGlobalMinJob(int[] bestLocalProcesses, int[] scheduled, double[][] etcMatrix, int numberofTasks, double[] processorTimes){

        double minJob = Double.MAX_VALUE;
        int globalMinProcessor = 0; // This will hold the minimum global processor
        int globalMinJob = 0; // This will hold the minimum global job number

        for(int i = 0; i < numberofTasks; i++){

            if(scheduled[i] != 1){ // If not scheduled job

                int p = bestLocalProcesses[i];
                double completionTime = processorTimes[p] + etcMatrix[p][i]; // Minimum completion time

                if(minJob > completionTime){ // Compare this with the minJob
                    minJob = completionTime;
                    globalMinProcessor = p; // Set the min process to the minimum one currently found
                    globalMinJob = i; // Set the min job to the minimum current found
                }
            }
        }

        return new int[]{globalMinProcessor, globalMinJob}; // Found this neat trick of returning two statements as an array
    }

    @Override
    public String getName() {
        return studentName;
    }
}