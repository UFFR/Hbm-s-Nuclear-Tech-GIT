package com.hbm.entity.mob.ai.squad;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;

import net.minecraft.entity.EntityLivingBase;

public class CachedEntityStats extends EntityStats
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8825278872246408515L;
	private transient EntityLivingBase delegate;
	private float health, maxHealth;
	private int sinceLastContact = 0;
	private boolean lostContact = false;
	public CachedEntityStats(EntityLivingBase entity)
	{
		super(entity);
		delegate = entity;
		health = entity.getHealth();
		maxHealth = entity.getMaxHealth();
	}

	@Override
	public void update()
	{
		if (lostContact)
			sinceLastContact++;
	}

	@Override
	public void lostContact()
	{
		lostContact = true;
	}

	@Override
	public void reestablishContact()
	{
		lostContact = false;
		sinceLastContact = 0;
		health = delegate.getHealth();
		maxHealth = delegate.getMaxHealth();
	}

	@Override
	public float getHealth()
	{
		return health;
	}

	@Override
	public float getMaxHealth()
	{
		return maxHealth;
	}

	@Override
	public boolean isStillFull()
	{
		return getHealthPercentage() >= 0.8f;
	}

	@Override
	public boolean isInjured()
	{
		return getHealthPercentage() <= 0.5f;
	}

	@Override
	public boolean isAlmostDead()
	{
		return getHealthPercentage() <= 0.2f;
	}

	@Override
	public int timeSinceLastContact()
	{
		return sinceLastContact;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(delegateID, health, maxHealth, sinceLastContact);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof CachedEntityStats))
			return false;
		final CachedEntityStats other = (CachedEntityStats) obj;
		return delegateID == other.delegateID && Float.floatToIntBits(health) == Float.floatToIntBits(other.health)
				&& Float.floatToIntBits(maxHealth) == Float.floatToIntBits(other.maxHealth)
				&& sinceLastContact == other.sinceLastContact;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("CachedEntityStats [health=").append(health).append(", maxHealth=").append(maxHealth)
				.append(", sinceLastContact=").append(sinceLastContact).append(", delegateID=").append(delegateID)
				.append(']');
		return builder.toString();
	}

	private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException
	{
		stream.defaultReadObject();
		delegate = (EntityLivingBase) WORLD_SUPPLIER.get().getEntityByID(delegateID);
	}
	
}
