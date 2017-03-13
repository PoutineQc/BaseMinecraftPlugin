package ca.poutineqc.base.datastorage;

public interface StringSerializable {

	static final char PAD_CHAR = 'օ';
	
	String toSString();
	
	int getMaxToStringLength();
	
	String getSqlDataTypeName();
	
	String pad(String toPad);
	
	String unpad(String toUnpad);

	boolean isSame(UniversalSerializable o);

}
