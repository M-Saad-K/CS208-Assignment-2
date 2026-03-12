package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

import java.util.Random;

public class LocalSearch2Algorithm extends SchedulingAlgorithm {

    // Set up a random point to access in the file
    Random rand = new Random();

    @Override
    public double[] runAlgorithm(double[][] etcMatrix) {
        int numberOfProcessors = etcMatrix.length;
        int numberOfTasks = etcMatrix[0].length;
        double[] processorTimes = new double[numberOfProcessors];
        // Array that will store all the chosen processes, with index of chosen job
        int[] chosenProcOfJobIn = new int[numberOfTasks];
        // Scheduled list
        int[] scheduled = new int[numberOfTasks];

        // Next now iterate through each task
        for(int i = 0; i < numberOfTasks; i++){
            // Intialise these every time
            // Current job
            int bestLocalProcess = getBestLocalProcess(etcMatrix, i, numberOfProcessors, processorTimes);
            // Now we've found the local minJob execution time of Job i
            chosenProcOfJobIn[i] = bestLocalProcess;

        }
        if(int i = 0; i < numberOfTasks; i++){

            int currMin =
            if(!scheduled[currMin]){
                processorTimes[bestLocalProcess] += etcMatrix[bestLocalProcess][i];
            }
        }

        processorTimes[bestLocalProcess] += etcMatrix[bestLocalProcess][i];
        // After loop ends, we've now gotten all the local mintime jobs's indexes in an array
        // Now we need to add each processes to the current processorTimes

        return processorTimes;
    }

    int getBestLocalProcess(double[][] etcMatrix, int i, int numberOfProcessors, double[] processorTimes) {
        // Get random starting positon
        int startPos = rand.nextInt(numberOfProcessors);
        System.out.println("startPos: " + startPos);
        System.out.println(startPos);

        double minJobDur = processorTimes[startPos] + etcMatrix[startPos][i]; // Get the chosen processor for the current task i

        /// Important, this will store which processor was chosen for job i
        int bestLocalProcess = startPos;

        // Iterating through the processor list for only two steps out
        for(int j = 1; j < 5; j++){
            int forward = (startPos+j)% numberOfProcessors;
            int backward = Math.floorMod(startPos - j, numberOfProcessors); // Java can't handle -1 using %,

            // Calculating the forward & backward completion times
            double forMinJobDur = processorTimes[forward] + etcMatrix[forward][i];
            double backMinJobDur = processorTimes[backward] + etcMatrix[backward][i];

            // I learned this the hard way
            System.out.println("Front position: " + forward);
            System.out.println("Back position: " + backward);

            if(minJobDur > forMinJobDur){
                // If the forward neighbour is min
                minJobDur = forMinJobDur;
                bestLocalProcess = forward; // Get the best process
            }

            if (minJobDur > backMinJobDur) {
                // If the backward neighbour is min
                minJobDur = backMinJobDur;
                bestLocalProcess = backward; // Get the best process
            }
        }
        return bestLocalProcess;
    }

    @Override
    public String getName() {
        return "";
    }
}
