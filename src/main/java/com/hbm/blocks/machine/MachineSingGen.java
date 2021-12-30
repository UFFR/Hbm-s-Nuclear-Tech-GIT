package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.handler.EnumGUI;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntitySingGen;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MachineSingGen extends BlockDummyable
{
	public MachineSingGen(Material mat)
	{
		super(mat);
	}
	
	@Override
	protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o)
	{
		super.fillSpace(world, x, y, z, dir, o);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;
		else if (!player.isSneaking())
		{
			int[] pos = findCore(world, x, y, z);
			if (pos == null)
				return false;
			FMLNetworkHandler.openGui(player, MainRegistry.instance, EnumGUI.SING_GEN.ordinal(), world, pos[0], pos[1], pos[2]);
			return true;
		}
		else
			return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if (meta >= 12)
			return new TileEntitySingGen();
		else if (meta >= 6)
			return new TileEntityProxyCombo(false, true, true);
		else
			return null;
	}

	@Override
	public int[] getDimensions()
	{
		return new int[] {9, 0, 6, 6, 6, 6};
	}

	@Override
	public int getOffset()
	{
		return 6;
	}

}
