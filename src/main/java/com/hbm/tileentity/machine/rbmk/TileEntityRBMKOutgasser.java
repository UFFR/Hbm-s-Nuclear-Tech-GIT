package com.hbm.tileentity.machine.rbmk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.hbm.blocks.ModBlocks;
import com.hbm.entity.projectile.EntityDebrisBase.DebrisType;
import com.hbm.interfaces.IFluidAcceptor;
import com.hbm.interfaces.IFluidSource;
import com.hbm.inventory.FluidTank;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemFluidIcon;
import com.hbm.lib.Library;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKConsole.ColumnType;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityRBMKOutgasser extends TileEntityRBMKSlottedBase implements IRBMKFluxReceiver, IFluidSource {

	public List<IFluidAcceptor> list = new ArrayList<IFluidAcceptor>();
	public FluidTank gas;
	public double progress;
	public static final int duration = 10000;
	private Optional<IrradiationRecipe> currentRecipe = Optional.empty();

	public TileEntityRBMKOutgasser() {
		super(2);
		gas = new FluidTank(Fluids.TRITIUM, 64000, 0);
	}

	@Override
	public String getName() {
		return "container.rbmkOutgasser";
	}
	
	@Override
	public void updateEntity() {
		
		if(!worldObj.isRemote) {
			gas.updateTank(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
			
			if(worldObj.getTotalWorldTime() % 10 == 0)
				fillFluidInit(gas.getTankType());
			if(!canProcess()) {
				this.progress = 0;
				currentRecipe = Optional.empty();
			}
		}
		
		super.updateEntity();
	}

	@Override
	public void receiveFlux(NType type, double flux) {
		if(canProcess()) {
			if (!currentRecipe.isPresent())
				currentRecipe = Optional.of(getRecipe(getStackInSlot(0)));
			if(type == NType.FAST)
				flux *= 0.2D;
			if (flux < currentRecipe.get().intensity)
				flux *= 0.1d;
			
			progress += flux * RBMKDials.getOutgasserMod(worldObj);
			
			if(progress > currentRecipe.get().time) {
				process();
				this.markDirty();
			}
		}
	}
	
	private static final HashMap<Object, IrradiationRecipe> recipes = new HashMap<Object, IrradiationRecipe>();
	
	static {
		recipes.put("blockLithium", new IrradiationRecipe(ItemFluidIcon.addQuantity(new ItemStack(ModItems.fluid_icon, 1, Fluids.TRITIUM.getID()), 10000)));
		recipes.put("ingotLithium", new IrradiationRecipe(ItemFluidIcon.addQuantity(new ItemStack(ModItems.fluid_icon, 1, Fluids.TRITIUM.getID()), 1000)));
		recipes.put("dustLithium", new IrradiationRecipe(ItemFluidIcon.addQuantity(new ItemStack(ModItems.fluid_icon, 1, Fluids.TRITIUM.getID()), 1000)));
		recipes.put(new ComparableStack(ModItems.powder_lithium_tiny), new IrradiationRecipe(ItemFluidIcon.addQuantity(new ItemStack(ModItems.fluid_icon, 1, Fluids.TRITIUM.getID()), 100)));
		recipes.put("ingotGold", new IrradiationRecipe(new ItemStack(ModItems.ingot_au198)));
		recipes.put("nuggetGold", new IrradiationRecipe(new ItemStack(ModItems.nugget_au198)));
		recipes.put("dustGold", new IrradiationRecipe(new ItemStack(ModItems.powder_au198)));
		recipes.put("ingotThorium232", new IrradiationRecipe(new ItemStack(ModItems.ingot_tha)));
		recipes.put("nuggetThorium232", new IrradiationRecipe(new ItemStack(ModItems.nugget_tha)));
		recipes.put("billetThorium232", new IrradiationRecipe(new ItemStack(ModItems.billet_tha)));
		recipes.put("blockThorium232", new IrradiationRecipe(new ItemStack(ModBlocks.block_tha)));
		recipes.put(new ComparableStack(Blocks.brown_mushroom), new IrradiationRecipe(new ItemStack(ModBlocks.mush)));
		recipes.put(new ComparableStack(Blocks.red_mushroom), new IrradiationRecipe(new ItemStack(ModBlocks.mush)));
		recipes.put(new ComparableStack(Items.mushroom_stew), new IrradiationRecipe(new ItemStack(ModItems.glowing_stew)));
	}
	
	private boolean canProcess() {
		
		if(slots[0] == null)
			return false;
		
		IrradiationRecipe recipe = getRecipe(slots[0]);
		
		if(recipe == null)
			return false;
		
		if(recipe.output.getItem() == ModItems.fluid_icon) {
			return recipe.output.getItemDamage() == gas.getTankType().getID() && gas.getFill() + ItemFluidIcon.getQuantity(recipe.output) <= gas.getMaxFill();
		}
		
		if(slots[1] == null)
			return true;
		
		return slots[1].getItem() == recipe.output.getItem() && slots[1].getItemDamage() == recipe.output.getItemDamage() && slots[1].stackSize + recipe.output.stackSize <= slots[1].getMaxStackSize();
	}
	
	public static IrradiationRecipe getRecipe(ItemStack stack) {
		
		if(stack == null || stack.getItem() == null)
			return null;
		
		ComparableStack comp = new ComparableStack(stack).makeSingular();
		
		if(recipes.containsKey(comp))
			return recipes.get(comp);
		
		String[] dictKeys = comp.getDictKeys();
		
		for(String key : dictKeys) {
			
			if(recipes.containsKey(key))
				return recipes.get(key);
		}
		
		return null;
	}
	
	public int getMaxDuration()
	{
		return currentRecipe.isPresent() ? currentRecipe.get().time : duration;
	}
	
	private void process() {
		
		ItemStack output = currentRecipe.get().getOutput();
		this.decrStackSize(0, 1);
		this.progress = 0;
		
		if(output.getItem() == ModItems.fluid_icon) {
			gas.setFill(gas.getFill() + ItemFluidIcon.getQuantity(output));
			return;
		}
		
		if(slots[1] == null) {
			slots[1] = output.copy();
		} else {
			slots[1].stackSize += output.stackSize;
		}
	}

	@Override
	public void fillFluidInit(FluidType type) {
		fillFluid(this.xCoord, this.yCoord + RBMKDials.getColumnHeight(worldObj) + 1, this.zCoord, getTact(), type);
		
		if(worldObj.getBlock(xCoord, yCoord - 1, zCoord) == ModBlocks.rbmk_loader) {

			fillFluid(this.xCoord + 1, this.yCoord - 1, this.zCoord, getTact(), type);
			fillFluid(this.xCoord - 1, this.yCoord - 1, this.zCoord, getTact(), type);
			fillFluid(this.xCoord, this.yCoord - 1, this.zCoord + 1, getTact(), type);
			fillFluid(this.xCoord, this.yCoord - 1, this.zCoord - 1, getTact(), type);
			fillFluid(this.xCoord, this.yCoord - 2, this.zCoord, getTact(), type);
		}
		
		if(worldObj.getBlock(xCoord, yCoord - 2, zCoord) == ModBlocks.rbmk_loader) {

			fillFluid(this.xCoord + 1, this.yCoord - 2, this.zCoord, getTact(), type);
			fillFluid(this.xCoord - 1, this.yCoord - 2, this.zCoord, getTact(), type);
			fillFluid(this.xCoord, this.yCoord - 2, this.zCoord + 1, getTact(), type);
			fillFluid(this.xCoord, this.yCoord - 2, this.zCoord - 1, getTact(), type);
			fillFluid(this.xCoord, this.yCoord - 1, this.zCoord, getTact(), type);
			fillFluid(this.xCoord, this.yCoord - 3, this.zCoord, getTact(), type);
		}
	}

	@Override
	public void fillFluid(int x, int y, int z, boolean newTact, FluidType type) {
		Library.transmitFluid(x, y, z, newTact, this, worldObj, type);
	}
	
	@Override
	@Deprecated //why are we still doing this?
	public boolean getTact() { return worldObj.getTotalWorldTime() % 20 < 10; }

	@Override
	public void setFillForSync(int fill, int index) {

		if(index == 0)
			gas.setFill(fill);
	}

	@Override
	public void setFluidFill(int fill, FluidType type) {
		
		if(type == gas.getTankType())
			gas.setFill(fill);
	}

	@Override
	public void setTypeForSync(FluidType type, int index) {

		if(index == 0)
			gas.setTankType(type);
	}

	@Override
	public int getFluidFill(FluidType type) {
		
		if(type == gas.getTankType())
			return gas.getFill();
		
		return 0;
	}

	@Override
	public List<IFluidAcceptor> getFluidList(FluidType type) {
		return list;
	}

	@Override
	public void clearFluidList(FluidType type) {
		list.clear();
	}
	
	@Override
	public void onMelt(int reduce) {
		
		int count = 4 + worldObj.rand.nextInt(2);
		
		for(int i = 0; i < count; i++) {
			spawnDebris(DebrisType.BLANK);
		}
		
		super.onMelt(reduce);
	}

	@Override
	public ColumnType getConsoleType() {
		return ColumnType.OUTGASSER;
	}

	@Override
	public NBTTagCompound getNBTForConsole() {
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("gas", this.gas.getFill());
		data.setInteger("maxGas", this.gas.getMaxFill());
		data.setShort("type", (short)this.gas.getTankType().getID());
		data.setDouble("progress", this.progress);
		return data;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		this.progress = nbt.getDouble("progress");
		this.gas.readFromNBT(nbt, "gas");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		nbt.setDouble("progress", this.progress);
		this.gas.writeToNBT(nbt, "gas");
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return getRecipe(itemStack) != null && i == 0;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int j) {
		return i == 1;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return new int[] {0, 1};
	}
	
	private static class IrradiationRecipe
	{
		final ItemStack output;
		public final short intensity;
		public final int time;
		public IrradiationRecipe(ItemStack out)
		{
			this(out, 30, TileEntityRBMKOutgasser.duration);
		}
		public IrradiationRecipe(ItemStack out, int heat, int dura)
		{
			output = out;
			intensity = (short) heat;
			time = dura;
		}
		public ItemStack getOutput()
		{
			return output.copy();
		}
	}
	static IrradiationRecipe newRecipe(ItemStack stackIn)
	{
		return new IrradiationRecipe(stackIn);
	}
	static IrradiationRecipe newRecipe(ItemStack stackIn, int intensity)
	{
		return new IrradiationRecipe(stackIn, intensity, duration);
	}
}
