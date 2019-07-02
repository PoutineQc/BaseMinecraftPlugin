package ca.sebastienchagnon.minecraft.prolib.commands.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ca.sebastienchagnon.minecraft.prolib.PPlugin;
import ca.sebastienchagnon.minecraft.prolib.instantiable.PPlayer;

public abstract class PInventory {
	protected PPlugin plugin;
	protected PPlayer pPlayer;

	protected Inventory inventory;
	protected int amountOfRows;
	protected String title;

	public PInventory(PPlugin plugin, PPlayer pPlayer) {
		this.plugin = plugin;
		this.pPlayer = pPlayer;
	}

	public abstract void fillInventory();

	public abstract void update(ItemStack itemStack, InventoryAction action);

	public static boolean areEqualOnColorStrip(String itemA, String itemB) {
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', itemA))
				.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', itemB)));
	}

	protected void createInventory() {
		inventory = Bukkit.createInventory(pPlayer.getPlayer(), amountOfRows * 9, getFullTitle());
	}

	protected String getFullTitle() {
		return ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + " " + this.title);
	}

	protected void openInventory() {
		pPlayer.getPlayer().openInventory(inventory);
		pPlayer.setCurrentInventory(this);
	}

	protected void closeInventory() {
		pPlayer.getPlayer().closeInventory();
		pPlayer.setCurrentInventory(null);
	}
}
