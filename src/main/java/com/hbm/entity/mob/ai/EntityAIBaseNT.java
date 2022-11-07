package com.hbm.entity.mob.ai;

import com.hbm.interfaces.ILocationProvider;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class EntityAIBaseNT extends EntityAIBase
{
	protected final EntityLivingBase host;
	protected final ILocationProvider hostLocation;
	public EntityAIBaseNT(EntityLivingBase host)
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
