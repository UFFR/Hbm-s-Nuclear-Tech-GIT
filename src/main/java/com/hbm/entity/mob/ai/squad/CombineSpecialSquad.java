package com.hbm.entity.mob.ai.squad;

import com.hbm.faction.Factions;

import api.hbm.faction.Faction;
import net.minecraft.world.World;

public class CombineSpecialSquad extends SquadBase
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7838197962373741982L;

	public CombineSpecialSquad(World world)
	{
		super(world);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName()
	{
		return "Special Stabilization Force Alpha";
	}
	
	@Override
	public int preferredSize()
	{
		switch (world.difficultySetting)
		{
			case EASY: return 4;
			case HARD: return 8;
			case NORMAL: return 6;
			default:
			case PEACEFUL: return 0;
		}
	}
	
	@Override
	public void update()
	{
		// TODO Auto-generated method stub
		checkDeadOrInvalidMembers();
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
	public void notifyUpdatedTask(SquadTask task)
	{
		// TODO Auto-generated method stub
		worldChat.accept(chatSupplier.apply("[Overwatch: Updated task to " + task + ']'));
	}

	@Override
	public Faction getFaction()
	{
		return Factions.COMBINE;
	}

}
