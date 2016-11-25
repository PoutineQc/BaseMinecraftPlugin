package me.poutineqc.data;

public class Column {
	
	private String name;
	private ValueType type;
	private String defaultValue;
	
	public Column(String name, ValueType type, String defaultValue) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	public Column(String name, ValueType type) {
		this(name, type, null);
	}

	public String getQuerryPart() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("`" + name + "` ");
		
		switch (type) {
		case BOOLEAN:
			builder.append("BOOLEAN");
			break;
			
		case DOUBLE:
			builder.append("DOUBLE");
			break;
			
		case INTEGER:
			builder.append("INT");
			break;
			
		case FLOAT:
			builder.append("FLOAT");
			break;
			
		case LONG:
			builder.append("LONG");
			break;
			
		case STRING:
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
	
}
