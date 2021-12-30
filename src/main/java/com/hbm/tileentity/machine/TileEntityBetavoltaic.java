package com.hbm.tileentity.machine;

import com.google.common.annotations.Beta;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.recipes.BetavoltaicFuels;
import com.hbm.tileentity.IRTGUser;
import com.hbm.tileentity.IRadioisotopeFuel;
import com.hbm.tileentity.TileEntityMachineBase;

import api.hbm.energy.IEnergyGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
@Beta
public class TileEntityBetavoltaic extends TileEntityMachineBase implements IRTGUser, IEnergyGenerator
{
	public static final long maxPower = 5000000;
	public static final short maxHeat = 32000;
	private short heat = 0;
	private long power = 0;
	public TileEntityBetavoltaic()
	{
		super(24);
		if (!BetavoltaicFuels.isInitialized())
			BetavoltaicFuels.initialize();
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public int getHeat()
	{
		return heat;
	}

	@Override
	public Class<? extends IRadioisotopeFuel> getDesiredClass()
	{
		return IRadioisotopeFuel.class;
	}

	@Override
	public String getName()
	{
		return "container.betavoltaic";
	}

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			heat = (short) updateRTGs(slots, BetavoltaicFuels.fuelMap);
			
			if (heat > maxHeat)
				heat = maxHeat;
			power += heat;
			if (power > maxPower)
				power = maxPower;
		}
		
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("power", power);
		data.setShort("heat", heat);
		networkPack(data, 25);
	}

	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		power = nbt.getLong("power");
		heat = nbt.getShort("heat");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		nbt.setShort("heat", heat);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		power = nbt.getLong("power");
		heat = nbt.getShort("heat");
	}
	
	@Override
	public boolean isItemValid(Item itemIn)
	{
		boolean flag = false;
		for (AStack stack : BetavoltaicFuels.getFuels())
			if (stack.isApplicable(new ItemStack(itemIn)))
				flag = true;
		
		return IRTGUser.super.isItemValid(itemIn) || flag;
	}

	@Override
	public void setPower(long power)
	{
		this.power = power;
	}

	@Override
	public long getPower()
	{
		return power;
	}

	@Override
	public long getMaxPower()
	{
		return maxPower;
	}
}
