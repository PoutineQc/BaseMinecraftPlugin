package ca.poutineqc.base.data.values;

public class SString implements SValue {

	private String value;

	private BinaryValue bv;

	public SString(String value, BinaryValue bv) {
		if (value.length() > bv.getValue())
			throw new OutOfMemoryError("The String does not fit in the required space");

		this.bv = bv;
		this.value = unpad(value);
	}

	public void setString(String value) throws IllegalArgumentException {
		if (value.length() > bv.getValue())
			throw new OutOfMemoryError("The String does not fit in the required space");

		this.value = unpad(value);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public boolean isSame(SValue o) {
		return toSString().equals(o.toSString());
	}

	@Override
	public String toSString() {
		return pad(value);
	}

	@Override
	public int getMaxToStringLength() {
		return bv.getValue();
	}
}
