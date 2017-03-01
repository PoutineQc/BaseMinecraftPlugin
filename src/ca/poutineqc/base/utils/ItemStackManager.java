package ca.poutineqc.base.utils;

import com.google.gson.JsonObject;

public class ItemStackManager {

	private static final String TYPE_KEY = "type";
	private static final String OBJECT_KEY = "object";

	public static SItem getSItemStack(JsonObject json) {
		switch (json.get(TYPE_KEY).getAsString()) {
		case "SItem":
			return new SItem(json);
		case "SHead":
			return new SHead(json);
		case "SBanner":
			return new SBanner(json);
		default:
			return null;
		}
	}

	public static <T extends SItem> JsonObject getSItemStack(T object) {
		JsonObject json = new JsonObject();

		json.addProperty(TYPE_KEY, object.getClass().getSimpleName());
		json.add(OBJECT_KEY, object.toJsonObject());

		return json;
	}

}
