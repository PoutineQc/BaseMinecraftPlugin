package ca.poutineqc.base.data.values;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.poutineqc.base.utils.PowerOfTwo;

public abstract class SList<T extends UniversalSavableValue> extends ArrayList<T>
		implements StringSavableValue, JSONSavableValue {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7789926118136433637L;

	private static final String LENGTH_KEY = "length";
	private static final String PRIMAL_KEY = "values";

	private int exponent;

	public SList(List<T> values, PowerOfTwo pt) throws OutOfMemoryError {
		super(values);

		if (pt.getPower() < values.size())
			throw new OutOfMemoryError("The size of the list provided is too large for the size of the SList");

		this.exponent = pt.getExponent();

		if (getMaxToStringLength() > 21844)
			throw new OutOfMemoryError("The SList cannot hold that much information.");

	}

	public SList(String values, PowerOfTwo pt) throws OutOfMemoryError {
		super(pt.getPower());

		this.exponent = pt.getExponent();

		if (getMaxToStringLength() > 21844)
			throw new OutOfMemoryError("The SList cannot hold that much information.");

		deserialize(values);
	}

	public SList(PowerOfTwo pt) throws OutOfMemoryError {

		this.exponent = pt.getExponent();

		if (getMaxToStringLength() > 21844)
			throw new OutOfMemoryError("The SList cannot hold that much information.");
	}

	public SList(JsonObject json) {
		this.exponent = json.get(LENGTH_KEY).getAsInt();
		JsonArray array = json.get(PRIMAL_KEY).getAsJsonArray();
		for (JsonElement element : array) {
			this.add(convert(element.getAsJsonObject()));
		}
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof UniversalSavableValue)
			for (UniversalSavableValue value : this)
				if (value.isSame((UniversalSavableValue) o))
					return true;

		return false;
	}

	@Override
	public boolean add(T e) throws OutOfMemoryError {
		if (this.size() >= PowerOfTwo.getPower(exponent))
			throw new OutOfMemoryError("The SList is full. It can't hold more Data.");

		if (this.contains(e))
			return true;

		return super.add(e);
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof UniversalSavableValue)
			for (UniversalSavableValue value : this)
				if (value.isSame((UniversalSavableValue) o)) {
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

		for (UniversalSavableValue value : this)
			sb.append(value.toSString());

		return sb.toString();
	}

	private void deserialize(String values) throws OutOfMemoryError {

		int amountToDeserialize = values.length() / getElementMaxStringLength();
		if (amountToDeserialize > PowerOfTwo.getPower(exponent))
			throw new OutOfMemoryError(
					"The amount of elements in the String provided is too large for the size of the SList");

		for (int i = 0; i < amountToDeserialize; i++) {
			int startPos = i * getElementMaxStringLength();
			String sString = values.substring(startPos, startPos + getElementMaxStringLength());
			this.add(convert(sString));
		}
	}

	@Override
	public int getMaxToStringLength() {
		return PowerOfTwo.getPower(exponent) * getElementMaxStringLength();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		JsonArray array = new JsonArray();
		for (T element : this)
			array.add(element.toJsonObject());

		json.addProperty(LENGTH_KEY, exponent);
		json.add(PRIMAL_KEY, array);

		return json;
	}

	public abstract T convert(String value);

	public abstract T convert(JsonObject value);

	public abstract int getElementMaxStringLength();
}
