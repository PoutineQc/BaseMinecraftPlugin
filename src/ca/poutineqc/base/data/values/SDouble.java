package ca.poutineqc.base.data.values;

public class SDouble implements SValue {

	public static final int MAX_STRING_LENGTH = 16;

	private Double value;

	public SDouble(double value) {
		this.value = value;
	}

	public SDouble(String value) {
		this.value = Double.longBitsToDouble(Long.parseUnsignedLong(unpad(value), 16));
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public double getDouble() {
		return value;
	}

	public void setDouble(double value) {
		this.value = value;
	}

	@Override
	public String toSString() {
		return pad(Long.toHexString(Double.doubleToLongBits(value)));
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

}
