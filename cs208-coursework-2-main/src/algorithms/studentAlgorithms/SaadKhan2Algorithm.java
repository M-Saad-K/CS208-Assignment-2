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

            int[] result = findGlobalMinJob(bestLocalProcesses, scheduled, etcMatrix, numberOfTasks, processorTimes, numberOfProcessors);

            int globalMinProcessor = result[0];
            int globalMinJob = result[1];

            processorTimes[globalMinProcessor] += etcMatrix[globalMinProcessor][globalMinJob]; // Add onto global processTimes
            scheduled[globalMinJob] = 1; // Show the job as scheduled!
        }

        // This method equalises the work load of the processors, and then returns the processorTimes
        return equalisingProcessors(bestLocalProcesses, scheduled, etcMatrix, numberOfTasks, processorTimes, numberOfProcessors);
    }

    @Override
    public String getName() {
        return "";
    }

    int[] findGlobalMinJob(int[] bestLocalProcesses, int[] scheduled, double[][] etcMatrix, int numberofTasks, double[] processorTimes, int numberofProcessors) {

        int globalLocalMinProcessor = 0; // This will hold the minimum global processor
        int globalLocalMinJob = 0; // This will hold the minimum global job number

        // Most minimumJob
        int minJobInd = 0;

        // Array of all the processors doing each task properly
        int[] bestProcessors = new int[numberofTasks];

        // Array of all the minimum times for each job
        double[] minJobs = new double[numberofTasks];

        // Find min execution time p/ job
        for(int i = 0; i < numberofTasks; i++){
            // How to fix -> include matrix of number of process
            // Check if scheduled
            double currMin = Double.MAX_VALUE; // Store the min etc value

            if(scheduled[i] != 1){
                // Find best process for job
                for(int j = 0; j < numberofProcessors; j++){
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

        // A very important point, minJobs contains all the min values of avaiable jobs and 0 at the positions of scheduled jobs
        double minTemp = Double.MAX_VALUE; // This is temp for storing min value
        for(int i = 0; i < numberofTasks; i++){
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
        globalLocalMinJob = minJobInd;
        globalLocalMinProcessor = bestProcessors[minJobInd]; // find the best job for the min job's index
        // Set the job
        scheduled[minJobInd] = 1;

        return new int[]{globalLocalMinProcessor, globalLocalMinJob};// Found this neat trick of returning two statements as an array
    }

    double[] equalisingProcessors(int[] bestLocalProcesses, int[] scheduled, double[][] etcMatrix, int numberofTasks, double[] processorTimes, int numberofProcessors){
        // Need to make removed array to hold values that jobs that have been removed
        int[] removed = new int[numberofTasks];

        // Create a temp value to hold the previous makespan
        double prevMakeSpan = 0.0;
        for (double processorTime : processorTimes) {
            prevMakeSpan = Math.max(prevMakeSpan, processorTime);
        }
        // This loop will loop through each element of the processTimes array
        for(int i = 0; i < numberofProcessors; i++){

            // From this we will do a local search through a random point in array
            // To search for most occupied processor
            int startPos = rand.nextInt(numberofProcessors);

            // This is for finding the heavy local proc
            double heavyLocalProcDuration = 0.0;
            double lightestLocalProcDuration = Double.MAX_VALUE;
            int heavyProcessorIndex = 0;
            int lightestProcessorIndex = 0;


            // This for loop finds the most loadful processor
            for(int j = 1; j < 7; j++){ // Check 2 forward 2 back
                int forward = (startPos + j) % numberofProcessors; // Forward neighbour
                int backward = Math.floorMod(startPos - j, numberofProcessors); // Back neighbour

                /// Next we'll find the job with the heaviest load
                if(processorTimes[forward] > heavyLocalProcDuration){ // If forward more
                    heavyLocalProcDuration = processorTimes[forward];
                    heavyProcessorIndex = forward; // Get the processor that is the most heaviest!
                }

                if(processorTimes[backward] > heavyLocalProcDuration){ // If backward more
                    heavyLocalProcDuration = processorTimes[backward];
                    heavyProcessorIndex = backward; // Get the processor that is the most heaviest!
                }

                /// Next we'll find the job with the lightest load
                if(processorTimes[forward] < lightestLocalProcDuration){ // If forward more
                    lightestLocalProcDuration = processorTimes[forward];
                    lightestProcessorIndex = forward; // Get the processor that is the most lightest!
                }

                if(processorTimes[backward] < lightestLocalProcDuration){ // If backward more
                    lightestLocalProcDuration = processorTimes[backward];
                    lightestProcessorIndex = backward; // Get the processor that is the most lightest!
                }

            }

            // This then experiements!

            // Now we need to try and subtract each job from the processor with the most load
            // Store in some temp
            // Add that to the lightest processor
            // See if has improved the makespan

            for(int k = 0; k < etcMatrix[heavyProcessorIndex].length; k++){ // Iterate thru each task in heavyProcessorIndex
                // Continue only if it is going well and having some improvement!
                processorTimes[heavyProcessorIndex] -= etcMatrix[heavyProcessorIndex][k]; // Remove job from the processor times of the heaviest process
                processorTimes[lightestProcessorIndex] += etcMatrix[lightestProcessorIndex][k]; // Add job to the lightest processor
                // Check if makespan has improved!
                double newMakeSpan = analyseTempMakeSpan(processorTimes);
                if(newMakeSpan < prevMakeSpan){ // If newMakespan if faster than the previous make span
                    // Make the newMakeSpan, the previous makespan
                    prevMakeSpan = newMakeSpan;

                } else { // There has been no improvement
                    // Reject the changes
                    processorTimes[heavyProcessorIndex] += etcMatrix[heavyProcessorIndex][k]; // Add job back from the processor times of the heaviest process
                    processorTimes[lightestProcessorIndex] -= etcMatrix[lightestProcessorIndex][k]; // remove job to the lightest processor

                }
            }

        }

        // From this we would've gotten an improved ProcessorTimes array with a best optimised makespan, I think?
        return processorTimes;
    }

    double analyseTempMakeSpan(double[] ProcessTimes){ // Specialised process for checking test Makespan
        double newMakeSpan = 0.0;
        for (double processorTime : ProcessTimes) {
            newMakeSpan = Math.max(newMakeSpan, processorTime);
        }
        return newMakeSpan;
    }

    /*

    // This function find the heaviest job and returns the job duration
    double findHeaviestJobOnProcessor(int chosenProcessor, double[][] etcMatrix, int numberofTasks, int[] removed){
        ///  !!! To ensure a processor that has already been selected and still has some have jobs on it
        ///  Doesn't accidently choose the same job again, we will used a removed array

        // Using the etcMatrix at the index of the chosen processor
        // Search the most loadfully job
        int heaviestJobIn = 0;
        double max = 0.0;
        for(int i = 0; i < numberofTasks; i++){
            if(removed[i] == 1){ // Check if task is not removed

                if(max < etcMatrix[chosenProcessor][i]){
                    max = etcMatrix[chosenProcessor][i]; // Get the most beefiest duration, that isn't removed
                    heaviestJobIn = i; // Get the index of the heaviest job
                }
            } else {
                // If removed, don't do anything
                ;
            }

        }
        // Set the chosen job to be remove
        removed[heaviestJobIn] = 1;

        return max;
    } */

}
