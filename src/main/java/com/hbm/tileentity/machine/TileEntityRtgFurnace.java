package com.hbm.tileentity.machine;

import com.hbm.blocks.machine.MachineRtgFurnace;
import com.hbm.interfaces.IRTGUser;
import com.hbm.interfaces.IRadioisotopeFuel;
import com.hbm.items.machine.ItemRTGPellet;
import com.hbm.tileentity.TileEntityMachineBase;

import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityRtgFurnace extends TileEntityMachineBase implements ISidedInventory, IRTGUser
{

	public short dualCookTime, speed;
	public static final short processingSpeed = 12000;
	
	private static final int[] slots_top = new int[] {0};
	private static final int[] slots_bottom = new int[] {4};
	private static final int[] slots_side = new int[] {1, 2, 3};
	
	public TileEntityRtgFurnace()
	{
		super(5);
	}

	public boolean isLoaded()
	{
		speed = (short) getHeat();
		return 150 <= getHeat();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		dualCookTime = nbt.getShort("CookTime");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("cookTime", dualCookTime);
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
				dualCookTime += speed;
				
				if(this.dualCookTime >= processingSpeed)
				{
					this.dualCookTime = 0;
					this.processItem();
					flag1 = true;
				}
			}else{
				dualCookTime = 0;
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
		data.setShort("speed", speed);
		data.setShort("cookTime", dualCookTime);
		networkPack(data, 25);
	}
	
	@Override
	public void networkUnpack(NBTTagCompound nbt)
	{
		speed = nbt.getShort("speed");
		dualCookTime = nbt.getShort("cookTime");
	}

	@Override
	public int getHeat()
	{
		return updateRTGs(slots, slots_side);
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
}
