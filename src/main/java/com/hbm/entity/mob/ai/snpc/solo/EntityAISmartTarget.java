package com.hbm.entity.mob.ai.snpc.solo;

import java.util.function.Predicate;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class EntityAISmartTarget extends EntityAISmartBase
{
	protected final Predicate<EntityLivingBase> badTargetDiscriminator;
	private final boolean checkSight;
	
	private int lostContactTimer;
	public EntityAISmartTarget(EntitySmartNPCBase host, Predicate<EntityLivingBase> badTargetDiscriminator)
	{
		this(host, badTargetDiscriminator, false);
	}
	
	
	public EntityAISmartTarget(EntitySmartNPCBase host, Predicate<EntityLivingBase> badTargetDiscriminator, boolean checkSight)
	{
		super(host);
		this.badTargetDiscriminator = badTargetDiscriminator;
		this.checkSight = checkSight;
	}
	
	@Override
	public void startExecuting()
	{
		lostContactTimer = 0;
	}
	
	@Override
	public boolean continueExecuting()
	{
		final EntityLivingBase target = host.getAttackTarget();
		
		if (target == null || !target.isEntityAlive())
			return false;
		
		if (host.getDistanceSqToEntity(target) > host.spotDistance() * host.spotDistance())
			return false;
		else
		{
			if (checkSight)
			{
				if (host.getEntitySenses().canSee(target))
					lostContactTimer = 0;
				else
				{
					if (lostContactTimer++ == 0)
						host.onLostEnemy();
					else if (lostContactTimer == 60)
					{
						host.onLostEnemyLong();
						return false;
					}
				}
			}
			
			return !(target instanceof EntityPlayer) || !((EntityPlayer) target).capabilities.isCreativeMode;
		}
	}
	
	@Override
	public void resetTask()
	{
		host.setAttackTarget(null);
	}
}
