package ca.poutineqc.base.data.values;

import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

public class SItemJukebox extends SItem {

	private static final String PLAYING = "playing";
	
	private Material playing;

	public SItemJukebox(Material playing) {
		super(Material.JUKEBOX);
		this.playing = playing;
	}

	public SItemJukebox(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		Jukebox state = (Jukebox) meta.getBlockState();
		
		this.playing = state.getPlaying();
	}

	public SItemJukebox(JsonObject json) {
		super(json);
		
		this.playing = Material.getMaterial(json.get(PLAYING).getAsString());
	}
	
	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.addProperty(PLAYING, playing.name());
		return json;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		Jukebox state = (Jukebox) meta.getBlockState();
		
		state.setPlaying(playing);
		
		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemJukebox))
			return false;

		SItemJukebox item = (SItemJukebox) o;
		
		if (!item.playing.equals(this.playing))
			return false;
		
		return super.equals(o);
	}
	
	public void setPlaying(Material playing) {
		this.playing = playing;
	}
	
	public Material getPlaying() {
		return playing;
	}

}
