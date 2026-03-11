package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

import java.util.Random;

public class LocalSearchAlgorithm extends SchedulingAlgorithm {

    int apporiateProcessor = 0;
    double curMinTimeJob = 0.0;
    Random rand = new Random();

    @Override
    public double[] runAlgorithm(double[][] etcMatrix) {

        int numberOfProcessors = etcMatrix.length;
        int numberOfTasks = etcMatrix[0].length;
        double[] processorTimes = new double[numberOfProcessors];

        for(int i = 0; i < numberOfTasks; i++){ // Iterate for each task
            findBestTimeLocal(i, etcMatrix, numberOfProcessors); // Find the min time and process of job;
            processorTimes[apporiateProcessor] = curMinTimeJob;
        }

        return processorTimes;
    }

    // This method will find and return the best local job to be sent on the apporiate processor
    void findBestTimeLocal(int currentJob, double[][] etcMatrix, int noOfProcessors){
        int minRange = currentJob*noOfProcessors;
        int maxRange = minRange+noOfProcessors;
        int startPoint = rand.nextInt(maxRange - minRange + 1) + minRange;
        double minJob = Double.MAX_VALUE;
        // Choose a random point inside the current job
        // Iterate through at least 5 iterations to find minimum and store in minProc
        for(int i = 0; i < 5; i++){
            if(minJob > etcMatrix[noOfProcessors][(startPoint+i)%currentJob]){
                minJob = etcMatrix[noOfProcessors][(startPoint+i)%currentJob];
                apporiateProcessor = (startPoint+i)%noOfProcessors;
            }
        }
        curMinTimeJob = minJob; // Compute minimum job found
        // Compute the processor of the min time, and store it in approriate processor
    }

    @Override
    public String getName() {
        return "";
    }
}
