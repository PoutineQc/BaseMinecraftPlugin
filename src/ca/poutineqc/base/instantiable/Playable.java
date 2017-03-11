package ca.poutineqc.base.instantiable;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Playable {

	void addPlayer(PPlayer pPlayer, boolean teleport);

	void removePlayer(Player player);

	Arena getArena();

	GameState getGameState();


	public enum GameState {
		UNREADY, ACTIVE, ENDING, READY

	}


	List<? extends PlayablePlayer> getPlayers();

	String getInformation(CommandSender commandSender);

	int getMaxStart();

	void resetArena(ItemStack itemStack);
}
