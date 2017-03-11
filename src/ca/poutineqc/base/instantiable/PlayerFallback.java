package ca.poutineqc.base.instantiable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.poutineqc.base.datastorage.FlatFile;
import ca.poutineqc.base.datastorage.JSON;
import ca.poutineqc.base.datastorage.serializable.SPlayer;
import ca.poutineqc.base.event.PlayerJoinGameEvent;
import ca.poutineqc.base.event.PlayerLeaveGameEvent;
import ca.poutineqc.base.plugin.Library;

public class PlayerFallback implements Listener {

	public static final String FOLDER_NAME = "CrashFallback";

	private Library library;
	private List<JSON> savedPlayersFiles;

	public PlayerFallback(Library library) {
		this.library = library;
		this.savedPlayersFiles = new ArrayList<JSON>();

		File folder = FlatFile.getFolder(library, FOLDER_NAME);
		File[] savedPlayersFiles = folder.listFiles();
		for (File file : savedPlayersFiles) {
			System.out.println(file);
			this.savedPlayersFiles.add(new JSON(file));
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogin(PlayerJoinEvent event) {

		Player player = event.getPlayer();
		if (!hasSaveFile(player))
			return;

		JSON file = getAndRemoveSaveFile(player);
		SPlayer sPlayer = new SPlayer(file.getJsonObject());
		sPlayer.apply(player);
		file.delete();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoinGame(PlayerJoinGameEvent event) {

		Player player = event.getPlayer().getPlayer();
		SPlayer sPlayer = new SPlayer(player);
		JSON file = getAndRemoveSaveFile(player);
		file.save(sPlayer.toJsonObject());
		this.savedPlayersFiles.add(file);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLeaveGame(PlayerLeaveGameEvent event) {

		Player player = event.getPlayer().getPlayer();
		if (!hasSaveFile(player))
			return;

		JSON file = getAndRemoveSaveFile(player);
		SPlayer sPlayer = new SPlayer(file.getJsonObject());
		sPlayer.apply(player);
		file.delete();
	}

	private JSON getAndRemoveSaveFile(Player player) {
		String uuidString = player.getUniqueId().toString();
		for (JSON file : savedPlayersFiles)
			if (file.getFileName().replace(".json", "").equals(uuidString)) {
				this.savedPlayersFiles.remove(file);
				return file;
			}

		return new JSON(library, uuidString, false, FOLDER_NAME);
	}

	private boolean hasSaveFile(Player player) {
		String uuidString = player.getUniqueId().toString();
		for (JSON file : savedPlayersFiles)
			if (file.getFileName().replace(".json", "").equals(uuidString))
				return true;

		return false;
	}
}
