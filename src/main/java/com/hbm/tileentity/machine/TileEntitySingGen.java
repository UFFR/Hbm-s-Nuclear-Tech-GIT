package com.hbm.tileentity.machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.hbm.config.BombConfig;
import com.hbm.config.MachineConfig;
import com.hbm.entity.effect.EntityCloudFleija;
import com.hbm.entity.effect.EntityVortex;
import com.hbm.entity.logic.EntityNukeExplosionMK3;
import com.hbm.interfaces.IFluidAcceptor;
import com.hbm.interfaces.Spaghetti;
import com.hbm.inventory.FluidTank;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.container.ContainerSingGen;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.recipes.SingGenRecipes;
import com.hbm.inventory.recipes.SingGenRecipes.SingGenRecipe;
import com.hbm.items.machine.ItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidTank;
import com.hbm.lib.Library;
import com.hbm.tileentity.TileEntityMachineBase;

import api.hbm.energy.IBatteryItem;
import api.hbm.energy.IEnergyUser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

@Spaghetti("Preemptive")
public class TileEntitySingGen extends TileEntityMachineBase implements IFluidAcceptor, IEnergyUser
{
	public long power;
	public static final long maxPower = 50000000000L;
	private static final long consumptionRate = (maxPower / 200) / 4;// TODO Placeholder(?)
	public final FluidTank tank;
	
	public boolean isOn;
	private int progress;
	private static final int[] ringSlots = new int[] {5, 9, 6, 11, 12, 7, 10, 8};
	private final Map<Integer, ItemStack> ring = new HashMap<Integer, ItemStack>(8);
	
	private final ItemStack[] ringArray = new ItemStack[8];
	private Optional<SingGenRecipe> currentRecipe = Optional.empty();
	
	public TileEntitySingGen()
	{
		super(14);
		tank = new FluidTank(Fluids.ASCHRAB, 64000, 0);
	}
	
	public boolean isProcessing()
	{
		return ((progress > 0 || canProcess()) && isOn);
	}
	public int getProgressPercent()
	{
		return (progress * 100) / 200;
	}
	/** Find and validate a recipe for the current machine layout, if possible
	 * @param ringIn - The array containing the ring of items obtained from updateRing()
	 * @param centerIn - The center item
	 * @param fluidIn - The current fluid type
	 * @param amountIn - Amount of fluid in the machine
	 * @return A valid recipe for the current machine input, if it exists, else it returns null 
	 */
	private SingGenRecipe validateRecipe(ItemStack[] ringIn, ItemStack centerIn, FluidType fluidIn, int amountIn)
	{
		for (SingGenRecipe recipe : SingGenRecipes.recipes)
		{
			//System.out.println(String.format("Checking recipe ring input: %s, Recipe center input: %s", recipe.inputRing, recipe.inputCenter.getUnlocalizedName()));
			//System.out.println("Is shaped? " + recipe.shaped);
			//System.out.println("Recipe center input: " + recipe.inputCenter.getUnlocalizedName());
//			if (centerIn != null && SingGenRecipes.doRingsMatch(ringIn, recipe.aInputRing, recipe.shaped))
//			{
//				if (SingGenRecipes.isItemValid(centerIn, recipe.inputCenter))
//				{
//					if (recipe.fluid.equals(fluidIn) && !recipe.fluid.equals(FluidType.NONE))
//					{
//						if (recipe.fluidAmount <= amountIn)
//						{
//							//System.out.println(String.format("Recipe outputs: %s, uses fluids", recipe.getOutput().getDisplayName()));
//							return recipe;
//						}
//					}
//					else if (recipe.fluid.equals(FluidType.NONE))
//					{
//						//System.out.println(String.format("Recipe outputs: %s, does not use fluids", recipe.getOutput().getDisplayName()));
//						return recipe;
//					}
//				}
//			}
//			if (SingGenRecipes.isRecipeValid(getRingArray(), recipe, centerIn, fluidIn) && recipe.fluidAmount <= amountIn)
//				return recipe;
			if (SingGenRecipes.isRecipeValid(getRingArray(), recipe, centerIn, fluidIn) && recipe.fluidAmount <= amountIn)
				return recipe;
			else
				continue;
		}
		//System.out.println("No valid recipe found");
		return null;
	}
	/* -- Input Slots --
	 * 5  9  6
	 * 11 4  12
	 * 7  10 8
	 */
	public boolean canProcess()
	{
		markDirty();
		updateRing();
		currentRecipe = Optional.ofNullable(validateRecipe(ringArray, slots[4], tank.getTankType(), tank.getFill()));
		if (currentRecipe.isPresent())
		{
			if ((tank.getTankType().equals(currentRecipe.get().fluid) && tank.getFill() >= currentRecipe.get().fluidAmount) || currentRecipe.get().fluid == Fluids.NONE)
			{
				if (this.getStackInSlot(1) == null || this.getStackInSlot(1).getMaxStackSize() >= this.getStackInSlot(1).stackSize + currentRecipe.get().getOutput().stackSize)
				{
					if (power >= consumptionRate)
					{
						return true;
					}
				}
			}
		}
		//System.out.println("Can't process");
		return false;
	}
	private void updateRing()
	{
		//System.out.println("Updating machine ring input...");
		ring.clear();
		int index = 0;
		for (int slot : ringSlots)
		{
			if (this.getStackInSlot(slot) != null)
			{
				//System.out.println(String.format("Item in slot #%s is not null, is: %s", slot, this.getStackInSlot(slot).getItem().getUnlocalizedName()));
				ringArray[index] = this.getStackInSlot(slot);
				ring.put(slot, getStackInSlot(slot));
			}
			else
			{
				//System.out.println(String.format("Item in slot #%s is null", slot));
				ringArray[index] = null;
				ring.put(slot, null);
			}
			index++;
		}
		//System.out.println(String.format("Ring input: %s", ring));
	}
	
