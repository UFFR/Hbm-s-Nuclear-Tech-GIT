package com.hbm.entity.grenade;

import com.google.common.annotations.Beta;
import com.hbm.calc.EasyLocation;
import com.hbm.items.weapon.ItemGrenadeEnum.GrenadeLogic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

@Beta
public class EntityGrenadeBouncyBaseNT extends EntityGrenadeBouncyBase
{
	protected final GrenadeLogic logic;
	protected final int maxFuse;
	public EntityGrenadeBouncyBaseNT(World world)
	{
		super(world);
		this.setSize(0.25F, 0.25F);
		this.logic = null;
		maxFuse = 0;
	}
	public EntityGrenadeBouncyBaseNT(World world, EntityLivingBase thrower, int fuse, GrenadeLogic logic)
	{
		super(world, thrower);
		this.maxFuse = fuse;
		this.logic = logic;
	}
	@Override
	protected double getBounceMod()
	{
		return 0.25d;
	}
	@Override
	protected int getMaxTimer()
	{
		return maxFuse;
	}
	@Override
	public void explode()
	{
		if (!worldObj.isRemote)
		{
			logic.explode(getThrower(), EasyLocation.constructGeneric(this), this);
			setDead();
		}
	}
}
