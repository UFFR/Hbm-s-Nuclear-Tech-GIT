package com.hbm.inventory.recipes;

import static com.hbm.inventory.OreDictManager.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.hbm.blocks.ModBlocks;
import com.hbm.config.GeneralConfig;
import com.hbm.interfaces.Spaghetti;
import com.hbm.inventory.FluidContainer;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.OreDictManager.DictFrame;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.fluid.*;
import com.hbm.items.ItemEnums.EnumTarType;
import com.hbm.items.ItemEnums.EnumWaferType;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemChemistryTemplate;
import com.hbm.items.machine.ItemFluidIcon;
import com.hbm.items.special.ItemHot;
import com.hbm.items.special.ItemWasteShort;
import com.hbm.items.weapon.ItemGunBase;
import com.hbm.util.EnchantmentUtil;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

//TODO: clean this shit up
@Spaghetti("everything")
public class MachineRecipes
{
	private static final HashMap<ComparableStack, ItemStack> arcFurnaceRecipes = new HashMap<ComparableStack, ItemStack>();
	
	public static ItemStack getArcFurnaceResult(ItemStack stackIn)
	{
		if (stackIn != null)
		{
			ComparableStack comp = new ComparableStack(stackIn.copy()).makeSingular();
			if (arcFurnaceRecipes.containsKey(comp))
				return arcFurnaceRecipes.get(comp).copy();
		}
		return null;
	}

	
	public MachineRecipes() {

	}

