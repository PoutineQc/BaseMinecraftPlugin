package ca.sebastienchagnon.minecraft.prolib.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import ca.sebastienchagnon.minecraft.prolib.Library;
import ca.sebastienchagnon.minecraft.prolib.commands.inventories.PInventory;
import ca.sebastienchagnon.minecraft.prolib.instantiable.PPlayer;

public class InventoryEvents implements Listener {

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (!(event.getPlayer() instanceof Player))
			return;

		PPlayer pPlayer = Library.getPPlayer((Player) event.getPlayer());
		if (pPlayer == null)
			return;
		
		pPlayer.setCurrentInventory(null);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getAction() == InventoryAction.NOTHING || event.getAction() == InventoryAction.UNKNOWN)
			return;

		if (!(event.getWhoClicked() instanceof Player))
			return;

		ItemStack item = event.getCurrentItem();
		PPlayer player = Library.getPPlayer((Player) event.getWhoClicked());
		if (player == null)
			return;

		PInventory inv = player.getCurrentInventory();
		if (inv == null)
			return;

		event.setCancelled(true);
		inv.update(item, event.getAction());
	}
}
