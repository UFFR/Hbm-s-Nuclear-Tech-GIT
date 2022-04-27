package com.hbm.items.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.generic.RedBarrel;
import com.hbm.blocks.machine.BlockFluidBarrel;
import com.hbm.interfaces.IHasLore;
import com.hbm.util.I18nUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockLore extends ItemBlock implements IHasLore
{
	@Deprecated
	public static HashSet<Block> epicList = new HashSet<Block>();
	@Deprecated
	public static HashSet<Block> rareList = new HashSet<Block>();
	@Deprecated
	public static HashSet<Block> uncommonList = new HashSet<Block>();
	public final static HashMap<Block, EnumRarity> formattingMap = new HashMap<Block, EnumRarity>();
	
	public ItemBlockLore(Block p_i45328_1_) {
		super(p_i45328_1_);
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool)
	{
		standardLore(itemstack, list);
		
		if(this.field_150939_a instanceof RedBarrel)
			list.add(I18nUtil.resolveKey("desc.block.barrel.static"));
		
		if (this.field_150939_a instanceof BlockFluidBarrel)
			list.addAll(((BlockFluidBarrel)this.field_150939_a).tooltip);
		
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		if (this.field_150939_a == ModBlocks.gravel_diamond)
			return EnumRarity.rare;

		
		return formattingMap.containsKey(field_150939_a) ? formattingMap.get(field_150939_a) : EnumRarity.common; 
		
		
//		if(this.field_150939_a == ModBlocks.block_euphemium || this.field_150939_a == ModBlocks.block_euphemium_cluster ||
//				this.field_150939_a == ModBlocks.plasma || this.field_150939_a == ModBlocks.fwatz_plasma)
//			return EnumRarity.epic;

	}

}
