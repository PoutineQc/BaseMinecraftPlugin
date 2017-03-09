package ca.poutineqc.base.datastorage.serializable.sitems;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

import ca.poutineqc.base.utils.Utils;

public class SItemCommandBlock extends SItem {

	private static final String NAME = "cbName";
	private static final String COMMAND = "command";

	String name;
	String command;

	public SItemCommandBlock(String command) {
		super(Material.COMMAND);
		this.command = command;
	}

	public SItemCommandBlock(ItemStack itemStack) {
		super(itemStack);

		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		CommandBlock state = (CommandBlock) meta.getBlockState();

		this.command = state.getCommand();
		this.name = state.getName();
	}

	public SItemCommandBlock(JsonObject json) {
		super(json);

		if (!json.get(NAME).isJsonNull())
			this.name = json.get(NAME).getAsString();

		this.command = json.get(COMMAND).getAsString();

	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.addProperty(NAME, name);
		json.addProperty(COMMAND, command);
		return json;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		meta.setBlockState(getCommandBlockState(name, command));
		item.setItemMeta(meta);
		return item;
	}
	
    public String getCustomName() {
		return name;
	}

	public void setCustomName(String name) {
		this.name = name;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public CommandBlock getCommandBlockState(String name, String command) {
        Block block = Utils.getFirstAvailableAirBlock();
        if (block == null)
        	return null;
        
        block.setType(Material.COMMAND);
        CommandBlock blockState = (CommandBlock) block.getState();
        blockState.setName(name);
        blockState.setCommand(command);
        blockState.update();

        block.setType(Material.AIR);
        return blockState;
    }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemCommandBlock))
			return false;

		SItemCommandBlock cb = (SItemCommandBlock) o;

		if (!super.equals(cb))
			return false;

		if (!cb.name.equals(this.name))
			return false;

		if (!cb.command.equals(this.command))
			return false;

		return true;
	}

}
