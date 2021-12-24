package com.hbm.blocks.generic;

import java.util.Random;

import com.hbm.blocks.ModBlocks;
import com.hbm.hazard.HazardData;
import com.hbm.interfaces.IItemHazard;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockOutgas extends BlockOre implements IItemHazard {
	
	boolean randomTick;
	int rate;
	boolean onBreak;
	boolean onNeighbour;
	
	HazardData module;
	GasType gType = GasType.NONE;
	public enum GasType
	{
		ASBESTOS,
		COAL,
		RADON,
		RADON_DENSE,
		RADON_TOMB,
		MONOXIDE,
		NONE;
	}

	public BlockOutgas(Material mat, boolean randomTick, int rate, boolean onBreak, GasType type) {
		super(mat);
		this.module = new HazardData();
		this.setTickRandomly(randomTick);
		this.randomTick = randomTick;
		this.rate = rate;
		this.onBreak = onBreak;
		this.onNeighbour = false;
		gType = type;
	}

	public BlockOutgas(Material mat, boolean randomTick, int rate, boolean onBreak, boolean onNeighbour, GasType type) {
		this(mat, randomTick, rate, onBreak, type);
		this.module = new HazardData();
		this.onNeighbour = onNeighbour;
	}

	@Override
	public HazardData getModule() {
		return module;
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return rate;
	}
	
	protected Block getGas() {
		
		switch (gType)
		{
		case ASBESTOS:
			return ModBlocks.gas_asbestos;
		case COAL:
			return ModBlocks.gas_coal;
		case MONOXIDE:
			return ModBlocks.gas_monoxide;
		case RADON:
			return ModBlocks.gas_radon;
		case RADON_DENSE:
			return ModBlocks.gas_radon_dense;
		case RADON_TOMB:
			return ModBlocks.gas_radon_tomb;
		case NONE:
		default:
			return Blocks.air;
		}
	}

	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		
		if(this.randomTick && getGas() == ModBlocks.gas_asbestos) {
			
			if(world.getBlock(x, y + 1, z) == Blocks.air) {
				
				if(world.rand.nextInt(10) == 0)
				world.setBlock(x, y + 1, z, ModBlocks.gas_asbestos);
				
				for(int i = 0; i < 5; i++)
					world.spawnParticle("townaura", x + world.rand.nextFloat(), y + 1.1, z + world.rand.nextFloat(), 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		
		ForgeDirection dir = ForgeDirection.getOrientation(rand.nextInt(6));
		
		if(world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == Blocks.air) {
			world.setBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, getGas());
		}
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
		
		if(onBreak) {
			world.setBlock(x, y, z, getGas());
		}
		
		super.dropBlockAsItemWithChance(world, x, y, z, meta, chance, fortune);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		
		if(onNeighbour) {

			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				
				if(world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == Blocks.air) {
					world.setBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, getGas());
				}
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int i) {
		super.breakBlock(world, x, y, z, block, i);
		
		if(this == ModBlocks.ancient_scrap) {
			for(int ix = -2; ix <= 2; ix++) {
				for(int iy = -2; iy <= 2; iy++) {
					for(int iz = -2; iz <= 2; iz++) {
						
						if(Math.abs(ix + iy + iz) < 5 && Math.abs(ix + iy + iz) > 0 && world.getBlock(x + ix, y + iy, z + iz) == Blocks.air) {
							world.setBlock(x + ix, y + iy, z + iz, this.getGas());
						}
					}
				}
			}
		}
	}

	@Override
	public Block toBlock() {
		// TODO Auto-generated method stub
		return IItemHazard.super.toBlock();
	}

}
