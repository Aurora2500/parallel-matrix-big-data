package org.example;

import java.util.Random;

public class MatrixGenerator {
	static double[][] generate(int size) {
		Random r = new Random();
		double[][] result = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				result[i][j] = r.nextFloat();
			}
		}
		return result;
	}

	static double[][] eye(int size) {
		double[][] result = new double[size][size];
		for (int i = 0; i < size; i++) {
			result[i][i] = 1;
		}
		return result;
	}
}
