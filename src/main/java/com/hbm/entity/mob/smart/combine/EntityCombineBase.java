package com.hbm.entity.mob.smart.combine;

import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.entity.mob.ai.squad.SquadSingle;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.entity.mob.smart.NameUtil;
import com.hbm.faction.Factions;

import api.hbm.faction.Faction;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityCombineBase extends EntitySmartNPCBase
{
	public EntityCombineBase(World world, SquadBase squad, String name, String squadName)
	{
		super(world, squad, name, squadName);
		this.name = name;
		Factions.COMBINE.getRegisteredNames().add(name);
	}
	
	public EntityCombineBase(World world, SquadBase squad)
	{
		this(world, squad == null ? new SquadSingle(world) : squad,
				NameUtil.generateNameFromDefined(NameUtil::generateCombineName, Factions.COMBINE.getRegisteredNames(), Short.MAX_VALUE),
				squad == null ? NameUtil.generateCombineSquadName() : NameUtil.generateNameFromDefined(NameUtil::generateCombineSquadName, squad.getRegisteredNames(), NameUtil.MAX_COMBINE_SQUAD_NAMES));
	}
	
	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
	{
//		name = NameUtil.generateNameFromDefined(NameUtil::generateCombineName, Factions.COMBINE.getRegisteredNames(), Short.MAX_VALUE);
		return super.onSpawnWithEgg(data);
	}
	
	@Override
	public float getWidth()
	{
		return 0.6f;
	}
	
	@Override
	public float getHeight()
	{
		return 1.8f;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getSquadName()
	{
		return squadName;
	}

	@Override
	public Faction getFaction()
	{
		return Factions.COMBINE;
	}
	
	@Override
	public boolean hasFaction()
	{
		return true;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setString("name", name);
		nbt.setString("squadName", squadName);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		name = nbt.getString("name");
		squadName = nbt.getString("squadName");
	}
	
	@Override
	public int getTalkInterval()
	{
		return 200;
	}
}