	public static MachineRecipes instance() {
		return new MachineRecipes();
	}
	@Deprecated
	public static ItemStack getFurnaceProcessingResult(ItemStack item, ItemStack item2) {
		return getFurnaceOutput(item, item2);
	}
	@Deprecated
	@Spaghetti("i am an affront to god and i desire to be cremated")
	public static ItemStack getFurnaceOutput(ItemStack item, ItemStack item2) {
		
		if(item == null || item2 == null)
			return null;
		
		if (GeneralConfig.enableDebugMode) {
			if (item.getItem() == Items.iron_ingot && item2.getItem() == Items.quartz
					|| item.getItem() == Items.quartz && item2.getItem() == Items.iron_ingot) {
				return new ItemStack(ModBlocks.test_render, 1);
			}
		}

		if (mODE(item, new String[] {"ingotTungsten", "dustTungsten"}) && mODE(item2, "gemCoal")
				|| mODE(item, "gemCoal") && mODE(item2, new String[] {"ingotTungsten", "dustTungsten"})) {
			return new ItemStack(ModItems.neutron_reflector, 2);
		}

		if (mODE(item, new String[] {"ingotIron", "dustIron"}) && mODE(item2, new String[] {"gemCoal", "dustCoal"})
				|| mODE(item, new String[] {"gemCoal", "dustCoal"}) && mODE(item2, new String[] {"ingotIron", "dustIron"})) {
			return new ItemStack(ModItems.ingot_steel, 2);
		}

		if (mODE(item, new String[] {"ingotCopper", "dustCopper"}) && item2.getItem() == Items.redstone
				|| item.getItem() == Items.redstone && mODE(item2, new String[] {"ingotCopper", "dustCopper"})) {
			return new ItemStack(ModItems.ingot_red_copper, 2);
		}

		if (item.getItem() == ModItems.canister_full && item.getItemDamage() == Fluids.DIESEL.getID() && item2.getItem() == Items.slime_ball
				|| item.getItem() == Items.slime_ball && item2.getItem() == ModItems.canister_full && item2.getItemDamage() == Fluids.DIESEL.getID()) {
			return new ItemStack(ModItems.canister_napalm, 1);
		}

		if (mODE(item, new String[] {"ingotMingrade", "dustMingrade"}) && mODE(item2, new String[] {"ingotSteel", "dustSteel"})
				|| mODE(item, new String[] {"ingotSteel", "dustSteel"}) && mODE(item2, new String[] {"ingotMingrade", "dustMingrade"})) {
			return new ItemStack(ModItems.ingot_advanced_alloy, 2);
		}

		if (mODE(item, new String[] {"ingotTungsten", "dustTungsten"}) && mODE(item2, "nuggetSchrabidium")
				|| mODE(item, "nuggetSchrabidium") && mODE(item2, new String[] {"ingotTungsten", "dustTungsten"})) {
			return new ItemStack(ModItems.ingot_magnetized_tungsten, 1);
		}

		if (mODE(item, new String[] {"ingotSteel", "dustSteel"}) && mODE(item2, new String[] {"nuggetTechnetium99", "tinyTc99"})
				|| mODE(item, new String[] {"nuggetTechnetium99", "tinyTc99"}) && mODE(item2, new String[] {"ingotSteel", "dustSteel"})) {
			return new ItemStack(ModItems.ingot_tcalloy, 1);
		}

		if (item.getItem() == ModItems.plate_mixed && mODE(item2, "plateGold")
				|| mODE(item, "plateGold") && item2.getItem() == ModItems.plate_mixed) {
			return new ItemStack(ModItems.plate_paa, 2);
		}

		if (mODE(item, new String[] {"ingotSteel", "dustSteel"}) && mODE(item2, new String[] {"ingotTungsten", "dustTungsten"})
				|| mODE(item, new String[] {"ingotTungsten", "dustTungsten"}) && mODE(item2, new String[] {"ingotSteel", "dustSteel"})) {
			return new ItemStack(ModItems.ingot_dura_steel, 2);
		}

		if (mODE(item, new String[] {"ingotSteel", "dustSteel"}) && mODE(item2, new String[] {"ingotCobalt", "dustCobalt"})
				|| mODE(item, new String[] {"ingotCobalt", "dustCobalt"}) && mODE(item2, new String[] {"ingotSteel", "dustSteel"})) {
			return new ItemStack(ModItems.ingot_dura_steel, 2);
		}

		if (mODE(item, new String[] {"ingotSaturnite", "dustSaturnite"}) && item2.getItem() == ModItems.powder_meteorite
				|| item.getItem() == ModItems.powder_meteorite && mODE(item2, new String[] {"ingotSaturnite", "dustSaturnite"})) {
			return new ItemStack(ModItems.ingot_starmetal, 2);
		}

		if (item.getItem() == ModItems.silicon_lump && item2.getItem() == ModItems.coke
				|| item.getItem() == ModItems.coke && item2.getItem() == ModItems.silicon_lump)
			return new ItemStack(ModItems.ingot_silicon, 4);
		
		if (mODE(item, new String[] {"sand"}) && mODE(item2, new String[] {COAL.gem(), COAL.dust()})
				|| mODE(item, new String[] {COAL.gem(), COAL.dust()}) && mODE(item2, new String[] {"sand"}))
			return new ItemStack(ModItems.nugget_silicon, 3);
		
		if (mODE(item, new String[] {STEEL.ingot(), STEEL.dust()}) && mODE(item2, new String[] {U238.ingot(), U238.dust()})
				|| mODE(item, new String[] {U238.ingot(), U238.dust()}) && mODE(item2, new String[] {STEEL.ingot(), STEEL.dust()}))
			return new ItemStack(ModItems.ingot_ferrouranium);
		
		if (mODE(item, new String[] {TI.ingot(), TI.dust()}) && mODE(item2, new String[] {U238.ingot(), U238.dust()})
				|| mODE(item, new String[] {U238.ingot(), U238.dust()}) && mODE(item2, new String[] {TI.ingot(), TI.dust()}))
			return new ItemStack(ModItems.ingot_staballoy);
		
		if (mODE(item, new String[] {W.ingot(), W.dust()}) && mODE(item2, new String[] {CO.ingot(), CO.dust()})
				|| mODE(item, new String[] {CO.ingot(), CO.dust()}) && mODE(item2, new String[] {W.ingot(), W.dust()}))
			return new ItemStack(ModItems.component_ftl, 1, 4);
		
		if (mODE(item, new String[] {NB.ingot(), NB.dust()}) && mODE(item2, new String[] {BE.ingot(), BE.dust()})
				|| mODE(item, new String[] {BE.dust(), BE.ingot()}) && mODE(item2, new String[] {NB.ingot(), NB.dust()}))
			return new ItemStack(ModItems.ingot_nbbe);
		
		List<ItemStack> electrum = OreDictionary.getOres("ingotElectrum");
		if (!electrum.isEmpty())
		{
			if (mODE(item, new String[] {"ingotGold", "dustGold"}) && mODE(item2, new String[] {"ingotSilver", "dustSilver"})
					|| mODE(item, new String[] {"ingotSilver", "dustSilver"}) && mODE(item2, new String[] {"ingotGold", "dustGold"}))
				return new ItemStack(electrum.get(0).getItem(), 2);
		}
		
		if (mODE(item, new String[] {DNT.ingot(), DNT.dust()}) && mODE(item2, new String[] {CMB.ingot(), CMB.dust()})
				|| mODE(item, new String[] {CMB.ingot(), CMB.dust()}) && mODE(item2, new String[] {DNT.ingot(), DNT.dust()}))
			return new ItemStack(ModItems.ingot_dineutronium_forged);
		
		if(GeneralConfig.enableBabyMode) {
			if(mODE(item, new String[] { "gemCoal", "dustCoal" }) && item2.getItem() == ModItems.canister_empty
					|| item.getItem() == ModItems.canister_empty && mODE(item2, new String[] { "gemCoal", "dustCoal" })) {
				return new ItemStack(ModItems.canister_full, 1, Fluids.OIL.getID());
			}

			if(item.getItem() == Item.getItemFromBlock(ModBlocks.block_meteor_cobble) && mODE(item2, new String[] { "ingotSteel", "dustSteel" })
					|| mODE(item, new String[] { "ingotSteel", "dustSteel" }) && item2.getItem() == Item.getItemFromBlock(ModBlocks.block_meteor_cobble)) {
				return new ItemStack(ModItems.ingot_meteorite);
			}
		}

		if (item.getItem() == Item.getItemFromBlock(ModBlocks.block_meteor) && mODE(item2, new String[] {"ingotCobalt", "dustCobalt"})
				|| mODE(item, new String[] {"ingotCobalt", "dustCobalt"}) && item2.getItem() == Item.getItemFromBlock(ModBlocks.block_meteor)) {
			return new ItemStack(ModItems.ingot_meteorite);
		}

		if (item.getItem() == ModItems.meteorite_sword_hardened && mODE(item2, new String[] {"ingotCobalt", "dustCobalt"})
				|| mODE(item, new String[] {"ingotCobalt", "dustCobalt"}) && item2.getItem() == ModItems.meteorite_sword_hardened) {
			return new ItemStack(ModItems.meteorite_sword_alloyed, 1);
		}
		
		if(item.getItem() instanceof ItemGunBase && item2.getItem() == Items.enchanted_book) {
			
			ItemStack result = item.copy();

            Map mapright = EnchantmentHelper.getEnchantments(item2);
            Iterator itr = mapright.keySet().iterator();

            while (itr.hasNext()) {
            	
            	int i = ((Integer)itr.next()).intValue();
            	int j = ((Integer)mapright.get(Integer.valueOf(i))).intValue();
            	Enchantment e = Enchantment.enchantmentsList[i];
            	
            	EnchantmentUtil.removeEnchantment(result, e);
            	EnchantmentUtil.addEnchantment(result, e, j);
            }
            
            return result;
		}

		return null;
	}
	
