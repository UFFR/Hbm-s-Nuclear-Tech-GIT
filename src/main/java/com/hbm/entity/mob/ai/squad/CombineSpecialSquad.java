package com.hbm.entity.mob.ai.squad;

import java.util.ArrayList;
import java.util.List;

import com.google.common.annotations.Beta;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.entity.mob.smart.combine.EntityCombineCommando;
import com.hbm.faction.Factions;
import com.hbm.interfaces.ILocationProvider;

import api.hbm.faction.Faction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@Beta
public class CombineSpecialSquad extends SquadBase
{

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
		switch (getWorldDifficulty())
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
		if (world.getWorldTime() % 20 == 0)
			updateMembers();
		
		for (EntitySmartNPCBase member : members)
		{
			if (member.getRNG().nextInt(member.getTalkInterval()) == 0)
				member.onIdle();
		}
	}

	@Override
	public void updateMembers()
	{
		// TODO Auto-generated method stub
		checkDeadMembers();
		refreshMemberSightlines();
		if (targets.addAll(checkMemberSightlines()))
		{
			final List<EntityLivingBase> sortedTargets = new ArrayList<EntityLivingBase>(targets);
			for (EntitySmartNPCBase member : members)
			{
				if (sortedTargets.isEmpty())
					break;
				ILocationProvider.sortEntitiesByDistanceFrom(sortedTargets, member.getMyLocation());
				if (sortedTargets.get(0) instanceof EntityPlayer)
					member.onSpotPlayer((EntityPlayer) sortedTargets.remove(0));
				else
					member.onNewSpotting(sortedTargets.remove(0));
			}
		}
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
		return false;
	}
	
	@Override
	public void notifyUpdatedTask(SquadTask task)
	{
		// TODO Auto-generated method stub
		worldChat.accept(chatSupplier.apply("[Overwatch: Updated task to " + task + ']'));
	}
	
	@Override
	public boolean addMember(EntitySmartNPCBase npc)
	{
		if (npc instanceof EntityCombineCommando)
			return super.addMember(npc);

		return false;
	}

	@Override
	public Faction getFaction()
	{
		return Factions.COMBINE;
	}

}
