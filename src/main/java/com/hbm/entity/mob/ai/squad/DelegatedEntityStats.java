package com.hbm.entity.mob.ai.squad;

import java.util.Objects;

import bytes.ByteSequence;
import net.minecraft.entity.EntityLivingBase;

public class DelegatedEntityStats extends EntityStats
{
	private boolean lostContact = false;
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
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(lostContact);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof DelegatedEntityStats))
			return false;
		final DelegatedEntityStats other = (DelegatedEntityStats) obj;
		return lostContact == other.lostContact;
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

	@Override
	public void writeToBytes(ByteSequence buf)
	{
		super.writeToBytes(buf);
		buf.writeBoolean(lostContact);
	}

	@Override
	public void readFromBytes(ByteSequence buf)
	{
		super.readFromBytes(buf);
		lostContact = buf.readBoolean();
	}

}