	//return: Fluids, amount produced, amount required, heat required (°C * 100)
	public static Object[] getBoilerOutput(FluidType type) {
		
		if(type == Fluids.WATER) return new Object[] { Fluids.STEAM, 500, 5, 10000 };
		if(type == Fluids.STEAM) return new Object[] { Fluids.HOTSTEAM, 5, 50, 30000 };
		if(type == Fluids.HOTSTEAM) return new Object[] { Fluids.SUPERHOTSTEAM, 5, 50, 45000 };
		if(type == Fluids.OIL) return new Object[] { Fluids.HOTOIL, 5, 5, 35000 };
		if(type == Fluids.CRACKOIL) return new Object[] { Fluids.HOTCRACKOIL, 5, 5, 35000 };
		
		return null;
	}
	
	//return: Fluids, amount produced, amount required, HE produced
	public static Object[] getTurbineOutput(FluidType type) {
		
		if(type == Fluids.STEAM) return new Object[] { Fluids.SPENTSTEAM, 5, 500, 50 };
		if(type == Fluids.HOTSTEAM) return new Object[] { Fluids.STEAM, 50, 5, 100 };
		if(type == Fluids.SUPERHOTSTEAM) return new Object[] { Fluids.HOTSTEAM, 50, 5, 150 };
		if(type == Fluids.ULTRAHOTSTEAM) return new Object[] { Fluids.SUPERHOTSTEAM, 50, 5, 250 };
		
		return null;
	}

