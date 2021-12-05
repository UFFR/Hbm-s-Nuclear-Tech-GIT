package com.hbm.blocks.generic;

import com.hbm.interfaces.IBlockRarity;
import com.hbm.blocks.BlockBase;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockGeneric extends BlockBase implements IBlockRarity
{


	public BlockGeneric(Material material) {
		super(material);
	}
	
}
