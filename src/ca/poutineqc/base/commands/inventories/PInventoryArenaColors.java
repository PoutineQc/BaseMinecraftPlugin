package ca.poutineqc.base.commands.inventories;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import ca.poutineqc.base.datastorage.serializable.sitems.SItem;
import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.Playable;
import ca.poutineqc.base.instantiable.Playable.GameState;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.PMessages;
import ca.poutineqc.base.plugin.PPlugin;

public class PInventoryArenaColors extends PInventory {

	private Playable game;

	public PInventoryArenaColors(PPlugin plugin, PPlayer pPlayer, Playable game) {
		super(plugin, pPlayer);

		this.game = game;
		Language local = pPlayer.getLanguage();
		this.title = local.get(PMessages.EDIT_COLORS_GUI_TITLE);
		this.amountOfRows = 6;
		createInventory();

		fillInventory();
	}

	@Override
	public void fillInventory() {
		Language local = pPlayer.getLanguage();
		SItem item;

		/***************************************************
		 * Instructions
		 ***************************************************/

		item = new SItem(Material.BOOKSHELF);
		item.setDisplayName(local.get(PMessages.KEYWORD_GUI_INSTRUCTIONS));
		for (String loreLine : local.get(PMessages.COLORS_GUI_INFO).split("\n"))
			item.addToLore(loreLine);
		inventory.setItem(4, item.getItem());

		/***************************************************
		 * Glass Spacer
		 ***************************************************/

		item = new SItem(Material.STAINED_GLASS_PANE);
		item.setData((short) 10);
		item.setDisplayName(ChatColor.RED + "");

		for (int i = 0; i < inventory.getSize(); i++)
			switch (i) {
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				inventory.setItem(i, item.getItem());
			}

		/***************************************************
		 * Blocks
		 ***************************************************/

		List<SItem> colorManager = game.getArena().getColorManager().getAllBlocks();
		for (int i = 0; i < 32; i++)
			inventory.setItem((int) ((Math.floor(i / 8.0) * 9) + 19 + (i % 8)), colorManager.get(i).getItem());

		/***************************************************
		 * Display
		 ***************************************************/

		openInventory();

	}

	@Override
	public void update(ItemStack itemStack, InventoryAction action) {
		if (itemStack.getType() != Material.STAINED_CLAY && itemStack.getType() != Material.WOOL)
			return;

		if (game.getGameState() == GameState.ACTIVE || game.getGameState() == GameState.ENDING) {
			pPlayer.getPlayer().closeInventory();
			pPlayer.setCurrentInventory(null);
			pPlayer.getLanguage().sendError(plugin, pPlayer.getPlayer(), PMessages.EDIT_COLOR_ERROR);
			return;
		}

		int valueOfItem = itemStack.getDurability();
		if (itemStack.getType() == Material.STAINED_CLAY)
			valueOfItem += 16;

		long previousColorIndice = game.getArena().getColorManager().getColorIndice();
		if (itemStack.getItemMeta().hasEnchants()) {
			game.getArena().getColorManager().setColorIndice(previousColorIndice - (int) Math.pow(2, valueOfItem));
		} else {
			game.getArena().getColorManager().setColorIndice(previousColorIndice + (int) Math.pow(2, valueOfItem));
		}

		game.resetArena(itemStack);
		new PInventoryArenaColors(plugin, pPlayer, game);
	}

}
