package ca.poutineqc.base.commands.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import ca.poutineqc.base.datastorage.serializable.sitems.SItem;
import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.Playable;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.PMessages;
import ca.poutineqc.base.plugin.PPlugin;

public class PInventoryArenaColor extends PInventory {

	private Playable game;

	public PInventoryArenaColor(PPlugin plugin, PPlayer pPlayer, Playable game) {
		super(plugin, pPlayer);

		Language local = pPlayer.getLanguage();
		this.title = local.get(PMessages.SETSTART_GUI_TITLE);
		this.game = game;

		this.amountOfRows = (int) Math.floor(game.getMaxStart() / 4.0) + 2;

		createInventory();
		fillInventory();
	}

	@Override
	public void fillInventory() {

		SItem item;
		Language local = pPlayer.getLanguage();

		// =================================================================
		// Instructions
		// =================================================================

		item = new SItem(Material.BOOKSHELF);
		item.setDisplayName(local.get(PMessages.KEYWORD_GUI_INSTRUCTIONS));
		for (String loreLine : local.get(PMessages.ARENA_COLOR_GUI_INFO).split("\n"))
			item.addToLore(loreLine);
		inventory.setItem(4, item.getItem());

		// =================================================================
		// Glass Spacer
		// =================================================================

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

		// =================================================================
		// Start Points
		// =================================================================

		item = new SItem(Material.INK_SACK);
		int slot = 18;

		for (int i = 0; i < game.getMaxStart(); i++) {
			item.setDisplayName(ChatColor.GOLD + ChatColor
					.stripColor(local.get(PMessages.KEYWORD_START_NUMBER).replace("%number%", String.valueOf(i))));

			while (slot % 9 == 0 || slot % 9 == 1 || slot % 9 == 4 || slot % 9 == 7 || slot % 9 == 8)
				slot++;

			inventory.setItem(slot, item.getItem());
		}

		// =================================================================
		// Final procedure
		// =================================================================

		openInventory();

	}

	@Override
	public void update(ItemStack itemStack, InventoryAction action) {

		String itemName = itemStack.getItemMeta().getDisplayName();

		String refinedTitle = ChatColor.stripColor(itemName).replaceAll("[^0-9]", "");
		int index = Integer.parseInt(refinedTitle);

		Language local = pPlayer.getLanguage();
		if (action == InventoryAction.PICKUP_HALF) {
			game.getArena().setStart(index, null);
			local.sendMessage(plugin, pPlayer.getPlayer(), local.get(PMessages.START_REMOVE)
					.replace("%arena%", game.getArena().getName()).replace("%number%", String.valueOf(index)));
		} else {
			game.getArena().setStart(index, pPlayer.getPlayer().getLocation());
			local.sendMessage(plugin, pPlayer.getPlayer(), local.get(PMessages.START_ADD)
					.replace("%arena%", game.getArena().getName()).replace("%number%", String.valueOf(index)));
		}

		new PInventoryArenaColor(plugin, pPlayer, game);
	}

}
