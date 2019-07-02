package ca.poutineqc.base;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.poutineqc.base.event.PlayerLoadEvent;
import ca.poutineqc.base.event.PlayerUnloadEvent;
import ca.poutineqc.base.instantiable.PPlayer;

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
