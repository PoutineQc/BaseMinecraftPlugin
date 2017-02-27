package ca.poutineqc.base.data.values;

public class SLong implements SValue {

	public static final int MAX_STRING_LENGTH = 16;
	
	private Long value;

	public SLong(long value) {
		this.value = value;
	};

	public SLong(String value) {
		this.value = Long.parseUnsignedLong(unpad(value), 16);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public long getLong() {
		return value;
	}

	@Override
	public String toSString() {
		return pad(Long.toHexString(value));
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}
}
