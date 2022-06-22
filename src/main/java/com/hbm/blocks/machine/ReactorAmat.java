package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.lib.RefStrings;
import com.hbm.tileentity.machine.TileEntityReactorWarp;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ReactorAmat extends BlockDummyable
{

	public ReactorAmat()
	{
		super(Material.iron);
		setBlockName("reactor_warp");
		setResistance(1800);
		setHardness(20);
		setBlockTextureName(RefStrings.MODID + ":machine_alpha");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;
		else if (!player.isSneaking())
		{
//			TileEntityReactorWarp te = (TileEntityReactorWarp) world.getTileEntity(x, y, z);
//			if (te != null)
//				FMLNetworkHandler.openGui(player, MainRegistry.instance, EnumGUI.WARP_CORE.ordinal(), world, x, y, z);
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
