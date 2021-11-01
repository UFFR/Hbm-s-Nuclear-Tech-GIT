package com.hbm.items.special;

import java.util.List;

import com.hbm.items.machine.ItemStorageMedium;
import com.hbm.main.MainRegistry;
import com.hbm.util.I18nUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import scala.actors.threadpool.Arrays;

public final class ItemSpecialFloppy extends ItemStorageMedium
{
	// It will do something, eventually...
	public ItemSpecialFloppy()
	{
		super((long)2.88, 0, 4000, true, EnumStorageItemType.SINGULARITY);
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
}
