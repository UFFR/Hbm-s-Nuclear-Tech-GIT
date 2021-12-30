package com.hbm.inventory.recipes;

import static com.hbm.blocks.ModBlocks.*;
import static com.hbm.items.ModItems.*;
import static com.hbm.inventory.OreDictManager.*;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.google.common.annotations.Beta;
import com.hbm.handler.FluidTypeHandler.FluidType;
import com.hbm.interfaces.IResearchableRecipe;
import com.hbm.interfaces.Untested;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.RecipesCommon.*;
import com.hbm.items.ModItems;
import com.hbm.items.special.ItemWasteLong;
import com.hbm.items.special.ItemWasteShort;
import com.hbm.lib.Library;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
@Untested
@Beta
public class ChemPlantRecipesNT
{
//	public static final HashMap<String, ChemPlantRecipe> recipes = new HashMap<>();
	public static final ArrayList<ChemPlantRecipe> recipes = new ArrayList<ChemPlantRecipesNT.ChemPlantRecipe>();
	public static void registerChemPlantRecipes()
	{
		recipes.add(new ChemPlantRecipe("CONCRETE")
				.setFluidInput(new FluidStack(2000, FluidType.WATER), null)
				.setItemInput(new OreDictStack("sand", 8), new ComparableStack(Blocks.gravel, 8))
				.setItemOutput(new ItemStack(concrete_smooth, 4), new ItemStack(concrete_smooth, 4), new ItemStack(concrete_smooth, 4), new ItemStack(concrete_smooth, 4)));
		recipes.add(new ChemPlantRecipe("LF_BASE", 400)
				.setFluidInput(new FluidStack(8000, FluidType.COOLANT), new FluidStack(1200, FluidType.OXYGEN))
				.setItemInput(new OreDictStack(AL.dust(), 2), new OreDictStack(BR.dust()), new OreDictStack(LI.dust(), 3), new OreDictStack(F.dust(), 3))
				.setFluidOutput(new FluidStack(1000, FluidType.SALT), null));
		recipes.add(new ChemPlantRecipe("LF_U", 200)
				.setFluidInput(new FluidStack(1000, FluidType.SALT), null)
				.setItemInput(new OreDictStack(U235.dust()), new OreDictStack(BE.nugget(), 3))
				.setFluidOutput(new FluidStack(1000, FluidType.SALT_U), null).setIcon("LF"));
		recipes.add(new ChemPlantRecipe("LF_PU", 200)
				.setFluidInput(new FluidStack(1000, FluidType.SALT), null)
				.setItemInput(new OreDictStack(PU239.dust()), new OreDictStack(BE.nugget(), 3))
				.setFluidOutput(new FluidStack(1000, FluidType.SALT_PU), null).setIcon("LF"));
		recipes.add(new ChemPlantRecipe("LF_SA", 300)
				.setFluidInput(new FluidStack(1000, FluidType.SAS3), new FluidStack(100, FluidType.MERCURY))
				.setItemInput(new ComparableStack(powder_nitan_mix, 2), new OreDictStack(BE.billet()))
				.setFluidOutput(new FluidStack(1000, FluidType.SAS3_NIT), null));
		recipes.add(new ChemPlantRecipe("RC_U", 200)
				.setFluidInput(new FluidStack(1000, FluidType.SALT_U_DP), null)
				.setFluidOutput(new FluidStack(1000, FluidType.SALT), null)
				.setItemOutput(new ItemStack(nugget_u235), new ItemStack(nuclear_waste_long_tiny, 4, ItemWasteLong.WasteClass.URANIUM235.ordinal()), new ItemStack(nuclear_waste_short_tiny, 4, ItemWasteShort.WasteClass.URANIUM235.ordinal())).setIcon("LF_RC"));
		recipes.add(new ChemPlantRecipe("RC_PU", 200)
				.setFluidInput(new FluidStack(1000, FluidType.SALT_PU_DP), null)
				.setFluidOutput(new FluidStack(1000, FluidType.SALT), null)
				.setItemOutput(new ItemStack(nugget_pu239), new ItemStack(nuclear_waste_short_tiny, 8, ItemWasteShort.WasteClass.PLUTONIUM239.ordinal())).setIcon("LF_RC"));
		recipes.add(new ChemPlantRecipe("ALCOHOL", 200)
				.setFluidInput(new FluidStack(500, FluidType.PETROLEUM), new FluidStack(2000, FluidType.WATER))
				.setItemInput(new OreDictStack(S.dust(), 2))
				.setFluidOutput(new FluidStack(1000, FluidType.ALCOHOL), null));
		recipes.add(new ChemPlantRecipe("SARIN", 1200)
				.setFluidInput(new FluidStack(2500, FluidType.ALCOHOL), new FluidStack(4000, FluidType.ACID))
				.setItemInput(new OreDictStack(P_RED.dust(), 4), new OreDictStack(F.dust(), 4))
				.setFluidOutput(new FluidStack(1000, FluidType.SARIN), null));
		recipes.add(new ChemPlantRecipe("CF3", 2500)
				.setFluidInput(new FluidStack(24000, FluidType.WATER), null)
				.setItemInput(new OreDictStack(F.dust(), 3))
				.setFluidOutput(new FluidStack(1000, FluidType.WRATH), null));
		
	}
	
