package algorithms;

/**
 * An example implementation of a very basic algorithm - Round Robin.
 * Can you understand what the algorithm is doing from its code?
 */
public class RoundRobinAlgorithm extends SchedulingAlgorithm {

    private String name = "Round Robin - Sidekick to Round Batman.";

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
            processorTimes[processorToAssign] += etcMatrix[processorToAssign][t];
            processorToAssign++;
        }

        return processorTimes;
    }

    @Override
    public String getName() {
        return name;
    }
}
