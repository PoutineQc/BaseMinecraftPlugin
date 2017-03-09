package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

public class SItemCreatureSpawner extends SItem {

	private static final String DELAY = "delay";
	private static final String ENTITY_TYPE = "entityType";
	
	private int delay = 20;
	private EntityType entityType;
	
	public SItemCreatureSpawner(EntityType entityType) {
		super(Material.MOB_SPAWNER);
		this.entityType = entityType;
	}

	public SItemCreatureSpawner(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		CreatureSpawner state = (CreatureSpawner) meta.getBlockState();
		
		this.delay = state.getDelay();
		this.entityType = state.getSpawnedType();
	}

	public SItemCreatureSpawner(JsonObject json) {
		super(json);
		String entityTypeName = json.get(ENTITY_TYPE).getAsString();
		for (EntityType type : EntityType.values())
			if (type.name().equals(entityTypeName))
				this.entityType = type;
		
		this.delay = json.get(DELAY).getAsInt();
	}
	
	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.addProperty(ENTITY_TYPE, entityType.name());
		json.addProperty(DELAY, delay);
		return json;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		CreatureSpawner state = (CreatureSpawner) meta.getBlockState();
		
		state.setSpawnedType(entityType);
		state.setDelay(delay);
		
		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemCreatureSpawner))
			return false;
		
		SItemCreatureSpawner spawner = (SItemCreatureSpawner) o;
		
		if (this.entityType != spawner.entityType)
			return false;
		
		if (this.delay != spawner.delay)
			return false;
		
		return super.equals(o);
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}
	
	public EntityType getEntityType() {
		return entityType;
	}

}
