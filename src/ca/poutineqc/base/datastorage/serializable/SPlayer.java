package ca.poutineqc.base.datastorage.serializable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.poutineqc.base.datastorage.JSONSerializable;
import ca.poutineqc.base.plugin.EssentialsHandle;
import ca.poutineqc.base.plugin.Library;

public class SPlayer implements JSONSerializable {

	private static final String LEVEL = "level";
	private static final String EXPERIENCE = "exp";
	private static final String GAMEMODE = "gamemode";
	private static final String HEALTH = "health";
	private static final String FOOD_LEVEL = "foodLevel";
	private static final String SATURATION = "saturation";
	private static final String EFFECTS = "effects";
	private static final String LOCATION = "location";
	private static final String FLYING = "flying";
	private static final String ALLOW_FLIGHT = "allowFlight";
	private static final String GOD_MODE = "god";
	private static final String INVENTORY = "inventory";

	private int level = 0;
	private float experience = 0;
	private GameMode gameMode = GameMode.ADVENTURE;
	private double health = 20;
	private int foodLevel = 20;
	private float saturation = 20;
	private List<SPotionEffect> effects = new ArrayList<SPotionEffect>();
	private SLocation location;
	private boolean flying = false;
	private boolean allowFlight = false;
	private boolean godMode = false;
	private SInventory inventory = new SInventory(41);

	public SPlayer(Location location) {
		this.location = new SLocation(location);
	}

	public SPlayer(Player player) {
		this.level = player.getLevel();
		this.experience = player.getExp();
		this.gameMode = player.getGameMode();
		this.health = player.getHealth();
		this.foodLevel = player.getFoodLevel();
		this.saturation = player.getSaturation();

		this.effects = new ArrayList<SPotionEffect>();
		for (PotionEffect effect : player.getActivePotionEffects())
			effects.add(new SPotionEffect(effect));

		this.location = new SLocation(player.getLocation());
		this.flying = player.isFlying();
		this.allowFlight = player.getAllowFlight();
		this.godMode = Library.isEssentialsEnabled() ? EssentialsHandle.isGodModeEnabled(player) : false;
		
		this.inventory = new SInventory(player.getInventory());

	}

	public SPlayer(JsonObject json) {
		this.level = json.get(LEVEL).getAsInt();
		this.experience = json.get(EXPERIENCE).getAsFloat();

		String gmName = json.get(GAMEMODE).getAsString();
		for (GameMode gm : GameMode.values())
			if (gm.name().equals(gmName))
				this.gameMode = gm;
		
		this.health = json.get(HEALTH).getAsDouble();
		this.foodLevel = json.get(FOOD_LEVEL).getAsInt();
		this.saturation = json.get(SATURATION).getAsFloat();
		
		this.effects = new ArrayList<SPotionEffect>();
		for (JsonElement effect : json.get(EFFECTS).getAsJsonArray())
			this.effects.add(new SPotionEffect(effect.getAsJsonObject()));
		
		this.location = new SLocation(json.get(LOCATION).getAsJsonObject());
		this.flying = json.get(FLYING).getAsBoolean();
		this.allowFlight = json.get(ALLOW_FLIGHT).getAsBoolean();
		this.godMode = json.get(GOD_MODE).getAsBoolean();
		
		this.inventory = new SInventory(json.get(INVENTORY).getAsJsonObject());
		
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		json.addProperty(LEVEL, level);
		json.addProperty(EXPERIENCE, experience);
		json.addProperty(GAMEMODE, gameMode.name());
		json.addProperty(HEALTH, health);
		json.addProperty(FOOD_LEVEL, foodLevel);
		json.addProperty(SATURATION, saturation);

		JsonArray effects = new JsonArray();
		for (SPotionEffect effect : this.effects) {
			effects.add(effect.toJsonObject());
			System.out.println(effect.toJsonObject().toString());
		}
		json.add(EFFECTS, effects);
		System.out.println(effects.toString());
		
		json.add(LOCATION, location.toJsonObject());
		json.addProperty(FLYING, flying);
		json.addProperty(ALLOW_FLIGHT, allowFlight);
		json.addProperty(GOD_MODE, godMode);
		
		json.add(INVENTORY, inventory.toJsonObject());
		
		return json;
	}

	public void apply(Player player) {
		player.teleport(location.getLocation());
		
		player.setLevel(level);
		player.setExp(experience);
		player.setGameMode(gameMode);
		player.setHealth(health);
		player.setFoodLevel(foodLevel);
		player.setSaturation(saturation);
		for (SPotionEffect effect : effects)
			player.addPotionEffect(effect.getPotionEffect(), true);
		
		player.setAllowFlight(allowFlight);
		if (flying)
			player.setAllowFlight(true);
		player.setFlying(flying);

		if (Library.isEssentialsEnabled())
			EssentialsHandle.setGodModeEnabled(player, godMode);

		player.getInventory().setContents(inventory.getContents());
	}

}
