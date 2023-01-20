package com.hbm.entity.mob.ai.snpc.solo;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;

import net.minecraft.entity.ai.EntityAIBase;

public abstract class EntityAISmartBase extends EntityAIBase
{
	protected final EntitySmartNPCBase host;
	public EntityAISmartBase(EntitySmartNPCBase host)
	{
		this.host = host;
	}
	
	@Override
	public abstract void startExecuting();
	@Override
	public abstract void updateTask();
	
	@Override
	public abstract int hashCode();
 	@Override
 	public abstract boolean equals(Object obj);
	
 	public EntitySmartNPCBase getHost()
	{
		return host;
	}
}
