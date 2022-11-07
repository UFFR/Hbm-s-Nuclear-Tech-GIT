package com.hbm.entity.mob.ai.snpc;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.interfaces.ILocationProvider;

import net.minecraft.entity.ai.EntityAIBase;

public abstract class EntityAISmartBase extends EntityAIBase
{
	protected final EntitySmartNPCBase host;
	protected final ILocationProvider hostLocation;
	public EntityAISmartBase(EntitySmartNPCBase host)
	{
		this.host = host;
		hostLocation = ILocationProvider.wrap(host, false);
	}
	
	@Override
	public abstract void startExecuting();
	@Override
	public abstract void updateTask();
	
	@Override
	public abstract int hashCode();
 	@Override
 	public abstract boolean equals(Object obj);
	
}
