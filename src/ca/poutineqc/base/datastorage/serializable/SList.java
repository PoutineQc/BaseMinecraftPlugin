package ca.poutineqc.base.datastorage.serializable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.JSONSerializable;
import ca.poutineqc.base.datastorage.StringSerializable;
import ca.poutineqc.base.datastorage.UniversalSerializable;
import ca.poutineqc.base.utils.PowerOfTwo;
import ca.poutineqc.base.utils.Utils;

public abstract class SList<T extends UniversalSerializable> extends ArrayList<T>
		implements StringSerializable, JSONSerializable {

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
		super(pt.getPower());

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
		if (o instanceof UniversalSerializable)
			for (UniversalSerializable value : this)
				if (value.isSame((UniversalSerializable) o))
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
		if (o instanceof UniversalSerializable)
			for (UniversalSerializable value : this)
				if (value.isSame((UniversalSerializable) o)) {
					this.remove(value);
					return true;
				}

		return false;
	}

	@Override
	public String toSString() {
		return serialize();
	}

//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder("[");
//		
//		for (T i : this)
//			sb.append(i.toString() + ", ");
//			
//		sb.replace(sb.length() - 2, sb.length(), "]");
//		
//		return sb.toString();
//	}

	private String serialize() {
		StringBuilder sb = new StringBuilder();

		for (UniversalSerializable value : this)
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
			this.add(i, convert(sString));
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

	@Override
	public String getSqlDataTypeName() {
		return "VARCHAR(" + getMaxToStringLength() + ")";
	}

	@Override
	public String pad(String toPad) {
		return Utils.padLeft(toPad, getMaxToStringLength()).replace(' ', PAD_CHAR);
	}

	@Override
	public String unpad(String toUnpad) {
		return (toUnpad.replace(PAD_CHAR, ' ')).trim();
	}

	@Override
	public boolean isSame(UniversalSerializable o) {
		return toSString().equals(o.toSString());
	}

	public abstract T convert(String value);

	public abstract T convert(JsonObject value);

	public abstract int getElementMaxStringLength();
}
