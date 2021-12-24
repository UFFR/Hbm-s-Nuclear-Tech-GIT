package com.hbm.tileentity.machine;

import java.util.ArrayList;

import com.hbm.config.GeneralConfig;
import com.hbm.items.ModItems;
import com.hbm.lib.Library;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.Tuple.Pair;

import api.hbm.energy.IEnergyUser;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityFoodSynthesizer extends TileEntityMachineBase implements IEnergyUser
{
	private long power = 0;
	public static final long powerRate = 100000;
	public static final long maxPower = 1000000000;
	private int fuel = 0;
	public static final int fuelRatio = GeneralConfig.enableBabyMode ? 500 : 250;
	public static final int maxFuel = 32000;
	private static ArrayList<Pair<Item, Integer>> recipes = new ArrayList<>();
	static
	{
		addRecipe(Items.apple, 250);
		addRecipe(Items.baked_potato, 350);
		addRecipe(ModItems.spongebob_macaroni, 300);
	}
	private static void addRecipe(Item food, int fuel)
	{
		recipes.add(new Pair<Item, Integer>(food, fuel));
	}
	public TileEntityFoodSynthesizer()
	{
		super(3);
	}

	@Override
	public void updateEntity()
	{
		// TODO Auto-generated method stub
		if (!worldObj.isRemote)
		{
			power = Library.chargeTEFromItems(slots, 0, power, maxPower);
			// Add to fuel
			if (fuel + fuelRatio <= maxFuel && slots[1] != null && getStackInSlot(1).getItem() == ModItems.biomass)
			{
				fuel += fuelRatio;
				decrStackSize(1, 1);
			}
		}
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("power", power);
		data.setInteger("fuel", fuel);
		networkPack(data, 10);
	}

	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		power = nbt.getLong("power");
		fuel = nbt.getInteger("fuel");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		nbt.setInteger("fuel", fuel);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		power = nbt.getLong("power");
		fuel = nbt.getInteger("fuel");
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
		return "container.foodSynthesizer";
	}
}
