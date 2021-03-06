package com.hbm.crafting;

import com.hbm.blocks.ModBlocks;
import com.hbm.config.GeneralConfig;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemBattery;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * For mining and utility tools
 * @author hbm
 */
public class ToolRecipes {
	
	public static void register() {
		
		//Regular tools
		basicToolSet(new Item[] { ModItems.steel_sword, ModItems.steel_pickaxe, ModItems.steel_axe, ModItems.steel_shovel, ModItems.steel_hoe }, ModItems.ingot_steel, Items.stick);
		basicToolSet(new Item[] { ModItems.titanium_sword, ModItems.titanium_pickaxe, ModItems.titanium_axe, ModItems.titanium_shovel, ModItems.titanium_hoe }, ModItems.ingot_titanium, Items.stick);
		basicToolSet(new Item[] { ModItems.cobalt_sword, ModItems.cobalt_pickaxe, ModItems.cobalt_axe, ModItems.cobalt_shovel, ModItems.cobalt_hoe }, ModItems.ingot_cobalt, Items.stick);
		basicToolSet(new Item[] { ModItems.alloy_sword, ModItems.alloy_pickaxe, ModItems.alloy_axe, ModItems.alloy_shovel, ModItems.alloy_hoe }, ModItems.ingot_advanced_alloy, Items.stick);
		Item[] cmbSet = new Item[] {ModItems.cmb_sword, ModItems.cmb_pickaxe, ModItems.cmb_axe, ModItems.cmb_shovel, ModItems.cmb_hoe};
		Item[] deshSet = new Item[] {ModItems.desh_sword, ModItems.desh_pickaxe, ModItems.desh_axe, ModItems.desh_shovel, ModItems.desh_hoe};
		upgradeToolSet2(cmbSet, ModItems.ingot_combine_steel, deshSet, ModItems.bolt_staballoy);
//		GameRegistry.addRecipe(new ItemStack(ModItems.cmb_sword, 1), new Object[] { "I", "I", "S", 'I', ModItems.ingot_combine_steel, 'S', Items.stick });
//		GameRegistry.addRecipe(new ItemStack(ModItems.cmb_pickaxe, 1), new Object[] { "III", " S ", " S ", 'I', ModItems.ingot_combine_steel, 'S', Items.stick });
//		GameRegistry.addRecipe(new ItemStack(ModItems.cmb_axe, 1), new Object[] { "II", "IS", " S", 'I', ModItems.ingot_combine_steel, 'S', Items.stick });
//		GameRegistry.addRecipe(new ItemStack(ModItems.cmb_shovel, 1), new Object[] { "I", "S", "S", 'I', ModItems.ingot_combine_steel, 'S', Items.stick });
//		GameRegistry.addRecipe(new ItemStack(ModItems.cmb_hoe, 1), new Object[] { "II", " S", " S", 'I', ModItems.ingot_combine_steel, 'S', Items.stick });
		GameRegistry.addRecipe(new ItemStack(ModItems.elec_sword, 1), new Object[] { "RPR", "RPR", " B ", 'P', ModItems.ingot_polymer, 'D', ModItems.ingot_dura_steel, 'R', ModItems.bolt_dura_steel, 'M', ModItems.motor, 'B', ModItems.battery_lithium });
		GameRegistry.addRecipe(new ItemStack(ModItems.elec_pickaxe, 1), new Object[] { "RDM", " PB", " P ", 'P', ModItems.ingot_polymer, 'D', ModItems.ingot_dura_steel, 'R', ModItems.bolt_dura_steel, 'M', ModItems.motor, 'B', ModItems.battery_lithium });
		GameRegistry.addRecipe(new ItemStack(ModItems.elec_axe, 1), new Object[] { " DP", "RRM", " PB", 'P', ModItems.ingot_polymer, 'D', ModItems.ingot_dura_steel, 'R', ModItems.bolt_dura_steel, 'M', ModItems.motor, 'B', ModItems.battery_lithium });
		GameRegistry.addRecipe(new ItemStack(ModItems.elec_shovel, 1), new Object[] { "  P", "RRM", "  B", 'P', ModItems.ingot_polymer, 'D', ModItems.ingot_dura_steel, 'R', ModItems.bolt_dura_steel, 'M', ModItems.motor, 'B', ModItems.battery_lithium });
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.centri_stick, 1), new Object[] { ModItems.centrifuge_element, ModItems.energy_core, Items.stick });
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.smashing_hammer, 1), new Object[] { "STS", "SPS", " P ", 'S', "blockSteel", 'T', "blockTungsten", 'P', "ingotPolymer" }));
//		GameRegistry.addRecipe(new ItemStack(ModItems.desh_sword, 1), new Object[] { "I", "I", "S", 'I', ModItems.ingot_desh, 'S', Items.stick });
//		GameRegistry.addRecipe(new ItemStack(ModItems.desh_pickaxe, 1), new Object[] { "III", " S ", " S ", 'I', ModItems.ingot_desh, 'S', Items.stick });
//		GameRegistry.addRecipe(new ItemStack(ModItems.desh_axe, 1), new Object[] { "II", "IS", " S", 'I', ModItems.ingot_desh, 'S', Items.stick });
//		GameRegistry.addRecipe(new ItemStack(ModItems.desh_shovel, 1), new Object[] { "I", "S", "S", 'I', ModItems.ingot_desh, 'S', Items.stick });
//		GameRegistry.addRecipe(new ItemStack(ModItems.desh_hoe, 1), new Object[] { "II", " S", " S", 'I', ModItems.ingot_desh, 'S', Items.stick });
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.meteorite_sword, 1), new Object[] { "  B", "GB ", "SG ", 'B', ModItems.blade_meteorite, 'G', "plateGold", 'S', Items.stick }));

		GameRegistry.addRecipe(new ItemStack(ModItems.cobalt_decorated_sword, 1), new Object[] { " I ", " I ", "SBS", 'I', ModItems.ingot_cobalt, 'S', ModItems.ingot_meteorite_forged, 'B', ModItems.cobalt_sword });
		GameRegistry.addRecipe(new ItemStack(ModItems.cobalt_decorated_pickaxe, 1), new Object[] { "III", " B ", " S ", 'I', ModItems.ingot_cobalt, 'S', ModItems.ingot_meteorite_forged, 'B', ModItems.cobalt_pickaxe });
		GameRegistry.addRecipe(new ItemStack(ModItems.cobalt_decorated_axe, 1), new Object[] { "II", "IB", " S", 'I', ModItems.ingot_cobalt, 'S', ModItems.ingot_meteorite_forged, 'B', ModItems.cobalt_axe });
		GameRegistry.addRecipe(new ItemStack(ModItems.cobalt_decorated_shovel, 1), new Object[] { "I", "B", "S", 'I', ModItems.ingot_cobalt, 'S', ModItems.ingot_meteorite_forged, 'B', ModItems.cobalt_shovel });
		GameRegistry.addRecipe(new ItemStack(ModItems.cobalt_decorated_hoe, 1), new Object[] { "II", " B", " S", 'I', ModItems.ingot_cobalt, 'S', ModItems.ingot_meteorite_forged, 'B', ModItems.cobalt_hoe });
		GameRegistry.addRecipe(new ItemStack(ModItems.starmetal_sword, 1), new Object[] { " I ", " I ", "SBS", 'I', ModItems.ingot_starmetal, 'S', ModItems.ingot_cobalt, 'B', ModItems.cobalt_decorated_sword });
		GameRegistry.addRecipe(new ItemStack(ModItems.starmetal_pickaxe, 1), new Object[] { "III", " B ", " S ", 'I', ModItems.ingot_starmetal, 'S', ModItems.ingot_cobalt, 'B', ModItems.cobalt_decorated_pickaxe });
		GameRegistry.addRecipe(new ItemStack(ModItems.starmetal_axe, 1), new Object[] { "II", "IB", " S", 'I', ModItems.ingot_starmetal, 'S', ModItems.ingot_cobalt, 'B', ModItems.cobalt_decorated_axe });
		GameRegistry.addRecipe(new ItemStack(ModItems.starmetal_shovel, 1), new Object[] { "I", "B", "S", 'I', ModItems.ingot_starmetal, 'S', ModItems.ingot_cobalt, 'B', ModItems.cobalt_decorated_shovel });
		GameRegistry.addRecipe(new ItemStack(ModItems.starmetal_hoe, 1), new Object[] { "II", " B", " S", 'I', ModItems.ingot_starmetal, 'S', ModItems.ingot_cobalt, 'B', ModItems.cobalt_decorated_hoe });
		Item[] ferrouraniumSet = new Item[] {ModItems.ferrouranium_sword, ModItems.ferrouranium_pickaxe, ModItems.ferrouranium_axe, ModItems.ferrouranium_shovel, ModItems.ferrouranium_hoe};
		basicToolSet(ferrouraniumSet, ModItems.ingot_ferrouranium, ModItems.bolt_dura_steel);
		upgradeToolSet2(deshSet, ModItems.ingot_desh, ferrouraniumSet, ModItems.bolt_staballoy);
		
		//Drax
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.drax, 1), new Object[] { "BDS", "CDC", "FMF", 'B', ModItems.starmetal_pickaxe, 'S', ModItems.starmetal_shovel, 'C', "ingotCobalt", 'F', ModItems.fusion_core, 'D', "ingotDesh", 'M', ModItems.motor_desh }));
		GameRegistry.addRecipe(new ItemStack(ModItems.drax_mk2, 1), new Object[] { "SCS", "IDI", "FEF", 'S', ModItems.ingot_starmetal, 'C', ModItems.crystal_trixite, 'I', ModItems.ingot_saturnite, 'D', ModItems.drax, 'F', ModItems.fusion_core, 'E', ModItems.circuit_targeting_tier5 });
		GameRegistry.addRecipe(new ItemStack(ModItems.drax_mk3, 1), new Object[] { "ECE", "CDC", "SBS", 'E', ModBlocks.block_euphemium_cluster, 'C', ModItems.crystal_schrabidium, 'D', ModItems.drax_mk2, 'S', ModItems.circuit_targeting_tier6, 'B', ItemBattery.getFullBattery(ModItems.battery_spark) });

		//Super pickaxes
		GameRegistry.addRecipe(new ItemStack(ModItems.chlorophyte_pickaxe, 1), new Object[] { " SD", "APS", "FA ", 'S', ModItems.blades_steel, 'D', ModItems.powder_chlorophyte, 'A', ModItems.ingot_fiberglass, 'P', ModItems.steel_pickaxe, 'F', ModItems.bolt_dura_steel });
		GameRegistry.addRecipe(new ItemStack(ModItems.mese_pickaxe, 1), new Object[] { " SD", "APS", "FA ", 'S', ModItems.blades_desh, 'D', ModItems.powder_dineutronium, 'A', ModItems.plate_paa, 'P', ModItems.chlorophyte_pickaxe, 'F', ModItems.shimmer_handle });

		//Chainsaws
		GameRegistry.addRecipe(new ItemStack(ModItems.chainsaw, 1), new Object[] { "  H", "BBP", "  C", 'H', ModItems.hull_small_steel, 'B', ModItems.blades_steel, 'P', ModItems.piston_selenium, 'C', ModItems.canister_fuel });
		GameRegistry.addRecipe(new ItemStack(ModItems.chainsaw, 1), new Object[] { "  H", "BBP", "  C", 'H', ModItems.hull_small_steel, 'B', ModItems.blades_steel, 'P', ModItems.piston_selenium, 'C', ModItems.canister_petroil });
		GameRegistry.addRecipe(new ItemStack(ModItems.chainsaw, 1), new Object[] { "  H", "BBP", "  C", 'H', ModItems.hull_small_steel, 'B', ModItems.blades_steel, 'P', ModItems.piston_selenium, 'C', ModItems.canister_biofuel });

		//Misc
		GameRegistry.addRecipe(new ItemStack(ModItems.euphemium_stopper, 1), new Object[] { "I", "S", "S", 'I', ModItems.ingot_euphemium, 'S', Items.stick });
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.crowbar, 1), new Object[] { "II", " I", " I", 'I', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.bottle_opener, 1), new Object[] { "S", "P", 'S', "plateSteel", 'P', "plankWood" }));

		//Matches
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.matchstick, 16), new Object[] { "I", "S", 'I', "sulfur", 'S', Items.stick }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.matchstick, 16), new Object[] { "I", "S", 'I', "dustSulfur", 'S', Items.stick }));
		GameRegistry.addRecipe(new ItemStack(ModItems.matchstick, 24), new Object[] { "I", "S", 'I', ModItems.powder_fire, 'S', Items.stick });

		//Gavels
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.wood_gavel, 1), new Object[] { "SWS", " R ", " R ", 'S', "slabWood", 'W', "logWood", 'R', "stickWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.lead_gavel, 1), new Object[] { "PIP", "IGI", "PIP", 'P', ModItems.pellet_buckshot, 'I', "ingotLead", 'G', ModItems.wood_gavel }));

		//Misc weapons
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.saw, 1), new Object[] { "IIL", "PP ", 'P', "plateSteel", 'I', "ingotSteel", 'L', Items.leather }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.bat, 1), new Object[] { "P", "P", "S", 'S', "plateSteel", 'P', "plankWood" }));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.bat_nail, 1), new Object[] { ModItems.bat, "plateSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.golf_club, 1), new Object[] { "IP", " P", " P", 'P', "plateSteel", 'I', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.pipe_rusty, 1), new Object[] { "II", " I", " I", 'I', "ingotIron" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.pipe_lead, 1), new Object[] { "II", " I", " I", 'I', "ingotLead" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ullapool_caber, 1), new Object[] { "ITI", " S ", " S ", 'I', "plateIron", 'T', Blocks.tnt, 'S', Items.stick }));

		//Utility
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.designator, 1), new Object[] { "  A", "#B#", "#B#", '#', "plateIron", 'A', "plateSteel", 'B', ModItems.circuit_red_copper }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.designator_range, 1), new Object[] { "RRD", "PIC", "  P", 'P', "plateSteel", 'R', Items.redstone, 'C', ModItems.circuit_gold, 'D', ModItems.designator, 'I', "ingotSteel" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.designator_manual, 1), new Object[] { "  A", "#C#", "#B#", '#', "ingotPolymer", 'A', "plateLead", 'B', ModItems.circuit_gold, 'C', ModItems.designator }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.linker, 1), new Object[] { "I I", "ICI", "GGG", 'I', "plateIron", 'G', "plateGold", 'C', ModItems.circuit_gold }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.oil_detector, 1), new Object[] { "W I", "WCI", "PPP", 'W', ModItems.wire_gold, 'I', "ingotCopper", 'C', ModItems.circuit_red_copper, 'P', "plateSteel" }));
		GameRegistry.addRecipe(new ItemStack(ModItems.turret_chip, 1), new Object[] { "WWW", "CPC", "WWW", 'W', ModItems.wire_gold, 'P', ModItems.ingot_polymer, 'C', ModItems.circuit_gold, });
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.turret_biometry, 1), new Object[] { "CC ", "GGS", "SSS", 'C', ModItems.circuit_copper, 'S', "plateSteel", 'G', "plateGold", 'I', "plateLead" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.survey_scanner, 1), new Object[] { "SWS", " G ", "PCP", 'W', ModItems.wire_gold, 'P', ModItems.ingot_polymer, 'C', ModItems.circuit_gold, 'S', "plateSteel", 'G', "ingotGold" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.geiger_counter, 1), new Object[] { "GPP", "WCS", "WBB", 'W', ModItems.wire_gold, 'P', ModItems.ingot_polymer, 'C', ModItems.circuit_copper, 'G', "ingotGold", 'S', "plateSteel", 'B', ModItems.ingot_beryllium }));
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.geiger), new Object[] { ModItems.geiger_counter });
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.mirror_tool), new Object[] { " A ", " IA", "I  ", 'A', "ingotAluminum", 'I', "ingotIron" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.defuser, 1), new Object[] { " PS", "P P", " P ", 'P', "ingotPolymer", 'S', "plateSteel" }));
		
		//Bobmazon
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.bobmazon_materials), new Object[] { Items.book, Items.gold_nugget, Items.string });
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.bobmazon_machines), new Object[] { Items.book, Items.gold_nugget, new ItemStack(Items.dye, 1, 1) });
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.bobmazon_weapons), new Object[] { Items.book, Items.gold_nugget, new ItemStack(Items.dye, 1, 8) });
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.bobmazon_tools), new Object[] { Items.book, Items.gold_nugget, new ItemStack(Items.dye, 1, 2) });
		
		//Configged
		if(GeneralConfig.enableBabyMode) {
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_sword, 1), new Object[] { "I", "I", "S", 'I', ModItems.ingot_schrabidium, 'S', Items.stick });
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_pickaxe, 1), new Object[] { "III", " S ", " S ", 'I', ModItems.ingot_schrabidium, 'S', Items.stick });
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_axe, 1), new Object[] { "II", "IS", " S", 'I', ModItems.ingot_schrabidium, 'S', Items.stick });
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_shovel, 1), new Object[] { "I", "S", "S", 'I', ModItems.ingot_schrabidium, 'S', Items.stick });
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_hoe, 1), new Object[] { "II", " S", " S", 'I', ModItems.ingot_schrabidium, 'S', Items.stick });
		} else {
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_sword, 1), new Object[] { "I", "W", "S", 'I', ModBlocks.block_schrabidium, 'W', ModItems.cmb_sword, 'S', ModItems.ingot_polymer });
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_pickaxe, 1), new Object[] { "SWS", " P ", " P ", 'S', ModItems.blades_schrabidium, 'W', ModItems.cmb_pickaxe, 'P', ModItems.ingot_polymer });
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_axe, 1), new Object[] { "SW", "SP", " P", 'S', ModItems.blades_schrabidium, 'W', ModItems.cmb_axe, 'P', ModItems.ingot_polymer });
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_shovel, 1), new Object[] { "S", "W", "P", 'S', ModItems.blades_schrabidium, 'W', ModItems.cmb_shovel, 'P', ModItems.ingot_polymer });
			GameRegistry.addRecipe(new ItemStack(ModItems.schrabidium_hoe, 1), new Object[] { "IW", " S", " S", 'I', ModItems.ingot_schrabidium, 'W', ModItems.cmb_hoe, 'S', ModItems.ingot_polymer });
		}
	}
	/**
	 * Register a tool set using vanilla style recipes
	 * @param outs - Must be in the order: sword, pickaxe, axe, shovel, then hoe
	 * @param headMaterial - The material of the head (ie iron)
	 * @param handleMaterial - The material oreDict of the handle (vanilla uses sticks)
	 */
	public static void basicToolSet(Item[] outs, Item headMaterial, String handleMaterial)
	{
		if (outs.length != 5)
			throw new IllegalArgumentException("Output array must be equal to 5!");
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[0]), new Object[] { "I", "I", "H", 'I', headMaterial, 'H', handleMaterial }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[1]), new Object[] { "III", " H ", " H ", 'I', headMaterial, 'H', handleMaterial }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[2]), new Object[] { "II ", "IH ", " H ", 'I', headMaterial, 'H', handleMaterial }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[3]), new Object[] { "I", "H", "H", 'I', headMaterial, 'H', handleMaterial }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[4]), new Object[] { "II", " H", " H", 'I', headMaterial, 'H', handleMaterial }));
	}
	/**
	 * Register a tool set using vanilla style recipes
	 * @param outs - Must be in the order: sword, pickaxe, axe, shovel, then hoe
	 * @param headMaterial - The material of the head (ie iron)
	 * @param handleMaterial - The material of the handle (vanilla uses sticks)
	 */
	public static void basicToolSet(Item[] outs, Item headMaterial, Item handleMaterial)
	{
		if (outs.length != 5)
			throw new IllegalArgumentException("Output array must be equal to 5!");
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[0]), new Object[] { "I", "I", "H", 'I', headMaterial, 'H', handleMaterial }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[1]), new Object[] { "III", " H ", " H ", 'I', headMaterial, 'H', handleMaterial }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[2]), new Object[] { "II ", "IH ", " H ", 'I', headMaterial, 'H', handleMaterial }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[3]), new Object[] { "I", "H", "H", 'I', headMaterial, 'H', handleMaterial }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(outs[4]), new Object[] { "II", " H", " H", 'I', headMaterial, 'H', handleMaterial }));
	}
	/**
	 * Register a tool that's an upgrade to a preexisting one, uses slightly less expensive method
	 * @param outs - Must be in the order: sword, pickaxe, axe, shovel, then hoe
	 * @param headMaterial - The material of the head (ie iron)
	 * @param priorTier - Previous tier tools, same order as before
	 * @param handleMaterial - The material of the handle (vanilla uses sticks)
	 */
	public static void upgradeToolSet1(Item[] outs, Item headMaterial, Item[] priorTier, Item handleMaterial)
	{
		if (outs.length != 5)
			throw new IllegalArgumentException("Output array must be equal to 5!");
		if (priorTier.length != 5)
			throw new IllegalArgumentException("Input array must be equal to 5!");
		
		GameRegistry.addRecipe(new ItemStack(outs[0]), new Object[] { " I ", " I ", "HPH", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[0] });
		GameRegistry.addRecipe(new ItemStack(outs[1]), new Object[] { "IPI", " H ", " H ", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[1] });
		GameRegistry.addRecipe(new ItemStack(outs[2]), new Object[] { "IP", "IH", " H", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[2] });
		GameRegistry.addRecipe(new ItemStack(outs[3]), new Object[] { "I", "P", "H", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[3] });
		GameRegistry.addRecipe(new ItemStack(outs[4]), new Object[] { "IP", " H", " H", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[4] });
	}
	/**
	 * Register a tool that's an upgrade to a preexisting one, uses slightly more expensive method
	 * @param outs - Must be in the order: sword, pickaxe, axe, shovel, then hoe
	 * @param headMaterial - The material of the head (ie iron)
	 * @param priorTier - Previous tier tools, same order as before
	 * @param handleMaterial - The material of the handle (vanilla uses sticks)
	 */
	public static void upgradeToolSet2(Item[] outs, Item headMaterial, Item[] priorTier, Item handleMaterial)
	{
		if (outs.length != 5)
			throw new IllegalArgumentException("Output array must be equal to 5!");
		if (priorTier.length != 5)
			throw new IllegalArgumentException("Input array must be equal to 5!");
		
		GameRegistry.addRecipe(new ItemStack(outs[0]), new Object[] { " I ", " I ", "HPH", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[0] });
		GameRegistry.addRecipe(new ItemStack(outs[1]), new Object[] { "III", "HPH", " H ", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[1] });
		GameRegistry.addRecipe(new ItemStack(outs[2]), new Object[] { "IIP", "IH", " H", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[2] });
		GameRegistry.addRecipe(new ItemStack(outs[3]), new Object[] { "I", "HPH", "H", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[3] });
		GameRegistry.addRecipe(new ItemStack(outs[4]), new Object[] { "II", "HP", " H", 'I', headMaterial, 'H', handleMaterial, 'P', priorTier[4] });
	}
	/**
	 * Create a new flat stamp
	 * @param stamp - The stamp itself
	 * @param mat - Material it's made of
	 */
	public static void newStamp(Item stamp, Item mat)
	{
		GameRegistry.addRecipe(new ItemStack(stamp), new Object[] {" R ", "BBB", "MMM", 'R', Items.redstone, 'B', Items.brick, 'M', mat});
	}
}
