package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.potion.PotionEffectType;

import com.google.gson.JsonObject;

public class SItemBeacon extends SItem {

	private static final String PRIMARY = "primary";
	private static final String SECONDARY = "secondary";

	private PotionEffectType primary;
	private PotionEffectType secondary;

	public SItemBeacon(PotionEffectType primary) {
		super(Material.BEACON);
		this.primary = primary;
	}

	public SItemBeacon(PotionEffectType primary, PotionEffectType secondary) {
		super(Material.BEACON);
		this.primary = primary;
		this.secondary = secondary;
	}

	public SItemBeacon(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		Beacon state = (Beacon) meta.getBlockState();

		if (state.getPrimaryEffect() != null)
			this.primary = state.getPrimaryEffect().getType();

		if (state.getSecondaryEffect() != null)
			this.secondary = state.getSecondaryEffect().getType();
	}

	public SItemBeacon(JsonObject json) {
		super(json);
		if (!json.get(PRIMARY).isJsonNull())
			this.primary = PotionEffectType.getByName(json.get(PRIMARY).getAsString());
		
		if (!json.get(SECONDARY).isJsonNull())
			this.secondary = PotionEffectType.getByName(json.get(SECONDARY).getAsString());
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.addProperty(PRIMARY, primary == null ? null : primary.getName());
		json.addProperty(SECONDARY, secondary == null ? null : secondary.getName());
		return json;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		Beacon state = (Beacon) meta.getBlockState();

		state.setPrimaryEffect(primary);
		state.setSecondaryEffect(secondary);

		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemBeacon))
			return false;

		SItemBeacon beacon = (SItemBeacon) o;

		if (!beacon.primary.equals(this.primary))
			return false;

		if (!beacon.secondary.equals(this.secondary))
			return false;

		return super.equals(o);
	}

	public PotionEffectType getPrimary() {
		return primary;
	}

	public PotionEffectType getSecondary() {
		return secondary;
	}

	public void setPrimary(PotionEffectType primary) {
		this.primary = primary;
	}

	public void setSecondary(PotionEffectType secondary) {
		this.secondary = secondary;
	}

}
