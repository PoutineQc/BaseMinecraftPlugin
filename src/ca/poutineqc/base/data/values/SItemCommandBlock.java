package ca.poutineqc.base.data.values;

import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

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
		CommandBlock state = (CommandBlock) meta.getBlockState();

		System.out.println("1:'" + (state.getCommand()) + "'");

		state.setCommand(command);
		state.setName(name);

		System.out.println("2:'" + state.getCommand() + "'");

		meta.setBlockState(state);

		System.out.println("3:'" + ((CommandBlock) meta.getBlockState()).getCommand() + "'");
		state = (CommandBlock) meta.getBlockState();
		state.setCommand(command);
		state.setName(name);
		System.out.println("4:'" + state.getCommand() + "'");

		item.setItemMeta(meta);

		System.out.println(
				"5:'" + ((CommandBlock) ((BlockStateMeta) item.getItemMeta()).getBlockState()).getCommand() + "'");

		return item;
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
