package ca.poutineqc.base.data.values;

public class SBoolean implements SValue {
	
	public static final int MAX_STRING_LENGTH = 1;

	private Boolean value;
	
	public SBoolean(boolean value) {
		this.value = value;
	}
	
	public SBoolean(String value) {
		this.value = value.equals("1");
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public boolean getBoolean() {
		return value;
	}
	
	@Override
	public String toSString() {
		return value ? "1" : "0";
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

}
