package com.hbm.entity.mob.ai.squad;

import java.util.Objects;

import net.minecraft.entity.EntityLivingBase;

public class CachedEntityStats extends EntityStats
{
	private float health, maxHealth;
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
		else
		{
			health = delegate.getHealth();
			maxHealth = delegate.getMaxHealth();
		}
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
		sinceLastContact = -1;
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
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(health, lostContact, maxHealth);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof CachedEntityStats))
			return false;
		final CachedEntityStats other = (CachedEntityStats) obj;
		return Float.floatToIntBits(health) == Float.floatToIntBits(other.health) && lostContact == other.lostContact
				&& Float.floatToIntBits(maxHealth) == Float.floatToIntBits(other.maxHealth);
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
}
