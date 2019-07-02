package ca.sebastienchagnon.minecraft.prolib.dao;

import java.util.UUID;

import ca.sebastienchagnon.minecraft.prolib.instantiable.PPlayer;

public interface ProPlayerDAO {

	public PPlayer getProPlayer(UUID uuid);
	
}
