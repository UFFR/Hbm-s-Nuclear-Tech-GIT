package com.hbm.inventory.recipes;

import static com.hbm.inventory.OreDictManager.*;

import java.util.HashMap;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon.*;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemBreedingRod.BreedingRodType;
import com.hbm.saveddata.TimeSavedData.TimeDurationType;
import com.hbm.tileentity.IRadioisotopeFuel;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class BetavoltaicFuels 
{
	public static final HashMap<AStack, IRadioisotopeFuel> fuelMap = new HashMap<>();
	
//	private static final int thaLife = 48000 * 40;
	private static final short thaPower = 700;
	private static final int sr90Life = 48000 * 100 * 10;
	private static final short sr90Power = 500;
	private static final short sa327Power = 300;
	private static final int lifeLong = 2 * 60 * 60 * 20;
	private static final int lifeShort = 30 * 60 * 20;
	static
	{
		addFuel(ModItems.ingot_tha, new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return thaPower;}
			@Override public long getMaxLifespan(){return lifeShort;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.ingot_thorium_fuel);}
		});
		addFuel(ModItems.nugget_tha, new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return thaPower / 10;}
			@Override public long getMaxLifespan(){return lifeShort;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.nugget_thorium_fuel);}
		});
		addFuel(ModItems.billet_tha, new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return thaPower;}// Intentional, my reasoning is that billets have greater surface area, therefore a larger amount of beta particles being emitted from that area. Either that or I'm going crazy
			@Override public long getMaxLifespan(){return lifeShort;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.billet_thorium_fuel);}
		});
		addFuel(Item.getItemFromBlock(ModBlocks.block_tha), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return thaPower * 10;}
			@Override public long getMaxLifespan(){return lifeShort;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModBlocks.block_thorium_fuel);}
		});
		addFuel(ModItems.rod, BreedingRodType.THA.ordinal(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return thaPower;}
			@Override public long getMaxLifespan(){return lifeShort;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.rod, 1, BreedingRodType.THF.ordinal());}
		});
		addFuel(ModItems.rod_dual, BreedingRodType.THA.ordinal(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return thaPower * 2;}
			@Override public long getMaxLifespan(){return lifeShort;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.rod_dual, 1, BreedingRodType.THF.ordinal());}
		});
		addFuel(ModItems.rod_quad, BreedingRodType.THA.ordinal(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return thaPower * 4;}
			@Override public long getMaxLifespan(){return lifeShort;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.rod_quad, 1, BreedingRodType.THF.ordinal());}
		});
		addFuel(SR90.dust(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sr90Power;}
			@Override public long getMaxLifespan(){return sr90Life;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.powder_zirconium);}
		});
		addFuel(SR90.ingot(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sr90Power;}
			@Override public long getMaxLifespan(){return sr90Life;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.ingot_zirconium);}
		});
		addFuel(SR90.dustTiny(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sr90Power / 10;}
			@Override public long getMaxLifespan(){return sr90Life;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.nugget_zirconium);}
		});
		addFuel(SR90.nugget(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sr90Power / 10;}
			@Override public long getMaxLifespan(){return sr90Life;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.nugget_zirconium);}
		});
		addFuel(SR90.dustTiny(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sr90Power / 10;}
			@Override public long getMaxLifespan(){return sr90Life;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.nugget_zirconium);}
		});
		addFuel(SR90.billet(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sr90Power / 10;}
			@Override public long getMaxLifespan(){return sr90Life;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.billet_zirconium);}
		});
		addFuel(ModItems.cell_tritium, new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return 10;}
			@Override public long getMaxLifespan(){return 48000 * 100 * 5;}
			@Override public boolean getDoesDecay(){return false;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.cell_empty);}
		});
		addFuel(ModItems.ingot_solinium, new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sa327Power;}
			@Override public long getMaxLifespan(){return lifeLong;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.ingot_pu239);}
		});
		addFuel(ModItems.billet_solinium, new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sa327Power;}
			@Override public long getMaxLifespan(){return lifeLong;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.billet_pu239);}

		});
		addFuel(ModItems.nugget_solinium, new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sa327Power / 10;}
			@Override public long getMaxLifespan(){return lifeLong;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.nugget_pu239);}

		});
		addFuel(TC99.ingot(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return 100;}
			@Override public long getMaxLifespan(){return lifeLong;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return null;}
		});
		addFuel(TC99.billet(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return 100;}
			@Override public long getMaxLifespan(){return lifeLong;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return null;}
		});
		addFuel(TC99.nugget(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return 10;}
			@Override public long getMaxLifespan(){return lifeLong;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return null;}
		});
		addFuel(BK249.ingot(), new IRadioisotopeFuel() {
			
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return 150;}
			@Override public long getMaxLifespan(){return IRadioisotopeFuel.getLifespan(330, TimeDurationType.SHORT) * 2;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return /*new ItemStack(ModItems.ingot_cf249)*/ null;}
		});
		addFuel(CS137.dust(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sr90Power;}
			@Override public long getMaxLifespan(){return IRadioisotopeFuel.getLifespan(30, TimeDurationType.MEDIUM) * 2;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.powder_zirconium);}
		});
		addFuel(CS137.dustTiny(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan){return this;}
			@Override public short getHeat(){return sr90Power / 10;}
			@Override public long getMaxLifespan(){return IRadioisotopeFuel.getLifespan(30, TimeDurationType.MEDIUM) * 2;}
			@Override public boolean getDoesDecay(){return true;}
			@Override public ItemStack getDecayItem(){return new ItemStack(ModItems.nugget_zirconium);}
		});
		addFuel(PA233.ingot(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan) {return this;}
			@Override public long getMaxLifespan() {return lifeShort;}
			@Override public short getHeat() {return thaPower * 3;}
			@Override public boolean getDoesDecay() {return true;}
			@Override public ItemStack getDecayItem() {return new ItemStack(ModItems.ingot_u233);}
		});
		addFuel(PA233.nugget(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan) {return this;}
			@Override public long getMaxLifespan() {return lifeShort;}
			@Override public short getHeat() {return (thaPower / 10) * 3;}
			@Override public boolean getDoesDecay() {return true;}
			@Override public ItemStack getDecayItem() {return new ItemStack(ModItems.nugget_u233);}
		});
		addFuel(PA233.billet(), new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan) {return this;}
			@Override public long getMaxLifespan() {return lifeShort;}
			@Override public short getHeat() {return thaPower * 3;}
			@Override public boolean getDoesDecay() {return true;}
			@Override public ItemStack getDecayItem() {return new ItemStack(ModItems.billet_u233);}
		});
		addFuel(ModItems.rod_zirnox_tha, new IRadioisotopeFuel()
		{
			@Override public IRadioisotopeFuel setDecays(ItemStack stack, long lifespan) {return this;}
			@Override public long getMaxLifespan() {return lifeShort;}
			@Override public short getHeat() {return thaPower * 2;}
			@Override public boolean getDoesDecay() {return true;}
			@Override public ItemStack getDecayItem() {return new ItemStack(ModItems.rod_zirnox_thorium_fuel);}
		});
	}
	private static void addFuel(Item fuel, IRadioisotopeFuel data)
	{
		fuelMap.put(new ComparableStack(fuel), data);
	}
	private static void addFuel(Item fuel, int meta, IRadioisotopeFuel data)
	{
		fuelMap.put(new ComparableStack(fuel, 1, meta), data);
	}
	private static void addFuel(String ore, IRadioisotopeFuel data)
	{
		fuelMap.put(new OreDictStack(ore), data);
	}
	public static Set<AStack> getFuels()
	{
		return ImmutableSet.copyOf(fuelMap.keySet());
	}
}