	public Map<Integer, ItemStack> getRingMap()
	{
		return ring;
	}
	
	public ItemStack[] getRingArray()
	{
		return ringArray.clone();
	}
	
	@Override
	public void updateEntity()
	{
		//System.out.println(String.format("Center input: %s", this.getStackInSlot(4)));
		if (!worldObj.isRemote)
		{
			this.power = Library.chargeTEFromItems(slots, 0, power, maxPower);
			tank.loadTank(2, 3, slots);
			if (getStackInSlot(13) != null && getStackInSlot(13).getItem() instanceof ItemFluidIdentifier)
				tank.setTankType(ItemFluidIdentifier.getType(getStackInSlot(13)));
			tank.updateTank(this);
			if (isOn)
			{
				if (canProcess())
				{
					progress++;
					power -= consumptionRate;
					if (progress == 200)
					{
						process();
						progress = 0;
					}
				}
				else
				{
					progress = 0;
					if (consumptionRate > power)
						attemptFail();
				}
			}
			else
			{
				progress = 0;
			}
			
		}
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("power", power);
		data.setInteger("progress", progress);
		data.setBoolean("isOn", isOn);
		this.networkPack(data, 25);

	}
	/** Called when the machine is on but has no power,
	 * if the config allows it, may explode with a
	 * Fölkvangr field with a 3/4 chance (yield also
	 * determined by config) or collapse into a black hole
	 * with a 1/4 chance
	 */
	private void attemptFail()
	{
		if ((power < consumptionRate && tank.getFill() > 0 && tank.getTankType().equals(Fluids.ASCHRAB) && (currentRecipe != null && currentRecipe.get().fluid.equals(Fluids.ASCHRAB)) && !MachineConfig.singGenFailType.equals(MachineConfig.EnumSingGenFail.NONE)))
		{
			if (rand.nextInt(4) == 0)
			{
				EntityVortex bhole = new EntityVortex(getWorldObj(), (tank.getFill() / 8000));
				bhole.posX = xCoord;
				bhole.posY = yCoord;
				bhole.posZ = zCoord;
				worldObj.spawnEntityInWorld(bhole);
			}
			else
			{
				int yield = BombConfig.aSchrabRadius * (tank.getFill() / 1000);
				if (yield > MachineConfig.singGenFailRadius && MachineConfig.singGenFailType.equals(MachineConfig.EnumSingGenFail.CAP_CUSTOM))
					yield = MachineConfig.singGenFailRadius;
				EntityNukeExplosionMK3 field = new EntityNukeExplosionMK3(getWorldObj());
				field.posX = xCoord;
				field.posY = yCoord;
				field.posZ = zCoord;
				field.destructionRange = yield;
				field.speed = 25;
				field.coefficient = 1.0F;
				field.waste = false;
				worldObj.spawnEntityInWorld(field);
				EntityCloudFleija effect = new EntityCloudFleija(getWorldObj(), yield);
				effect.posX = xCoord;
				effect.posY = yCoord;
				effect.posZ = zCoord;
				worldObj.spawnEntityInWorld(effect);
			}
		}
	}
	
