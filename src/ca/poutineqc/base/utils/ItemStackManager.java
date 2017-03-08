package ca.poutineqc.base.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.avaje.ebeaninternal.server.jmx.MAdminAutofetch;
import com.google.gson.JsonObject;

import ca.poutineqc.base.data.values.SItem;
import ca.poutineqc.base.data.values.SItemBanner;
import ca.poutineqc.base.data.values.SItemBeacon;
import ca.poutineqc.base.data.values.SItemBook;
import ca.poutineqc.base.data.values.SItemBrewingStand;
import ca.poutineqc.base.data.values.SItemChest;
import ca.poutineqc.base.data.values.SItemCommandBlock;
import ca.poutineqc.base.data.values.SItemCreatureSpawner;
import ca.poutineqc.base.data.values.SItemDispenser;
import ca.poutineqc.base.data.values.SItemDropper;
import ca.poutineqc.base.data.values.SItemEnchantedBook;
import ca.poutineqc.base.data.values.SItemEnchantmentTable;
import ca.poutineqc.base.data.values.SItemEndGateway;
import ca.poutineqc.base.data.values.SItemFirework;
import ca.poutineqc.base.data.values.SItemFireworkStar;
import ca.poutineqc.base.data.values.SItemFurnace;
import ca.poutineqc.base.data.values.SItemHead;
import ca.poutineqc.base.data.values.SItemHopper;
import ca.poutineqc.base.data.values.SItemJukebox;
import ca.poutineqc.base.data.values.SItemLeatherArmour;
import ca.poutineqc.base.data.values.SItemNoteBlock;
import ca.poutineqc.base.data.values.SItemPotion;
import ca.poutineqc.base.data.values.SItemShulkerBox;
import ca.poutineqc.base.data.values.SItemSpawnEgg;

public class ItemStackManager {

	private static final String TYPE_KEY = "type";
	private static final String OBJECT_KEY = "object";

	public static SItem fromFileFormat(JsonObject json) {
		JsonObject object = json.get(OBJECT_KEY).getAsJsonObject();
		switch (json.get(TYPE_KEY).getAsString()) {
		case "SItem":
			return new SItem(object);
		case "SItemBanner":
			return new SItemBanner(object);
		case "SItemBeacon":
			return new SItemBeacon(object);
		case "SItemBook":
			return new SItemBook(object);
		case "SItemBrewingStand":
			return new SItemBrewingStand(object);
		case "SItemChest":
			return new SItemChest(object);
		case "SItemCommandBlock":
			return new SItemCommandBlock(object);
		case "SItemCreatureSpawner":
			return new SItemCreatureSpawner(object);
		case "SItemDispenser":
			return new SItemDispenser(object);
		case "SItemDropper":
			return new SItemDropper(object);
		case "SItemEnchantedBook":
			return new SItemEnchantedBook(object);
		case "SItemEnchantmentTable":
			return new SItemEnchantmentTable(object);
		case "SItemEndGateway":
			return new SItemEndGateway(object);
		case "SItemFirework":
			return new SItemFirework(object);
		case "SItemFireworkStar":
			return new SItemFireworkStar(object);
		case "SItemFurnace":
			return new SItemFurnace(object);
		case "SItemHead":
			return new SItemHead(object);
		case "SItemHopper":
			return new SItemHopper(object);
		case "SItemJukebox":
			return new SItemJukebox(object);
		case "SItemLeatherArmour":
			return new SItemLeatherArmour(object);
		case "SItemNoteBlock":
			return new SItemNoteBlock(object);
		case "SItemPotion":
			return new SItemPotion(object);
		case "SItemShulkerBox":
			return new SItemShulkerBox(object);
		case "SItemSpawnEgg":
			return new SItemSpawnEgg(object);
		default:
			return null;
		}
	}

