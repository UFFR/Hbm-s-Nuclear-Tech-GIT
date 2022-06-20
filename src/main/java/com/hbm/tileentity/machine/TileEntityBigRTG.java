package com.hbm.tileentity.machine;

import java.math.BigInteger;

import com.google.common.annotations.Beta;
import com.hbm.interfaces.Untested;
import com.hbm.items.machine.ItemRTGPellet;
import com.hbm.saveddata.TimeSavedData;
import com.hbm.tileentity.IRTGUser;
import com.hbm.tileentity.IRadioisotopeFuel;
import com.hbm.tileentity.TileEntityMachineBase;

import api.hbm.energy.IEnergyGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
@Beta
@Untested
public class TileEntityBigRTG extends TileEntityMachineBase implements IRTGUser, IEnergyGenerator
{
	public static final long maxPower = 1000000;
	public static final short maxHeat = 32000;
	private short heat = 0;
	private long power = 0;
	private BigInteger date = BigInteger.ZERO;
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
		date = TimeSavedData.getData(getWorldObj()).getFullTotal();
		if (!worldObj.isRemote)
		{
			heat = (short) updateRTGs();
			
			if (heat > maxHeat)
				heat = maxHeat;
			power += heat;
			if (power > maxPower)
				power = maxPower;
		}
		
		NBTTagCompound data = new NBTTagCompound();
		data.setShort("heat", heat);
		data.setLong("power", power);
		data.setByteArray("date", date.toByteArray());
		networkPack(data, 25);
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		heat = nbt.getShort("heat");
		power = nbt.getLong("power");
		date = new BigInteger(nbt.getByteArray("date"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		nbt.setShort("heat", heat);
		nbt.setByteArray("date", date.toByteArray());
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		power = nbt.getLong("power");
		heat = nbt.getShort("heat");
		date = new BigInteger(nbt.getByteArray("date"));
	}

	@Override
	public Class<? extends IRadioisotopeFuel> getDesiredClass()
	{
		return ItemRTGPellet.class;
	}
	
	@Override
	public ItemStack[] getInventory()
	{
		return slots;
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

	@Override
	public void incrementAge()
	{
		date = date.add(BigInteger.ONE);
	}

	@Override
	public BigInteger getInternalDate()
	{
		return date;
	}

}
