package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NaiveMatrixMultiplicator implements MatrixMultiplicator {
	private final static int THREADS = 16;
	private final static int TIMEOUT = 1000;
	@Override
	public double[][] multiply(double[][] left, double[][] right) {
		ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
		int size = left.length;
		double[][] result = new double[size][size];
		if (size < THREADS) {
			// there are more threads than rows, so we just use one thread per row for as many rows as it is needed.
			for (int i = 0; i < size; i++) {
				submitTask(executorService, left, right, result, i, i + 1);
			}
		} else {
			// there are at least as many rows as threads, so we assign possibly more than one row to each thread.
			int rowsPerThread = size / THREADS;
			int remainingRows = size % THREADS;
			int start, end = 0;
			for (int t = 0; t < THREADS; t++) {
				start = end;
				end = start + rowsPerThread + (t < remainingRows ? 1 : 0);
				submitTask(executorService, left, right, result, start, end);
			}

		}
		// all threads are now running, so we wait for them to finish.
		try {
			executorService.shutdown();
			executorService.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException("Timed out waiting for threads to finish.", e);
		}
		return result;
	}

	private void submitTask(ExecutorService es, double[][] left, double[][] right, double[][] result, int start, int end)
	{
		// This is safe as long as there is no overlap between the rows assigned to different tasks.
		es.submit(() -> {
			for (int i = start; i < end; i++) {
				for (int k = 0; k < left.length; k++) {
					for (int j = 0; j < left.length; j++) {
						result[i][j] += left[i][k] * right[k][j];
					}
				}
			}
		});
	}
}
