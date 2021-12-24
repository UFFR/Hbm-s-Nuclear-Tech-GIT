package com.hbm.tileentity.machine;

import com.google.common.annotations.Beta;
import com.hbm.interfaces.Untested;
import com.hbm.items.machine.ItemRTGPellet;
import com.hbm.tileentity.IRTGUser;
import com.hbm.tileentity.IRadioisotopeFuel;
import com.hbm.tileentity.TileEntityMachineBase;

import api.hbm.energy.IEnergyGenerator;
import net.minecraft.nbt.NBTTagCompound;
@Beta
@Untested
public class TileEntityBigRTG extends TileEntityMachineBase implements IRTGUser, IEnergyGenerator
{
	public static final long maxPower = 1000000;
	public static final short maxHeat = 32000;
	private short heat = 0;
	private long power = 0;
	public TileEntityBigRTG()
	{
		super(24);
	}

	@Override
	public int getHeat()
	{
		return heat;
	}

	@Override
	public String getName()
	{
		return "container.bigRTG";
	}

	@Override
	public void updateEntity()
	{
		if (!worldObj.isRemote)
		{
			heat = (short) updateRTGs(slots);
			
			if (heat > maxHeat)
				heat = maxHeat;
			power += heat;
			if (power > maxPower)
				power = maxPower;
		}
		
		NBTTagCompound data = new NBTTagCompound();
		data.setShort("heat", heat);
		data.setLong("power", power);
		networkPack(data, 25);
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		heat = nbt.getShort("heat");
		power = nbt.getLong("power");
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
	public Class<? extends IRadioisotopeFuel> getDesiredClass()
	{
		return ItemRTGPellet.class;
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
