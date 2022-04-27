package com.hbm.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityTesseract extends Entity
{
	private static final short aveLife = 20 * 15;
	private final short maxLife;
	private short life = 0;
	public EntityTesseract(World worldIn)
	{
		super(worldIn);
		maxLife = (short) (worldIn.rand.nextInt(20 * 5) + aveLife);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		life++;
		if (life >= maxLife)
			setDead();
	}
	
	@Override
	protected void entityInit()
	{
		isImmuneToFire = true;
		ignoreFrustumCheck = true;
		motionX = worldObj.rand.nextDouble();
		motionY = worldObj.rand.nextDouble();
		motionZ = worldObj.rand.nextDouble();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		life = nbt.getShort("lifespan");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setShort("lifespan", life);
	}

}
