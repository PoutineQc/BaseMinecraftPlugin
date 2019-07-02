package ca.poutineqc.base.utils;

import org.bukkit.entity.Player;

public class Verify {

	public static void notNull(Object toCheck, String errorMessage) throws VerifyException {
		if (toCheck == null)
			throw new VerifyException(errorMessage);
	}

	public static void isInstance(Object toCheck, Class<?> isInstance,  String errorMessage) throws VerifyException {
		if (!isInstance.isInstance(toCheck))
			throw new VerifyException(errorMessage);
	}

	public static void isNull(Object toCheck, String errorMessage) throws VerifyException {
		if (toCheck != null)
			throw new VerifyException(errorMessage);
	}

	public static void isPlayer(Object toCheck, String errorMessage) throws VerifyException {
		if (!(toCheck instanceof Player))
			throw new VerifyException(errorMessage);
	}

	public static void equals(int n1, int n2, String errorMessage) throws VerifyException {
		if (n1 != n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterThan(int n1, int n2, String errorMessage) throws VerifyException {
		if (n1 <= n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterOrEqualThan(int n1, int n2, String errorMessage) throws VerifyException {
		if (n1 < n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerThan(int n1, int n2, String errorMessage) throws VerifyException {
		if (n1 >= n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerOrEqualThan(int n1, int n2, String errorMessage) throws VerifyException {
		if (n1 > n2)
			throw new VerifyException(errorMessage);
	}

	public static void equals(double n1, double n2, String errorMessage) throws VerifyException {
		if (n1 != n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterThan(double n1, double n2, String errorMessage) throws VerifyException {
		if (n1 <= n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterOrEqualThan(double n1, double n2, String errorMessage) throws VerifyException {
		if (n1 < n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerThan(double n1, double n2, String errorMessage) throws VerifyException {
		if (n1 >= n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerOrEqualThan(double n1, double n2, String errorMessage) throws VerifyException {
		if (n1 > n2)
			throw new VerifyException(errorMessage);
	}

	public static void equals(long n1, long n2, String errorMessage) throws VerifyException {
		if (n1 != n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterThan(long n1, long n2, String errorMessage) throws VerifyException {
		if (n1 <= n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterOrEqualThan(long n1, long n2, String errorMessage) throws VerifyException {
		if (n1 < n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerThan(long n1, long n2, String errorMessage) throws VerifyException {
		if (n1 >= n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerOrEqualThan(long n1, long n2, String errorMessage) throws VerifyException {
		if (n1 > n2)
			throw new VerifyException(errorMessage);
	}

	public static void equals(float n1, float n2, String errorMessage) throws VerifyException {
		if (n1 != n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterThan(float n1, float n2, String errorMessage) throws VerifyException {
		if (n1 <= n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterOrEqualThan(float n1, float n2, String errorMessage) throws VerifyException {
		if (n1 < n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerThan(float n1, float n2, String errorMessage) throws VerifyException {
		if (n1 >= n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerOrEqualThan(float n1, float n2, String errorMessage) throws VerifyException {
		if (n1 > n2)
			throw new VerifyException(errorMessage);
	}

	public static void equals(short n1, short n2, String errorMessage) throws VerifyException {
		if (n1 != n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterThan(short n1, short n2, String errorMessage) throws VerifyException {
		if (n1 <= n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterOrEqualThan(short n1, short n2, String errorMessage) throws VerifyException {
		if (n1 < n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerThan(short n1, short n2, String errorMessage) throws VerifyException {
		if (n1 >= n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerOrEqualThan(short n1, short n2, String errorMessage) throws VerifyException {
		if (n1 > n2)
			throw new VerifyException(errorMessage);
	}

	public static void equals(byte n1, byte n2, String errorMessage) throws VerifyException {
		if (n1 != n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterThan(byte n1, byte n2, String errorMessage) throws VerifyException {
		if (n1 <= n2)
			throw new VerifyException(errorMessage);
	}

	public static void greaterOrEqualThan(byte n1, byte n2, String errorMessage) throws VerifyException {
		if (n1 < n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerThan(byte n1, byte n2, String errorMessage) throws VerifyException {
		if (n1 >= n2)
			throw new VerifyException(errorMessage);
	}

	public static void smallerOrEqualThan(byte n1, byte n2, String errorMessage) throws VerifyException {
		if (n1 > n2)
			throw new VerifyException(errorMessage);
	}
	
	public static class VerifyException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6772406722024894682L;
		
		
		public VerifyException(String message) {
			super(message);
		}
	}
}
