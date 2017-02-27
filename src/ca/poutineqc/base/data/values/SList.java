package ca.poutineqc.base.data.values;

import java.util.ArrayList;
import java.util.List;

public abstract class SList<T extends SValue> extends ArrayList<T> implements SValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7789926118136433637L;

	private BinaryValue bv;

	public SList(List<T> values, BinaryValue bv) throws OutOfMemoryError {
		super(values);

		if (bv.getValue() < values.size())
			throw new OutOfMemoryError("The size of the list provided is too large for the size of the SList");

		this.bv = bv;

		if (getMaxToStringLength() > 21844)
			throw new OutOfMemoryError("The SList cannot hold that much information.");

	}

	public SList(String values, BinaryValue bv) throws OutOfMemoryError {
		super(bv.getValue());

		this.bv = bv;

		if (getMaxToStringLength() > 21844)
			throw new OutOfMemoryError("The SList cannot hold that much information.");

		deserialize(values);
	}

	public SList(BinaryValue bv) throws OutOfMemoryError {

		this.bv = bv;

		if (getMaxToStringLength() > 21844)
			throw new OutOfMemoryError("The SList cannot hold that much information.");
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof SValue)
			for (SValue value : this)
				if (value.isSame((SValue) o))
					return true;

		return false;
	}

	@Override
	public boolean add(T e) throws OutOfMemoryError {
		if (this.size() >= bv.getValue())
			throw new OutOfMemoryError("The SList is full. It can't hold more Data.");

		if (this.contains(e))
			return true;

		return super.add(e);
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof SValue)
			for (SValue value : this)
				if (value.isSame((SValue) o)) {
					this.remove(value);
					return true;
				}

		return false;
	}

	@Override
	public String toSString() {
		return serialize();
	}

	private String serialize() {
		StringBuilder sb = new StringBuilder();

		for (SValue value : this)
			sb.append(value.toSString());

		return sb.toString();
	}

	private void deserialize(String values) throws OutOfMemoryError {

		int amountToDeserialize = values.length() / getElementMaxStringLength();
		if (amountToDeserialize > bv.getValue())
			throw new OutOfMemoryError(
					"The amount of elements in the String provided is too large for the size of the SList");

		for (int i = 0; i < amountToDeserialize; i++) {
			this.add(convert(
					values.substring(i * getElementMaxStringLength(), ((i + 1) * getElementMaxStringLength() - 1))));
		}
	}

	@Override
	public int getMaxToStringLength() {
		return bv.getValue() * getElementMaxStringLength();
	}

	public abstract T convert(String value);

	public abstract int getElementMaxStringLength();
}
