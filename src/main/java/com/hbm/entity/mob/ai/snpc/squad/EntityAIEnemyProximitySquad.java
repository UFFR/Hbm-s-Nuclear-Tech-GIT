package com.hbm.entity.mob.ai.snpc.squad;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.hbm.entity.mob.ai.snpc.solo.EntityAISmartBase;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.util.lists.SortedList;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIEnemyProximitySquad extends EntityAISmartBase
{
	private final List<EntityLivingBase> sortedTargets;
	private final int proximitySq;
	private EntityLivingBase target;
	private int meleeTimer, rangeTimer;
	public EntityAIEnemyProximitySquad(EntitySmartNPCBase host)
	{
		super(host);
		if (!host.hasSquad())
			throw new IllegalArgumentException("Host must use squads!");
		
		sortedTargets = new SortedList<EntityLivingBase>(Comparator.comparing(e -> this.host.getDistanceSqToEntity(e)));
		proximitySq = host.proximityDistance() * host.proximityDistance();
	}

	@Override
	public boolean continueExecuting()
	{
		return target.isEntityAlive() && !target.isDead;
	}
	
	@Override
	public void startExecuting()
	{
		meleeTimer = 20;
		if (host.getMainGunConfiguration() != null)
			rangeTimer = host.getMainGunConfiguration().rateOfFire;
		target = sortedTargets.get(0);
		host.notifyEnemyTooClose(target);
		host.setAttackTarget(target);
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
	}

	@Override
	public void updateTask()
	{
		final double meleeDistance = host.getWidth() * 2 * host.getWidth() * 2 + target.width, distanceSq = host.getDistanceSqToEntity(target);
		
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
		
		if (meleeTimer > 0)
			meleeTimer--;
		if (rangeTimer > 0)
			rangeTimer--;
		
		if (distanceSq <= meleeDistance && meleeTimer <= 0)
		{
			meleeTimer = 20;
			if (host.getHeldItem() != null)
				host.swingItem();
			
			host.attackEntityAsMob(target);
		} else if (host.getEntitySenses().canSee(target) && rangeTimer <= 0)
			host.primaryFire();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(meleeTimer, proximitySq, rangeTimer, sortedTargets, target);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof EntityAIEnemyProximitySquad))
			return false;
		final EntityAIEnemyProximitySquad other = (EntityAIEnemyProximitySquad) obj;
		return meleeTimer == other.meleeTimer && proximitySq == other.proximitySq && rangeTimer == other.rangeTimer
				&& Objects.equals(sortedTargets, other.sortedTargets) && Objects.equals(target, other.target);
	}
	
	@Override
	public void resetTask()
	{
		meleeTimer = 20;
		if (host.getMainGunConfiguration() != null)
			rangeTimer = host.getMainGunConfiguration().rateOfFire;
		target = null;
		host.setAttackTarget(null);
		sortedTargets.clear();
	}

	@Override
	public boolean shouldExecute()
	{
		sortedTargets.addAll(host.getSquad().getTargets());
		return sortedTargets.isEmpty() ? false : host.shouldReprioritizeTarget(sortedTargets.get(0)) && host.getDistanceSqToEntity(sortedTargets.get(0)) <= proximitySq;
	}

}
