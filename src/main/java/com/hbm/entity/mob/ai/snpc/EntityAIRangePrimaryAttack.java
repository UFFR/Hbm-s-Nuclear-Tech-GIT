package com.hbm.entity.mob.ai.snpc;

import java.util.Objects;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.entity.mob.smart.NPCTask;
import com.hbm.interfaces.ILocationProvider;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIRangePrimaryAttack extends EntityAISmartBase
{
	private EntityLivingBase target;
	private final double movementSpeed, minEngageDistance, minEngageDistanceSquared;
	private final int attackDelay;
//	private final boolean strengthLossOverDistance;
	private int timer, spotTime, lostTime;//, sinceLastAttack;
	public EntityAIRangePrimaryAttack(EntitySmartNPCBase host, double movementSpeed, double minEngageDistance, int attackDelay)
	{
		super(host);
		this.movementSpeed = movementSpeed;
		this.minEngageDistance = minEngageDistance;
		this.attackDelay = attackDelay;
//		this.strengthLossOverDistance = strengthLossOverDistance;
		
		minEngageDistanceSquared = minEngageDistance * minEngageDistance;
		
		setMutexBits(3); // TODO
	}
	
	@Override
	public void startExecuting()
	{
		host.onBeginAttack(target);
		host.applyTask(NPCTask.TARGET);
	}
	
	@Override
	public boolean continueExecuting()
	{
		return shouldExecute() || !host.getNavigator().noPath();
	}

	@Override
	public void resetTask()
	{
		target = null;
	}
	
	@Override
	public void updateTask()
	{
		// TODO Auto-generated method stub
		final double distanceSq = ILocationProvider.distanceSquared(hostLocation, ILocationProvider.wrap(target, false));
		final boolean canSee = host.getEntitySenses().canSee(target);
		
		if (target.getHealth() / target.getMaxHealth() <= 0.5f)
			host.onEnemyAlmostDead(target);
		
		if (canSee)
		{
			spotTime++;
			if (lostTime >= 20)
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
		
		if (distanceSq <= minEngageDistance && spotTime >= 20)
		{
			host.getNavigator().clearPathEntity();
		} else
			host.getNavigator().tryMoveToEntityLiving(target, movementSpeed);
		
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
		
		if (--timer == 0)
		{
			if (distanceSq >= minEngageDistanceSquared || !canSee)
			{
				host.onLostEnemy();
				return;
			}
//			final float strength = strengthLossOverDistance ? MathHelper.clamp_float((float) (Math.sqrt(distanceSq) / minEngageDistance), 0.1f, 1) : 1;
			
			host.setTarget(target);
			host.primaryFire();
			
			timer = attackDelay;
		}
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(attackDelay, lostTime, minEngageDistance, minEngageDistanceSquared, movementSpeed, spotTime,
				target, timer);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof EntityAIRangePrimaryAttack))
			return false;
		final EntityAIRangePrimaryAttack other = (EntityAIRangePrimaryAttack) obj;
		return attackDelay == other.attackDelay && lostTime == other.lostTime
				&& Double.doubleToLongBits(minEngageDistance) == Double.doubleToLongBits(other.minEngageDistance)
				&& Double.doubleToLongBits(minEngageDistanceSquared) == Double
						.doubleToLongBits(other.minEngageDistanceSquared)
				&& Double.doubleToLongBits(movementSpeed) == Double.doubleToLongBits(other.movementSpeed)
				&& spotTime == other.spotTime && Objects.equals(target, other.target) && timer == other.timer;
	}

	@Override
	public boolean shouldExecute()
	{
		if (target == null)
		{
			if (host.getAttackTarget() == null)
				return false;
			else
			{
				target = host.getAttackTarget();
				return true;
			}
		} else
			return true;
	}

}
