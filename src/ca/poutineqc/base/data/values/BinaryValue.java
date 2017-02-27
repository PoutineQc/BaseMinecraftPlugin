package ca.poutineqc.base.data.values;

public enum BinaryValue {

	BINARY_8(8), BINARY_16(16), BINARY_32(32), BINARY_64(64),

	BINARY_128(128), BINARY_254(254), BINARY_512(512), BINARY_1024(1024),

	BINARY_2048(1024), BINARY_4096(1024), BINARY_8192(8192), BINARY_16384(16384);

	private int value;

	private BinaryValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
