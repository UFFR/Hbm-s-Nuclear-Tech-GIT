package com.hbm.entity.mob.ai.snpc.squad;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.hbm.entity.mob.ai.snpc.solo.EntityAISmartBase;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.util.lists.SortedList;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIReprioritizeTargetSquad extends EntityAISmartBase
{
	private final List<EntityLivingBase> sortedTargets;
	private EntityLivingBase target;
	public EntityAIReprioritizeTargetSquad(EntitySmartNPCBase host)
	{
		super(host);
		if (!host.hasSquad())
			throw new IllegalArgumentException("Host must use squads!");
		
		sortedTargets = new SortedList<EntityLivingBase>(Comparator.comparing(e -> this.host.getDistanceSqToEntity(e)));
	}

	@Override
	public void startExecuting()
	{
		target = sortedTargets.get(0);
		host.setAttackTarget(target);
		host.onBeginAttack(target);
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
	}

	@Override
	public void updateTask()
	{
		// TODO Auto-generated method stub
		sortedTargets.retainAll(host.getSquad().getTargets());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(sortedTargets, target);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof EntityAIReprioritizeTargetSquad))
			return false;
		final EntityAIReprioritizeTargetSquad other = (EntityAIReprioritizeTargetSquad) obj;
		return Objects.equals(sortedTargets, other.sortedTargets) && Objects.equals(target, other.target);
	}
	
	@Override
	public void resetTask()
	{
		sortedTargets.clear();
		target = null;
	}

	@Override
	public boolean shouldExecute()
	{
		sortedTargets.addAll(host.getSquad().getTargets());
		
		if (host.getAttackTarget() == null && !sortedTargets.isEmpty())
			return true;
		
		return sortedTargets.isEmpty() ? false : host.getAttackTarget() != sortedTargets.get(0) && host.getAttackTarget().getDistanceSqToEntity(host) > sortedTargets.get(0).getDistanceSqToEntity(host) && host.shouldReprioritizeTarget(sortedTargets.get(0));
	}

}
