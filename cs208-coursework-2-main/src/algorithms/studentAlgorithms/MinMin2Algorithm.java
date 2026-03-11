package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

public class MinMin2Algorithm extends SchedulingAlgorithm {

    int chosenProcessor = 0; // This will store the processor index
    int chosenJob = 0; // This is will store the job's index, e.g 0, 1, 2, 4

    @Override
    public double[] runAlgorithm(double[][] etcMatrix) {
        int numberOfProcessors = etcMatrix.length;
        int numberOfTasks = etcMatrix[0].length;
        double[] processorTimes = new double[numberOfProcessors];
        // Array of all the elements that are done
        int[] scheduled = new int[numberOfTasks];
            // Do this method for all jobs, so to assign the minimum of all the jobs to the apporiate processors
            for(int i = 0; i < numberOfTasks; i++){
                // This method will find the minimum of all the job and their processes
                // Must set a process to assign & we need to assign the min time chosen at the selected job
                // We may need to return the min time from
                // DEBUG!

                processorTimes[chosenProcessor] += findGlobalMinJob(etcMatrix, numberOfTasks, numberOfProcessors, scheduled, processorTimes); //We do noofTask -1 to find reduce the array size
                System.out.println("Chosen job is: Job" + chosenJob);
                System.out.println("Chosen processor is : " + chosenProcessor);
            }

        return processorTimes;
    }

    double findGlobalMinJob(double[][] etcMatrix, int numberOfTasks, int numberOfProcessors, int[] scheduled, double[] processorTimes){
        // Most minimumJob
        int minJobInd = 0;

        // Array of all the processors doing each task properly
        int[] bestProcessors = new int[numberOfTasks];

        // Array of all the minimum times for each job
        double[] minJobs = new double[numberOfTasks];

        // Find min execution time p/ job
        for(int i = 0; i < numberOfTasks; i++){
            // The issue is it is thinging in only Job0, not any other job
            // How to fix -> include matrix of number of process
            // Check if scheduled
            double currMin = Double.MAX_VALUE; // Store the min etc value

            if(scheduled[i] != 1){
                // Find best process for job
                for(int j = 0; j < numberOfProcessors; j++){
                    // Check for min
                    double completionTime = processorTimes[j] + etcMatrix[j][i]; // Completion time is the combined processTime and isolated duration of job

                    if(completionTime < currMin){ // We're actually comparing the currentMinimum time to the completion time
                        currMin = completionTime; // Store completion time
                        bestProcessors[i] = j; // Best processor j for job i
                    }
                }
                // Put the currMin job for i inside i slot of minJobs
                minJobs[i] = currMin; // I is our index
            } else {
                ; //Do nothing
            }
        }

        // Find the min overall & its corresponding job and corresponding processor
        /** Be cautious, I might make a mistake, so do this carefully
         * - Note to self
         * **/
        // A very important point, minJobs contains all the min values of avaiable jobs and 0 at the positions of scheduled jobs
        double minTemp = Double.MAX_VALUE; // This is temp for storing min value
        for(int i = 0; i < numberOfTasks; i++){
            if(scheduled[i] != 1){
                if(minTemp > minJobs[i]){ // If last min is more than current
                    minTemp = minJobs[i]; // Put current in temp
                    minJobInd = i; // Store the current min index
                }
            } else {
                ; // Do nothing
            }
        }

        // Now assign to finals
        chosenJob = minJobInd;
        chosenProcessor = bestProcessors[minJobInd]; // find the best job for the min job's index
        // Set the job
        scheduled[minJobInd] = 1;

        System.out.println("The current minimum time is: " + minJobs[chosenJob]);
        return etcMatrix[chosenProcessor][chosenJob];
    }

    @Override
    public String getName() {
        return "";
    }
}
