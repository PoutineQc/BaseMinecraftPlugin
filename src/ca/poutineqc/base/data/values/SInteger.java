package ca.poutineqc.base.data.values;

public class SInteger implements SValue {

	public static final int MAX_STRING_LENGTH = 8;
	private Integer value;

	public SInteger(int value) {
		this.value = value;
	}

	public SInteger(String value) {
		this.value = Integer.parseUnsignedInt(unpad(value), 16);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public int getInt() {
		return value;
	}

	public void setInt(int value) {
		this.value = value;
	}
	
	@Override
	public String toSString() {
		return pad(Integer.toHexString(value));
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}
	
	
}
