package ch.javasoft.decimal.arithmetic;

import ch.javasoft.decimal.scale.ScaleMetrics;
import ch.javasoft.decimal.truncate.DecimalRounding;
import ch.javasoft.decimal.truncate.OverflowMode;

/**
 * Base class for arithmetic implementations implementing those functions where
 * rounding is no issue. Overflow is checked, that is,
 * {@link #getOverflowMode()} returns {@link OverflowMode#CHECKED}.
 */
abstract public class AbstractCheckedScaleNfArithmetics extends AbstractCheckedArithmetics {

	private final ScaleMetrics scaleMetrics;
	
	// FIXME why is it called unchecked?
	// FIXME field should not be protected
	protected final DecimalArithmetics unchecked;

	public AbstractCheckedScaleNfArithmetics(ScaleMetrics scaleMetrics) {
		this.scaleMetrics = scaleMetrics;
		this.unchecked = scaleMetrics.getArithmetics(getRoundingMode());
	}
	
	public AbstractCheckedScaleNfArithmetics(ScaleMetrics scaleMetrics, DecimalRounding rounding) {
		this.scaleMetrics = scaleMetrics;
		this.unchecked = scaleMetrics.getArithmetics(rounding.getRoundingMode());
	}

	@Override
	public ScaleMetrics getScaleMetrics() {
		return scaleMetrics;
	}

	@Override
	public OverflowMode getOverflowMode() {
		return OverflowMode.CHECKED;
	}

	@Override
	public long fromLong(long value) {
		return getScaleMetrics().multiplyByScaleFactorExact(value);
	}

	@Override
	public final long fromUnscaled(long unscaledValue, int scale) {
		return Scale.rescale(this, unscaledValue, scale, getScale());
	}

	@Override
	public long toLong(long uDecimal) {
		return unchecked.toLong(uDecimal);
	}

	@Override
	public float toFloat(long uDecimal) {
		return unchecked.toFloat(uDecimal);
	}

	@Override
	public double toDouble(long uDecimal) {
		return unchecked.toDouble(uDecimal);
	}

	@Override
	public String toString(long uDecimal) {
		return unchecked.toString(uDecimal);
	}

}
