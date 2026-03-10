package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

public class MinMinAlgorithm extends SchedulingAlgorithm {
    private String name = "MinMinAlgorithm";
    // Create a varialbe for start point -> corresponding to the findMinJob method
    // Create a variable that coresponds to the processor that also iterates
    // Create a variale for the min time
    int currPoint = 0;
    int apporiateProcessor = 0;
    double curMinTmeJob = 0.0;

    @Override
    public double[] runAlgorithm(double[][] etcMatrix) {
        int numberOfProcessors = etcMatrix.length;
        int numberOfTasks = etcMatrix[0].length;
        double[] processorTimes = new double[numberOfProcessors];

        int processorToAssign = 0;
        for (int t = 0; t < numberOfTasks; t++) {
            if (t % numberOfProcessors - 1 == 0) {
                processorToAssign = 0;
            }
            processorTimes[processorToAssign] += etcMatrix[processorToAssign][t]; // Assigning here using the two variables
            processorToAssign++;
            // Increment currentPoint by 16
        }

        return processorTimes;
    }

    // Method takes the start point as a parameter
    public void findMinJob(int currentPoint, double[][] etcMatrix){
        // Create a variable that coresponds to the processor that also iterates
        // Create a variale for the min time
        // Compare all the process with iterator of 16, to find min using the start point + 16/0
            // Increment processor varialble
        // Store that minjob and process in a variable for second indes of etcMatrix
        double min = etcMatrix[0][0];
        for(int i = 0; i < currentPoint; i++){
            if (etcMatrix[i][0] < min) {
                min = etcMatrix[i][0];
                apporiateProcessor = i; // Change to the best processor
            }
        }
        curMinTmeJob = min;
    }

    @Override
    public String getName() {
        return name;
    }
}
