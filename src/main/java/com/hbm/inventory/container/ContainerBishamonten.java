package com.hbm.inventory.container;

import com.hbm.tileentity.turret.TileEntityBishamonten;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerBishamonten extends Container
{
	private TileEntityBishamonten luna;
	public ContainerBishamonten(InventoryPlayer invPlayer, TileEntityBishamonten te)
	{
		luna = te;
		
		for (int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18 + 7, 84 + i * 18 + 49));
		
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 15 + i * 18, 191));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return luna.isUseableByPlayer(playerIn);
	}

}
