package me.poutineqc.data;

import me.poutineqc.instantiable.SavableParameter;

public class Column<T extends SavableParameter, S> {
	
	private String name;
	private T defaultValue;
	
	public Column(String name, T defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}
	
	public Column(String name) {
		this.name = name;
	}

	public void setDefault(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getQuerryPart() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("`" + name + "` ");
		
		switch (defaultValue.getClass().getSimpleName()) {
		case "Boolean":
			builder.append("BOOLEAN");
			break;
			
		case "Double":
			builder.append("DOUBLE");
			break;
			
		case "Integer":
			builder.append("INT");
			break;
			
		case "Float":
			builder.append("FLOAT");
			break;
			
		case "Long":
			builder.append("LONG");
			break;
			
		case "String":
			builder.append("VARCHAR(64)");
			break;
		}
		
		if (defaultValue != null) {
			builder.append(" DEFAULT '" + defaultValue + "'");
		}
		
		return builder.toString();
	}

	public String getName() {
		return name;
	}

	public T getDefaultValue() {
		return defaultValue;
	}
	
}
