package org.example;

import java.util.List;

public class Benchmark {
	private static final List<Integer> SIZES = List.of(64, 128, 256, 512, 1024, 2048);
	private static final int TILE_SIZE = 4;
	public void run() {
		MatrixMultiplicator naiveMul = new NaiveMatrixMultiplicator();
		MatrixMultiplicator tiledMul = new TiledMatrixMultiplicator(TILE_SIZE);

		System.out.println("size,naive,tiled");
		for (int size : SIZES) {
			double[][] a = MatrixGenerator.generate(size);
			double[][] b = MatrixGenerator.generate(size);

			long startN = System.currentTimeMillis();
			naiveMul.multiply(a, b);
			long endN = System.currentTimeMillis();
			long startT = System.currentTimeMillis();
			tiledMul.multiply(a, b);
			long endT = System.currentTimeMillis();

			System.out.println(size + "," + (endN - startN) / 1000.0 + "," + (endT - startT) / 1000.0);
		}
	}
}
