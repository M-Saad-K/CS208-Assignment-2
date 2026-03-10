package algorithms.studentAlgorithms;

import algorithms.SchedulingAlgorithm;

/**
 * Please rename this class to FirstNameLastNameAlgorithm
 */
public class YourAlgorithm extends SchedulingAlgorithm {

    // Please put your name in here.
    private String studentName = "Please Enter Your Name Here";

    /**
     * Fill in this method for your submission. You have a maximum of 4.5 minutes of processing time.
     * If your submission exceeds this time, then your grade will be penalised.
     *
     * @param etcMatrix The Estimate To Compute Matrix (ETC Matrix) of the chosen file.
     * @return The total amount of time required for each processor
     */
    @Override
    public double[] runAlgorithm(double[][] etcMatrix) {
        double[] processorTimes = new double[etcMatrix.length];

        // Build your code in here

        return processorTimes;
    }

    @Override
    public String getName() {
        return studentName;
    }
}
