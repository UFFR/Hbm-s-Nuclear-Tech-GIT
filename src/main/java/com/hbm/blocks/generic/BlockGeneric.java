package com.hbm.blocks.generic;

import com.hbm.blocks.BlockBase;
import com.hbm.interfaces.IBlockRarity;
import com.hbm.lib.RefStrings;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockGeneric extends BlockBase implements IBlockRarity
{


	public BlockGeneric(Material material) {
		super(material);
	}
	
	@Override
	public Block setBlockName(String name)
	{
		setBlockTextureName(RefStrings.MODID + name);
		return super.setBlockName(name);
	}
}
