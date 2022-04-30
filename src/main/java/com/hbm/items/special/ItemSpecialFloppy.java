package com.hbm.items.special;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.hbm.lib.Library;
import com.hbm.main.MainRegistry;
import com.hbm.util.BobMathUtil;
import com.hbm.util.I18nUtil;

import api.hbm.Date;
import api.hbm.computer.filesystem.FileBase;
import api.hbm.computer.filesystem.FileMetadata;
import api.hbm.computer.filesystem.IDataStorageItem;
import api.hbm.computer.filesystem.TextFile;
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
		return (int) (2.88 * BobMathUtil.MB);
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
	public boolean writeFile(FileBase file, ItemStack stack)
	{
		return false;
	}
	@Override
	public boolean deleteFile(String filename, ItemStack stack)
	{
		return false;
	}
	@Override
	public boolean overwriteFile(FileBase file, ItemStack stack)
	{
		return false;
	}
	
	private static final String name = "GULF_BLUEPRINT";
	// TODO The funny
	private static final FileBase blueprint = new TextFile("Bababoey");/*(name, (long) (2.88 * BobMathUtil.MB), TimeSavedData.constructCompressed(1980, 92, 6411));*/
	@Override
	public FileBase readFile(String filename, ItemStack stack)
	{
		return filename.equals(name) ? blueprint.clone() : null;
	}
	@Override
	public String getFileHash(String filename, ItemStack stack)
	{
		return filename.equals(name) ? Library.getHash(blueprint.serialize()) : "";
	}
	@Override
	public FileMetadata getFileMetadata(String filename, ItemStack stack)
	{
		return filename.equals(filename) ? new FileMetadata(name, TextFile.class, (int) 2.88 * BobMathUtil.MB, blueprint.hashCode(), new Date(1980, 92, 6411), new Date(1980, 92, 6411), false) : null;
	}
	@Override
	public Set<String> getFileList(ItemStack stack)
	{
		return ImmutableSet.of(name);
	}
}