	public ArrayList<ItemStack> getAlloyFuels() {
		ArrayList<ItemStack> fuels = new ArrayList<ItemStack>();
		fuels.add(new ItemStack(Items.coal));
		fuels.add(new ItemStack(Blocks.coal_block));
		fuels.add(new ItemStack(Items.lava_bucket));
		fuels.add(new ItemStack(Items.blaze_rod));
		fuels.add(new ItemStack(Items.blaze_powder));
		fuels.add(new ItemStack(ModItems.lignite));
		fuels.add(new ItemStack(ModItems.powder_lignite));
		fuels.add(new ItemStack(ModItems.briquette_lignite));
		fuels.add(new ItemStack(ModItems.coke));
		fuels.add(new ItemStack(ModItems.solid_fuel));
		fuels.add(new ItemStack(ModItems.powder_coal));
		return fuels;
	}

	public ArrayList<ItemStack> getCentrifugeFuels() {
		ArrayList<ItemStack> fuels = new ArrayList<ItemStack>();
		fuels.add(new ItemStack(Items.coal));
		fuels.add(new ItemStack(Item.getItemFromBlock(Blocks.coal_block)));
		fuels.add(new ItemStack(Items.lava_bucket));
		fuels.add(new ItemStack(Items.redstone));
		fuels.add(new ItemStack(Item.getItemFromBlock(Blocks.redstone_block)));
		fuels.add(new ItemStack(Item.getItemFromBlock(Blocks.netherrack)));
		fuels.add(new ItemStack(Items.blaze_rod));
		fuels.add(new ItemStack(Items.blaze_powder));
		return fuels;
	}
	
