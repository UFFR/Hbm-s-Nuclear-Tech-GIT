package com.hbm.entity.mob.ai.snpc.solo;

import java.util.List;
import java.util.function.Predicate;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.interfaces.ILocationProvider;

import net.minecraft.entity.EntityLivingBase;

public class EntityAINearestTarget extends EntityAISmartTarget
{
//	private final IEntitySelector targeter;
	private EntityLivingBase target;
	public EntityAINearestTarget(EntitySmartNPCBase host, Predicate<EntityLivingBase> badTargetDiscriminator)
	{
		super(host, badTargetDiscriminator);
//		targeter = new TargetSelector(badTargetDiscriminator);
	}

	@Override
	public void startExecuting()
	{
		// TODO Auto-generated method stub
		super.startExecuting();
		host.onBeginAttack(target);
		host.setAttackTarget(target);
	}

	@Override
	public void updateTask()
	{
		// TODO Auto-generated method stub
		if (!target.isEntityAlive() || target.isDead)
			host.onKillEntity(target);
	}

	@Override
	public boolean continueExecuting()
	{
		return !(target == null || !target.isEntityAlive() || target.isDead || host.getDistanceSqToEntity(target) > host.spotDistance() * host.spotDistance());
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
		final List<EntityLivingBase> entities = host.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, host.getSpottingBox());
//		final List<EntityLivingBase> entities = host.worldObj.selectEntitiesWithinAABB(EntityLivingBase.class, host.boundingBox.expand(host.spotDistance(), 5, host.spotDistance()), targeter);
		entities.removeIf(badTargetDiscriminator);
		if (entities.isEmpty())
			return false;
		else
		{
			ILocationProvider.sortEntitiesByDistanceFrom(entities, host);
			target = entities.get(0);
			return true;
		}
	}

}
