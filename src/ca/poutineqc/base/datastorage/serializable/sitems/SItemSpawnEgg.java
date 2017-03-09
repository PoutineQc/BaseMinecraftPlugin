package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import com.google.gson.JsonObject;

public class SItemSpawnEgg extends SItem {

	private static final String ENTITY_TYPE = "entityType";

	protected EntityType entity;

	public SItemSpawnEgg(EntityType entity) {
		super(Material.MONSTER_EGG);

		this.entity = entity;
	}

	public SItemSpawnEgg(ItemStack itemStack) {
		super(itemStack);

		SpawnEggMeta meta = (SpawnEggMeta) itemStack.getItemMeta();
		this.entity = meta.getSpawnedType();

	}

	public SItemSpawnEgg(JsonObject json) {
		super(json);

		String typeName = json.get(ENTITY_TYPE).getAsString();
		for (EntityType type : EntityType.values())
			if (type.name().equals(typeName))
				this.entity = type;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();

		json.addProperty(ENTITY_TYPE, entity.name());

		return json;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		SpawnEggMeta meta = (SpawnEggMeta) item.getItemMeta();
		meta.setSpawnedType(entity);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemSpawnEgg))
			return false;

		SItemSpawnEgg spawn = (SItemSpawnEgg) o;

		if (!this.entity.equals(spawn.entity))
			return false;

		return super.equals(o);
	}

}
