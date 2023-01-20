package com.hbm.entity.mob.ai.snpc.squad;

import java.util.List;
import java.util.Objects;

import com.hbm.entity.mob.ai.snpc.solo.EntityAISmartBase;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.lib.Library;
import com.hbm.util.lists.ArraySet;

import net.minecraft.entity.EntityLivingBase;

public class EntityAINearestTargetSquad extends EntityAISmartBase
{
	private final List<EntityLivingBase> sortedTargets = new ArraySet<EntityLivingBase>();
	private final boolean burstFire;
	private final int burstTimer, burstCount;
	
	private EntityLivingBase mainTarget;
	private int RoFCooldown, burstCooldown, burstFired;
	public EntityAINearestTargetSquad(EntitySmartNPCBase host)
	{
		this(host, false, 0, 0);
	}
	
	public EntityAINearestTargetSquad(EntitySmartNPCBase host, boolean burstFire, int burstTimer, int burstCount)
	{
		super(host);
		if (!host.hasSquad())
			throw new IllegalArgumentException("Host must use squads!");
		if (host.getMainGunConfiguration() == null)
			throw new IllegalArgumentException("Host must use gun!");
		
		this.burstFire = burstFire;
		this.burstTimer = burstTimer;
		this.burstCount = burstCount;
	}

	@Override
	public void startExecuting()
	{
		// TODO Auto-generated method stub
		mainTarget = sortedTargets.get(0);
		host.setAttackTarget(mainTarget);
		host.onBeginAttack(mainTarget);
		RoFCooldown = host.getMainGunConfiguration().rateOfFire;
		burstCooldown = burstTimer;
	}

	@Override
	public boolean continueExecuting()
	{
		return mainTarget != null && !mainTarget.isDead && mainTarget.isEntityAlive();
	}
	
	@Override
	public void updateTask()
	{
		// TODO Auto-generated method stub
		if (mainTarget.isDead || !mainTarget.isEntityAlive())
		{
			host.onKillEntity(mainTarget);
			host.setAttackTarget(null);
			mainTarget = null;
			return;
		}
		
		if (host.getEntitySenses().canSee(mainTarget))
		{
			host.getLookHelper().setLookPositionWithEntity(mainTarget, 30, 30);
			
			if (burstFire && burstCooldown > 0)
				burstCooldown--;
			
			if (burstFire && burstCooldown <= 0 && RoFCooldown <= 0 && burstFired <= burstCount)
			{
				host.primaryFire();
				RoFCooldown = host.getMainGunConfiguration().rateOfFire;
				burstFired++;
			} else if (burstFire && burstFired >= burstCount)
				burstCooldown = burstTimer;
			else if (!burstFire && RoFCooldown <= 0)
			{
				host.primaryFire();
				RoFCooldown = host.getMainGunConfiguration().rateOfFire;
			}
		}
		
		if (RoFCooldown > 0)
			RoFCooldown--;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(mainTarget, sortedTargets);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof EntityAINearestTargetSquad))
			return false;
		final EntityAINearestTargetSquad other = (EntityAINearestTargetSquad) obj;
		return Objects.equals(mainTarget, other.mainTarget) && Objects.equals(sortedTargets, other.sortedTargets);
	}

	@Override
	public void resetTask()
	{
		sortedTargets.clear();
		mainTarget = null;
		host.setAttackTarget(null);
		RoFCooldown = host.getMainGunConfiguration().rateOfFire;
		burstCooldown = burstTimer;
	}
	
	@Override
	public boolean shouldExecute()
	{
		sortedTargets.addAll(host.getSquad().getTargets());
		Library.removeListDuplicates(sortedTargets);
		if (sortedTargets.isEmpty())
			return false;
		
		return host.getAttackTarget() == null || host.shouldReprioritizeTarget(sortedTargets.get(0));
	}

}
