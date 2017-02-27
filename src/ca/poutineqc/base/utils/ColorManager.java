package ca.poutineqc.base.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import ca.poutineqc.base.data.values.SLong;
import ca.poutineqc.base.data.values.SValue;

public class ColorManager implements SValue {

	public static final int MAX_STRING_LENGTH = SLong.MAX_STRING_LENGTH;

	private SLong colorIndice;
	private List<ItemStackManager> allBlocks;
	private List<ItemStackManager> onlyChoosenBlocks;

	public ColorManager(String value) {
		this.colorIndice = new SLong(value);
		updateLists();
	}

	public void setColorIndice(long value) {
		this.colorIndice = new SLong(value);
		updateLists();
	}

	public void updateLists() {
		allBlocks = new ArrayList<ItemStackManager>();
		onlyChoosenBlocks = new ArrayList<ItemStackManager>();
		long tempColorIndice = colorIndice.getLong();

		for (int i = 31; i >= 0; i--) {
			ItemStackManager icon;
			if (i >= 16)
				icon = new ItemStackManager(Material.STAINED_CLAY);
			else
				icon = new ItemStackManager(Material.WOOL);

			icon.setData((short) (i % 16));

			int value = (int) Math.pow(2, i);
			if (value <= tempColorIndice) {
				icon.addEnchantement(Enchantment.DURABILITY, 1);
				tempColorIndice -= value;
				onlyChoosenBlocks.add(0, icon);
			}

			allBlocks.add(0, icon);
		}

		if (onlyChoosenBlocks.size() == 0)
			onlyChoosenBlocks = allBlocks;
	}

	public ItemStackManager getRandomAvailableBlock() {
		return onlyChoosenBlocks.get((int) Math.floor(Math.random() * onlyChoosenBlocks.size()));
	}

	public List<ItemStackManager> getAllBlocks() {
		return allBlocks;
	}

	public List<ItemStackManager> getOnlyChoosenBlocks() {
		return onlyChoosenBlocks;
	}

	public long getColorIndice() {
		return colorIndice.getLong();
	}

	@Override
	public String toSString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxToStringLength() {
		return MAX_STRING_LENGTH;
	}
}
