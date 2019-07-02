package ca.sebastienchagnon.minecraft.prolib;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.sebastienchagnon.minecraft.prolib.event.PlayerLoadEvent;
import ca.sebastienchagnon.minecraft.prolib.event.PlayerUnloadEvent;
import ca.sebastienchagnon.minecraft.prolib.instantiable.PPlayer;

public class PlayerLoader implements Listener {

	Library library;

	public PlayerLoader(Library library) {
		this.library = library;
	}

	@EventHandler
	public void onPlayerLoginEvent(PlayerJoinEvent event) {

		if (library.getPlayers().loadIfSaved(event.getPlayer().getUniqueId())) {
			library.getServer().getPluginManager()
					.callEvent(new PlayerLoadEvent(Library.getPPlayer(event.getPlayer())));
		}
	}

	@EventHandler
	public void onPlayerDisconnectEvent(PlayerQuitEvent event) {
		PPlayer pPlayer = Library.getPPlayer(event.getPlayer());
		if (pPlayer != null) {
			library.getPlayers().removeIfHandled(event.getPlayer().getUniqueId());
			library.getServer().getPluginManager().callEvent(new PlayerUnloadEvent(pPlayer));
		}
	}
}
