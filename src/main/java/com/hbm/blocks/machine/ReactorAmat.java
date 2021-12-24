package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ModBlocks;
import com.hbm.handler.EnumGUI;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.machine.TileEntityReactorWarp;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ReactorAmat extends BlockDummyable
{

	public ReactorAmat(Material material)
	{
		super(material);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;
		else if (!player.isSneaking())
		{
			TileEntityReactorWarp te = (TileEntityReactorWarp) world.getTileEntity(x, y, z);
			if (te != null)
				FMLNetworkHandler.openGui(player, MainRegistry.instance, EnumGUI.WARP_CORE.ordinal(), world, x, y, z);
			return true;
		}
		else
			return false;
	}
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileEntityReactorWarp();
	}

	@Override
	public int[] getDimensions()
	{
		return new int[] {18, 0, 3, 3, 3, 3};
	}

	@Override
	public int getOffset()
	{
		return 2;
	}
}
