package ca.poutineqc.base.commands.inventories;

import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import ca.poutineqc.base.datastorage.serializable.sitems.SItem;
import ca.poutineqc.base.datastorage.serializable.sitems.SItemBanner;
import ca.poutineqc.base.datastorage.serializable.sitems.SItemBanner.CustomPattern;
import ca.poutineqc.base.instantiable.PPlayer;
import ca.poutineqc.base.instantiable.Playable;
import ca.poutineqc.base.instantiable.Playable.GameState;
import ca.poutineqc.base.instantiable.PlayablePlayer;
import ca.poutineqc.base.lang.Language;
import ca.poutineqc.base.lang.PMessages;
import ca.poutineqc.base.plugin.PPlugin;
import ca.poutineqc.base.utils.Utils;

public class PInventoryGameJoin extends PInventory {

	private int page;
	private int startingIndex;
	private int amountOnPage;
	private boolean hasNext;
	List<Playable> arenas;

	public PInventoryGameJoin(PPlugin plugin, PPlayer pPlayer, int page) {
		super(plugin, pPlayer);

		Language local = pPlayer.getLanguage();
		this.title = local.get(PMessages.JOIN_GUI_TITLE);
		this.page = page;

		this.arenas = plugin.getGames();
		this.arenas.sort(new Comparator<Playable>() {

			@Override
			public int compare(Playable o1, Playable o2) {
				return o1.getArena().getName().compareTo(o2.getArena().getName());
			}
		});

		this.startingIndex = (page - 1) * 27;
		int amoundBeforeEnd = this.arenas.size() - this.startingIndex;
		this.hasNext = amoundBeforeEnd > 27;
		this.amountOnPage = this.hasNext ? 27 : amoundBeforeEnd;
		this.amountOfRows = ((int) Math.ceil(amountOnPage / 9.0)) + 3;

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
		for (String loreLine : local.get(PMessages.JOIN_GUI_INFO).split("\n"))
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
		// Arenas
		// =================================================================

		item = new SItem(Material.INK_SACK);
		int slot = 18;

		for (int i = 0; i < amountOnPage; i++) {
			Playable game = arenas.get(startingIndex + i);
			item.setDisplayName(ChatColor.GOLD + game.getArena().getName());
			item.clearLore();

			if (game.getGameState() == GameState.UNREADY) {
				item.setData((short) 8);
				item.addToLore(
						ChatColor.DARK_GRAY + ChatColor.stripColor(local.get(PMessages.KEYWORD_GAMESTATE_UNSET)));

			} else if (game.getGameState() == GameState.ACTIVE || game.getGameState() == GameState.ENDING) {
				item.setData((short) 12);
				item.addToLore(ChatColor.RED + ChatColor.stripColor(local.get(PMessages.KEYWORD_GAMESTATE_ACTIVE)));

			} else {
				item.setData((short) 10);
				item.addToLore(ChatColor.GREEN + ChatColor.stripColor(local.get(PMessages.KEYWORD_GAMESTATE_READY)));
				item.addToLore(ChatColor.YELLOW + ChatColor.stripColor(local.get(PMessages.KEYWORD_PLAYERS)) + " : "
						+ String.valueOf(game.getPlayers().size()) + "/"
						+ String.valueOf(game.getArena().getMaxPlayer()));
				for (PlayablePlayer playablePlayer : game.getPlayers())
					item.addToLore(ChatColor.GRAY + "- " + playablePlayer.getPPlayer().getPlayer().getName());
			}

			inventory.setItem(slot++, item.getItem());
		}

		// =================================================================
		// Current
		// =================================================================

		int centerPosition = ((amountOfRows - 1) * 9) + 4;
		item = new SItemBanner(CustomPattern.ARROW_ACTUAL);
		item.setDisplayName(local.get(PMessages.KEYWORD_GUI_PAGE).replace("%number%", String.valueOf(page)));
		inventory.setItem(centerPosition, item.getItem());

		// =================================================================
		// Next Page
		// =================================================================

		if (hasNext) {
			item = new SItemBanner(CustomPattern.ARROW_RIGHT);
			item.setDisplayName(local.get(PMessages.KEYWORD_GUI_PAGE).replace("%number%", String.valueOf(page + 1)));
			inventory.setItem(centerPosition + 1, item.getItem());
		}

		// =================================================================
		// Previous Page
		// =================================================================

		if (page > 1) {
			item = new SItemBanner(CustomPattern.ARROW_LEFT);
			item.setDisplayName(local.get(PMessages.KEYWORD_GUI_PAGE).replace("%number%", String.valueOf(page - 1)));
			inventory.setItem(centerPosition - 1, item.getItem());
		}

		// =================================================================
		// Final Procedure
		// =================================================================

		openInventory();

	}

	@Override
	public void update(ItemStack itemStack, InventoryAction action) {

		Language local = pPlayer.getLanguage();
		String itemName = itemStack.getItemMeta().getDisplayName();

		if (Utils.isEqualOnColorStrip(itemName,
				local.get(PMessages.KEYWORD_GUI_PAGE).replace("%number%", String.valueOf(page + 1)))) {
			new PInventoryGameJoin(plugin, pPlayer, page + 1);
			return;
		}

		if (Utils.isEqualOnColorStrip(itemName,
				local.get(PMessages.KEYWORD_GUI_PAGE).replace("%number%", String.valueOf(page - 1)))) {
			new PInventoryGameJoin(plugin, pPlayer, page - 1);
			return;
		}

		Playable game = plugin.getGameFromArenaName(ChatColor.stripColor(itemName));
		if (game == null)
			return;

		if (action == InventoryAction.PICKUP_HALF) {
			pPlayer.getPlayer().sendMessage(game.getInformation(pPlayer.getPlayer()));
		} else {
			game.addPlayer(pPlayer, true);
		}

		pPlayer.getPlayer().closeInventory();

	}

}
