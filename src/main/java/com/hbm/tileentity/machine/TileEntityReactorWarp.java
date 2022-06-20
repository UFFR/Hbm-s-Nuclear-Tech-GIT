package com.hbm.tileentity.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;

import org.apache.logging.log4j.Level;

import com.google.common.annotations.Beta;
import com.hbm.calc.EasyLocation;
import com.hbm.entity.effect.EntityNukeCloudSmall;
import com.hbm.entity.logic.EntityBalefire;
import com.hbm.handler.ThreeInts;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.interfaces.IFluidAcceptor;
import com.hbm.interfaces.IFluidSource;
import com.hbm.interfaces.Untested;
import com.hbm.inventory.FluidTank;
import com.hbm.inventory.RecipesCommon.*;
import com.hbm.inventory.fluid.*;
import com.hbm.inventory.recipes.BreederRecipes;
import com.hbm.inventory.recipes.BreederRecipes.BreederRecipe;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemCassette.TrackType;
import com.hbm.lib.Library;
import com.hbm.main.MainRegistry;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.TESirenPacket;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.Tuple.Quartet;

import api.hbm.energy.IEnergyUser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import scala.actors.threadpool.Arrays;
@Beta
public class TileEntityReactorWarp extends TileEntityMachineBase implements IEnergyUser, IFluidAcceptor, IFluidSource, IControlReceiver
{
	private FluidTank[] tanks = new FluidTank[4];
	private static final HashMap<AStack, ThreeInts> catalystList = new HashMap<AStack, ThreeInts>(); 
	private static final HashMap<AStack, ThreeInts> boosterList = new HashMap<AStack, ThreeInts>();
	private static final HashMap<AStack, Quartet<Boolean, Integer, Integer, Integer>> coreList = new HashMap<>();
	private List<IFluidAcceptor> cList = new ArrayList<IFluidAcceptor>();
	public static final long maxPower = 1000000000, powerRate = 100000;
	private long power = 0;
	public static final int maxTemp = 3500000, maxBar = 200000, maxDyne = 5000, breedingSpeed = 2000;
	public static final int critTemp = (int) (maxTemp * 0.8), critBar = (int) (maxBar * 0.8), critDyne = (int) (maxDyne * 0.8);
	// temperature, pressure (bar), force (teradynes), breeding progress 
	private int[] stats = {20, 1, 0, 0};
	private byte[] intermix = {0, 0};
	private byte level, age;
	public static final byte maxLevel = 120;
	private boolean magnets, active;
	public static final String nameMain = "container.reactorWarp", nameCore = "container.reactorWarp.core", nameCoreOut = "container.reactorWarp.coreOut", nameIntermix = "container.reactorWarp.mix";
	private final boolean[] criticalStats = {false, false, false};
	static
	{
		coreList.put(new ComparableStack(ModItems.singularity_micro), new Quartet<Boolean, Integer, Integer, Integer>(true, 1, 10, 48000 * 5));
		coreList.put(new ComparableStack(ModItems.singularity), new Quartet<Boolean, Integer, Integer, Integer>(false, 1, 10, 0));
		coreList.put(new ComparableStack(ModItems.singularity_super_heated), new Quartet<Boolean, Integer, Integer, Integer>(true, 1, 20, 48000 * 50));
		catalystList.put(new ComparableStack(ModItems.catalyst_clay), new ThreeInts(100, 0, 2));
		catalystList.put(new ComparableStack(ModItems.catalyst_rare), new ThreeInts(1000, 0, 10));
		catalystList.put(new ComparableStack(ModItems.catalyst_ten), new ThreeInts(10000, 0, 30));
		boosterList.put(new OreDictStack("gemDiamond"), new ThreeInts(100, 1, 2));
		boosterList.put(new ComparableStack(ModItems.powder_power), new ThreeInts(250, 5, 15));
		boosterList.put(new ComparableStack(ModItems.powder_nitan_mix), new ThreeInts(1000, 0, 10));
	}
	public TileEntityReactorWarp()
	{
		super(11);
		tanks[0] = new FluidTank(Fluids.DEUTERIUM, 64000, 0);
		tanks[1] = new FluidTank(Fluids.AMAT, 64000, 1);
		tanks[2] = new FluidTank(Fluids.PLASMA_WARP, 256000, 2);
		tanks[3] = new FluidTank(Fluids.CRYOGEL, 32000, 3);
	}
	
