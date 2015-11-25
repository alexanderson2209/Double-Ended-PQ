/*
 * Author: Alex Anderson
 * Summer 2014
 */
package a07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/* 
 *
 * Example usage of the MinMaxPQ data structure.
 *
 */
public class MinMaxPQApp {
	public static MinMaxPQ<String> m;
	public static String filePath;

	public static void main(String[] args) {
		filePath = "../resources/words.txt";
		m = new MinMaxPQ<>();
		populateMinMax();

		/* 0 < i <= 30
		 * int[0][i] add()
		 * int[1][i] removeMax()
		 * int[2][i] removeMin()
		 * int[3][i] max()
		 * int[4][i] min()
		 * int[5][i] isEmpty()
		 */
		long[][] data = new long[6][30];

		populateData(data);	
		System.out.println(dataMatrixString(data));
	}

	private static void populateData(long[][] data) {
		long before;
		for (int i = 0; i <= 29; i++) {
			before = System.nanoTime();
			m.add("zzzzzz");
			data[0][i] = (System.nanoTime() - before) / 1000;
			m.removeMax();

			before = System.nanoTime();
			m.removeMax();
			data[1][i] = (System.nanoTime() - before) / 1000;
			m.add("zurich");

			before = System.nanoTime();
			m.removeMin();
			data[2][i] = (System.nanoTime() - before) / 1000;
			m.add("aarhus");

			before = System.nanoTime();
			m.max();
			data[3][i] = (System.nanoTime() - before) / 1000;

			before = System.nanoTime();
			m.min();
			data[4][i] = (System.nanoTime() - before) / 1000;

			before = System.nanoTime();
			m.isEmpty();
			data[5][i] = (System.nanoTime() - before) / 1000;

		}
	}

	private static String dataMatrixString(long[][] data) {
		int spacingOfNumbers = 11;
		StringBuilder sb = new StringBuilder();
		
		String temp = "%s %" 
				+ 11 +"s %" + 10 +"s %" 
				+ 2 +"s %" + 11 +"s %"
				+ 10 +"s %" + 10 + "s";
		sb.append(String.format(temp + "%n", "Methods","add", "removeMax", "removeMin"
				, "max", "min", "isEmpty"));
		
		for (int i = 0; i < 30; i++) {
			if (i < 9) sb.append("iter. 0" + (i + 1));
			else sb.append("iter. " + (i + 1));
			for (int j = 0; j < 6; j++) {
				sb.append(String.format("%" + spacingOfNumbers + "d", data[j][i]));
			}
			sb.append("\n\n");
		}

		long[] averages = averages(data);
		sb.append("-------------------------------------------------------------"
				+ "-------------\n");
		sb.append("Average:");
		for (int i = 0; i < averages.length; i++) {
			sb.append(String.format("%" + 11 + "d", averages[i]));
		}
		return sb.toString();

	}

	private static long[] averages(long[][] data) {
		long sum = 0;
		long[] sums = new long[6];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 30; j++) {
				sum += data[i][j];
			}
			sums[i] = sum / 30;
			sum = 0;
		}
		return sums;
	}

	private static void populateMinMax() {
		String temp;
		try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			while ((temp = br.readLine()) != null) {
				m.add(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
