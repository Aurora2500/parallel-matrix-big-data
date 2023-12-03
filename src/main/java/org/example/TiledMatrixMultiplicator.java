package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TiledMatrixMultiplicator implements MatrixMultiplicator {
	private static final int THREADS = 16;

		private final int tileSize;

	TiledMatrixMultiplicator(int tileSize) {
		this.tileSize = tileSize;
	}

	@Override
	public double[][] multiply(double[][] left, double[][] right) {
		int size = left.length;
		if (size % tileSize != 0) throw new IllegalArgumentException("Matrix size must be a multiple of tile size");
		int numTiles = size / tileSize;

		Future<?>[] futures = new Future<?>[numTiles * numTiles];
		ExecutorService es = Executors.newFixedThreadPool(THREADS);

		double[][] result = new double[size][size];
		for (int kk = 0; kk < numTiles; kk++) {
			for (int ii = 0; ii < numTiles; ii++) {
				for (int jj = 0; jj < numTiles; jj++) {
					int finalIi = ii * tileSize;
					int finalKk = kk * tileSize;
					int finalJj = jj * tileSize;
					futures[ii * numTiles + jj] = es.submit(() -> {
						for (int i = finalIi; i < finalIi + tileSize; i++) {
							for (int k = finalKk; k < finalKk + tileSize; k++) {
								for (int j = finalJj; j < finalJj + tileSize; j++) {
									result[i][j] += left[i][k] * right[k][j];
								}
							}
						}
					});
				}
			}
			for (Future<?> f : futures) {
				try {
					f.get();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return result;
	}
}
