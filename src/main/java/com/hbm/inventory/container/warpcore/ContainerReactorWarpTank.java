package com.hbm.inventory.container.warpcore;

import com.hbm.tileentity.machine.TileEntityReactorWarp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerReactorWarpTank extends Container
{
	private final TileEntityReactorWarp core;
	public ContainerReactorWarpTank(InventoryPlayer invPlayer, TileEntityReactorWarp te)
	{
		core = te;
		
		for (int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18 + 40, 84 + i * 18 + 21));
		
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 48 + i * 18, 163));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return core.isUseableByPlayer(player);
	}

}