	//keep this
	//like in a museum or something
	//this is a testament of my incompetence
	//look at it
	//look at how horrifying it is
	//children, never do this
	/*public class ShredderRecipe {

		public ItemStack input;
		public ItemStack output;

		public void registerEverythingImSrs() {
			
			String[] names = OreDictionary.getOreNames();
			List<ItemStack> stacks = new ArrayList<ItemStack>();
			
			for(int i = 0; i < names.length; i++) {
				stacks.addAll(OreDictionary.getOres(names[i]));
			}
			
			for(int i = 0; i < stacks.size(); i++) {
				
				int[] ids = OreDictionary.getOreIDs(stacks.get(i));

				List<String> oreNames = new ArrayList<String>();
				
				for(int j = 0; j < ids.length; j++) {
					oreNames.add(OreDictionary.getOreName(ids[j]));
				}
				
				theWholeThing.add(new DictCouple(stacks.get(i), oreNames));
			}
			
			MainRegistry.logger.info("Added " + theWholeThing.size() + " elements from the Ore Dict!");
		}
		
		public boolean doesExist(ItemStack stack) {
			
			for(DictCouple dic : theWholeThing) {
				if(dic.item.getItem() == stack.getItem() && dic.item.getItemDamage() == stack.getItemDamage())
					return true;
			}
			
			return false;
		}

		public void addRecipes() {

			// Not very efficient, I know, but at least it works AND it's
			// somewhat smart!
			
			for(int i = 0; i < theWholeThing.size(); i++)
			{
				for(int j = 0; j < theWholeThing.get(i).list.size(); j++)
				{
					String s = theWholeThing.get(i).list.get(j);
					
					if (s.length() > 5 && s.substring(0, 5).equals("ingot")) {
						ItemStack stack = canFindDustByName(s.substring(5));
						if (stack != null) {
							setRecipe(theWholeThing.get(i).item, stack);
						} else {
							setRecipe(theWholeThing.get(i).item, new ItemStack(ModItems.scrap));
						}
					} else if (s.length() > 3 && s.substring(0, 3).equals("ore")) {
						ItemStack stack = canFindDustByName(s.substring(3));
						if (stack != null) {
							setRecipe(theWholeThing.get(i).item, new ItemStack(stack.getItem(), 2, stack.getItemDamage()));
						} else {
							setRecipe(theWholeThing.get(i).item, new ItemStack(ModItems.scrap));
						}
					} else if (s.length() > 5 && s.substring(0, 5).equals("block")) {
						ItemStack stack = canFindDustByName(s.substring(5));
						if (stack != null) {
							setRecipe(theWholeThing.get(i).item, new ItemStack(stack.getItem(), 9, stack.getItemDamage()));
						} else {
							setRecipe(theWholeThing.get(i).item, new ItemStack(ModItems.scrap));
						}
					} else if (s.length() > 3 && s.substring(0, 3).equals("gem")) {
						ItemStack stack = canFindDustByName(s.substring(3));
						if (stack != null) {
							setRecipe(theWholeThing.get(i).item, new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
						} else {
							setRecipe(theWholeThing.get(i).item, new ItemStack(ModItems.scrap));
						}
					} else if (s.length() > 4 && s.substring(0, 4).equals("dust")) {
						setRecipe(theWholeThing.get(i).item, new ItemStack(ModItems.dust));
					} else if (s.length() > 6 && s.substring(0, 6).equals("powder")) {
						setRecipe(theWholeThing.get(i).item, new ItemStack(ModItems.dust));
					} else {
						setRecipe(theWholeThing.get(i).item, new ItemStack(ModItems.scrap));
					}
				}

				if(theWholeThing.get(i).list.isEmpty())
					setRecipe(theWholeThing.get(i).item, new ItemStack(ModItems.scrap));
				if(!theWholeThing.get(i).list.isEmpty() && theWholeThing.get(i).list.get(0).equals("Unknown"))
					setRecipe(theWholeThing.get(i).item, new ItemStack(ModItems.scrap));
			}

			MainRegistry.logger.info("Added " + recipesShredder.size() + " in total.");
			MainRegistry.logger.info("Added " + dustCount + " ore dust recipes.");
		}
		
		public ItemStack canFindDustByName(String s) {
			
			for(DictCouple d : theWholeThing)
			{
				for(String s1 : d.list)
				{
					if(s1.length() > 4 && s1.substring(0, 4).equals("dust") && s1.substring(4).equals(s))
					{
						dustCount++;
						return d.item;
					}
				}
			}
			
			return null;
		}
		
		public void setRecipe(ItemStack inp, ItemStack outp) {
			ShredderRecipe recipe = new ShredderRecipe();
			
			recipe.input = inp;
			recipe.output = outp;
			
			recipesShredder.add(recipe);
		}
		
		public void overridePreSetRecipe(ItemStack inp, ItemStack outp) {
			
			boolean flag = false;
			
			for(int i = 0; i < recipesShredder.size(); i++)
			{
				if(recipesShredder.get(i) != null && 
						recipesShredder.get(i).input != null && 
						recipesShredder.get(i).output != null && 
						inp != null && 
						outp != null && 
						recipesShredder.get(i).input.getItem() == inp.getItem() && 
						recipesShredder.get(i).input.getItemDamage() == inp.getItemDamage()) {
					recipesShredder.get(i).output = outp;
					flag = true;
				}
			}
			
			if(!flag) {
				ShredderRecipe rec = new ShredderRecipe();
				rec.input = inp;
				rec.output = outp;
				recipesShredder.add(rec);
			}
		}
		
		public void removeDuplicates() {
			List<ShredderRecipe> newList = new ArrayList<ShredderRecipe>();
			
			for(ShredderRecipe piv : recipesShredder)
			{
				boolean flag = false;
				
				if(newList.size() == 0)
				{
					newList.add(piv);
				} else {
					for(ShredderRecipe rec : newList) {
						if(piv != null && rec != null && piv.input != null && rec.input != null && rec.input.getItem() != null && piv.input.getItem() != null && rec.input.getItemDamage() == piv.input.getItemDamage() && rec.input.getItem() == piv.input.getItem())
							flag = true;
						if(piv == null || rec == null || piv.input == null || rec.input == null)
							flag = true;
					}
				}
				
				if(!flag)
				{
					newList.add(piv);
				}
			}
		}
		
		public void PrintRecipes() {

			MainRegistry.logger.debug("TWT: " + theWholeThing.size() + ", REC: " + recipesShredder.size());
		}
	}

	public static class DictCouple {
		
		public ItemStack item;
		public List<String> list;

		public DictCouple(ItemStack item, List<String> list) {
			this.item = item;
			this.list = list;
		}
		
		public static List<String> findWithStack(ItemStack stack) {
			for(DictCouple couple : theWholeThing) {
				if(couple.item == stack);
					return couple.list;
			}
			
			return null;
		}
	}

	public static List<ShredderRecipe> recipesShredder = new ArrayList<ShredderRecipe>();
	public static List<DictCouple> theWholeThing = new ArrayList<DictCouple>();
	public static int dustCount = 0;
	
	public static ItemStack getShredderResult(ItemStack stack) {
		for(ShredderRecipe rec : recipesShredder)
		{
			if(stack != null && 
					rec.input.getItem() == stack.getItem() && 
					rec.input.getItemDamage() == stack.getItemDamage())
				return rec.output.copy();
		}
		
		return new ItemStack(ModItems.scrap);
	}
	
	public Map<Object, Object> getShredderRecipes() {
		Map<Object, Object> recipes = new HashMap<Object, Object>();
		
		for(int i = 0; i < MachineRecipes.recipesShredder.size(); i++) {
			if(MachineRecipes.recipesShredder.get(i) != null && MachineRecipes.recipesShredder.get(i).output.getItem() != ModItems.scrap)
				recipes.put(MachineRecipes.recipesShredder.get(i).input, getShredderResult(MachineRecipes.recipesShredder.get(i).input));
		}
		
		return recipes;
	}*/

