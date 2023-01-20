package com.hbm.entity.mob.ai.snpc;

import java.util.function.Predicate;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class TargetSelector implements IEntitySelector
{
	private final Predicate<EntityLivingBase> badTargetPredicate;

	public TargetSelector(Predicate<EntityLivingBase> badTargetPredicate)
	{
		this.badTargetPredicate = badTargetPredicate;
	}

	@Override
	public boolean isEntityApplicable(Entity e)
	{
		return e instanceof EntityLivingBase ? !badTargetPredicate.test((EntityLivingBase) e) : false;
	}

}
