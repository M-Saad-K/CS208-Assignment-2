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

        // Do this method for all jobs, so to assign the minimum of all the jobs to the apporiate processors
        for(int i = 0; i < numberOfTasks; i++){
            // This method will find the minimum of all the job and their processes
            // Must set a process to assign & we need to assign the min time chosen at the selected job
            // We may need to return the min time from
            processorTimes[chosenProcessor] += findMinJob(etcMatrix, numberOfTasks-i, numberOfProcessors); //We do noofTask -1 to find reduce the array size
        }

        return processorTimes;
    }

    double findMinJob(double[][] etcMatrix, int numberOfTasks, int numberOfProcessors){
        // Most minimumJob
        int minJobInd = 0;

        // Array of all the elements that are done
        int[] scheduled = new int[numberOfTasks];

        // Array of all the processors doing each task properly
        int[] bestProcessors = new int[numberOfTasks];

        // Array of all the minimum times for each job
        double[] minJobs = new double[numberOfTasks];
        double currMin = Double.MAX_VALUE; // Store the min etc value

        // Find min execution time p/ job
        for(int i = 0; i < numberOfTasks; i++){
            // Check if scheduled
            if(scheduled[i] != 1){
                // Find best process for job
                for(int j = 0; j < numberOfProcessors; j++){
                    // Check for min
                    if(etcMatrix[j][i] < currMin){
                        currMin = etcMatrix[j][i]; // Store etc
                        bestProcessors[i] = j; // Best processor j for job i
                    }
                }
                // Put the currMin job for i inside i slot of minJobs
                minJobs[i] = currMin; // I is our index
                // Set the job
                scheduled[i] = 1;
            }
        }

        // Find the min overall & its corresponding job and corresponding processor
        /** Be cautious, I might make a mistake, so do this carefully
         * - Note to self
         * **/
        double minTemp = Double.MAX_VALUE; // This is temp for storing min value
        for(int i = 0; i < numberOfTasks; i++){
            if(minTemp < minJobs[i]){ // If last min is more than current
                minTemp = minJobs[i]; // Put current in temp
                minJobInd = i; // Store the current min index
            }
        } // After this loop is done, we would've gotten the min. job's index in variable

        // Now assign to finals
        chosenJob = minJobInd;
        chosenProcessor = bestProcessors[minJobInd]; // find the best job for the min job's index

        return minJobs[chosenJob];
    }

    @Override
    public String getName() {
        return "";
    }
}
