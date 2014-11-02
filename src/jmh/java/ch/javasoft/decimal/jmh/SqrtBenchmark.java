package ch.javasoft.decimal.jmh;

import java.math.BigDecimal;
import java.math.BigInteger;

import ch.javasoft.decimal.Decimal;
import ch.javasoft.decimal.scale.ScaleMetrics;
import ch.javasoft.decimal.truncate.OverflowMode;

/**
 * Micro benchmarks for multiplication based on the jmh library.
 */
public class SqrtBenchmark extends AbstractPositiveOnlyBenchmark {

	@Override
	protected <S extends ScaleMetrics> double doubles(PositiveOnlyBenchmarkState state, Values<S> values) {
		return Math.sqrt(values.double1);
	}
	
	private static BigDecimal sqrt(BigDecimal bigDecimal) {
		if (bigDecimal.signum() < 0) {
			throw new ArithmeticException("Square root of a negative value: " + bigDecimal);
		}
		final int scale = bigDecimal.scale();
		final BigInteger bigInt = bigDecimal.unscaledValue().multiply(BigInteger.TEN.pow(scale));
		int len = bigInt.bitLength();
		len += len & 0x1;//round up if odd
		BigInteger rem = BigInteger.ZERO;
		BigInteger root = BigInteger.ZERO;
		for (int i = len-1; i >= 0; i-=2) {
			root = root.shiftLeft(1);
			rem = rem.shiftLeft(2);
			final int add = (bigInt.testBit(i) ? 2 : 0) + (bigInt.testBit(i-1) ? 1 : 0);
			rem = rem.add(BigInteger.valueOf(add));
			final BigInteger rootPlusOne = root.add(BigInteger.ONE);
			if (rootPlusOne.compareTo(rem) <= 0) {
				rem = rem.subtract(rootPlusOne);
				root = rootPlusOne.add(BigInteger.ONE);
			}
		}
		return new BigDecimal(root.shiftRight(1), scale);
	}
	@Override
	protected <S extends ScaleMetrics> BigDecimal bigDecimals(PositiveOnlyBenchmarkState state, Values<S> values) {
		final BigDecimal result = sqrt(values.bigDecimal1);
		if (state.overflowMode == OverflowMode.CHECKED) {
			//check overflow
			result.unscaledValue().longValueExact();
		}
		return result;
	}

	@Override
	protected <S extends ScaleMetrics> Decimal<S> immitableDecimals(PositiveOnlyBenchmarkState state, Values<S> values) {
		return values.immutable1.sqrt(state.roundingMode);
	}

	@Override
	protected <S extends ScaleMetrics> Decimal<S> mutableDecimals(PositiveOnlyBenchmarkState state, Values<S> values) {
		return values.mutable.set(values.immutable1).sqrt(state.roundingMode);
	}

	@Override
	protected <S extends ScaleMetrics> long nativeDecimals(PositiveOnlyBenchmarkState state, Values<S> values) {
		return state.arithmetics.sqrt(values.unscaled1);
	}
}