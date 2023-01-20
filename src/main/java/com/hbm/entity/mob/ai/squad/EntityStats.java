package com.hbm.entity.mob.ai.squad;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.hbm.interfaces.IByteSerializable;
import com.hbm.main.ModEventHandler;

import bytes.ByteSequence;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public abstract class EntityStats implements IByteSerializable
{
	protected static final Supplier<World> WORLD_SUPPLIER = MinecraftServer.getServer()::getEntityWorld;
	protected int sinceLastContact = -1;
	protected UUID delegateID;
	protected EntityLivingBase delegate;
	public EntityStats(EntityLivingBase entity)
	{
		delegateID = entity.getPersistentID();
	}
	
	public boolean stillValid()
	{
		return delegate.isEntityAlive();
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
	public void writeToBytes(ByteSequence buf)
	{
		buf.writeInt(sinceLastContact).writeLong(delegateID.getMostSignificantBits()).writeLong(delegateID.getLeastSignificantBits());
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void readFromBytes(ByteSequence buf)
	{
		sinceLastContact = buf.readInt();
		delegateID = new UUID(buf.readLong(), buf.readLong());
		delegate = (EntityLivingBase) ModEventHandler.getEntityByUUID(delegateID);
	}

}
