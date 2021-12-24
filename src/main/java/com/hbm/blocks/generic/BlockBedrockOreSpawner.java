package com.hbm.blocks.generic;

import com.hbm.explosion.ExplosionLarge;
import com.hbm.interfaces.IBomb;
import com.hbm.items.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockBedrockOreSpawner extends BlockGeneric implements IBomb
{
	private final Block ore;
	public BlockBedrockOreSpawner(Block ore)
	{
		super(Material.rock);
		this.ore = ore;
		setResistance(Float.MAX_VALUE);
		setHardness(Float.MAX_VALUE);
		setBlockUnbreakable();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (player.getItemInUse() != null && player.getItemInUse().getItem() == ModItems.drill_nuclear)
		{
			worldIn.setBlockMetadataWithNotify(x, y, z, 1, 3);
			return true;
		}
		else
			return false;
	}

	
	public Block getOre()
	{
		return ore;
	}

	@Override
	public void explode(World world, int x, int y, int z)
	{
		if (world.getBlockMetadata(x, y, z) == 0)
			return;
		
		for (int bx = x - 10; bx < x + 10; bx++)
			for (int by = y - 10; by < y + 10; by++)
				for (int bz = z - 10; bz < z + 10; bz++)
					if (world.getBlock(bx, by, bz) == Blocks.bedrock)
						world.setBlock(bx, by, bz, getOre());
		ExplosionLarge.jolt(world, x, y + 15, z, 10, 500, 0.75);
	}
}
