package com.hbm.entity.mob.ai.squad;

import api.hbm.faction.Faction;
import net.minecraft.world.World;

public class SquadSingle extends SquadBase
{

	public SquadSingle(World world)
	{
		super(world);
	}

	@Override
	public String getName()
	{
		return "[None]";
	}

	@Override
	public int preferredSize()
	{
		return 1;
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateMembers()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isTaskComplete()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean strictSize()
	{
		return true;
	}

	@Override
	public void notifyUpdatedTask(SquadTask task)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Faction getFaction()
	{
		return members.get(0).getFaction();
	}

}