	@Untested
	@Beta
	public static class ChemPlantRecipe implements IResearchableRecipe
	{
		public static final ChemPlantRecipe NONE = new ChemPlantRecipe("NONE").setFluidInput(new FluidStack(0, FluidType.NONE), new FluidStack(0, FluidType.NONE)).setItemInput(Library.filledArray(AStack.class, new ComparableStack(ModItems.nothing), 4)).setFluidOutput(new FluidStack(0, FluidType.NONE), new FluidStack(0, FluidType.NONE)).setItemOutput(Library.filledArray(ItemStack.class, new ItemStack(ModItems.nothing), 4));
		public int time = 100;
		public String name = new String();
		public ResourceLocation icon;
		
		public FluidStack[] fIn = new FluidStack[2];
		public AStack[] inputs = new AStack[4];
		
		public FluidStack[] fOut = new FluidStack[2];
		public ItemStack[] outputs = new ItemStack[4];
		public ChemPlantRecipe(String name)
		{
			this.name = name;
			icon = new ResourceLocation("hbm", "chem_icon_".concat(name));
		}
		public ChemPlantRecipe(String name, int dura)
		{
			this(name);
			time = dura;
		}
		public ChemPlantRecipe setFluidInput(FluidStack f1, @Nullable FluidStack f2)
		{
			fIn[0] = f1;
			fIn[1] = f2;
			return this;
		}
		public ChemPlantRecipe setItemInput(AStack...stacks)
		{
			assert stacks.length <= 4 : "Stack array length must be equal or less than 4!";
			
			for (byte i = 0; i < stacks.length; i++)
				inputs[i] = stacks[i];
			
			return this;
		}
		
		public ChemPlantRecipe setFluidOutput(FluidStack f1, @Nullable FluidStack f2)
		{
			fOut[0] = f1;
			fOut[1] = f2;
			return this;
		}
		public ChemPlantRecipe setItemOutput(ItemStack...stacks)
		{
			assert stacks.length <= 4 : "Stack array length must be equal or less than 4!";
			
			for (int i = 0; i < stacks.length && i < 4; i++)
				outputs[i] = Library.carefulCopy(stacks[i]);
			
			return this;
		}
		public ChemPlantRecipe setIcon(String icon)
		{
			this.icon = new ResourceLocation("hbm", "chem_icon_".concat(icon));
			return this;
		}
		@Override
		public String getKey()
		{
			return name;
		}
//		@Override
//		public void writeToNBT(NBTTagCompound nbt)
//		{
//			NBTTagCompound recipe = new NBTTagCompound();
//			recipe.setString("NAME", name);
//			recipe.setString("ICON", icon.toString());
//			recipe.setInteger("DURATION", time);
//			recipe.setString("FIN_1_TYPE", fIn[0].type.toString());
//			recipe.setInteger("FIN_1_FILL", fIn[0].fill);
//		}
//		@Override
//		public IResearchableRecipe readFromNBT(NBTTagCompound nbt)
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
	}
}
