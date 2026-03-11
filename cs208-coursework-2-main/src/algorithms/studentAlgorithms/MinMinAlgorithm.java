
package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

public class MinMinAlgorithm extends SchedulingAlgorithm {
    private String name = "MinMinAlgorithm";
    // Create a varialbe for start point -> corresponding to the findMinJob method
    // Create a variable that coresponds to the processor that also iterates
    // Create a variale for the min time
    int apporiateProcessor = 0;
    double curMinTmeJob = 0.0;
    int jobsCompleted = 0;

    @Override
    public double[] runAlgorithm(double[][] etcMatrix) {
        int numberOfProcessors = etcMatrix.length;
        int numberOfTasks = etcMatrix[0].length;
        double[] processorTimes = new double[numberOfProcessors];

        for(int i = 0; i < numberOfProcessors; i++){ // Iterate for each processor

            processorTimes[apporiateProcessor] = findMinJob(etcMatrix, numberOfProcessors, numberOfTasks, jobsCompleted); // Find the min time and process of job;
            apporiateProcessor = 0;
            jobsCompleted++;
        }
        return processorTimes;
    }

    // Method takes the start point as a parameter
    public double findMinJob(double[][] etcMatrix, int noOfProcessors, int noOfJobs, int currIter){
        // CurrMinTimeJob will hold the smallest job currently
        // Array for if job is already done
        int[] scheduledJob = new int[noOfJobs];
        // Add to time Addon
        int[] procAddon = new int[noOfProcessors];
        double minJob = Double.MAX_VALUE;
        int minProc = 0;

        // Find the smallest job and its corresponding processor
        for(int i = 0; i < 16; i++){ // Process
            // Find the smallest overall job for each process and the smallest one
            for (int j = 0; j < 16; j++){ // Job
                if(!containsJob(j, scheduledJob)){
                    if(minJob > etcMatrix[i][j]){
                        minJob = etcMatrix[i][j];
                        minProc = i;
                        // Make the job done
                        scheduledJob[currIter] = j;
                    }
                }
            }
        }

        // Assign the final min Job and min process
        curMinTmeJob = minJob;
        apporiateProcessor = minProc;
        // Add the process's add on

        curMinTmeJob = 0.0; // Don't forget to unintialise these

        return procAddon[apporiateProcessor] += curMinTmeJob;

    }

    boolean containsJob(int jobNum, int[] arr){
        for (int curr : arr) {
            if (curr == jobNum) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }
}
