package com.hbm.tileentity.machine;

import java.util.ArrayList;
import java.util.List;

import com.google.common.annotations.Beta;
import com.hbm.interfaces.IFluidAcceptor;
import com.hbm.inventory.FluidTank;
import com.hbm.inventory.UpgradeManager;
import com.hbm.inventory.fluid.*;
import com.hbm.items.machine.ItemMachineUpgrade;
import com.hbm.items.machine.ItemMachineUpgrade.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.saveddata.TimeSavedData;
import com.hbm.tileentity.TileEntityMachineBase;

import api.hbm.energy.IEnergyUser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
@Beta
public class TileEntityAtomicClock extends TileEntityMachineBase implements IEnergyUser, IFluidAcceptor
{
	private FluidTank tank = new FluidTank(Fluids.CRYOGEL, 64000, 0);
	private long power = 0, internalTime;
	public static final long powerRate = 100000;
	public static final long maxPower = 50000000;
	private Number[] time = {2030l, 1, 0f};
//	private float time = 0.0F;
//	private byte day = 1;
//	private long year = 2300L;
	private boolean isOn = false, needsReset = true;
	public long consumption = powerRate;
	private int[] upgrades = {0, 0};
	public TileEntityAtomicClock()
	{
		super(5);
	}

	@Override
	public void updateEntity()
	{
		// TODO Auto-generated method stub
		if (!worldObj.isRemote)
		{
			tank.updateTank(this);
			power = Library.chargeTEFromItems(slots, 0, power, maxPower);
			updateUpgrades();
//			time = timeData.getTime();
//			day = timeData.getDay();
//			year = timeData.getYear();
			if (needsReset)
			{
				TimeSavedData data = TimeSavedData.getData(worldObj);
				time = data.getDateFull();
				internalTime = data.getInternalTime();
			}
			else
				increment();
			if (isOn && canOperate())
			{
				tank.decrementFill(1);
				power -= getConsumption();
			}
		}
		
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("power", power);
		data.setFloat("time", time[2].floatValue());
		data.setByte("day", time[1].byteValue());
		data.setLong("year", time[0].longValue());
		data.setBoolean("isOn", isOn);
		data.setLong("consumption", consumption);
		data.setLong("internalTime", internalTime);
		networkPack(data, 100);
	}
	
	private void increment()
	{
		if (internalTime >= 24000)
		{
			internalTime = 0;
			if (time[1].byteValue() >= 100)
			{
				time[1] = 1;
				time[0] = time[0].longValue() + 1;
			}
			else
				time[1] = time[1].byteValue() + 1;
		}
		else
			internalTime++;
		if (rand.nextInt(upgrades[0] * 100) == 0)
			internalTime = rand.nextInt(20 - upgrades[0]);
		time[3] = (float) Library.convertScale(internalTime, 0L, 24000L, 0F, 10.0000F);
	}
	
	public boolean canOperate()
	{
		return tank.getFill() > 0 && power >= getConsumption();
	}
	
	@Override
	public void handleButtonPacket(int value, int meta)
	{
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "gui.button.press", 1.0F, 1.0F);
		isOn = !isOn;
	}
	
	private void updateUpgrades()
	{
		for (byte i = 1; i < 5; i++)
		{
			if (getStackInSlot(i) == null || !(getStackInSlot(i).getItem() instanceof ItemMachineUpgrade))
				continue;
			
			switch (getStackInSlot(i).getItem().getUnlocalizedName())
			{
			case "upgrade_clock_1":
				upgrades[0] += 1;
				break;
			case "upgrade_clock_2":
				upgrades[0] += 2;
				break;
			case "upgrade_clock_3":
				upgrades[0] += 3;
				break;
			case "upgrade_clock_4":
				upgrades[0] += 20;
				break;
			default:
				UpgradeManager.eval(slots, 1, 4);
				break;
			}
		}
		upgrades[0] = MathHelper.clamp_int(upgrades[0], 0, 20);
		upgrades[1] = Math.min(UpgradeManager.getLevel(UpgradeType.POWER), 3) * 50;
	}
	
	public long getConsumption()
	{
		consumption = powerRate - upgrades[1];
		return consumption;
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		power = nbt.getLong("power");
		time[2] = nbt.getFloat("time");
		time[1] = nbt.getByte("day");
		time[0] = nbt.getLong("year");
		isOn = nbt.getBoolean("isOn");
		consumption = nbt.getLong("consumption");
		internalTime = nbt.getLong("internalTime");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		nbt.setBoolean("isOn", isOn);
		tank.writeToNBT(nbt, "tank");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		power = nbt.getLong("power");
		isOn = nbt.getBoolean("isOn");
		tank.readFromNBT(nbt, "tank");
	}
	
	@Override
	public void setFillstate(int fill, int index)
	{
		tank.setFill(fill);
	}

	@Override
	public void setFluidFill(int fill, FluidType type)
	{
		tank.setFill(fill);
	}

	@Override
	public void setType(FluidType type, int index)
	{
		tank.setTankType(type);
	}

	@Override
	public List<FluidTank> getTanks()
	{
		List<FluidTank> l = new ArrayList<FluidTank>(1);
		l.add(tank);
		return l;
	}

	@Override
	public int getFluidFill(FluidType type)
	{
		return tank.getFill();
	}

	@Override
	public int getMaxFluidFill(FluidType type)
	{
		return tank.getMaxFill();
	}

	@Override
	public void setPower(long i)
	{
		power = i;
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
	public String getName()
	{
		return "container.atomicClock";
	}
	
	public boolean getIsOn()
	{
		return isOn;
	}
	
	public Number[] getTimeData()
	{
//		Number[] data = new Number[3];
//		data[0] = time;
//		data[1] = day;
//		data[2] = year;
//		return data;
		return time.clone();
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack)
	{
		super.setInventorySlotContents(i, itemStack);
//		System.out.println("Inserted item is: " + (itemStack == null ? "null" : itemStack.getItem().getUnlocalizedName()));
		if(itemStack != null && i >= 1 && i <= 5 && itemStack.getItem() instanceof ItemMachineUpgrade)
			worldObj.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "hbm:item.upgradePlug", 1.0F, 1.0F);
	}
}