	public static SItem getSItem(ItemStack itemStack) {
		if (itemStack.getType().equals(Material.BANNER))
			return new SItemBanner(itemStack);

		if (itemStack.getType().equals(Material.BEACON))
			return new SItemBeacon(itemStack);

		if (itemStack.getType().equals(Material.WRITTEN_BOOK) || itemStack.getType().equals(Material.BOOK_AND_QUILL))
			return new SItemBook(itemStack);

		if (itemStack.getType().equals(Material.BREWING_STAND_ITEM))
			return new SItemBrewingStand(itemStack);

		if (itemStack.getType().equals(Material.STORAGE_MINECART) || itemStack.getType().equals(Material.TRAPPED_CHEST)
				|| itemStack.getType().equals(Material.CHEST))
			return new SItemChest(itemStack);

		if (itemStack.getType().equals(Material.COMMAND) || itemStack.getType().equals(Material.COMMAND_CHAIN)
				|| itemStack.getType().equals(Material.COMMAND_MINECART)
				|| itemStack.getType().equals(Material.COMMAND_REPEATING))
			return new SItemCommandBlock(itemStack);

		if (itemStack.getType().equals(Material.MOB_SPAWNER))
			return new SItemCreatureSpawner(itemStack);

		if (itemStack.getType().equals(Material.DISPENSER))
			return new SItemDispenser(itemStack);

		if (itemStack.getType().equals(Material.DROPPER))
			return new SItemDropper(itemStack);

		if (itemStack.getType().equals(Material.ENCHANTED_BOOK))
			return new SItemEnchantedBook(itemStack);

		if (itemStack.getType().equals(Material.ENCHANTMENT_TABLE))
			return new SItemEnchantmentTable(itemStack);

		if (itemStack.getType().equals(Material.END_GATEWAY))
			return new SItemEndGateway(itemStack);

		if (itemStack.getType().equals(Material.FIREWORK))
			return new SItemFirework(itemStack);

		if (itemStack.getType().equals(Material.FIREWORK_CHARGE))
			return new SItemFireworkStar(itemStack);

		if (itemStack.getType().equals(Material.FURNACE))
			return new SItemFurnace(itemStack);

		if (itemStack.getType().equals(Material.SKULL_ITEM) && itemStack.getDurability() == 3)
			return new SItemHead(itemStack);

		if (itemStack.getType().equals(Material.HOPPER) || itemStack.getType().equals(Material.HOPPER_MINECART))
			return new SItemHopper(itemStack);

		if (itemStack.getType().equals(Material.JUKEBOX))
			return new SItemJukebox(itemStack);

		if (itemStack.getType().equals(Material.LEATHER_HELMET)
				|| itemStack.getType().equals(Material.LEATHER_CHESTPLATE)
				|| itemStack.getType().equals(Material.LEATHER_LEGGINGS)
				|| itemStack.getType().equals(Material.LEATHER_BOOTS))
			return new SItemLeatherArmour(itemStack);

		if (itemStack.getType().equals(Material.NOTE_BLOCK))
			return new SItemNoteBlock(itemStack);

		if (itemStack.getType().equals(Material.TIPPED_ARROW) || itemStack.getType().equals(Material.POTION)
				|| itemStack.getType().equals(Material.LINGERING_POTION)
				|| itemStack.getType().equals(Material.SPLASH_POTION))
			return new SItemPotion(itemStack);

		if (itemStack.getType().equals(Material.WHITE_SHULKER_BOX)
				|| itemStack.getType().equals(Material.RED_SHULKER_BOX)
				|| itemStack.getType().equals(Material.BLUE_SHULKER_BOX)
				|| itemStack.getType().equals(Material.CYAN_SHULKER_BOX)
				|| itemStack.getType().equals(Material.YELLOW_SHULKER_BOX)
				|| itemStack.getType().equals(Material.ORANGE_SHULKER_BOX)
				|| itemStack.getType().equals(Material.LIGHT_BLUE_SHULKER_BOX)
				|| itemStack.getType().equals(Material.GREEN_SHULKER_BOX)
				|| itemStack.getType().equals(Material.LIME_SHULKER_BOX)
				|| itemStack.getType().equals(Material.BROWN_SHULKER_BOX)
				|| itemStack.getType().equals(Material.GRAY_SHULKER_BOX)
				|| itemStack.getType().equals(Material.MAGENTA_SHULKER_BOX)
				|| itemStack.getType().equals(Material.PINK_SHULKER_BOX)
				|| itemStack.getType().equals(Material.PURPLE_SHULKER_BOX)
				|| itemStack.getType().equals(Material.SILVER_SHULKER_BOX)
				|| itemStack.getType().equals(Material.BLACK_SHULKER_BOX))

			if (itemStack.getType().equals(Material.MONSTER_EGG))
				return new SItemSpawnEgg(itemStack);

		return new SItem(itemStack);
	}

	public static <T extends SItem> JsonObject getFileFormat(T sItem) {
		JsonObject json = new JsonObject();

		json.addProperty(TYPE_KEY, sItem.getClass().getSimpleName());
		json.add(OBJECT_KEY, sItem.toJsonObject());

		return json;
	}

}
