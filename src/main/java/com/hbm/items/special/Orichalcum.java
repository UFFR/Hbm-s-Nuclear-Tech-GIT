package com.hbm.items.special;

import net.minecraft.item.ItemStack;

// Why do I have this class again?
public final class Orichalcum extends ItemWithSubtypes
{
	public static final byte INGOT = 0;
	public static final byte GEM = 1;
	public static final byte INGOT_SMALL = 2;
	public static final byte POWDER = 3;
	public static final byte INGOT_SMALL_IRR = 4;
	public static final byte PLATE = 5;
	public static final byte FRAGMENT = 6;
	public static final byte INGOT_FINSIHED = 7;
	public Orichalcum()
	{
		super("ingot_orichalcum", "gem_orichalcum", "ingot_orichalcum_small", "powder_orichalcum_mix", "ingot_orichalcum_small_irr", "plate_orichalcum", "fragment_orichalcum", "ingot_orichalcum_finished");
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return stack.getItemDamage() == INGOT_FINSIHED;
	}
}