	public Map<Object[], Object> getCMBRecipes() {
		Map<Object[], Object> recipes = new HashMap<Object[], Object>();
		recipes.put(new ItemStack[] { new ItemStack(ModItems.ingot_advanced_alloy), new ItemStack(ModItems.ingot_magnetized_tungsten) },
				new ItemStack(ModItems.ingot_combine_steel, 4));
		recipes.put(new ItemStack[] { new ItemStack(ModItems.powder_advanced_alloy), new ItemStack(ModItems.powder_magnetized_tungsten) },
				new ItemStack(ModItems.ingot_combine_steel, 4));
		return recipes;
	}

	public List<ItemStack> getBatteries() {
		ArrayList<ItemStack> fuels = new ArrayList<ItemStack>();
		fuels.add(new ItemStack(ModItems.battery_potato));
		fuels.add(new ItemStack(ModItems.battery_potatos));
		fuels.add(new ItemStack(ModItems.battery_su));
		fuels.add(new ItemStack(ModItems.battery_su_l));
		fuels.add(new ItemStack(ModItems.battery_generic));
		fuels.add(new ItemStack(ModItems.battery_red_cell));
		fuels.add(new ItemStack(ModItems.battery_red_cell_6));
		fuels.add(new ItemStack(ModItems.battery_red_cell_24));
		fuels.add(new ItemStack(ModItems.battery_advanced));
		fuels.add(new ItemStack(ModItems.battery_advanced_cell));
		fuels.add(new ItemStack(ModItems.battery_advanced_cell_4));
		fuels.add(new ItemStack(ModItems.battery_advanced_cell_12));
		fuels.add(new ItemStack(ModItems.battery_lithium));
		fuels.add(new ItemStack(ModItems.battery_lithium_cell));
		fuels.add(new ItemStack(ModItems.battery_lithium_cell_3));
		fuels.add(new ItemStack(ModItems.battery_lithium_cell_6));
		fuels.add(new ItemStack(ModItems.battery_schrabidium));
		fuels.add(new ItemStack(ModItems.battery_schrabidium_cell));
		fuels.add(new ItemStack(ModItems.battery_schrabidium_cell_2));
		fuels.add(new ItemStack(ModItems.battery_schrabidium_cell_4));
		fuels.add(new ItemStack(ModItems.battery_trixite));
		fuels.add(new ItemStack(ModItems.battery_spark));
		fuels.add(new ItemStack(ModItems.battery_spark_cell_6));
		fuels.add(new ItemStack(ModItems.battery_spark_cell_25));
		fuels.add(new ItemStack(ModItems.battery_spark_cell_100));
		fuels.add(new ItemStack(ModItems.battery_spark_cell_1000));
		fuels.add(new ItemStack(ModItems.battery_spark_cell_10000));
		fuels.add(new ItemStack(ModItems.battery_spark_cell_power));
		fuels.add(new ItemStack(ModItems.fusion_core));
		fuels.add(new ItemStack(ModItems.energy_core));
		return fuels;
	}

