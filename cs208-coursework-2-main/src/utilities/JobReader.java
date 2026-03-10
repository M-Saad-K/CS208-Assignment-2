package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JobReader {

    private final static int NO_OF_JOBS = 512;
    private final static int NO_OF_PROCESSORS = 16;

    private final double[][] etcMatrix;


    public JobReader() {
        this.etcMatrix = new double[NO_OF_PROCESSORS][NO_OF_JOBS];
    }

    public void readFileIntoETCMatrix(String pathToFile) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(pathToFile))) {
            int job = 0;
            int processor = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                etcMatrix[processor][job] = Double.parseDouble(line);
                processor++;

                if (processor == NO_OF_PROCESSORS) {
                    processor = 0;
                    job++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public double[][] getEtcMatrix() {
        return etcMatrix;
    }

    public void printETCMatrix() {
        System.out.println("ETC Matrix is as follows: No of Processors = 16, No of Tasks = 512");

        for (int i = 0; i < NO_OF_JOBS; i++) {
            System.out.print("\tT" + i);
        }

        for (int i = 0; i < NO_OF_PROCESSORS; i++) {
            System.out.print("\n Processor " + i + ":");
            for (int j = 0; j < NO_OF_JOBS; j++) {
                System.out.print("\t" + etcMatrix[i][j]);
            }
        }
    }
}
