package ca.poutineqc.base.data.values;

import ca.poutineqc.base.utils.Utils;

public interface SValue {

	static final char PAD_CHAR = '·';
	
	String toSString();
	
	int getMaxToStringLength();
	
	default public String getSqlDataTypeName() {
		return "VARCHAR(" + getMaxToStringLength() + ")";
	}
	
	default String pad(String toPad) {
		return Utils.padLeft(toPad, getMaxToStringLength()).replace(' ', PAD_CHAR);
	}
	
	default String unpad(String toUnpad) {
		return (toUnpad.replace(PAD_CHAR, ' ')).trim();
	}

	default boolean isSame(SValue o) {
		return toSString().equals(o.toSString());
	}
}
