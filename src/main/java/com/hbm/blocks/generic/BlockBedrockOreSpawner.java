package com.hbm.blocks.generic;

import com.hbm.entity.effect.EntityNukeCloudSmall;
import com.hbm.entity.logic.EntityNukeExplosionMK4;
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
		setBlockName(ore.getUnlocalizedName().substring(5).concat("_spawner"));
		setCreativeTab(ore.getCreativeTabToDisplayOn());
		setResistance(Float.MAX_VALUE);
		setHardness(Float.MAX_VALUE);
		setBlockUnbreakable();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ModItems.drill_nuclear && worldIn.getBlockMetadata(x, y, z) == 0)
		{
			player.getCurrentEquippedItem().stackSize--;
			worldIn.playSoundAtEntity(player, "hbm:block.minerOperate", 1, 1);
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
	static final byte range = 4;
	@Override
	public void explode(World world, int x, int y, int z)
	{
		if (world.getBlockMetadata(x, y, z) == 0)
			return;
		
		for (int bx = x - range; bx < x + range; bx++)
			for (int by = y - range; by < y + range; by++)
				for (int bz = z - range; bz < z + range; bz++)
					if (world.getBlock(bx, by, bz) == Blocks.bedrock || world.getBlock(bx, by, bz) == this)
						world.setBlock(bx, by, bz, getOre());
		
		world.spawnEntityInWorld(EntityNukeExplosionMK4.statFac(world, 10, x + 0.5, y + 0.5, z + 0.5));
		world.spawnEntityInWorld(EntityNukeCloudSmall.statFac(world, x, y + 10, z, 50));
	}
}
