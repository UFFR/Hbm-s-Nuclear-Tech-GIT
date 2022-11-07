package com.hbm.entity.mob.smart.combine;

import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.entity.mob.smart.NameUtil;
import com.hbm.faction.Factions;

import api.hbm.faction.Faction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class EntityCombineBase extends EntitySmartNPCBase
{
	protected final String name;
	protected String squadName;
	protected ItemStack heldGun;
	public EntityCombineBase(World world, SquadBase squad, String name)
	{
		super(world, squad);
		this.name = name;
		Factions.COMBINE.getRegisteredNames().add(name);
	}
	
	public EntityCombineBase(World world, SquadBase squad)
	{
		this(world, squad, NameUtil.generateNameFromDefined(NameUtil::generateCombineName, Factions.COMBINE.getRegisteredNames(), Short.MAX_VALUE));
	}
	
	@Override
	public float getWidth()
	{
		return 1;
	}
	
	@Override
	public float getHeight()
	{
		return 2;
	}
	
	@Override
	public String getName()
	{
		return name;
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
}
