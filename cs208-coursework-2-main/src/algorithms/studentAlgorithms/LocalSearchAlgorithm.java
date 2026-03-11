package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

import java.util.Random;

public class LocalSearchAlgorithm extends SchedulingAlgorithm {

    int apporiateProcessor = 0;
    double curMinTmeJob = 0.0;
    Random rand = new Random();

    @Override
    public double[] runAlgorithm(double[][] etcMatrix) {

        int numberOfProcessors = etcMatrix.length;
        int numberOfTasks = etcMatrix[0].length;
        double[] processorTimes = new double[numberOfProcessors];

        for(int i = 0; i < numberOfProcessors; i++){ // Iterate for each processor

            processorTimes[apporiateProcessor] = findBestTimeLocal(i, etcMatrix, numberOfProcessors); // Find the min time and process of job;
            apporiateProcessor = 0;
        }

        return processorTimes;
    }

    // This method will find and return the best local job to be sent on the apporiate processor
    int findBestTimeLocal(int currentJob, double[][] etcMatrix, int noOfProcessors){
        int maxRange = currentJob + 16;
        int minRange = maxRange-16;
        int startPoint = rand.nextInt(maxRange - minRange + 1) + minRange;
        double minJob = Double.MAX_VALUE;
        // Choose a random point inside the current job
        // Iterate through at least 5 iterations to find minimum and store in minProc
        for(int i = 0; i < 5; i++){
            if(minJob > etcMatrix[(startPoint+i)%16][currentJob]){
                minJob = etcMatrix[(startPoint+i)%16][i];
                apporiateProcessor = (startPoint+i)%16;
            }
        }
        curMinTmeJob = minJob; // Compute minimum job found
        int[] procAddon = new int[noOfProcessors];

        // Compute the processor of the min time, and store it in approriate processor
        procAddon[apporiateProcessor] += curMinTmeJob;
        curMinTmeJob = 0.0;
        apporiateProcessor = 0;
        // Return minProc
        return procAddon[apporiateProcessor];
    }

    @Override
    public String getName() {
        return "";
    }
}
