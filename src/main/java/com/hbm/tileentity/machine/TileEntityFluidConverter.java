package com.hbm.tileentity.machine;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckReturnValue;

import com.google.common.annotations.Beta;
import com.google.common.collect.HashBiMap;
import com.hbm.handler.FluidTypeHandler.FluidType;
import com.hbm.interfaces.IFluidAcceptor;
import com.hbm.interfaces.Untested;
import com.hbm.inventory.FluidTank;
import com.hbm.items.machine.ItemFluidIdentifier;
import com.hbm.tileentity.TileEntityMachineBase;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
@Beta
@Untested
public class TileEntityFluidConverter extends TileEntityMachineBase implements IFluidAcceptor, IFluidHandler
{
//	public static final HashMap<FluidType, Fluid> bobToForge = new HashMap<>();
//	public static final HashMap<Fluid, FluidType> forgeToBob = new HashMap<>();
	public static final HashBiMap<FluidType, Fluid> conversions = HashBiMap.create();

	public static final short maxCapacity = 16000;
	private FluidTank bobTank = new FluidTank(FluidType.NONE, maxCapacity, 0);
	private net.minecraftforge.fluids.FluidTank forgeTank = new net.minecraftforge.fluids.FluidTank(maxCapacity);
	public static final short conversionRate = 100;
	/**
	 * {@literal true}: Convert from Bob to Forge<br>
	 * {@literal false}: Convert from Forge to Bob
	 */
	private boolean mode = true;
	public TileEntityFluidConverter()
	{
		super(4);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setFillstate(int fill, int index)
	{
		bobTank.setFill(fill);
	}

	@Override
	public void setFluidFill(int fill, FluidType type)
	{
		if (bobTank.getTankType().equals(type))
			bobTank.setFill(fill);
	}

	@Override
	public void setType(FluidType type, int index)
	{
		bobTank.setTankType(type);
	}

	@Override
	public List<FluidTank> getTanks()
	{
		ArrayList<FluidTank> tanks = new ArrayList<FluidTank>(1);
		tanks.add(bobTank);
		return tanks;
	}

	@Override
	public int getFluidFill(FluidType type)
	{
		if (bobTank.getTankType().equals(type))
			return bobTank.getFill();
		else
			return 0;
	}

	@Override
	public int getMaxFluidFill(FluidType type)
	{
		return maxCapacity;
	}

	@Override
	public String getName()
	{
		return "container.fluidConverter";
	}

	@Override
	public void updateEntity()
	{
		// TODO Auto-generated method stub
		if (!worldObj.isRemote)
		{
			if (getStackInSlot(4) != null && getStackInSlot(4).getItem() instanceof ItemFluidIdentifier)
				bobTank.setTankType(ItemFluidIdentifier.getType(getStackInSlot(4)));
			
			if (mode)
				bobTank.loadTank(0, 1, slots);
			else
				bobTank.unloadTank(0, 1, slots);
			
			if (canProcess())
			{
				int emptyRemaining = maxCapacity - (mode ? forgeTank.getCapacity() : bobTank.getFill());
				int toConvert = emptyRemaining < conversionRate ? conversionRate : emptyRemaining;
				bobTank.setFill(bobTank.getFill() + (toConvert * (mode ? -1 : 1)));
				forgeTank.setCapacity(forgeTank.getCapacity() + (toConvert * (mode ? 1 : -1)));
			}
			bobTank.updateTank(this);
		}
		NBTTagCompound data = new NBTTagCompound();
		forgeTank.writeToNBT(data);
		networkPack(data, 100);
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		forgeTank.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		bobTank.writeToNBT(nbt, "bobTank");
		NBTTagCompound fTankNBT = new NBTTagCompound();
		forgeTank.writeToNBT(fTankNBT);
		nbt.setTag("forgeTank", fTankNBT);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		bobTank.readFromNBT(nbt, "bobTank");
		forgeTank.readFromNBT(nbt.getCompoundTag("forgeTank"));
	}
	
	private boolean canProcess()
	{
		if (mode)
		{
			Fluid forgeFluid = getForgeFromBob(bobTank.getTankType());
			return forgeTank.getCapacity() == 0 || (forgeFluid != null && forgeTank.getFluid().getFluid().equals(forgeFluid) && forgeTank.getCapacity() != maxCapacity && bobTank.getFill() > 0);
		}
		else
		{
			FluidType bobFluid = getBobFromForge(forgeTank.getFluid().getFluid());
			return bobTank.getFill() < maxCapacity && bobTank.getTankType() == bobFluid && forgeTank.getCapacity() > 0;
		}
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return forgeTank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource != null && resource.isFluidEqual(forgeTank.getFluid()))
			return forgeTank.drain(resource.amount, doDrain);
		else
			return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return forgeTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return !mode;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return mode;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] { forgeTank.getInfo() };
	}
	
	@CheckReturnValue
	public static FluidType getBobFromForge(Fluid f)
	{
		return conversions.inverse().containsKey(f) ? conversions.inverse().get(f) : FluidType.NONE;
	}
	@CheckReturnValue
	public static Fluid getForgeFromBob(FluidType f)
	{
		return conversions.containsKey(f) ? conversions.get(f) : null;
	}

}
