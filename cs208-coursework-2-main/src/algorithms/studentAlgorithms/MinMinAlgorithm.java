
package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

public class MinMinAlgorithm extends SchedulingAlgorithm {
    private String name = "MinMinAlgorithm";

    int apporiateProcessor = 0; // Variable for holding the best processor for the current minimum etc time
    double curMinTmeJob = 0; // Varaible for holding the current minimum etc time

    @Override
    public double[] runAlgorithm(double[][] etcMatrix) {

        int jobsCompleted = 0; // Follows how many jobs currently completed
        int numberOfProcessors = etcMatrix.length;
        int numberOfTasks = etcMatrix[0].length;
        double[] processorTimes = new double[numberOfProcessors]; // Holds the total makespan waiting time for each processor
        int[] scheduledJob = new int[numberOfTasks]; // Holds which jobs have already been scheduled, to ensure jobs aren't
        // scheduled again.

        while(jobsCompleted < numberOfTasks){ // Iterate for each processor

            // FindMinJob finds the min process and its approriate processor and then returns the total completionTime of a process
            findMinJob(etcMatrix, numberOfProcessors, numberOfTasks, jobsCompleted, scheduledJob, processorTimes);
            processorTimes[apporiateProcessor] = curMinTmeJob;
            jobsCompleted++;
        }
        return processorTimes;
    }


    public void findMinJob(double[][] etcMatrix, int noOfProcessors, int noOfJobs, int currIter, int[] scheduledJob, double[] processorTimes){
        // currIteration parameter is just to allow a scheduled job to take a space in scheduledJob array
        // This will be our return value of the process times
        double mostMinJob = Double.MAX_VALUE;
        int mostApporiateProcessor = 0;
        int selectedJob = 0;

            // Finds the best processor for each job
            for (int j = 0; j < noOfJobs; j++) {

                if (!containsJob(j, scheduledJob)) { // Only do this for unscheduled jobs

                    double minCompletionTime = Double.MAX_VALUE;

                    for (int i = 0; i < noOfProcessors; i++) {

                        double completionTime = processorTimes[i] + etcMatrix[i][j];

                        if (completionTime < minCompletionTime) {
                            minCompletionTime += completionTime;
                            apporiateProcessor = i;
                        }
                    }

                    if (minCompletionTime < mostMinJob) {
                        mostMinJob = minCompletionTime; // Getting the most minimal job
                        selectedJob = j; // Getting the best chosen job with the best chosen processor
                        mostApporiateProcessor = apporiateProcessor; // basically, very bad naming convention but this stores the best processor for the min job as the selected processor
                    }
                }
            }
            scheduledJob[currIter] = selectedJob; // Put the selected job in the scheduled zone
            curMinTmeJob = etcMatrix[mostApporiateProcessor][selectedJob]; // Found the most minimum time
            // This will be passed into processor times
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
