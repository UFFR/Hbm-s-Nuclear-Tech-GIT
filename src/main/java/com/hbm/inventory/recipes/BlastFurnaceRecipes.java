package com.hbm.inventory.recipes;

import static com.hbm.inventory.OreDictManager.*;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.CheckForNull;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.GeneralConfig;
import com.hbm.inventory.OreDictManager.DictFrame;
import com.hbm.inventory.RecipesCommon.*;
import com.hbm.items.ModItems;
import com.hbm.util.Tuple.Triplet;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlastFurnaceRecipes
{
	private static final ArrayList<Triplet<Object, Object, ItemStack>> blastFurnaceRecipes = new ArrayList<Triplet<Object,Object,ItemStack>>();
	private static final ArrayList<ItemStack> hiddenRecipes = new ArrayList<ItemStack>();
	private static void addRecipe(Object in1, Object in2, ItemStack out)
	{
		blastFurnaceRecipes.add(new Triplet<Object, Object, ItemStack>(in1, in2, out));
	}
	static
	{
		addRecipe(FE, COAL, new ItemStack(ModItems.ingot_steel, 2));
		addRecipe(CU, REDSTONE, new ItemStack(ModItems.ingot_red_copper, 2));
		addRecipe(STEEL, MINGRADE, new ItemStack(ModItems.ingot_advanced_alloy, 2));
		addRecipe(W, COAL, new ItemStack(ModItems.neutron_reflector, 2));
		addRecipe(ModItems.canister_fuel, "slimeball", new ItemStack(ModItems.canister_napalm));
		addRecipe(STEEL, CO, new ItemStack(ModItems.ingot_dura_steel, 2));
		addRecipe(STEEL, W, new ItemStack(ModItems.ingot_dura_steel, 2));
		addRecipe(STEEL, U238, new ItemStack(ModItems.ingot_ferrouranium));
		addRecipe(TI, U238, new ItemStack(ModItems.ingot_staballoy));
		addRecipe(W, SA326.nugget(), new ItemStack(ModItems.ingot_magnetized_tungsten));
		addRecipe(STEEL, TC99.nugget(), new ItemStack(ModItems.ingot_tcalloy));
		addRecipe(AU.plate(), ModItems.plate_mixed, new ItemStack(ModItems.plate_paa, 2));
		addRecipe(BIGMT, ModItems.powder_meteorite, new ItemStack(ModItems.ingot_starmetal, 2));
		addRecipe(ModItems.silicon_lump, ModItems.coke, new ItemStack(ModItems.ingot_silicon, 4));
		addRecipe("sand", COAL, new ItemStack(ModItems.nugget_silicon, 3));
		addRecipe(NB, BE, new ItemStack(ModItems.ingot_nbbe));
		if (GeneralConfig.enableBabyMode)
			addRecipe(ModItems.canister_empty, COAL, new ItemStack(ModItems.canister_oil));
		addRecipe(CO, ModBlocks.block_meteor, new ItemStack(ModItems.ingot_meteorite));
		addRecipe(ModItems.meteorite_sword_hardened, CO, new ItemStack(ModItems.meteorite_sword_alloyed));
		addRecipe(W, CO, new ItemStack(ModItems.component_ftl, 1, 4));
		addRecipe(DNT, OSMIRIDIUM, new ItemStack(ModItems.ingot_dineutronium_forged));
		addRecipe(ModBlocks.block_meteor, CO, new ItemStack(ModItems.ingot_meteorite));
		addRecipe(ModItems.meteorite_sword_hardened, CO, new ItemStack(ModItems.meteorite_sword_alloyed));
		
		hiddenRecipes.add(new ItemStack(ModItems.meteorite_sword_alloyed));
	}
	@CheckForNull
	public static ItemStack getOutput(ItemStack in1, ItemStack in2)
	{
		for (Triplet<Object, Object, ItemStack> recipe : blastFurnaceRecipes)
		{
			AStack[] recipeItem1 = getRecipeStacks(recipe.getX());
			AStack[] recipeItem2 = getRecipeStacks(recipe.getY());

			if ((doStacksMatch(recipeItem1, in1) && doStacksMatch(recipeItem2, in2)) ||
					(doStacksMatch(recipeItem2, in1) && doStacksMatch(recipeItem1, in2)))
				return recipe.getZ().copy();
			else
				continue;
		}
		return null;
	}
	
	private static boolean doStacksMatch(AStack[] recipe, ItemStack in)
	{
		boolean flag = false;
		byte i = 0;
		while (!flag && i < recipe.length)
		{
			flag = recipe[i].matchesRecipe(in, true);
			i++;
		}
		return flag;
	}
	
	private static AStack[] getRecipeStacks(Object in)
	{
		AStack[] recipeItem1;
		if (in instanceof DictFrame)
		{
			DictFrame recipeItem = (DictFrame) in;
			recipeItem1 = new AStack[] {new OreDictStack(recipeItem.ingot()), new OreDictStack(recipeItem.dust()), new OreDictStack(recipeItem.plate()), new OreDictStack(recipeItem.gem())};
		}
		else if (in instanceof AStack)
			recipeItem1 = new AStack[] {(AStack) in};
		else if (in instanceof String)
			recipeItem1 = new AStack[] {new OreDictStack((String) in)};
		else if (in instanceof Block)
			recipeItem1 = new AStack[] {new ComparableStack((Block) in)};
		else
			recipeItem1 = new AStack[] {new ComparableStack((Item) in)};
		
		return recipeItem1;
	}
	
	public static HashMap<ItemStack[], ItemStack> getRecipesForNEI()
	{
		HashMap<ItemStack[], ItemStack> recipes = new HashMap<>();
		
		for (Triplet<Object, Object, ItemStack> recipe : blastFurnaceRecipes)
			if (!hiddenRecipes.contains(recipe.getZ()))
				recipes.put(new ItemStack[] {getRecipeStacks(recipe.getX())[0].toStack(), getRecipeStacks(recipe.getY())[0].toStack()}, recipe.getZ().copy());
		
		return recipes;
	}
}
