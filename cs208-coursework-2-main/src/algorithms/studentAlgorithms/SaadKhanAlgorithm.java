package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

import java.util.Random;

/**
 * Min - Min:
 * - Find the minimum completion time of all the jobs
 * - Chooses the most global minimum job and processor for it
 * - Schedules that first and then goes to the next minimum job
 *
 * Pros:
 * - Very effective in finding minimum job duration
 *
 * Cons:
 * - Takes quite a lot of computation power
 *
 * Local Search:
 * - In each job do a local search from a randomised point, from neighbours
 *
 * Pros:
 * - Very fast and memory efficient
 *
 * Cons:
 * - It all dependes on where the randomised order happens, doesn't always get the best result
 * - Doesn't check the global minimum
 *
 * Saad Khan's Algorithm:
 *  - For each job, use randomised point and search neighbours for min
 *  - Store each job's min in an array
 *  - Find the minimum completion time from the array and execute that first, then execute the next minimum
 *
 *  Pro:
 *  - Fast and efficient search for minimum of job and minimum globally
 *  - Ensure's global shortest completion time first
 *
 *  Cons:
 *  - Won't be as accurate or stable since the randomised point neighbour search may not find the best
 * **/
public class SaadKhanAlgorithm extends SchedulingAlgorithm {

    // Please put your name in here.
    private String studentName = "Muhammad Saad Khan";

    /**
     * Fill in this method for your submission. You have a maximum of 4.5 minutes of processing time.
     * If your submission exceeds this time, then your grade will be penalised.
     *
     * @param etcMatrix The Estimate To Compute Matrix (ETC Matrix) of the chosen file.
     * @return The total amount of time required for each processor
     */
        // Set up a random point to access in the file
        Random rand = new Random();

        @Override
        public double[] runAlgorithm(double[][] etcMatrix) {
            int numberOfProcessors = etcMatrix.length;
            int numberOfTasks = etcMatrix[0].length;
            double[] processorTimes = new double[numberOfProcessors];
            // Scheduled list
            int[] scheduled = new int[numberOfTasks]; // This is scheduled list for checking if something is scheduled or not
            // Chosen processor
            int globalMinProcessor = 0;

            // Next now iterate through each task
            for(int i = 0; i < numberOfTasks; i++){
                // Intialise these every time
                // Current job

                // find all the local min processes!
                int[] bestLocalProcesses = getBestLocalProcess(etcMatrix, i, numberOfProcessors, numberOfTasks, processorTimes);
                // find the global of all the jobs!
                double globalAv_MinDuration = findGlobalMinJob(bestLocalProcesses, scheduled, etcMatrix, numberOfProcessors, globalMinProcessor);

                // Now we've found the local minJob execution time of Job i
                processorTimes[globalMinProcessor] += globalAv_MinDuration;

            }
            // After loop ends, we've now gotten all the local mintime jobs's indexes in an array
            // Now we need to add each processes to the current processorTimes

            return processorTimes;
        }

        int[] getBestLocalProcess(double[][] etcMatrix, int i, int numberOfProcessors, int numberOfTasks, double[] processorTimes) {
            // Get random starting positon
            int startPos = rand.nextInt(numberOfProcessors);
            System.out.println("startPos: " + startPos);
            System.out.println(startPos);

            double minJobDur = processorTimes[startPos] + etcMatrix[startPos][i]; // Get the chosen processor for the current task i

            /// Important, this will store which processor was chosen for job i
            int[] bestLocalProcesses = new int[numberOfTasks];

            // Iterating through the processor list for only two steps out
            for(int j = 1; j < 5; j++){

                // Intialise best local process for job
                bestLocalProcesses[j] = startPos;

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
                    bestLocalProcesses[j] = forward; // Get the best process
                }

                if (minJobDur > backMinJobDur) {
                    // If the backward neighbour is min
                    minJobDur = backMinJobDur;
                    bestLocalProcesses[j] = backward; // Get the best process
                }
            }

            // From this you get an array of all the best local processes at their respective job slots
            return bestLocalProcesses;
        }

        double findGlobalMinJob(int[] bestLocalProcesses, int[] scheduled, double[][] etcMatrix, int numberofProcesses, int globalMinProcessor){
            // Find the most minimum job from the array
            Double minJob = Double.MAX_VALUE; // Re-intialise this every time the method is called
            int globalMinJob = 0;
            for(int i = 0; i < numberofProcesses; i++){

                // First check if not scheduled job
                if(scheduled[i] != 1 && minJob > etcMatrix[bestLocalProcesses[i]][i]){ // If the current job is not scheduled
                    // What the if statement checks that in the current best local processor for job i's job completion time
                    // is less than the minJob
                    minJob = etcMatrix[bestLocalProcesses[i]][i];
                    globalMinProcessor = bestLocalProcesses[i];
                    globalMinJob = i;
                }
            }
            scheduled[globalMinJob] = 1; // Set the current global job to be scheduled
            return etcMatrix[globalMinProcessor][globalMinJob]; // return the global minimum job

        }

    @Override
    public String getName() {
        return studentName;
    }
}
