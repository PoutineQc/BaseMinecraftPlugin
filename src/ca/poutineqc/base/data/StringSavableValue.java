package ca.poutineqc.base.data;

import ca.poutineqc.base.utils.Utils;

public interface StringSavableValue {

	static final char PAD_CHAR = '·';
	
	String toSString();
	
	int getMaxToStringLength();
	
	default public String getSqlDataTypeName() {
		return "VARCHAR(" + getMaxToStringLength() + ")";
	}
	
	default String pad(String toPad) {
		return Utils.padLeft(toPad, getMaxToStringLength()).replace(' ', PAD_CHAR);
	}
	
	static String unpad(String toUnpad) {
		return (toUnpad.replace(PAD_CHAR, ' ')).trim();
	}

	default boolean isSame(UniversalSavableValue o) {
		return toSString().equals(o.toSString());
	}

}
