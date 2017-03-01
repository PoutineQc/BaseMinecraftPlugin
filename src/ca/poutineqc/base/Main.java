package ca.poutineqc.base;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.w3c.dom.Document;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.poutineqc.base.data.values.PowerOfTwo;
import ca.poutineqc.base.data.values.SDouble;
import ca.poutineqc.base.data.values.SInteger;
import ca.poutineqc.base.data.values.SList;
import ca.poutineqc.base.data.values.SLong;
import ca.poutineqc.base.data.values.SString;
import ca.poutineqc.base.data.values.SUUID;
import ca.poutineqc.base.utils.ItemStackManager;
import ca.poutineqc.base.utils.SHead;
import ca.poutineqc.base.utils.SItem;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {

		System.out.println(Enchantment.DAMAGE_ALL.getName());
		
		SInteger i = new SInteger(-234534);
		System.out.println(i);
		String is = i.toSString();
		System.out.println(is);
		SInteger j = new SInteger(is);
		System.out.println(j);
		System.out.println(j.getInt());

		SList<SDouble> ints = new SList<SDouble>(PowerOfTwo.POWER_16) {

			private static final long serialVersionUID = 220386316051045917L;

			@Override
			public int getElementMaxStringLength() {
				return SDouble.MAX_STRING_LENGTH;
			}

			@Override
			public SDouble convert(String value) {
				return new SDouble(value);
			}

			@Override
			public SDouble convert(JsonObject value) {
				return new SDouble(value);
			}
		};

		for (int z = 0; z < 14; z++) {
			ints.add(new SDouble((Math.random() * 10000) - 5000));
		}

		System.out.println(ints.getMaxToStringLength());
		System.out.println(ints);
		System.out.println(ints.size());
		System.out.println(ints.toSString());
		System.out.println(ints.toSString().length());
		System.out.println(ints.toSString().length() / SDouble.MAX_STRING_LENGTH);

		SString s = new SString("23546wergjje ery wert ert qpqpwer qwerpqwepr qwee4", PowerOfTwo.POWER_128);
		System.out.println(s);
		System.out.println(s.toSString());

		SString t = new SString(s.toSString(), PowerOfTwo.POWER_128);
		System.out.println(t);

		System.out.println(new SUUID(UUID.randomUUID()).toSString() + new SDouble(0).toSString());

		System.out.println(new SInteger(0).toSString());
		System.out.println(new SInteger(1).toSString());
		System.out.println(new SInteger(12).toSString());
		System.out.println(new SLong(1).toSString());
		System.out.println(new SString("null", PowerOfTwo.POWER_32).toSString());

		ConfigurationSection sc = new YamlConfiguration();
		sc.set("a", 23);

		ConfigurationSection scs = new YamlConfiguration();
		scs.set("b", 2);
		scs.set("c", "wetrs");

		sc.set("d", scs);

		System.out.println(sc.getKeys(false));
		System.out.println(sc.getConfigurationSection("d").getKeys(false));

		File folder = new File(".");
		File file = new File(folder, "file.xml");

		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();

		File filjson = new File(folder, "file.json");

		if (!filjson.exists())
			try {
				filjson.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		JsonElement jsonElement = new JsonParser().parse(new FileReader(filjson));
		System.out.println(jsonElement.toString());
		if (jsonElement.isJsonNull())
			jsonElement = new JsonObject();
		System.out.println(jsonElement.toString());
		JsonObject json = jsonElement.getAsJsonObject();
		System.out.println();

		json.addProperty("a", 13);
		json.addProperty("b", "d");
		json.add("c", new JsonObject());
		json.add("doubles", ints.toJsonObject());

		System.out.println(json.toString());

		try (FileWriter writer = new FileWriter(filjson, false)) {
			writer.write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(json.get("a").getAsInt());

		
		SItem item1 = new SItem(Material.APPLE);
		item1.addToLore("I shall love");
		item1.addToLore("PINEAPPLE");
		item1.addToLore("I shall love");
		item1.addToLore("PIZZA");
		item1.addToLore("I shall love");
		item1.addToLore("SPAGETTI");
		item1.addToLore("I shall love");
		item1.addToLore("CHEEZE");
		item1.addToLore("I shall love");
		item1.addToLore("APPLE");
		
		item1.setDisplayName("Eve's apple");
		item1.setData((short) 2);
		
		
		
		SItem item2 = new SHead("PoutineQc");
		
		System.out.println(ItemStackManager.getSItemStack(item1));
		System.out.println(ItemStackManager.getSItemStack(item2));
		
		
	}

}
