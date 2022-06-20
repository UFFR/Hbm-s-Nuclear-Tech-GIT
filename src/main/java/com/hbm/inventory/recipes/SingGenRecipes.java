package com.hbm.inventory.recipes;

import static com.hbm.inventory.OreDictManager.*;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.Nonnegative;

import com.hbm.blocks.ModBlocks;
import com.hbm.interfaces.Spaghetti;
import com.hbm.inventory.RecipesCommon.*;
import com.hbm.inventory.container.ContainerSingGen;
import com.hbm.inventory.fluid.*;
import com.hbm.items.ModItems;
import com.hbm.items.special.Orichalcum;

import api.hbm.computer.Researchable;
import api.hbm.energy.IBatteryItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;
@Spaghetti("Is there a more efficient way of doing this? Probably")
public class SingGenRecipes
{
	public static final ArrayList<SingGenRecipe> recipes = new ArrayList<SingGenRecipes.SingGenRecipe>();
	/* -- Input Slots --
	 * 5  9  6
	 * 11 4  12
	 * 7  10 8
	 */
	// Output:{<Item(s) out>}, Input:{<corner>, <side>, <corner>, <side>, <side>, <corner>, <side>, <corner>, <center>, Shaped?, Keep shell?, Fluid, Fluid amount}
	public static void register()
	{
		recipes.add(commonFluid(new ItemStack(ModItems.singularity, 1), ModItems.nugget_euphemium, Fluids.ASCHRAB, new ComparableStack(ModBlocks.block_schrabidium), 4000).setLevel(1));
		recipes.add(commonStandard(new ItemStack(ModItems.singularity_super_heated, 1), ModItems.plate_advanced_alloy, ModItems.powder_power, new ComparableStack(ModItems.singularity)).setLevel(2));
		recipes.add(commonStandard(new ItemStack(ModItems.singularity_counter_resonant, 1), ModItems.plate_combine_steel, ModItems.ingot_magnetized_tungsten, new ComparableStack(ModItems.singularity)).setLevel(3));
		recipes.add(addRecipe(new ItemStack(ModItems.black_hole, 1), new AStack[]
				{
					new ComparableStack(ModItems.crystal_xen), new ComparableStack(ModItems.crystal_xen), new ComparableStack(ModItems.crystal_xen),
					new ComparableStack(ModItems.crystal_xen), new ComparableStack(ModItems.crystal_xen),
					new ComparableStack(ModItems.crystal_xen), new ComparableStack(ModItems.crystal_xen), new ComparableStack(ModItems.crystal_xen)
				}, new ComparableStack(ModItems.singularity)).setLevel(4));
		recipes.add(addRecipe(new ItemStack(ModItems.overfuse, 1), new ComparableStack[]
				{
					new ComparableStack(ModItems.screwdriver_chad), new ComparableStack(ModItems.board_copper), new ComparableStack(ModItems.powder_nitan_mix),
					new ComparableStack(ModItems.powder_nitan_mix), new ComparableStack(ModItems.powder_nitan_mix),
					new ComparableStack(ModItems.powder_nitan_mix), new ComparableStack(ModItems.powder_nitan_mix), new ComparableStack(ModItems.powder_nitan_mix)
				}, new ComparableStack(ModItems.black_hole)).setLevel(5).setShapeless());
		recipes.add(addRecipe(new ItemStack(ModItems.overfuse, 1), new AStack[]
				{
					new ComparableStack(ModItems.screwdriver_chad), new ComparableStack(ModItems.board_copper), new OreDictStack(NP237.dust()),
					new OreDictStack(I.dust()), new OreDictStack(TH232.dust()),
					new OreDictStack(AT.dust()), new OreDictStack(ND.dust()), new OreDictStack(CS.dust())
				}, new ComparableStack(ModItems.black_hole)).setLevel(5).setShapeless());
		recipes.add(addRecipe(new ItemStack(ModItems.overfuse, 1), new AStack[]
				{
					new ComparableStack(ModItems.screwdriver_chad), new ComparableStack(ModItems.board_copper), new OreDictStack(ST.dust()),
					new OreDictStack(BR.dust()), new OreDictStack(CO.dust()),
					new OreDictStack(TS.dust()), new OreDictStack(NB.dust()), new OreDictStack(CE.dust())
				}, new ComparableStack(ModItems.black_hole)).setLevel(5).setShapeless());
		recipes.add(addRecipe(new ItemStack(ModItems.singularity_spark, 1), new ComparableStack[]
				{
					new ComparableStack(ModItems.plate_dineutronium), new ComparableStack(ModItems.singularity_super_heated), new ComparableStack(ModItems.plate_dineutronium),
					new ComparableStack(ModItems.singularity_counter_resonant), new ComparableStack(ModItems.singularity_counter_resonant),
					new ComparableStack(ModItems.plate_dineutronium), new ComparableStack(ModItems.singularity_super_heated), new ComparableStack(ModItems.plate_dineutronium)
				}, new ComparableStack(ModItems.black_hole)).setLevel(5));
		recipes.add(addRecipe(new ItemStack(ModItems.singularity_spark, 1), new ComparableStack[]
				{
					new ComparableStack(ModItems.plate_dineutronium), new ComparableStack(ModItems.singularity_counter_resonant), new ComparableStack(ModItems.plate_dineutronium),
					new ComparableStack(ModItems.singularity_super_heated), new ComparableStack(ModItems.singularity_super_heated),
					new ComparableStack(ModItems.plate_dineutronium), new ComparableStack(ModItems.singularity_counter_resonant), new ComparableStack(ModItems.plate_dineutronium)
				}, new ComparableStack(ModItems.black_hole)).setLevel(5));
		recipes.add(commonFluid(new ItemStack(ModItems.ams_core_sing, 1), ModItems.plate_euphemium, Fluids.ASCHRAB, new ComparableStack(ModItems.singularity), 4000).setLevel(5));
		recipes.add(commonStandard(new ItemStack(ModItems.ams_core_wormhole, 1), ModItems.plate_dineutronium, ModItems.powder_spark_mix, new ComparableStack(ModItems.singularity)).setLevel(5));
		recipes.add(commonFluid(new ItemStack(ModItems.ams_core_eyeofharmony, 1), ModItems.plate_dalekanium, Fluids.PLASMA_WARP, new ComparableStack(ModItems.black_hole), 64000).setLevel(5));
		recipes.add(new SingGenRecipe(new ItemStack(ModItems.singularity_micro, 8), new AStack[]
				{
					new ComparableStack(ModItems.nugget_euphemium), new ComparableStack(ModItems.catalyst_rare), new ComparableStack(ModItems.nugget_euphemium),
					new ComparableStack(ModItems.catalyst_rare), new ComparableStack(ModItems.catalyst_rare),
					new ComparableStack(ModItems.nugget_euphemium), new ComparableStack(ModItems.catalyst_rare), new ComparableStack(ModItems.nugget_euphemium)
				}, new ComparableStack(ModItems.ingot_schrabidium)).setFluid(Fluids.ASCHRAB, 1000).setKeepRing());
		recipes.add(addRecipe(new ItemStack(ModItems.ingot_dineutronium, 1), new ComparableStack[]
				{
					new ComparableStack(ModBlocks.block_starmetal), new ComparableStack(ModItems.powder_spark_mix), new ComparableStack(ModBlocks.block_starmetal),
					new ComparableStack(ModItems.powder_spark_mix), new ComparableStack(ModItems.powder_spark_mix),
					new ComparableStack(ModBlocks.block_starmetal), new ComparableStack(ModItems.powder_spark_mix), new ComparableStack(ModBlocks.block_starmetal)
				}, new ComparableStack(ModBlocks.block_schrabidate), true, false, Fluids.ASCHRAB, 4000).setLevel(1));
		recipes.add(commonStandard(IBatteryItem.emptyBattery(ModItems.battery_fast_2_advanced), ModItems.ingot_schrabidate, ModItems.powder_tennessine, new ComparableStack(ModItems.battery_fast_2_enhanced)).setLevel(2));
		recipes.add(addRecipe(IBatteryItem.emptyBattery(ModItems.battery_fast_2_elite), new ComparableStack[]
				{
					new ComparableStack(ModItems.powder_spark_mix), new ComparableStack(ModItems.plate_dineutronium), new ComparableStack(ModItems.powder_spark_mix),
					new ComparableStack(ModItems.plate_euphemium), new ComparableStack(ModItems.plate_euphemium),
					new ComparableStack(ModItems.powder_spark_mix), new ComparableStack(ModItems.plate_dineutronium), new ComparableStack(ModItems.powder_spark_mix)
				}, new ComparableStack(ModItems.battery_fast_2_advanced)).setLevel(3).setFluid(Fluids.PLASMA_WARP, 2000));
		recipes.add(addRecipe(IBatteryItem.emptyBattery(ModItems.battery_fast_2_elite), new ComparableStack[]
				{
					new ComparableStack(ModItems.powder_spark_mix), new ComparableStack(ModItems.plate_euphemium), new ComparableStack(ModItems.powder_spark_mix),
					new ComparableStack(ModItems.plate_dineutronium), new ComparableStack(ModItems.plate_dineutronium),
					new ComparableStack(ModItems.powder_spark_mix), new ComparableStack(ModItems.plate_euphemium), new ComparableStack(ModItems.powder_spark_mix)
				}, new ComparableStack(ModItems.battery_fast_2_advanced)).setLevel(3).setFluid(Fluids.ASCHRAB, 2000));
		recipes.add(addRecipe(new ItemStack(ModItems.orichalcum, 1, Orichalcum.PLATE), new ComparableStack[]
				{
						new ComparableStack(ModItems.billet_yharonite), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.INGOT_SMALL_IRR), new ComparableStack(ModItems.billet_yharonite),
						new ComparableStack(ModItems.orichalcum, 1, Orichalcum.INGOT_SMALL_IRR), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.INGOT_SMALL_IRR),
						new ComparableStack(ModItems.billet_yharonite), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.INGOT_SMALL_IRR), new ComparableStack(ModItems.billet_yharonite)
				}, new OreDictStack(TA.block())).setLevel(4).setFluid(Fluids.PLASMA_WARP, 32000));
		recipes.add(addRecipe(new ItemStack(ModItems.orichalcum, 1, Orichalcum.INGOT_FINSIHED), new ComparableStack[]
				{
						new ComparableStack(ModItems.orichalcum, 1, Orichalcum.GEM), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.PLATE), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.GEM),
						new ComparableStack(ModItems.orichalcum, 1, Orichalcum.PLATE), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.PLATE),
						new ComparableStack(ModItems.orichalcum, 1, Orichalcum.GEM), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.PLATE), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.GEM)
				}, new ComparableStack(ModItems.orichalcum, 1, Orichalcum.INGOT)).setLevel(5).setFluid(Fluids.ASCHRAB, 4000));
		recipes.add(addRecipe(new ItemStack(ModItems.orichalcum, 1, Orichalcum.GEM), new ComparableStack[]
				{
						new ComparableStack(ModItems.ingot_bismuth), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.FRAGMENT), new ComparableStack(ModItems.ingot_bismuth),
						new ComparableStack(ModItems.orichalcum, 1, Orichalcum.FRAGMENT), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.FRAGMENT),
						new ComparableStack(ModItems.ingot_bismuth), new ComparableStack(ModItems.orichalcum, 1, Orichalcum.FRAGMENT), new ComparableStack(ModItems.ingot_bismuth)
				}, new ComparableStack(ModItems.crystal_trixite)).setLevel(3).setFluid(Fluids.PLASMA_WARP, 8000));
	}
	/**
	 * Standard recipe for upgrading singularities
	 * @param output - The resultant item output
	 * @param containmentItem - Item used for upgrading containment, goes in the corners
	 * @param modifierItem - Item used for modifying the item, goes in the sides
	 * @param centerItem - Item to be upgraded, goes in the center
	 */
	private static SingGenRecipe commonStandard(ItemStack output, Item containmentItem, Item modifierItem, AStack centerItem)
	{
		return addRecipe(output, new ComparableStack[] {new ComparableStack(containmentItem), new ComparableStack(modifierItem), new ComparableStack(containmentItem), new ComparableStack(modifierItem), new ComparableStack(modifierItem), new ComparableStack(containmentItem),  new ComparableStack(modifierItem), new ComparableStack(containmentItem)}, centerItem, true, false, Fluids.NONE, 0);
	}
	/**
	 * Standard recipe type
	 * @param output - The resultant item
	 * @param containmentItem - Item used for containment of the item, goes in the corners
	 * @param fluid - The fluid type used, must be Antischrabidium or warp plasma
	 * @param centerItem - The item that goes in the center
	 * @param fluidAmount - The amount of fluid needed, max 64b
	 */
	private static SingGenRecipe commonFluid(ItemStack output, Item containmentItem, FluidType fluid, AStack centerItem, int fluidAmount)
	{
		return addRecipe(output, new ComparableStack[] {new ComparableStack(containmentItem), null, new ComparableStack(containmentItem), null, null, new ComparableStack(containmentItem), null, new ComparableStack(containmentItem)}, centerItem, true, false, fluid, fluidAmount);
	}
	/**
	 * Add a recipe directly
	 * @param output - The output, may be more than 1
	 * @param inputRing - The ring of 8 items around the center
	 * @param inputCenter - The center item
	 * @param shaped - Does placement of items in the ring matter?
	 * @param keepRing - Do the items in the ring persist?
	 * @param fluid - A fluid type, use NONE if it doesn't use any
	 * @param fluidAmount - Amount of fluid needed, max 64b
	 */
	private static SingGenRecipe addRecipe(ItemStack output, AStack[] inputRing, AStack inputCenter, boolean shaped, boolean keepRing, FluidType fluid, @Nonnegative int fluidAmount)
	{
		// Error checking
		if (inputRing.length != 8)
			throw new IllegalArgumentException("Recipe ring input must be exactly 8! Recipe output: " + output.getUnlocalizedName() + "; Ring length: " + inputRing.length);// So we know which recipe errored and why
		if (fluidAmount > 64000 || fluidAmount < 0)
			throw new IndexOutOfBoundsException("Recipe fluid input amount out of bounds, must be between 0 and 64000! Recipe output: " + output.getUnlocalizedName() + "; Fluid amount attempted: " + fluidAmount);

		return new SingGenRecipe(output, inputRing, inputCenter, shaped, keepRing, fluid, fluidAmount);
	}
	
	private static SingGenRecipe addRecipe(ItemStack output, AStack[] inputRing, AStack inputCenter)
	{
		return addRecipe(output, inputRing, inputCenter, true, false, Fluids.NONE, 0);
	}
	
	/**
	 * Updated version, can basically handle everything in theory, not quite finished yet
	 * @param machineRing - The ring that came from the machine
	 * @param recipe - The currently checked recipe
	 * @param center - Machine's center item
	 * @param fluidIn - Machine's fluid type
	 * @return If the recipe is valid with the contents of the machine
	 */
	public static boolean isRecipeValid(ItemStack[] machineRing, SingGenRecipe recipe, ItemStack center, FluidType fluidIn)
	{
		if (!(recipe.fluid.equals(fluidIn) || recipe.fluid.equals(Fluids.NONE)))
			return false;
		if ((center == null && recipe.inputCenter != null) || (center != null && recipe.inputCenter == null))
			return false;
		else if (!isItemValid(center, recipe.inputCenter))
			return false;
		
		if (recipe.shaped)
		{
			for (int i = 0; i < 8; i++)
				if (!isItemValid(machineRing[i], recipe.inputRing[i]))
					return false;
		}
		else
		{
			ArrayList<AStack> recipeStacks = new ArrayList<AStack>(Arrays.asList(recipe.inputRing));
//			ArrayList<ItemStack> machineStacks = new ArrayList<ItemStack>(Arrays.asList(machineRing));
			for (ItemStack mStack : machineRing)
			{
				boolean inRecipe = false;
				Iterator<AStack> recipeIterator = recipeStacks.iterator();
				while (recipeIterator.hasNext())
				{
					AStack rStack = recipeIterator.next();
					if (isItemValid(mStack, rStack))
					{
						inRecipe = true;
						recipeStacks.remove(rStack);
						break;
					}
				}
				if (!inRecipe)
					return false;
			}
			return recipeStacks.isEmpty();
		}
		
		return true;
	}
	/**
	 * Check if an item reasonably matches one in a recipe 
	 * @param stackIn - The itemstack that will be checked
	 * @param recipeIn - The AStack that it will be checked against
	 * @return If it is valid or not
	 */
	public static boolean isItemValid(ItemStack stackIn, AStack recipeIn)
	{
		if ((stackIn == null && recipeIn != null) || (stackIn != null && recipeIn == null))
			return false;
		if (stackIn == null && recipeIn == null)
			return true;
		return (recipeIn.matchesRecipe(stackIn, true));
	}
	
	public static class SingGenRecipe extends Researchable
	{
		private static final byte[] corners = {0, 2, 5, 7};
		private static final byte[] sides = {1, 3, 4, 6};
		private ItemStack output;
		public AStack[] inputRing = new AStack[8];
		public AStack inputCenter;
		public boolean shaped = true;
		public boolean keepRing = false;
		public FluidType fluid = Fluids.NONE;
		public int fluidAmount;
		private byte level = 0;
		public SingGenRecipe(ItemStack output, AStack[] inputRing, AStack inputCenter, boolean shaped, boolean keepRing, FluidType fluid, int fluidAmount)
		{
			super(output.getUnlocalizedName());
			if (inputRing.length != 8)
				throw new IllegalArgumentException("Recipe ring input must be exactly 8! Recipe output: " + output.getUnlocalizedName() + "; Ring length: " + inputRing.length);// So we know which recipe errored and why
			if (fluidAmount > 64000 || fluidAmount < 0)
				throw new IndexOutOfBoundsException("Recipe fluid input amount out of bounds, must be between 0 and 64000! Recipe output: " + output.getUnlocalizedName() + "; Fluid amount attempted: " + fluidAmount);

			this.output = output;
			this.inputRing = inputRing;
			this.inputCenter = inputCenter;
			this.shaped = shaped;
			this.keepRing = keepRing;
			this.fluid = fluid;
			this.fluidAmount = fluidAmount;
			// Why?
			ContainerSingGen.centerItems.add(inputCenter);
			if (shaped)
			{
				for (byte i = 0; i < 4; i++)
				{
					if (inputRing[corners[i]] == null)
						continue;
					else
						ContainerSingGen.cornerItems.add(inputRing[corners[i]]);
				}
				for (byte i = 0; i < 4; i++)
				{
					if (inputRing[sides[i]] == null)
						continue;
					else
						ContainerSingGen.sideItems.add(inputRing[sides[i]]);
				}
			}
			else
				for (AStack stack : inputRing)
					if (stack == null) continue; else ContainerSingGen.anyItems.add(stack);
		}
		public SingGenRecipe(ItemStack output, AStack[] inputRing, AStack inputCenter)
		{
			this(output, inputRing, inputCenter, true, false, Fluids.NONE, 0);
		}
		public SingGenRecipe setShapeless()
		{
			shaped = false;
			return this;
		}
		public SingGenRecipe setKeepRing()
		{
			keepRing = true;
			return this;
		}
		/**
		 * To set the recipe "level", purely decorative
		 * @param levelIn The desired level
		 * @return Itself
		 * @throws IndexOutOfBoundsException If the input is above 5
		 */
		public SingGenRecipe setLevel(@Nonnegative int levelIn) throws IndexOutOfBoundsException
		{
			if (levelIn > 5)
				throw new IndexOutOfBoundsException("Attempted to use number greater than 5! Number attempted: " + levelIn);
			
			level = (byte) levelIn;
			return this;
		}
		/**
		 * Set the recipe to use a fluid
		 * @param fluid The fluid type
		 * @param amount Fluid amount
		 * @return Itself
		 */
		public SingGenRecipe setFluid(FluidType fluid, @Nonnegative int amount)
		{
			this.fluid = fluid;
			this.fluidAmount = amount;
			return this;
		}
		public ItemStack getOutput()
		{
			return output.copy();
		}
		public byte getLevel()
		{
			return level;
		}
		@Override
		public String toString()
		{
			return String.format("Stock toString(): %s; Recipe output: %s; Center input: %s; Ring input: [%s, %s, %s, %s, %s, %s, %s, %s]; Fluid input: %s; Fluid amount: %s; Recipe level: %s", super.toString(), output.getDisplayName(), inputCenter.getFriendlyName(), inputRing.toString(), fluid.getUnlocalizedName(), fluidAmount, level);
		}
		@Override
		public SingGenRecipe setPrevious(Researchable prev)
		{
			return (SingGenRecipe) super.setPrevious(prev);
		}
		@Override
		public SingGenRecipe setWork(long ram, long cpu)
		{
			return (SingGenRecipe) super.setWork(ram, cpu);
		}
	}
}