	@Override
	public void updateEntity()
	{
		// TODO Auto-generated method stub
		if (!worldObj.isRemote)
		{
			updateCritical();
			for (boolean b : criticalStats)
			{
				if (b)
				{
					PacketDispatcher.wrapper.sendToAllAround(new TESirenPacket(new EasyLocation(this), TrackType.AMS_SIREN.ordinal(), true), Library.easyTargetPoint(this, 1500));
					PacketDispatcher.wrapper.sendToAllAround(new TESirenPacket(new EasyLocation(this), TrackType.AMS_SIREN.ordinal(), false), Library.easyTargetPoint(this, 1500));
					break;
				}
			}
			
			if (canOperate())
			{
				doBreeding();
			}
		}
		
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("power", power);
		data.setIntArray("stats", stats);
		data.setByteArray("intermix", intermix);
		data.setByte("level", level);
		data.setBoolean("magnetsOn", magnets);
		data.setBoolean("isActive", active);
		networkPack(data, 100);
	}
	
	private void doBreeding()
	{
		BreederRecipe recipe = BreederRecipes.getOutput(getStackInSlot(3));
		if (slots[3] == null || recipe == null)
			return;
		
		if (slots[4] != null && (!recipe.output.isItemEqual(getStackInSlot(4)) || getStackInSlot(4).stackSize + recipe.output.stackSize > getStackInSlot(4).getMaxStackSize()))
			return;
		
		stats[3] += 25 * (breedingSpeed / BreederRecipes.getOutput(slots[0]).flux);
		
		if (stats[3] >= 10000)
		{
			stats[3] = 0;
			decrStackSize(3, 1);
			if (slots[4] == null)
				slots[4] = recipe.output.copy();
			else
				slots[4].stackSize += recipe.output.stackSize;
			markDirty();
		}
	}
	
	/** Uses an euler/natural logarithm type logarithmic function
	 * @param targLvl Target level of the stat
	 * @param currLvl Current level of the stat
	 * @return The new level of the stat
	 **/
	@Beta
	@Untested
	private static int modStatLevel(int targLvl, int currLvl)
	{
		try
		{
			return (int) Math.floor((targLvl * 1.125) / 1 + Math.pow(Math.E, Math.log(currLvl)));
		}
		catch (ArithmeticException e)
		{
			e.printStackTrace();
			MainRegistry.logger.catching(Level.WARN, e);
			return 0;
		}

	}
	/**
	 * Uses a square root function
	 * @param fLevel Current fluid level of the plasma tank
	 * @param max Maximum level of the statistic
	 * @return The target level for a stat
	 */
	@Beta
	@Untested
	private static int getMaxLevel(int fLevel, int max)
	{
		try
		{
			return (int) Math.floor(Math.sqrt(fLevel) * -(max / fLevel) + max * 1.125);
		}
		catch (ArithmeticException e)
		{
			e.printStackTrace();
			MainRegistry.logger.catching(Level.WARN, e);
			return 0;
		}
	}
	
	private void updateCritical()
	{
		criticalStats[0] = stats[0] >= critTemp;
		criticalStats[1] = stats[1] >= critBar;
		criticalStats[2] = stats[2] >= critDyne;
	}
	
	static final byte height = 1;
	protected void meltdown()
	{
		int yield = 0;
		yield += (getFluidFill(Fluids.AMAT) / 1000) * 5;
		yield += (getFluidFill(Fluids.PLASMA_WARP) / 10000) * 15;
		final EntityBalefire bf = new EntityBalefire(getWorldObj());
		bf.posX = xCoord;
		bf.posY = xCoord + height;
		bf.posZ = zCoord;
		bf.destructionRange = yield;
		worldObj.spawnEntityInWorld(bf);
		worldObj.spawnEntityInWorld(EntityNukeCloudSmall.statFacBale(getWorldObj(), xCoord, yCoord + height * 10, zCoord, yield * 1.5F, 1000));
	}
	
	@Override
	public void handleButtonPacket(int value, int meta)
	{
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "gui.button.press", 1.0F, 1.0F);
		switch (value)
		{
		case 0:
			magnets = !magnets;
			if (!magnets)
				active = false;
			break;
		case 1:
			active = magnets ? !active : false;
			break;
		case 2:
			level = (byte) meta;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		power = nbt.getLong("power");
		stats = nbt.getIntArray("stats");
		intermix = nbt.getByteArray("intermix");
		level = nbt.getByte("level");
		magnets = nbt.getBoolean("magnetsOn");
		active = nbt.getBoolean("isActive");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		for (int i = 0; i < 3; i++)
			tanks[i].writeToNBT(nbt, "tank_" + i);
		nbt.setLong("power", power);
		nbt.setIntArray("stats", stats);
		nbt.setByte("delay", level);
		nbt.setBoolean("magnetsOn", magnets);
		nbt.setBoolean("isActive", active);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		for (int i = 0; i < 3; i++)
			tanks[i].readFromNBT(nbt, "tank_" + i);
		power = nbt.getLong("power");
		stats = nbt.getIntArray("stats");
		level = nbt.getByte("delay");
		magnets = nbt.getBoolean("magnetsOn");
		active = nbt.getBoolean("isActive");
	}
	/**
	 * Constructs a {@link#ComparableStack} for comparisons. Automatically made singular.
	 * @param slot The slot the stack will be constructed from
	 * @return The new ComparableStack or null if invalid
	 */
	@CheckForNull
	private final ComparableStack constructComparable(int slot)
	{
		return constructComparable(getStackInSlot(slot));
	}
	/**
	 * Constructs a {@link#ComparableStack} for comparisons. Automatically made singular.
	 * @param stack The stack that the ComparableStack will be constructed from
	 * @return The new ComparableStack or null if invalid
	 */
	@CheckForNull
	private final static ComparableStack constructComparable(ItemStack stack)
	{
		return stack == null ? null : new ComparableStack(stack).makeSingular();
	}
	