	public List<ItemStack> getBlades() {
		ArrayList<ItemStack> fuels = new ArrayList<ItemStack>();
		fuels.add(new ItemStack(ModItems.blades_advanced_alloy));
		fuels.add(new ItemStack(ModItems.blades_aluminium));
		fuels.add(new ItemStack(ModItems.blades_combine_steel));
		fuels.add(new ItemStack(ModItems.blades_gold));
		fuels.add(new ItemStack(ModItems.blades_iron));
		fuels.add(new ItemStack(ModItems.blades_steel));
		fuels.add(new ItemStack(ModItems.blades_titanium));
		fuels.add(new ItemStack(ModItems.blades_schrabidium));
		fuels.add(new ItemStack(ModItems.blades_desh));
		return fuels;
	}
	
	public static boolean mODE(Item item, String[] names) {
		return mODE(new ItemStack(item), names);
	}
	
	public static boolean mODE(ItemStack item, String[] names) {
		boolean flag = false;
		if(names.length > 0) {
			for(int i = 0; i < names.length; i++) {
				if(mODE(item, names[i]))
					flag = true;
			}
		}
		
		return flag;
	}
	
	public static boolean mODE(Item item, String name) {
		return mODE(new ItemStack(item), name);
	}
	
	//Matches Ore Dict Entry
	public static boolean mODE(ItemStack stack, String name) {
		
		int[] ids = OreDictionary.getOreIDs(new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
		
		for(int i = 0; i < ids.length; i++) {
			
			String s = OreDictionary.getOreName(ids[i]);
			
			if(s.length() > 0 && s.equals(name))
				return true;
		}
		
		return false;
	}
	
	
	public Map<Object, Object> getBoilerRecipes() {

		Map<Object, Object> recipes = new HashMap<Object, Object>();
		
		for(int i = 0; i < Fluids.getAll().length; i++) {
			Object[] outs = getBoilerOutput(Fluids.fromID(i));
			
			if(outs != null) {

				ItemStack in = new ItemStack(ModItems.fluid_icon, 1, i);
				in.stackTagCompound = new NBTTagCompound();
				in.stackTagCompound.setInteger("fill", (Integer) outs[2]);
				
				ItemStack out = new ItemStack(ModItems.fluid_icon, 1, ((FluidType)outs[0]).getID());
				out.stackTagCompound = new NBTTagCompound();
				out.stackTagCompound.setInteger("fill", (Integer) outs[1]);
				
				recipes.put(in, out);
			}
		}
		
		return recipes;
	}
	
	public Map<Object, Object> getFluidContainers() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		for(FluidContainer con : FluidContainerRegistry.allContainers) {
			if(con != null) {
				ItemStack fluid = new ItemStack(ModItems.fluid_icon, 1, con.type.getID());
				fluid.stackTagCompound = new NBTTagCompound();
				fluid.stackTagCompound.setInteger("fill", con.content);
				map.put(fluid, con.fullContainer);
			}
		}
		
		return map;
	}


	public static void registerArcFurnaceRecipes()
	{
		arcFurnaceRecipes.put(new ComparableStack(ModBlocks.sand_quartz), new ItemStack(ModBlocks.glass_quartz));
		arcFurnaceRecipes.put(new ComparableStack(ModItems.powder_acrylic), new ItemStack(ModItems.acrylic));
//		arcFurnaceRecipes.put(new ComparableStack(ModItems.storage_optical_raw), new ItemStack(ModItems.acrylic));
		arcFurnaceRecipes.put(new ComparableStack(ModItems.powder_quartz), new ItemStack(Items.quartz));
		for (int i = 0; i < 6; i++)
			arcFurnaceRecipes.put(new ComparableStack(ModItems.ingot_dineutronium_forged, 1, i), ItemHot.heatUp(new ItemStack(ModItems.ingot_dineutronium_forged, 1, i)));
		arcFurnaceRecipes.put(new ComparableStack(ModItems.powder_niobium_alloy), new ItemStack(ModItems.ingot_niobium_alloy));

	}

	public static Map<ComparableStack, ItemStack> getArcFurnaceRecipes()
	{
		return ImmutableMap.copyOf(arcFurnaceRecipes);
	}
}
