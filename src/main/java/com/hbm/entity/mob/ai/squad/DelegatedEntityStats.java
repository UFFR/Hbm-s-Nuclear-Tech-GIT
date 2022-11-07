package com.hbm.entity.mob.ai.squad;

import java.util.Objects;

import net.minecraft.entity.EntityLivingBase;

public class DelegatedEntityStats extends EntityStats
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7241716187372205945L;
	private transient EntityLivingBase delegate;
	private boolean lostContact = false;
	private int sinceLastContact = 0;
	public DelegatedEntityStats(EntityLivingBase entity)
	{
		super(entity);
		delegate = entity;
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
	}
	
	@Override
	public float getHealth()
	{
		return delegate.getHealth();
	}

	@Override
	public float getMaxHealth()
	{
		return delegate.getMaxHealth();
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
		return Objects.hash(delegate, lostContact, sinceLastContact);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof DelegatedEntityStats))
			return false;
		final DelegatedEntityStats other = (DelegatedEntityStats) obj;
		return delegateID == other.delegateID && lostContact == other.lostContact
				&& sinceLastContact == other.sinceLastContact;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("DelegatedEntityStats [delegate=").append(delegate).append(", lostContact=").append(lostContact)
				.append(", sinceLastContact=").append(sinceLastContact).append(", delegateID=").append(delegateID)
				.append(']');
		return builder.toString();
	}

}
