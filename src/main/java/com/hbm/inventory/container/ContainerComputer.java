package com.hbm.inventory.container;

import com.hbm.tileentity.machine.computer.TileEntityComputer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerComputer extends Container
{
	private final TileEntityComputer computer;
	public ContainerComputer(InventoryPlayer invPlayer, TileEntityComputer te)
	{
		computer = te;
	}

	@Override
	public boolean canInteractWith(EntityPlayer p)
	{
		return computer.isUseableByPlayer(p);
	}

}
