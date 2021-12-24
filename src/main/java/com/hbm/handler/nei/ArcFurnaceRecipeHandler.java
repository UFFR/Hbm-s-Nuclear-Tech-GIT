package com.hbm.handler.nei;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.gui.GUIMachineArcFurnace;
import com.hbm.inventory.recipes.MachineRecipes;
import com.hbm.items.ModItems;
import com.hbm.lib.RefStrings;
import com.hbm.util.I18nUtil;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;
// TODO what is this??!!
public class ArcFurnaceRecipeHandler extends TemplateRecipeHandler
{
	public static final Item[] electrodes = new Item[2];
	public static final Random rand = new Random();
	static
	{
		electrodes[0] = ModItems.arc_electrode;
		electrodes[1] = ModItems.arc_electrode_desh;
	}
	public class ArcSet extends TemplateRecipeHandler.CachedRecipe
	{
		PositionedStack input;
		PositionedStack output;
		PositionedStack[] electrodeSlots = new PositionedStack[3];

		public ArcSet(ComparableStack comparableStack, ItemStack output)
		{
			comparableStack.stacksize = 1;
			this.input = new PositionedStack(comparableStack.toStack(), 56, 17);
			this.output = new PositionedStack(output, 116, 35);
		}
		
		@Override
		public PositionedStack getResult()
		{
			return output;
		}
		
		@Override
		public List<PositionedStack> getIngredients()
		{
			return Arrays.asList(new PositionedStack[] {input});
		}
		
		@Override
		public List<PositionedStack> getOtherStacks()
		{
			return getCycledIngredients(cycleticks / 48, Arrays.asList(electrodeSlots));
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals("arcsmelting") && getClass() == ArcFurnaceRecipeHandler.class)
		{
			Map<ComparableStack, ItemStack> recipes = MachineRecipes.instance().getArcFurnaceRecipes();
			for (Entry<ComparableStack, ItemStack> recipe : recipes.entrySet())
				this.arecipes.add(new ArcSet(recipe.getKey(), recipe.getValue()));
		}
		else
			super.loadCraftingRecipes(outputId, results);
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		Map<ComparableStack, ItemStack> recipes = MachineRecipes.instance().getArcFurnaceRecipes();
		for (Entry<ComparableStack, ItemStack> recipe : recipes.entrySet())
			arecipes.add(new ArcSet(recipe.getKey(), recipe.getValue()));
	}
	
	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients)
	{
		if (inputId.equals("arcsmelting") && getClass() == ArcFurnaceRecipeHandler.class)
			loadCraftingRecipes("arcsmelting", new Object[0]);
		else
			super.loadUsageRecipes(inputId, ingredients);
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		Map<ComparableStack, ItemStack> recipes = MachineRecipes.instance().getArcFurnaceRecipes();
		for (Entry<ComparableStack, ItemStack> recipe : recipes.entrySet())
			if (NEIServerUtils.areStacksSameType(recipe.getKey().toStack(), ingredient))
				this.arecipes.add(new ArcSet(recipe.getKey(), recipe.getValue()));
	}
		
	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GUIMachineArcFurnace.class;
	}
	
	public Item getElectrode()
	{
		return electrodes[rand.nextInt(1)];
	}
	
	@Override
	public String getRecipeName()
	{
		return I18nUtil.resolveKey("container.arcFurnace");
	}

	@Override
	public String getGuiTexture()
	{
		return RefStrings.MODID + ":textures/gui/nei/gui_nei_arc.png";
	}

}
