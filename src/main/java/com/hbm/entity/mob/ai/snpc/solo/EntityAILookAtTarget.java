package com.hbm.entity.mob.ai.snpc.solo;

import java.util.Objects;
import java.util.function.Supplier;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;

import net.minecraft.entity.EntityLivingBase;

public class EntityAILookAtTarget extends EntityAISmartBase
{
	private final transient Supplier<? extends EntityLivingBase> targetSupplier;
	private final int spotDistanceSquared;
	private EntityLivingBase watchTarget;
	public EntityAILookAtTarget(EntitySmartNPCBase host, Supplier<? extends EntityLivingBase> targetSupplier)
	{
		super(host);
		this.targetSupplier = targetSupplier;
		
		spotDistanceSquared = host.spotDistance() * host.spotDistance();
	}

	@Override
	public void startExecuting()
	{
		watchTarget = targetSupplier.get();
	}

	@Override
	public boolean continueExecuting()
	{
		return watchTarget.isEntityAlive() && host.getDistanceSqToEntity(watchTarget) < spotDistanceSquared;
	}
	
	@Override
	public void updateTask()
	{
		host.getLookHelper().setLookPositionWithEntity(watchTarget, 30, 30);
	}
	
	@Override
	public void resetTask()
	{
		watchTarget = null;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(spotDistanceSquared, watchTarget);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof EntityAILookAtTarget))
			return false;
		final EntityAILookAtTarget other = (EntityAILookAtTarget) obj;
		return spotDistanceSquared == other.spotDistanceSquared && Objects.equals(watchTarget, other.watchTarget);
	}

	@Override
	public boolean shouldExecute()
	{
		return (watchTarget = targetSupplier.get()) != null && watchTarget.isEntityAlive();
	}

}
