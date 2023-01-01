package com.hbm.entity.mob.smart.combine;

import com.hbm.entity.mob.ai.squad.SquadBase;

import net.minecraft.world.World;

public abstract class EntityCombineCommando extends EntityCombineBase
{

	public EntityCombineCommando(World world, SquadBase squad, String name, String squadName)
	{
		super(world, squad, name, squadName);
		// TODO Auto-generated constructor stub
	}

	public EntityCombineCommando(World world, SquadBase squad)
	{
		super(world, squad);
		// TODO Auto-generated constructor stub
	}

}
