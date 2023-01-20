package com.hbm.entity.mob.ai.snpc.solo;

import java.util.List;
import java.util.function.Predicate;

import com.hbm.entity.mob.smart.EntitySmartNPCBase;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIHurtByEntity extends EntityAISmartTarget
{
	private final boolean callsForHelp;
	private int lastTimer;
	public EntityAIHurtByEntity(EntitySmartNPCBase host, Predicate<EntityLivingBase> badTargetDiscriminator, boolean callsForHelp)
	{
		super(host, badTargetDiscriminator);
		this.callsForHelp = callsForHelp;
	}

	@Override
	public void startExecuting()
	{
		// TODO Auto-generated method stub
		super.startExecuting();
		host.onBeginAttack(host.getAITarget());
		host.setAttackTarget(host.getAITarget());
		host.getLookHelper().setLookPositionWithEntity(host.getAttackTarget(), 30, 30);
		if (callsForHelp)
		{
			for (EntityLivingBase entity : (List<EntityLivingBase>) host.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, host.getSpottingBox()))
			{
				if (entity == host || !(entity instanceof EntitySmartNPCBase))
					continue;
				
				((EntitySmartNPCBase) entity).onFriendNeedHelp(getHost());
			}
		}
	}

	@Override
	public void updateTask()
	{
		// TODO Auto-generated method stub

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
		return host.func_142015_aE() != lastTimer && !badTargetDiscriminator.test(host.getAITarget());
	}

}
