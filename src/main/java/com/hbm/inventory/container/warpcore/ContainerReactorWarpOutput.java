package com.hbm.inventory.container.warpcore;

import com.hbm.inventory.SlotMachineOutput;
import com.hbm.tileentity.machine.TileEntityReactorWarp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerReactorWarpOutput extends Container
{
	private final TileEntityReactorWarp core;
	public ContainerReactorWarpOutput(InventoryPlayer invPlayer, TileEntityReactorWarp te)
	{
		core = te;
		
		addSlotToContainer(new Slot(te, 3, 33, 46));
		addSlotToContainer(new SlotMachineOutput(te, 4, 65, 46));
		
		for (byte i = 5; i < 8; i++)
		{
			addSlotToContainer(new Slot(te, i, 97, (i - 5) * 16 + 14));
			addSlotToContainer(new Slot(te, i + 3, 125, (i - 5) * 16 + 14));
		}
		
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
