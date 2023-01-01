package com.hbm.entity.mob.ai.snpc;

import java.util.ArrayList;
import java.util.List;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.interfaces.ILocationProvider;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIEnemyProximity extends EntityAISmartBase
{
	private final List<EntityLivingBase> nearbyEntities = new ArrayList<EntityLivingBase>();
	private final int proximityDistanceSq;
	private EntityLivingBase primary;
	private ILocationProvider primaryLoc;
	public EntityAIEnemyProximity(EntitySmartNPCBase host)
	{
		super(host);
		proximityDistanceSq = host.proximityDistance() * host.proximityDistance();
	}

	@Override
	public void startExecuting()
	{
		// TODO Auto-generated method stub
		ILocationProvider.sortEntitiesByDistanceFrom(nearbyEntities, hostLocation);
		primary = nearbyEntities.get(0);
		primaryLoc = ILocationProvider.wrap(primary, false);
		host.notifyEnemyTooClose(primary);
	}
	
	@Override
	public boolean continueExecuting()
	{
		return !primary.isDead && ILocationProvider.distanceSquared(primaryLoc, hostLocation) <= proximityDistanceSq;
	}
	
	@Override
	public void resetTask()
	{
		primary = null;
		primaryLoc = null;
		nearbyEntities.clear();
	}

	@Override
	public void updateTask()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object obj)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldExecute()
	{
		nearbyEntities.clear();
		nearbyEntities.addAll(host.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, host.getProximityBox()));
		nearbyEntities.remove(host);
		
		return nearbyEntities.isEmpty() ? false : nearbyEntities.get(0).getDistanceSqToEntity(host) <= proximityDistanceSq;
	}

}
