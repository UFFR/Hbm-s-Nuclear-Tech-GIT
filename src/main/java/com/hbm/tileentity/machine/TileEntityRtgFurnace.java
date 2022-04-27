package com.hbm.tileentity.machine;

import java.math.BigInteger;

import com.hbm.blocks.machine.MachineRtgFurnace;
import com.hbm.items.machine.ItemRTGPellet;
import com.hbm.tileentity.IRTGUser;
import com.hbm.tileentity.IRadioisotopeFuel;
import com.hbm.tileentity.TileEntityMachineBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityRtgFurnace extends TileEntityMachineBase implements IRTGUser
{

	
	public int dualCookTime;
	public static final int processingSpeed = 1000;
	
	private static final int[] slots_top = new int[] {0};
	private static final int[] slots_bottom = new int[] {4};
	private static final int[] slots_side = new int[] {1, 2, 3};
	private BigInteger date = BigInteger.ZERO;
	public TileEntityRtgFurnace()
	{
		super(5);
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.rtgFurnace";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && this.customName.length() > 0;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		if(worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
		{
			return false;
		}else{
			return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <=64;
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return true;
	}
	
	public boolean isLoaded() {
		return getHeat() > 0;
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(slots[i] != null)
		{
			if(slots[i].stackSize <= j)
			{
				ItemStack itemStack = slots[i];
				slots[i] = null;
				return itemStack;
			}
			ItemStack itemStack1 = slots[i].splitStack(j);
			if (slots[i].stackSize == 0)
			{
				slots[i] = null;
			}
			
			return itemStack1;
		} else {
			return null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		dualCookTime = nbt.getShort("CookTime");
		date = new BigInteger(nbt.getByteArray("date"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("cookTime", dualCookTime);
		nbt.setByteArray("date", date.toByteArray());
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_)
    {
        return p_94128_1_ == 0 ? slots_bottom : (p_94128_1_ == 1 ? slots_top : slots_side);
    }

	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int j) {
		return j != 0 || i != 1 || itemStack.getItem() == Items.bucket;
	}
	
	public int getDiFurnaceProgressScaled(int i) {
		return (dualCookTime * i) / processingSpeed;
	}
	
	public boolean canProcess() {
		if(slots[0] == null)
		{
			return false;
		}
        ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);
		if(itemStack == null)
		{
			return false;
		}
		
		if(slots[4] == null)
		{
			return true;
		}
		
		if(!slots[4].isItemEqual(itemStack)) {
			return false;
		}
		
		if(slots[4].stackSize < getInventoryStackLimit() && slots[4].stackSize < slots[4].getMaxStackSize()) {
			return true;
		}else{
			return slots[4].stackSize < itemStack.getMaxStackSize();
		}
	}
	
	private void processItem() {
		if(canProcess()) {
	        ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);
			
			if(slots[4] == null)
			{
				slots[4] = itemStack.copy();
			}else if(slots[4].isItemEqual(itemStack)) {
				slots[4].stackSize += itemStack.stackSize;
			}
			
			for(int i = 0; i < 1; i++)
			{
				if(slots[i].stackSize <= 0)
				{
					slots[i] = new ItemStack(slots[i].getItem().setFull3D());
				}else{
					slots[i].stackSize--;
				}
				if(slots[i].stackSize <= 0)
				{
					slots[i] = null;
				}
			}
		}
	}
	
	public boolean hasPower() {
		return isLoaded();
	}
	
	public boolean isProcessing() {
		return this.dualCookTime > 0;
	}
	
	@Override
	public void updateEntity() {
		this.hasPower();
		boolean flag1 = false;
		
		if(!worldObj.isRemote)
		{
			if(hasPower() && canProcess())
			{
				dualCookTime += updateRTGs();
				
				if(this.dualCookTime >= TileEntityRtgFurnace.processingSpeed)
				{
					this.dualCookTime = 0;
					this.processItem();
					flag1 = true;
				}
			}else{
				dualCookTime = 0;
				updateRTGs();
			}
			
			boolean trigger = true;
			
			if(hasPower() && canProcess() && this.dualCookTime == 0)
			{
				trigger = false;
			}
			
			if(trigger)
            {
                flag1 = true;
                MachineRtgFurnace.updateBlockState(this.dualCookTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
		}
		
		if(flag1)
		{
			this.markDirty();
		}
		
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("cookTime", dualCookTime);
		data.setByteArray("date", date.toByteArray());
		networkPack(data, 25);
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		dualCookTime = nbt.getShort("cookTime");
		date = new BigInteger(nbt.getByteArray("date"));
	}

	@Override
	public int getHeat()
	{
		return updateRTGs();
	}
	
	@Override
	public ItemStack[] getInventory()
	{
		ItemStack[] rtgSlots = new ItemStack[slots_side.length];
		for (byte i = 0; i < slots_side.length; i++)
			rtgSlots[i] = slots[slots_side[i]];
		return rtgSlots;
	}

	@Override
	public Class<? extends IRadioisotopeFuel> getDesiredClass()
	{
		return ItemRTGPellet.class;
	}

	@Override
	public String getName()
	{
		return "container.rtgFurnace";
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
