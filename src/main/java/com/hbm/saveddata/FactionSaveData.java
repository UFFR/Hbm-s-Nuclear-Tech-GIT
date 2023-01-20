package com.hbm.saveddata;

import java.util.Map;

import com.hbm.faction.Factions;
import com.hbm.main.DeserializationException;
import com.hbm.main.MainRegistry;

import api.hbm.faction.Faction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class FactionSaveData extends WorldSavedData
{
	private static final String KEY = "ntmFactionData";
	private static Map<String, Faction> factionMap = Factions.getFactionMap();
	
	public static FactionSaveData getData(World world)
	{
		FactionSaveData data = (FactionSaveData) world.perWorldStorage.loadData(FactionSaveData.class, KEY);
		if (data == null)
		{
			data = new FactionSaveData();
			world.perWorldStorage.setData(KEY, data);
		}
		return data;
	}
	
	public FactionSaveData()
	{
		super(KEY);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		for (Faction faction : factionMap.values())
			nbt.setByteArray(faction.getRegistryName(), faction.writeToBytes());
		
		nbt.setByteArray("treaties", Faction.serializeFactionTreaties());
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		for (Faction faction : factionMap.values())
		{
			try
			{
				faction.readFromBytes(nbt.getByteArray(faction.getRegistryName()));
			} catch (DeserializationException e)
			{
				MainRegistry.logger.catching(e);
			}
		}
		
		Faction.deserializeFactionTreaties(nbt.getByteArray("treaties"));
	}

}
