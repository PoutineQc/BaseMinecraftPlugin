package ca.poutineqc.base.data.values;

import org.bukkit.Material;
import org.bukkit.block.EndGateway;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

public class SItemEndGateway extends SItem {

	private static final String DESTINATION = "destination";
	private static final String EXACT = "exact";
	
	private SLocation destination;
	private boolean exact;

	public SItemEndGateway(SLocation destination, boolean exact) {
		super(Material.END_GATEWAY);
		this.destination = destination;
		this.exact = exact;
	}

	public SItemEndGateway(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		EndGateway state = (EndGateway) meta.getBlockState();
		
		this.destination = new SLocation(state.getExitLocation());
		this.exact = state.isExactTeleport();
	}

	public SItemEndGateway(JsonObject json) {
		super(json);
		this.destination = new SLocation(json.get(DESTINATION).getAsJsonObject());
		this.exact = json.get(EXACT).getAsBoolean();
	}
	
	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.add(DESTINATION, destination.toJsonObject());
		json.addProperty(EXACT, exact);
		return json;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		EndGateway state = (EndGateway) meta.getBlockState();
		
		state.setExitLocation(destination.getLocation());
		state.setExactTeleport(exact);
		
		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemEndGateway))
			return false;

		SItemEndGateway item = (SItemEndGateway) o;
		
		if (!item.destination.equals(this.destination))
			return false;
		
		if (this.exact ^ item.exact)
				return false;
		
		return super.equals(o);
	}

}
