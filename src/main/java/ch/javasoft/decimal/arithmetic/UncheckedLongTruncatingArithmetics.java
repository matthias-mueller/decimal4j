package ch.javasoft.decimal.arithmetic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import ch.javasoft.decimal.ScaleMetrics;
import ch.javasoft.decimal.ScaleMetrics.Scale0f;
import ch.javasoft.decimal.ScaleMetrics.Scale18f;

/**
 * The special case for longs with {@link Scale0f} and no rounding.
 */
public class UncheckedLongTruncatingArithmetics extends AbstractUncheckedArithmetics {
	
	/**
	 * The singleton instance.
	 */
	public static final UncheckedLongTruncatingArithmetics INSTANCE = new UncheckedLongTruncatingArithmetics();
	
	@Override
	public ScaleMetrics getScaleMetrics() {
		return Scale0f.INSTANCE;
	}

	@Override
	public int getScale() {
		return 0;
	}

	@Override
	public RoundingMode getRoundingMode() {
		return RoundingMode.DOWN;
	}

	@Override
	public long one() {
		return 1L;
	}

	@Override
	public long multiply(long uDecimal1, long uDecimal2) {
		return uDecimal1 * uDecimal2;
	}

	@Override
	public long divide(long uDecimalDividend, long uDecimalDivisor) {
		return uDecimalDividend / uDecimalDivisor;
	}

	@Override
	public long divideByLong(long uDecimalDividend, long lDivisor) {
		return uDecimalDividend / lDivisor;
	}

	@Override
	public long multiplyByPowerOf10(long uDecimal, int positions) {
		return multiplyByPowerOf10(this, uDecimal, positions);
	}
	static long multiplyByPowerOf10(DecimalArithmetics arith, long uDecimal, int positions) {
		if (uDecimal == 0 | positions == 0) {
			return uDecimal;
		}
		if (positions > 0) {
			int pos = positions;
			long result = uDecimal;
			//NOTE: this is not very efficient for positions >> 18
			//      but how else do we get the correct truncated value?
			while (pos > 18) {
				result = Scale18f.INSTANCE.multiplyByScaleFactor(result);
				pos -= 18;
			}
			final ScaleMetrics scaleMetrics = ScaleMetrics.valueOf(pos);
			return scaleMetrics.multiplyByScaleFactor(result);
		} else {
			if (positions >= -18) {
				final ScaleMetrics scaleMetrics = ScaleMetrics.valueOf(-positions);
				return scaleMetrics.divideByScaleFactor(uDecimal);
			}
			//truncated result is 0
			return 0;
		}
	}

	@Override
	public long divideByPowerOf10(long uDecimal, int positions) {
		return divideByPowerOf10(this, uDecimal, positions);
	}
	static long divideByPowerOf10(DecimalArithmetics arith, long uDecimal, int positions) {
		if (uDecimal == 0 | positions == 0) {
			return uDecimal;
		}
		if (positions > 0) {
			if (positions <= 18) {
				final ScaleMetrics scaleMetrics = ScaleMetrics.valueOf(positions);
				return scaleMetrics.divideByScaleFactor(uDecimal);
			}
			//truncated result is 0
			return 0;
		} else {
			int pos = positions;
			long result = uDecimal;
			//NOTE: this is not very efficient for positions << -18
			//      but how else do we get the correct truncated value?
			while (pos < -18) {
				result = Scale18f.INSTANCE.multiplyByScaleFactor(result);
				pos += 18;
			}
			final ScaleMetrics scaleMetrics = ScaleMetrics.valueOf(-pos);
			return scaleMetrics.multiplyByScaleFactor(result);
		}
	}
	
	@Override
	public long average(long a, long b) {
		return average(this, a, b);
	}
	static long average(DecimalArithmetics arith, long a, long b) {
		final long xor = a ^ b;
		final long floor = (a & b) + (xor >> 1);
		return floor + ((floor >>> 63) & xor);
	}

	@Override
	public long fromLong(long value) {
		return value;
	}

	@Override
	public long fromDouble(double value) {
		return (long)value;
	}

	@Override
	public long fromBigInteger(BigInteger value) {
		return value.longValue();
	}

	@Override
	public long fromBigDecimal(BigDecimal value) {
		return value.longValue();
	}

	@Override
	public long fromUnscaled(long unscaledValue, int scale) {
		if (scale == 0) {
			return fromLong(unscaledValue);
		}
		if (scale > 0) {
			long value = unscaledValue;
			while (scale > 18) {
				//not very efficient for large scale, but how do we otherwise
				//get the correct truncated value?
				value = Scale18f.INSTANCE.multiplyByScaleFactor(value);
				scale -= 18;
			}
			final ScaleMetrics scaleMetrics = ScaleMetrics.valueOf(scale);
			return scaleMetrics.multiplyByScaleFactor(value);
		} else {
			if (scale >= -18) {
				final ScaleMetrics scaleMetrics = ScaleMetrics.valueOf(-scale);
				return scaleMetrics.divideByScaleFactor(unscaledValue);
			}
			//truncating division leads to zero
			return 0;
		}
	}

	@Override
	public long parse(String value) {
		return Long.parseLong(value);
	}

	@Override
	public long toLong(long uDecimal) {
		return uDecimal;
	}

	@Override
	public double toDouble(long uDecimal) {
		return (double)uDecimal;
	}
	
	@Override
	public float toFloat(long uDecimal) {
		return (float)uDecimal;
	}

	@Override
	public String toString(long uDecimal) {
		return Long.toString(uDecimal);
	}

}
