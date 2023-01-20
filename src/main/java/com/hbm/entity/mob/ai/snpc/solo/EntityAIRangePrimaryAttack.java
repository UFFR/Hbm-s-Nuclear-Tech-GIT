package com.hbm.entity.mob.ai.snpc.solo;

import java.util.Objects;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.interfaces.ILocationProvider;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIRangePrimaryAttack extends EntityAISmartBase
{
	private EntityLivingBase target;
	private final double movementSpeed;
	private final int attackDelay, burstAmount, burstTimer, maxEngageDistanceSquared, preferredDistanceSquared;
	private final boolean burstFire;
//	private final boolean strengthLossOverDistance;
	private int RoFCooldown, spotTime, lostTime, burstCounter;//, sinceLastAttack;
	
	public EntityAIRangePrimaryAttack(EntitySmartNPCBase host)
	{
		this(host, false, 0, 0);
	}
	
	public EntityAIRangePrimaryAttack(EntitySmartNPCBase host, boolean burstFire, int burstAmount, int burstTimer)
	{
		super(host);
		if (host.getMainGunConfiguration() == null)
			throw new IllegalArgumentException("Host must use gun!");
		
		this.burstFire = burstFire;
		this.burstAmount = burstAmount;
		this.burstTimer = burstTimer;
		
		attackDelay = host.getMainGunConfiguration().rateOfFire;
		movementSpeed = host.getAIMoveSpeed();
		maxEngageDistanceSquared = host.spotDistance() * host.spotDistance();
		preferredDistanceSquared = host.preferredDistance() * host.preferredDistance();
		
		setMutexBits(3); // TODO
	}
	
	@Override
	public void startExecuting()
	{
		target = host.getAttackTarget();
		host.onBeginAttack(target);
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
	}
	
	@Override
	public boolean continueExecuting()
	{
		return shouldExecute() && !host.getNavigator().noPath() && target.isEntityAlive() && !target.isDead && host.getEntitySenses().canSee(target);
	}

	@Override
	public void resetTask()
	{
		target = null;
		host.setAttackTarget(null);
		RoFCooldown = host.getMainGunConfiguration().rateOfFire;
	}
	
	@Override
	public void updateTask()
	{
		// TODO Auto-generated method stub
		final double distanceSq = ILocationProvider.distanceSquared(host, ILocationProvider.wrap(target, false));
		final boolean canSee = host.getEntitySenses().canSee(target) && distanceSq < maxEngageDistanceSquared;
		
		if (RoFCooldown > 0)
			RoFCooldown--;
		
		if (canSee)
		{
			spotTime++;
			if (lostTime >= 100)
				host.onReestablishContact(target);
			
			lostTime = 0;
		} else
		{
			spotTime = 0;
			lostTime++;
			
			if (lostTime == 100)
				host.onLostEnemy();
			else if (lostTime == 200)
				host.onLostEnemyLong();
		}

		if (distanceSq <= preferredDistanceSquared && spotTime >= 20)
			host.getNavigator().clearPathEntity();
		else
			host.getNavigator().tryMoveToEntityLiving(target, movementSpeed);
		
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
		
		if (RoFCooldown <= 0 && spotTime >= 20)
		{
			if (burstFire && RoFCooldown <= 0 && burstCounter <= burstAmount)
			{
				host.primaryFire();
				RoFCooldown = host.getMainGunConfiguration().rateOfFire;
				burstCounter++;
			} else if (burstFire && burstCounter >= burstAmount)
				RoFCooldown = burstTimer;
			else if (!burstFire && RoFCooldown <= 0)
			{
				host.primaryFire();
				RoFCooldown = host.getMainGunConfiguration().rateOfFire;
			}
		}
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(RoFCooldown, attackDelay, burstAmount, burstCounter, burstFire, burstTimer,
				lostTime, maxEngageDistanceSquared, movementSpeed, preferredDistanceSquared, spotTime, target);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof EntityAIRangePrimaryAttack))
			return false;
		final EntityAIRangePrimaryAttack other = (EntityAIRangePrimaryAttack) obj;
		return RoFCooldown == other.RoFCooldown && attackDelay == other.attackDelay && burstAmount == other.burstAmount
				&& burstCounter == other.burstCounter && burstFire == other.burstFire && burstTimer == other.burstTimer
				&& lostTime == other.lostTime && maxEngageDistanceSquared == other.maxEngageDistanceSquared
				&& Double.doubleToLongBits(movementSpeed) == Double.doubleToLongBits(other.movementSpeed)
				&& preferredDistanceSquared == other.preferredDistanceSquared && spotTime == other.spotTime
				&& Objects.equals(target, other.target);
	}

	@Override
	public boolean shouldExecute()
	{
		return host.getAttackTarget() != null && !host.getAttackTarget().isDead && host.getAttackTarget().isEntityAlive() && host.getEntitySenses().canSee(host.getAttackTarget());
	}

}
