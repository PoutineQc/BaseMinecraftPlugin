package me.poutineqc.instantiable;

public interface SavableParameter {
	
	public enum DataValue {
		BOOLEAN("BOOLEAN"), DOUBLE("DOUBLE"), FLOAT("FLOAT"), INTEGER("INT"), LONG("LONG"), STRING("VARCHAR(64)");
		
		private String sqlName;

		private DataValue(String sqlName) {
			this.sqlName = sqlName;
		}
		
		public String getSqlName() {
			return sqlName;
		}
		
//		public boolean isSameType(Class<?> toCheck) {
//			return toCheck == classType;
//		}
	}
	
	public <T> T getDefaultValue() throws ClassCastException;
	public String getKey();
	public String getCreateQuerryPart();
	
}
