package edu.utexas.se.swing.sample;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/* For this project I used a free chart library called jfreechart
 * I created a java swing application to show the chart
 * The program asks the user how many iterations or times you would like to run the
 * sequence. The it runs two Fibonacci sequences, one recursive and one iterative.
 * It will then print out the speed in nanoseconds and then shows the chart
 * and also saves the chart as a png
 */


@SuppressWarnings("serial")
public class FibonacciRuntimeTest extends JFrame {

	//Recursive method
	public int recursiveFibonacci(int number) {
		int sequence = 0;
		if (number <= 1) {
			sequence = number;
			return sequence;
		}
		return recursiveFibonacci(number - 1) + recursiveFibonacci(number - 2);
	}

	//Iterative method
	public int iterativeFibonacci(int number) {
		int sequence = 0;
		if (number <= 1) {
			sequence = number;
			return sequence;
		}

		int prevPrevNumber = 0;
		int prevNumber = 1;

		for (int i = 2; i <= number; i++) {
			sequence = prevNumber + prevPrevNumber;
			prevPrevNumber = prevNumber;
			prevNumber = sequence;
		}
		return sequence;
	}


	 //Create the frame to hold the chart on a JPanel

	public FibonacciRuntimeTest() {
		super("Fibonacci Runtime Test");

		JPanel chartPanel = createChartPanel();
		add(chartPanel, BorderLayout.CENTER);

		setSize(640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/*This is all for the line chart. It plots points, set colors, backgrounds..etc
	 * I input the data through the createDataset method
	 * This method also creates the png
	 * Note: this took considerable amount of time to play around with and get
	 * it looking decent
	 */
	public JPanel createChartPanel() {
		String chartTitle = "Iterative vs Recursive Fibonacci Runtime";
		String xAxisLabel = "Input (n)";
		String yAxisLabel = "Time (ns)";

		XYDataset dataset = createDataset();

		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset);
		XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		// sets paint color for each series
		renderer.setSeriesPaint(0, Color.GREEN);
		renderer.setSeriesPaint(1, Color.RED);

		// sets thickness for series (using strokes)
		renderer.setSeriesStroke(0, new BasicStroke(4.0f));
		renderer.setSeriesStroke(1, new BasicStroke(3.0f));
		plot.setRenderer(renderer);

		plot.setOutlinePaint(Color.BLUE);
		plot.setOutlineStroke(new BasicStroke(2.0f));
		plot.setBackgroundPaint(Color.DARK_GRAY);

		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);

		// Creates a PNG photo file of the chart and exports it
		File imageFile = new File("Fibonacci_runtime_efficiency_chart.png");
		int width = 640;
		int height = 480;
		try {
			ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
		} catch (IOException ex) {
			System.err.println(ex);
		}

		return new ChartPanel(chart);
	}

	// Holds all the data to input into the chart
	public XYDataset createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("Iterative Function");
		XYSeries series2 = new XYSeries("Recursive Function");

		// Asks user for the amount of times they would like the sequence run
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the number of terms of the Fibonacci sequence: ");
		int i = input.nextInt();
		int number = 0;
		long startTime = 0;
		long endTime = 0;

		System.out.println("\nIterative version:\n");
		while (i >= number) {
			startTime = System.nanoTime();
			System.out.println("Fibonacci Sequence: " + iterativeFibonacci(number));
			endTime = System.nanoTime();
			series1.add(number, endTime - startTime);
			System.out.println("(Term Number (n): " + number + ", Speed: " + (endTime - startTime) + " ns)");
			number = number + 1;
		}

		number = 0;
		System.out.println("\nRecursive version:\n");
		while (i >= number) {
			startTime = System.nanoTime();
			System.out.println("Fibonacci Sequence: " + recursiveFibonacci(number));
			endTime = System.nanoTime();
			series2.add(number, endTime - startTime);
			System.out.println("(Term Number (n): " + number + ", Speed: " + (endTime - startTime) + " ns)");
			number = number + 1;
		}

		dataset.addSeries(series1);
		dataset.addSeries(series2);

		return dataset;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FibonacciRuntimeTest().setVisible(true);
			}
		});
	}
}
