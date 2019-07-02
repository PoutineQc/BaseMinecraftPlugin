package ca.poutineqc.base.datastorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import ca.poutineqc.base.PPlugin;
import ca.poutineqc.base.datastorage.serializable.SUUID;
import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.utils.Pair;

public class JSON extends FlatFile {

	JsonObject json;

	public JSON(PPlugin plugin, String fileName, boolean buildIn, String... folders) {
		super(plugin, fileName.replace(".json", "") + ".json", buildIn, folders);
		json = getJson();
	}

	public JSON(File file) {
		super(file);
		json = getJson();

	}

	private JsonObject getJson() {
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			JsonElement jsonelement = new JsonParser().parse(reader);
			return jsonelement.getAsJsonObject();

		} catch (IllegalStateException | JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			return new JsonObject();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public void setString(SavableParameter identification, SUUID uuid, SavableParameter parameter, String value) {
		JsonObject savable = json.getAsJsonObject(uuid.getUUID().toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.getUUID().toString(), savable);
		save();
	}

	@Override
	public void setInt(SavableParameter identification, SUUID uuid, SavableParameter parameter, int value) {
		JsonObject savable = json.getAsJsonObject(uuid.getUUID().toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.getUUID().toString(), savable);
		save();
	}

	@Override
	public void setDouble(SavableParameter identification, SUUID uuid, SavableParameter parameter, double value) {
		JsonObject savable = json.getAsJsonObject(uuid.getUUID().toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.getUUID().toString(), savable);
		save();
	}

	@Override
	public void setLong(SavableParameter identification, SUUID uuid, SavableParameter parameter, long value) {
		JsonObject savable = json.getAsJsonObject(uuid.getUUID().toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.getUUID().toString(), savable);
		save();
	}

	@Override
	public void setBoolean(SavableParameter identification, SUUID uuid, SavableParameter parameter, boolean value) {
		JsonObject savable = json.getAsJsonObject(uuid.getUUID().toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.getUUID().toString(), savable);
		save();
	}

	@Override
	public void setFloat(SavableParameter identification, SUUID uuid, SavableParameter parameter, float value) {
		JsonObject savable = json.getAsJsonObject(uuid.getUUID().toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.getUUID().toString(), savable);
		save();
	}

	@Override
	public void setStringSavableValue(SavableParameter identifier, SUUID uuid, SavableParameter parameter,
			StringSerializable value) {
		JsonObject savable = json.getAsJsonObject(uuid.getUUID().toString());
		savable.addProperty(parameter.getKey(), value.toSString());
		json.add(uuid.getUUID().toString(), savable);
		save();
	}

	@Override
	public List<UUID> getAllIdentifications(SavableParameter identification, List<SavableParameter> columns) {
		List<UUID> ids = new ArrayList<UUID>();
		for (Entry<String, JsonElement> entry : json.entrySet())
			ids.add(new SUUID(UUID.fromString(entry.getKey())).getUUID());

		return ids;
	}

	@Override
	public void newInstance(SavableParameter identification, SUUID uuid,
			List<Pair<SavableParameter, StringSerializable>> createParameters) {
		JsonObject savable = new JsonObject();

		for (Pair<SavableParameter, StringSerializable> parameter : createParameters)
			savable.addProperty(parameter.getKey().getKey(), parameter.getValue().toSString());

		json.add(uuid.getUUID().toString(), savable);
		save();
	}

	@Override
	public void deleteInstance(SavableParameter identification, SUUID uuid) {
		if (json.has(uuid.getUUID().toString()))
			json.remove(uuid.getUUID().toString());
		
		save();
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(SavableParameter identification, SUUID uuid,
			List<SavableParameter> columns) {
		Map<SavableParameter, String> individualData = new HashMap<SavableParameter, String>();

		JsonObject individualJson = json.get(uuid.getUUID().toString()).getAsJsonObject();
		for (SavableParameter parameter : columns) {
			if (identification.equals(parameter))
				continue;

			individualData.put(parameter, individualJson.get(parameter.getKey()).getAsString());
		}

		return individualData;
	}

	public void save(JsonObject json) {
		this.json = json;
		save();
	}

	private void save() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try (FileWriter writer = new FileWriter(file, false)) {
					writer.write(json.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public JsonObject getJsonObject() {
		return json;
	}

}
