package com.hbm.saveddata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class FactionSavedData extends WorldSavedData
{
	public static final String dataKey = "NTM_FACTION_DATA";
	private static FactionSavedData instance;
	private final World world;
	private FactionSavedData(World world)
	{
		super(dataKey);
		this.world = world;
		instance = this;
		markDirty();
	}

	public static FactionSavedData getInstance(World world)
	{
		return instance == null ? new FactionSavedData(world) : instance;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

}
