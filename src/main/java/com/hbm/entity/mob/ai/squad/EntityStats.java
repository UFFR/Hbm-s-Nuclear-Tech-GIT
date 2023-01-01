package com.hbm.entity.mob.ai.squad;

import java.util.Objects;
import java.util.function.Supplier;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.hbm.interfaces.INBTSerializable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public abstract class EntityStats implements INBTSerializable
{
	protected static final Supplier<World> WORLD_SUPPLIER = MinecraftServer.getServer()::getEntityWorld;
	protected int delegateID, sinceLastContact = -1;
	protected EntityLivingBase delegate;
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

	public boolean isStillFull()
	{
		return getHealthPercentage() >= 0.8f;
	}

	public boolean isInjured()
	{
		return getHealthPercentage() < 0.8f;
	}

	public boolean isAlmostDead()
	{
		return getHealthPercentage() <= 0.15f;
	}

	public int timeSinceLastContact()
	{
		return sinceLastContact;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(delegate, delegateID, sinceLastContact);
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof EntityStats))
			return false;
		final EntityStats other = (EntityStats) obj;
		return Objects.equals(delegate, other.delegate) && delegateID == other.delegateID
				&& sinceLastContact == other.sinceLastContact;
	}
	@Override
	public abstract String toString();
	
	@Override
	@OverridingMethodsMustInvokeSuper
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("sinceLastContact", sinceLastContact);
		nbt.setInteger("delegateID", delegateID);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void readFromNBT(NBTTagCompound nbt)
	{
		sinceLastContact = nbt.getInteger("sinceLastConact");
		delegateID = nbt.getInteger("delegateID");
		delegate = (EntityLivingBase) WORLD_SUPPLIER.get().getEntityByID(delegateID);
	}

}
