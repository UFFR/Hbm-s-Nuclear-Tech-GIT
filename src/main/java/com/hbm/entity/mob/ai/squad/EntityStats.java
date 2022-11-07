package com.hbm.entity.mob.ai.squad;

import java.io.Serializable;
import java.util.function.Supplier;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public abstract class EntityStats implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8202924670830667031L;
	protected static final Supplier<World> WORLD_SUPPLIER = MinecraftServer.getServer()::getEntityWorld;
	protected final int delegateID;
	public EntityStats(EntityLivingBase entity)
	{
		delegateID = entity.hashCode();
	}
	
	public boolean stillValid()
	{
		return WORLD_SUPPLIER.get().getEntityByID(delegateID) != null;
	}
	
	public float getHealthPercentage()
	{
		return getHealth() / getMaxHealth();
	}
	
	public abstract void update();
	public abstract void lostContact();
	public abstract void reestablishContact();
	
	public abstract float getHealth();
	public abstract float getMaxHealth();
	public abstract boolean isStillFull();
	public abstract boolean isInjured();
	public abstract boolean isAlmostDead();
	public abstract int timeSinceLastContact();
	
	@Override
	public abstract int hashCode();
	@Override
	public abstract boolean equals(Object obj);
	@Override
	public abstract String toString();

}
