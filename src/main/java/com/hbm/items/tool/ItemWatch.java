package com.hbm.items.tool;

import java.util.List;

import com.hbm.lib.Library;
import com.hbm.saveddata.TimeSavedData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWatch extends Item
{
	public ItemWatch()
	{
		setUnlocalizedName("watch_digital");
		setTextureName("hbm:" + getUnlocalizedName());
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean b)
	{
		tooltip.add(getTime(player.worldObj));
	}
	
	public static String getTime(World world)
	{
		double time = TimeSavedData.getData(world).getTime();
		time = Library.roundNumber(time, 4);
		return String.valueOf(time);
	}
}
