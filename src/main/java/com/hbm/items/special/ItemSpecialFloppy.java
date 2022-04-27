package com.hbm.items.special;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.hbm.main.MainRegistry;
import com.hbm.saveddata.TimeSavedData;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;

import api.hbm.computer.File;
import api.hbm.internet.IDataStorageItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

public final class ItemSpecialFloppy extends ItemCustomLore implements IDataStorageItem
{
	// It will do something, eventually...
	public ItemSpecialFloppy()
	{
//		super((long)2.88, 0, 4000, true, EnumStorageItemType.SINGULARITY);
	}
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean bool)
	{
		list.add(I18nUtil.resolveKey(getUnlocalizedName() + ".desc19"));
		list.add("");
		list.addAll(Arrays.asList(I18nUtil.resolveKeyArray("item.storage_magnetic_fdd_tainted.desc" + MainRegistry.polaroidID)));
		list.add("");
		list.add(I18nUtil.resolveKey(getUnlocalizedName() + ".desc20"));
	}
	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_)
	{
		return EnumRarity.epic;
	}
	@Override
	public long getMaxCapacity(ItemStack stack)
	{
		return (long) (2.88 * BobMathUtil.MB);
	}
	@Override
	public long getReadSpeed(ItemStack stack)
	{
		return 4 * BobMathUtil.KB;
	}
	@Override
	public long getWriteSpeed(ItemStack stack)
	{
		return 0;
	}
	@Override
	public boolean writeFile(File file, ItemStack stack)
	{
		return false;
	}
	@Override
	public boolean deleteFile(String filename, ItemStack stack)
	{
		return false;
	}
	@Override
	public boolean overwriteFile(File file, ItemStack stack)
	{
		return false;
	}
	
	private static final String name = "GULF_BLUEPRINT";
	private static final File blueprint = new File(name, (long) (2.88 * BobMathUtil.MB), TimeSavedData.constructCompressed(1980, 92, 6411));
	// TODO The funny
	@Override
	public File readFile(String filename, ItemStack stack)
	{
		if (filename.equals(name))
			return blueprint.clone();
		else
			return null;
	}
	@Override
	public String readFileHash(String filename, ItemStack stack)
	{
		return "";
	}
	@Override
	public Set<String> getFileList(ItemStack stack)
	{
		return ImmutableSet.of(name);
	}
}