	private final boolean areMagnetsOn()
	{
		return magnets && power >= powerRate;
	}
	
	private final boolean canOperate()
	{
		return active && getFluidFill(Fluids.DEUTERIUM) >= intermix[0] && getFluidFill(Fluids.AMAT) >= intermix[0] && intermix[1] + getFluidFill(Fluids.PLASMA_WARP) <= getMaxFluidFill(Fluids.PLASMA_WARP) && areMagnetsOn();
	}
	
	private final static byte indexFromType(FluidType type)
	{
		switch (type.getName())
		{
		case "DEUTERIUM":
			return 0;
		case "AMAT":
			return 1;
		case "PLASMA_WARP":
			return 2;
		case "CRYOGEL":
			return 3;
		default:
			return -1;
		}
	}
	
	@Override
	public void setFillstate(int fill, int index)
	{
		tanks[index].setFill(fill);
	}

	@Override
	public void setFluidFill(int fill, FluidType type)
	{
		tanks[indexFromType(type)].setFill(fill);
	}

	@Override
	public void setType(FluidType type, int index)
	{
		tanks[index].setTankType(type);
	}

	@Override
	public List<FluidTank> getTanks()
	{
		return Arrays.asList(tanks);
	}

	@Override
	public int getFluidFill(FluidType type)
	{
		return tanks[indexFromType(type)].getFill();
	}
	
	@Override
	public boolean getTact()
	{
		return age >= 0 && age < 10 ? true : false;
	}
	
	@Override
	public int getMaxFluidFill(FluidType type)
	{
		return tanks[indexFromType(type)].getMaxFill();
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
		return "container.reactorAmat";
	}

	@Override
	public void fillFluidInit(FluidType type)
	{
		fillFluid(xCoord + 1, yCoord, zCoord, getTact(), type);
		fillFluid(xCoord - 1, yCoord, zCoord, getTact(), type);
		fillFluid(xCoord, yCoord + 1, zCoord, getTact(), type);
		fillFluid(xCoord, yCoord - 1, zCoord, getTact(), type);
		fillFluid(xCoord, yCoord, zCoord + 1, getTact(), type);
		fillFluid(xCoord, yCoord, zCoord - 1, getTact(), type);
	}

	@Override
	public void fillFluid(int x, int y, int z, boolean newTact, FluidType type)
	{
		Library.transmitFluid(x, y, z, newTact, this, getWorldObj(), type);
	}

	@Override
	public List<IFluidAcceptor> getFluidList(FluidType type)
	{
		return cList;
	}

	@Override
	public void clearFluidList(FluidType type)
	{
		cList.clear();
	}

	public boolean[] getCriticals()
	{
		return criticalStats.clone();
	}
	public int[] assembleStats()
	{
		final int[] statsOut = new int[7];
		statsOut[0] = level;
		statsOut[1] = intermix[0];
		statsOut[2] = intermix[1];
		statsOut[3] = stats[0];
		statsOut[4] = stats[1];
		statsOut[5] = stats[2];
		statsOut[6] = stats[3];
		return statsOut;
	}
	public byte getDelayLevel()
	{
		return level;
	}
	public boolean isCoreValid()
	{
		for (Entry<AStack, Quartet<Boolean, Integer, Integer, Integer>> e : coreList.entrySet())
			if (e.getKey().isApplicable(constructComparable(0)))
				return true;
		return false;
	}
	public boolean magnetsActivated()
	{
		return magnets;
	}
	public boolean isRunning()
	{
		return active;
	}
	public boolean hasPower(boolean low)
	{
		return low ? power < maxPower * 0.75 : power >= powerRate;
	}
	public boolean hasPlasma(boolean low)
	{
		return low ? getFluidFill(Fluids.PLASMA_WARP) > getMaxFluidFill(Fluids.PLASMA_WARP) * 0.25 : getFluidFill(Fluids.PLASMA_WARP) > 0;
	}

	@Override
	public boolean hasPermission(EntityPlayer player)
	{
		return isUseableByPlayer(player);
	}

	@Override
	public void receiveControl(NBTTagCompound data)
	{
		// TODO Auto-generated method stub
	}
}
