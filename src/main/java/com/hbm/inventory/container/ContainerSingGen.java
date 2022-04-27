package com.hbm.inventory.container;

import java.util.HashSet;

import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.SlotMachineOutput;
import com.hbm.tileentity.machine.TileEntitySingGen;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSingGen extends Container
{
	private TileEntitySingGen tileEntity;
	/* Shift click items */
	// Items that go into the corners
	public static final HashSet<AStack> cornerItems = new HashSet<>();
	// Items that go into the sides
	public static final HashSet<AStack> sideItems = new HashSet<>();
	// Items that can go anywhere in the outer ring
	public static final HashSet<AStack> anyItems = new HashSet<>();
	// Items that go into the center
	public static final HashSet<AStack> centerItems = new HashSet<>();

	public ContainerSingGen(InventoryPlayer invPlayer, TileEntitySingGen tEntity)
	{
		tileEntity = tEntity;
		/* -- Input Slots --
		 * 5  9  6
		 * 11 4  12
		 * 7  10 8
		 */
		// Battery
		addSlotToContainer(new Slot(tEntity, 0, 8, 112));
		// Output
		addSlotToContainer(new SlotMachineOutput(tEntity, 1, 80, 106));
		// Fluid input
		addSlotToContainer(new Slot(tEntity, 2, 152, 94));
		// Fluid output
		addSlotToContainer(new SlotMachineOutput(tEntity, 3, 152, 112));
		// Center item input
		addSlotToContainer(new Slot(tEntity, 4, 80, 49));
		// Corner item input
		addSlotToContainer(new Slot(tEntity, 5, 54, 23));
		addSlotToContainer(new Slot(tEntity, 6, 106, 23));
		addSlotToContainer(new Slot(tEntity, 7, 54, 75));
		addSlotToContainer(new Slot(tEntity, 8, 106, 75));
		// Side item input
		addSlotToContainer(new Slot(tEntity, 9, 80, 16));
		addSlotToContainer(new Slot(tEntity, 10, 80, 82));
		addSlotToContainer(new Slot(tEntity, 11, 47, 49));
		addSlotToContainer(new Slot(tEntity, 12, 113, 49));
		// Fluid identifier
		addSlotToContainer(new Slot(tEntity, 13, 134, 112));
		
		for (int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + 60));
		
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142 + 60));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tileEntity.isUseableByPlayer(player);
	}
	// TODO Finish
	@Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int par2)
    {
		ItemStack var3 = null;
		Slot var4 = (Slot) this.inventorySlots.get(par2);
		
		if (var4 != null && var4.getHasStack())
		{
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();
			
            if (par2 <= 3) {
				if (!this.mergeItemStack(var5, 4, this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var5, 0, 3, false))
			{
				return null;
			}
			
			if (var5.stackSize == 0)
			{
				var4.putStack((ItemStack) null);
			}
			else
			{
				var4.onSlotChanged();
			}
		}
		
		return var3;
    }}
