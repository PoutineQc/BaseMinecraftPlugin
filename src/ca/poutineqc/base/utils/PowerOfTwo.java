package ca.poutineqc.base.utils;

public enum PowerOfTwo {

	POWER_1(0), POWER_2(1), POWER_4(2), POWER_8(3), POWER_16(4), POWER_32(5), POWER_64(6),

	POWER_128(7), POWER_254(8), POWER_512(9), POWER_1024(10),

	POWER_2048(11), POWER_4096(12), POWER_8192(13), POWER_16384(14);

	private int exponent;

	private PowerOfTwo(int value) {
		this.exponent = value;
	}
	
	public int getPower() {
		return (int) Math.pow(2, exponent);
	}

	public int getExponent() {
		return exponent;
	}

	public static int getPower(int exponent) {
		return (int) Math.pow(2, exponent);
	}

	public static double getExponent(int power) {
		return Math.log(power) / Math.log(2);
	}
}
