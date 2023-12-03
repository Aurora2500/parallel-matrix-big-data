package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixMultiplicatorTest {
	private static final int N_ITERATIONS = 100;
	private static final int N_SIZE = 64;
	private static final float EPSILON = 0.0001f;

	void assertEqualMatrices(double[][] expected, double[][] actual) {
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i].length, actual[i].length);
			for (int j = 0; j < expected[i].length; j++) {
				assertEquals(expected[i][j], actual[i][j], EPSILON);
			}
		}
	}

	void genericAssociativityTest(MatrixMultiplicator impl)
	{
		for (int iter = 0; iter < N_ITERATIONS; iter++) {
			double[][] a = MatrixGenerator.generate(N_SIZE);
			double[][] b = MatrixGenerator.generate(N_SIZE);
			double[][] c = MatrixGenerator.generate(N_SIZE);

			double[][] result1 = impl.multiply(a, impl.multiply(b, c));
			double[][] result2 = impl.multiply(impl.multiply(a, b), c);
			assertEqualMatrices(result1, result2);
		}
	}

	void genericIdentityTest(MatrixMultiplicator impl)
	{
		for (int iter = 0; iter < N_ITERATIONS; iter++) {
			double[][] a = MatrixGenerator.generate(N_SIZE);
			double[][] eye = MatrixGenerator.eye(N_SIZE);

			double[][] leftIdent = impl.multiply(eye, a);
			double[][] rightIdent = impl.multiply(a, eye);

			assertEqualMatrices(a, leftIdent);
			assertEqualMatrices(a, rightIdent);
		}
	}

	@Test
	void naiveAssoc() {
		MatrixMultiplicator multiplicator = new NaiveMatrixMultiplicator();
		genericAssociativityTest(multiplicator);
	}

	@Test
	void naiveIdent() {
		MatrixMultiplicator multiplicator = new NaiveMatrixMultiplicator();
		genericIdentityTest(multiplicator);
	}

	@Test
	void tiledAssoc() {
		MatrixMultiplicator multiplicator = new TiledMatrixMultiplicator(4);
		genericAssociativityTest(multiplicator);
	}

	@Test
	void tiledIdent() {
		MatrixMultiplicator multiplicator = new TiledMatrixMultiplicator(4);
		genericIdentityTest(multiplicator);
	}
}