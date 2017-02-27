package ca.poutineqc.base.data.values;

import java.util.UUID;

public class PUUID implements SValue {

	public static final int MAX_STRING_LENGTH = 36;

	UUID value;
	
	public PUUID(UUID value) {
		this.value = value;
	}
	
	public PUUID(String value) {
		this.value = UUID.fromString(unpad(value));
	}

	public UUID getUUID() {
		return value;
	}

	@Override
	public String toSString() {
		return pad(this.value.toString());
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}

}
