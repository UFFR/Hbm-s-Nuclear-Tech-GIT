package com.hbm.blocks.test;

import com.hbm.blocks.machine.BlockBasicMachine;
import com.hbm.handler.EnumGUI;
import com.hbm.interfaces.IBomb;
import com.hbm.items.ModItems;
import com.hbm.lib.Library;
import com.hbm.packet.AuxButtonPacket;
import com.hbm.packet.NBTControlPacket;
import com.hbm.packet.PacketDispatcher;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TestMachine extends BlockBasicMachine implements IBomb
{
	
	public TestMachine(Material mat, Class<? extends TileEntity> teClassIn, EnumGUI gui)
	{
		super(mat, teClassIn, gui);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (Library.checkForHeld(player, ModItems.designator_range) || Library.checkForHeld(player, ModItems.detonator_laser))
			PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(player.getHeldItem().getTagCompound(), x, y, z));
		
		return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
	}
	
	@Override
	public BombReturnCode explode(World world, int x, int y, int z)
	{
		PacketDispatcher.wrapper.sendToServer(new AuxButtonPacket(x, y, z, 1, 0));
		return BombReturnCode.DETONATED;
	}
}