	@Override
	public void networkUnpack(NBTTagCompound data)
	{
		this.isOn = data.getBoolean("isOn");
		this.power = data.getLong("power");
		this.progress = data.getInteger("progress");
	}
	@Override
	public void handleButtonPacket(int value, int meta)
	{
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, buttonPress, 1, 1);
		this.isOn = !this.isOn;
	}
	private void process()
	{
		//System.out.println("Processing...");
		if (!currentRecipe.get().keepRing)
		{
			for (int slot : ringSlots)
				if (this.getStackInSlot(slot) != null)
					this.decrStackSize(slot, 1);
		}
		this.decrStackSize(4, currentRecipe.get().inputCenter.stacksize);
		if (currentRecipe.get().fluid != Fluids.NONE)
			tank.setFill(tank.getFill() - currentRecipe.get().fluidAmount);
		
		if (this.getStackInSlot(1) == null)
			this.setInventorySlotContents(1, currentRecipe.get().getOutput());
		else if (this.getStackInSlot(1) != null && slots[1].getItem() == currentRecipe.get().getOutput().getItem())
			this.getStackInSlot(1).stackSize += currentRecipe.get().getOutput().stackSize;

		markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		power = nbt.getLong("power");
		isOn = nbt.getBoolean("isOn");
		progress = nbt.getInteger("progress");
		tank.readFromNBT(nbt, "tank");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		nbt.setBoolean("isOn", isOn);
		nbt.setInteger("progress", progress);
		tank.writeToNBT(nbt, "tank");
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack)
	{
		Item itemIn = itemStack.getItem();
		switch (i)
		{
		case 0:
			return itemIn instanceof IBatteryItem;
		case 1:
			return false;
		case 2:
			return itemIn instanceof ItemFluidTank;
		case 3:
			return false;
		case 4:
			return ContainerSingGen.centerItems.contains(new ComparableStack(itemIn));
		case 5:
		case 6:
		case 7:
		case 8:
			return ContainerSingGen.cornerItems.contains(new ComparableStack(itemIn));
		case 9:
		case 10:
		case 11:
		case 12:
			return ContainerSingGen.sideItems.contains(new ComparableStack(itemIn));
		case 13:
			return itemIn instanceof ItemFluidIdentifier;
		default:
			return false;
		}
	}
	
	public SingGenRecipe getCurrentRecipe()
	{
		return currentRecipe.orElse(null);
	}
	
	@Override
	public void setPower(long i)
	{
		this.power = i;
	}

	@Override
	public long getPower()
	{
		return this.power;
	}

	@Override
	public long getMaxPower()
	{
		return maxPower;
	}

	public int getPowerScaled(long i)
	{
		return (int) ((power * i) / maxPower);
	}
	
	@Override
	public int getMaxFluidFill(FluidType type)
	{
		return tank.getMaxFill();
	}

	@Override
	public void setFluidFill(int fill, FluidType type)
	{
		if (type.equals(tank.getTankType()))
			tank.setFill(fill);
	}

	@Override
	public int getFluidFill(FluidType type)
	{
		return tank.getFill();
	}

	@Override
	public String getName()
	{
		return "container.machineSingGen";
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
	
	@Override
	public double getMaxRenderDistanceSquared()
	{
		return renderRange;
	}

	@Override
	public void setFillForSync(int fill, int index)
	{
		tank.setFill(fill);
	}

	@Override
	public void setTypeForSync(FluidType type, int index)
	{
		tank.setTankType(type);
	}
}