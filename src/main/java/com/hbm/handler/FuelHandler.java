package com.hbm.handler;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

import java.util.HashMap;

import com.hbm.blocks.ModBlocks;
import com.hbm.items.ModItems;
import com.hbm.util.wrappers.HashWrapper;
import com.hbm.util.wrappers.Wrapper;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler {
	
	private static final HashMap<Wrapper<ItemStack>, Integer> FUEL_MAP = new HashMap<Wrapper<ItemStack>, Integer>();
	private static final short single = 200;
	
	static
	{
		registerFuel(ModItems.solid_fuel,	 				single * 16);
		registerFuel(ModItems.solid_fuel_presto,	 		single * 40);
		registerFuel(ModItems.solid_fuel_presto_triplet,	single * 200);
		registerFuel(ModItems.solid_fuel_bf, 				single * 160);
		registerFuel(ModItems.solid_fuel_presto_bf, 		single * 400);
		registerFuel(ModItems.solid_fuel_presto_triplet_bf,	single * 2000);
		registerFuel(ModItems.rocket_fuel, 					single * 32);
		registerFuel(ModItems.biomass, 						single * 4);
		registerFuel(ModItems.biomass_compressed, 			single * 12);
		registerFuel(ModItems.powder_coal, 					single * 8);
		registerFuel(ModItems.scrap, 						single * 4);
		registerFuel(ModItems.dust, 						single * 2);
		registerFuel(ModItems.powder_fire, 					single * 32);
		registerFuel(ModBlocks.block_scrap, 				single * 20);
		registerFuel(ModItems.lignite, 						single * 6);
		registerFuel(ModItems.powder_lignite, 				single * 6);
		registerFuel(ModItems.briquette_lignite, 			single * 8);
		registerFuel(ModItems.coke, 	  WILDCARD_VALUE, 	single * 16);
		registerFuel(ModItems.book_guide, WILDCARD_VALUE, 	single * 4);
		registerFuel(ModItems.coal_infernal, 				single * 32);
		registerFuel(ModItems.powder_sawdust, 				100);
	}
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		return getFuelTime(fuel);
	}
	
	public static void registerFuel(Item item, int fuelTime)
	{
		registerFuel(item, 0, fuelTime);
	}

	public static void registerFuel(Item item, int meta, int fuelTime)
	{
		FUEL_MAP.put(new HashWrapper<ItemStack>(new ItemStack(item, 1, meta), Wrapper::hashItemStack, Wrapper::testItemStack), fuelTime);
	}
	
	public static void registerFuel(Block block, int fuelTime)
	{
		registerFuel(block, 0, fuelTime);
	}
	
	public static void registerFuel(Block block, int meta, int fuelTime)
	{
		FUEL_MAP.put(new HashWrapper<ItemStack>(new ItemStack(block, 1, meta), Wrapper::hashItemStack, Wrapper::testItemStack), fuelTime);
	}
	
	public static int getFuelTime(ItemStack stack)
	{
		return FUEL_MAP.getOrDefault(new HashWrapper<ItemStack>(stack, Wrapper::hashItemStack, Wrapper::testItemStack), 0);
	}
	
}
