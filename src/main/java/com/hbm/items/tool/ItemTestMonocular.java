package com.hbm.items.tool;

import java.util.List;

import com.hbm.interfaces.IMonocular;
import com.hbm.render.util.RenderScreenOverlay.Crosshair;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemTestMonocular extends Item implements IMonocular
{

	public ItemTestMonocular()
	{
		setUnlocalizedName("test_monocular");
	}

	@Override
	public Crosshair getCrosshairType()
	{
		return Crosshair.CLASSIC;
	}

	@Override
	public int maxZoom()
	{
		return 20;
	}

	@Override
	public int getZoomLevel(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.stackTagCompound = new NBTTagCompound();
			stack.stackTagCompound.setInteger(KEY_ZOOM, 1);
		}
		return stack.stackTagCompound.getInteger(KEY_ZOOM);
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public boolean hasNightVision()
	{
		return true;
	}

	@Override
	public boolean hasHUD()
	{
		return true;
	}

	@Override
	public boolean canTrack()
	{
		return true;
	}

	@Override
	public void setTargetedEntity(Entity e, ItemStack stack)
	{
		stack.stackTagCompound.setInteger(KEY_ENTITY, e.hashCode());
	}

	@Override
	public Entity getTargetedEntity(World world, ItemStack stack)
	{
		return world.getEntityByID(stack.stackTagCompound.getInteger(KEY_ENTITY));
	}

	@Override
	public List<String> getAdditionalText()
	{
		return null;
	}

}
