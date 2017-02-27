package ca.poutineqc.base.data.values;

public class SFloat implements SValue {

	public static final int MAX_STRING_LENGTH = 8;

	private Float value;

	public SFloat(float value) {
		this.value = value;
	}

	public SFloat(String value) {
		this.value = Float.intBitsToFloat(Integer.parseUnsignedInt(unpad(value), 16));
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public float getFloat() {
		return value;
	}

	@Override
	public String toSString() {
		return pad(Integer.toHexString(Float.floatToIntBits(value)));
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

	public void setFloat(float value) {
		this.value = value;	
	}

}