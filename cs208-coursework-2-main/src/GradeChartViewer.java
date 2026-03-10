import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GradeChartViewer extends JFrame {

    public GradeChartViewer(String filename) {
        super("Grade Trends");

        XYDataset dataset = createDataset(filename);

        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private XYDataset createDataset(String filename) {
        XYSeries series = new XYSeries("Grades");
        XYSeries trendLine = new XYSeries("Trend");

        int attempt = 0;

        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));


            for (String line : lines) {
                attempt++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        double grade = Double.parseDouble(parts[2].trim());

                        series.add(attempt, grade);

                    } catch (Exception e) {
                        System.err.println("Skipping invalid line: " + line);
                        e.printStackTrace();
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(series);

        if (dataset.getSeries().size() == 1) {
            double[] trend = Regression.getOLSRegression(dataset, 0);

            for (int i = 1; i <= attempt; i++) {
                trendLine.add(i, trend[0] + trend[1] * i);
            }

            dataset.addSeries(trendLine);
        }

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Grade Over Time",  // Chart title
                "Attempts",                    // X-axis label
                "Grade",                   // Y-axis label
                dataset,
                PlotOrientation.VERTICAL,
                true,                      // Include legend
                true,                      // Include tooltips
                false                      // URLs
        );

        // Customize chart appearance
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.getRangeAxis().setRange(0, 100);

        XYLineAndShapeRenderer renderer = customiseLineAndShapeRenderer();

        plot.setRenderer(renderer);

        return chart;
    }

    private static XYLineAndShapeRenderer customiseLineAndShapeRenderer() {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        renderer.setSeriesPaint(0, Color.ORANGE);

        return renderer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new GradeChartViewer("MinMin AlgorithmResults.txt").setVisible(true);
        });
    }
}