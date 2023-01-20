package com.hbm.entity.mob.ai.snpc.solo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.interfaces.ILocationProvider;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIEnemyProximity extends EntityAISmartTarget
{
	private final List<EntityLivingBase> nearbyEntities = new ArrayList<EntityLivingBase>();
	private final int proximityDistanceSq;
	private EntityLivingBase primary;
	private int timer = 20;
	public EntityAIEnemyProximity(EntitySmartNPCBase host, Predicate<EntityLivingBase> badTargetDiscriminator)
	{
		super(host, badTargetDiscriminator);
		proximityDistanceSq = host.proximityDistance() * host.proximityDistance();
		
		setMutexBits(1);
	}

	@Override
	public void startExecuting()
	{
		// TODO Auto-generated method stub
		ILocationProvider.sortEntitiesByDistanceFrom(nearbyEntities, host);
		primary = nearbyEntities.get(0);
		host.notifyEnemyTooClose(primary);
		host.setAttackTarget(primary);
		host.getLookHelper().setLookPositionWithEntity(primary, 30, 30);
		timer = 20;
	}
	
	@Override
	public void resetTask()
	{
		primary = null;
		nearbyEntities.clear();
		timer = 20;
	}

	@Override
	public void updateTask()
	{
		final double meleeDistance = host.getWidth() * 2 * host.getWidth() * 2 + primary.width, distanceSq = host.getDistanceSqToEntity(primary);
		
		if (distanceSq <= meleeDistance && --timer <= 0)
		{
			timer = 20;
			if (host.getHeldItem() != null)
				host.swingItem();
			
			host.attackEntityAsMob(primary);
		} else
			host.primaryFire();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(nearbyEntities, primary, proximityDistanceSq, timer);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof EntityAIEnemyProximity))
			return false;
		final EntityAIEnemyProximity other = (EntityAIEnemyProximity) obj;
		return Objects.equals(nearbyEntities, other.nearbyEntities) && Objects.equals(primary, other.primary)
				&& proximityDistanceSq == other.proximityDistanceSq && timer == other.timer;
	}

	@Override
	public boolean shouldExecute()
	{
		nearbyEntities.clear();
		nearbyEntities.addAll(host.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, host.getProximityBox()));
		nearbyEntities.removeIf(badTargetDiscriminator);
		
		return nearbyEntities.isEmpty() ? false : nearbyEntities.get(0).getDistanceSqToEntity(host) <= proximityDistanceSq && host.shouldReprioritizeTarget(nearbyEntities.get(0));
	}

}
