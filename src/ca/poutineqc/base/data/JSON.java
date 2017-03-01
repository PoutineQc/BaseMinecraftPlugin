package ca.poutineqc.base.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import ca.poutineqc.base.data.values.SUUID;
import ca.poutineqc.base.data.values.StringSavableValue;
import ca.poutineqc.base.data.values.UniversalSavableValue;
import ca.poutineqc.base.instantiable.SavableParameter;
import ca.poutineqc.base.plugin.Library;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.Pair;

public class JSON extends FlatFile {

	JsonObject json;

	public JSON(PPlugin plugin, String fileName, boolean buildIn, String... folders) {
		super(plugin, fileName, buildIn, folders);

		try {
			JsonElement jsonelement = new JsonParser().parse(new FileReader(file));
			if (jsonelement.isJsonNull())
				jsonelement = new JsonObject();
			
			json = jsonelement.getAsJsonObject();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
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
	}

	@Override
	public void setDouble(SavableParameter identification, SUUID uuid, SavableParameter parameter, double value) {
	}

	@Override
	public void setLong(SavableParameter identification, SUUID uuid, SavableParameter parameter, long value) {
	}

	@Override
	public void setBoolean(SavableParameter identification, SUUID uuid, SavableParameter parameter, boolean value) {
	}

	@Override
	public void setFloat(SavableParameter identification, SUUID uuid, SavableParameter parameter, float value) {
	}

	@Override
	public void setStringSavableValue(SavableParameter identifier, SUUID uuid, SavableParameter parameter,
			StringSavableValue value) {
	}

	@Override
	public List<SUUID> getAllIdentifications(SavableParameter identification) {
		List<SUUID> ids = new ArrayList<SUUID>();
		for (Entry<String, JsonElement> entry : json.entrySet())
			ids.add(new SUUID(UUID.fromString(entry.getKey())));

		return ids;
	}

	@Override
	public void newInstance(SavableParameter identification, SUUID uuid,
			List<Pair<SavableParameter, UniversalSavableValue>> createParameters) {
		JsonObject savable = new JsonObject();
		
		for (Pair<SavableParameter, UniversalSavableValue> parameter : createParameters)
			savable.addProperty(parameter.getKey().getKey(), parameter.getValue().toSString());
		
		json.add(uuid.getUUID().toString(), savable);
		save();
	}

	@Override
	public Map<SavableParameter, String> getIndividualData(SavableParameter parameter, SUUID uuid,
			Collection<SavableParameter> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	private void save() {
		try (FileWriter file = new FileWriter(this.file, false)) {
			file.write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
