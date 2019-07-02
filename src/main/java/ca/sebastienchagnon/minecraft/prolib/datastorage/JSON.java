package ca.sebastienchagnon.minecraft.prolib.datastorage;

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

import ca.sebastienchagnon.minecraft.prolib.PPlugin;
import ca.sebastienchagnon.minecraft.prolib.instantiable.SavableParameter;
import ca.sebastienchagnon.minecraft.prolib.utils.Pair;

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
	public void setString(SavableParameter identification, UUID uuid, SavableParameter parameter, String value) {
		JsonObject savable = json.getAsJsonObject(uuid.toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.toString(), savable);
		save();
	}

	@Override
	public void setInt(SavableParameter identification, UUID uuid, SavableParameter parameter, int value) {
		JsonObject savable = json.getAsJsonObject(uuid.toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.toString(), savable);
		save();
	}

	@Override
	public void setDouble(SavableParameter identification, UUID uuid, SavableParameter parameter, double value) {
		JsonObject savable = json.getAsJsonObject(uuid.toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.toString(), savable);
		save();
	}

	@Override
	public void setLong(SavableParameter identification, UUID uuid, SavableParameter parameter, long value) {
		JsonObject savable = json.getAsJsonObject(uuid.toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.toString(), savable);
		save();
	}

	@Override
	public void setBoolean(SavableParameter identification, UUID uuid, SavableParameter parameter, boolean value) {
		JsonObject savable = json.getAsJsonObject(uuid.toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.toString(), savable);
		save();
	}

	@Override
	public void setFloat(SavableParameter identification, UUID uuid, SavableParameter parameter, float value) {
		JsonObject savable = json.getAsJsonObject(uuid.toString());
		savable.addProperty(parameter.getKey(), value);
		json.add(uuid.toString(), savable);
		save();
	}

	@Override
	public void setStringSavableValue(SavableParameter identifier, UUID uuid, SavableParameter parameter,
			StringSerializable value) {
		JsonObject savable = json.getAsJsonObject(uuid.toString());
		savable.addProperty(parameter.getKey(), value.toSString());
		json.add(uuid.toString(), savable);
		save();
	}

	@Override
	public List<UUID> getAllIdentifications(SavableParameter identification, List<SavableParameter> columns) {
		List<UUID> ids = new ArrayList<UUID>();
		for (Entry<String, JsonElement> entry : json.entrySet())
			ids.add(UUID.fromString(entry.getKey()));

		return ids;
	}

	@Override
	public void newInstance(SavableParameter identification, UUID uuid,
			List<Pair<SavableParameter, StringSerializable>> createParameters) {
		JsonObject savable = new JsonObject();

		for (Pair<SavableParameter, StringSerializable> parameter : createParameters)
			savable.addProperty(parameter.getKey().getKey(), parameter.getValue().toSString());

		json.add(uuid.toString(), savable);
		save();
	}

	@Override
	public void deleteInstance(SavableParameter identification, UUID uuid) {
		if (json.has(uuid.toString()))
			json.remove(uuid.toString());
		
		save();
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(SavableParameter identification, UUID uuid,
			List<SavableParameter> columns) {
		Map<SavableParameter, String> individualData = new HashMap<SavableParameter, String>();

		JsonObject individualJson = json.get(uuid.toString()).getAsJsonObject();
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
