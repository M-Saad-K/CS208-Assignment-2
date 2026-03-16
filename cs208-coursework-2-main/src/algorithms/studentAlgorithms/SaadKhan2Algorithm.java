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

        // This method equalises the work load of the processors, and then returns the processorTimes
        return equalisingProcessors(bestLocalProcesses, scheduled, etcMatrix, numberOfTasks, processorTimes, numberOfProcessors);
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

            for (int j = 1; j < 12; j++) { // Iterate for 2 neighbours

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
            int heavyProcessorIndex = 0;

            // This for loop finds the most loadful processor
            for(int j = 1; j < 12; j++){ // Check 2 forward 2 back
                int forward = (startPos + j) % numberofProcessors; // Forward neighbour
                int backward = Math.floorMod(startPos - j, numberofProcessors); // Back neighbour

                // Next we'll find the job with the heaviest load
                if(processorTimes[forward] > heavyLocalProcDuration){ // If forward more
                    heavyLocalProcDuration = processorTimes[forward];
                    heavyProcessorIndex = forward; // Get the processor that is the most heaviest!
                }

                if(processorTimes[backward] > heavyLocalProcDuration){ // If backward more
                    heavyLocalProcDuration = processorTimes[backward];
                    heavyProcessorIndex = backward; // Get the processor that is the most heaviest!
                }

            }

            // This then experiements!

            // Now we need to subtract the job with the most load from chosen processor
            // Store in some temp
            // Add that to the current processor

            // Use this func to find the heaviest job
            double heaviestJob = findHeaviestJobOnProcessor(heavyProcessorIndex, etcMatrix, numberofTasks, removed);

            processorTimes[heavyProcessorIndex] -= heaviestJob; // Remove loadful job
            processorTimes[i] += heaviestJob; // Add heaviest job to the current processor

            // Run new makespan calculator
            double newMakeSpan = analyseTempMakeSpan(processorTimes);
            if(newMakeSpan < prevMakeSpan){ // If this newMakeSpan is better!
                prevMakeSpan = newMakeSpan;
            } else {
                // Reject the changes we just did!
                processorTimes[heavyProcessorIndex] += heaviestJob; // Add back loadful job
                processorTimes[i] -= heaviestJob; // Remove heaviest job to the current processor
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
    }

}
