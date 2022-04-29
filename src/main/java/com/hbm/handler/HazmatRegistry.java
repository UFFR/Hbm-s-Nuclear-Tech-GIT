package com.hbm.handler;

import java.util.HashMap;

import com.hbm.items.ModItems;
import com.hbm.items.armor.ItemModCladding;
import com.hbm.lib.Library;
import com.hbm.potion.HbmPotion;
import com.hbm.util.Compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HazmatRegistry
{
	

		//assuming coefficient of 10
		//real coefficient turned out to be 5
		//oops
		
		static final double helmet = 0.2D;
		static final double chest = 0.4D;
		static final double legs = 0.3D;
		static final double boots = 0.1D;

		static final double iron = 0.0225D; // 5%
		static final double gold = 0.0225D; // 5%
		static final double steel = 0.045D; // 10%
		static final double titanium = 0.045D; // 10%
		static final double alloy = 0.07D; // 15%
		static final double cobalt = 0.125D; // 25%
		
		double hazYellow = 0.6D; // 50%
		double hazRed = 1.0D; // 90%
		double hazGray = 2D; // 99%
		double paa = 1.7D; // 97%
		double liquidator = 2.4D; // 99.6%

		double t45 = 1D; // 90%
		double ajr = 1.3D; // 95%
		double bj = 1D; // 90%
		double hev = 1.3D; // 95%
		double rpa = 2D; // 99%
		double fau = 4D; // 99.99%
		double dns = 5D; // 99.999%
		double security = 0.825D; // 85%
		double star = 1D; // 90%
		double cmb = 1.3D; // 95%
		double schrab = 3D; // 99.9%
		double euph = 10D; // <100%
		
	public static void registerHazmats()
	{
		registerHazmat(ModItems.hazmat_helmet, hazYellow * helmet);
		registerHazmat(ModItems.hazmat_plate, hazYellow * chest);
		registerHazmat(ModItems.hazmat_legs, hazYellow * legs);
		registerHazmat(ModItems.hazmat_boots, hazYellow * boots);

		registerHazmat(ModItems.hazmat_helmet_red, hazRed * helmet);
		registerHazmat(ModItems.hazmat_plate_red, hazRed * chest);
		registerHazmat(ModItems.hazmat_legs_red, hazRed * legs);
		registerHazmat(ModItems.hazmat_boots_red, hazRed * boots);

		registerHazmat(ModItems.hazmat_helmet_grey, hazGray * helmet);
		registerHazmat(ModItems.hazmat_plate_grey, hazGray * chest);
		registerHazmat(ModItems.hazmat_legs_grey, hazGray * legs);
		registerHazmat(ModItems.hazmat_boots_grey, hazGray * boots);

		Item rec_helmet = Compat.tryLoadItem(Compat.MOD_REC, "reactorcraft_item_hazhelmet");
		Item rec_chest = Compat.tryLoadItem(Compat.MOD_REC, "reactorcraft_item_hazchest");
		Item rec_legs = Compat.tryLoadItem(Compat.MOD_REC, "reactorcraft_item_hazlegs");
		Item rec_boots = Compat.tryLoadItem(Compat.MOD_REC, "reactorcraft_item_hazboots");
		if(rec_helmet != null)	HazmatRegistry.registerHazmat(rec_helmet, hazGray * helmet);
		if(rec_chest != null)	HazmatRegistry.registerHazmat(rec_chest, hazGray * chest);
		if(rec_legs != null)	HazmatRegistry.registerHazmat(rec_legs, hazGray * legs);
		if(rec_boots != null)	HazmatRegistry.registerHazmat(rec_boots, hazGray * boots);

		Item efn_helmet = Compat.tryLoadItem(Compat.MOD_EF, "netherite_helmet");
		Item efn_chest = Compat.tryLoadItem(Compat.MOD_EF, "netherite_chestplate");
		Item efn_legs = Compat.tryLoadItem(Compat.MOD_EF, "netherite_leggings");
		Item efn_boots = Compat.tryLoadItem(Compat.MOD_EF, "netherite_boots");
		if(efn_helmet != null)	HazmatRegistry.registerHazmat(efn_helmet, star * helmet);
		if(efn_chest != null)	HazmatRegistry.registerHazmat(efn_chest, star * chest);
		if(efn_legs != null)	HazmatRegistry.registerHazmat(efn_legs, star * legs);
		if(efn_boots != null)	HazmatRegistry.registerHazmat(efn_boots, star * boots);

		registerHazmat(ModItems.liquidator_helmet, liquidator * helmet);
		registerHazmat(ModItems.liquidator_plate, liquidator * chest);
		registerHazmat(ModItems.liquidator_legs, liquidator * legs);
		registerHazmat(ModItems.liquidator_boots, liquidator * boots);

		registerHazmat(ModItems.t45_helmet, t45 * helmet);
		registerHazmat(ModItems.t45_plate, t45 * chest);
		registerHazmat(ModItems.t45_legs, t45 * legs);
		registerHazmat(ModItems.t45_boots, t45 * boots);

		registerHazmat(ModItems.ajr_helmet, ajr * helmet);
		registerHazmat(ModItems.ajr_plate, ajr * chest);
		registerHazmat(ModItems.ajr_legs, ajr * legs);
		registerHazmat(ModItems.ajr_boots, ajr * boots);
		registerHazmat(ModItems.ajro_helmet, ajr * helmet);
		registerHazmat(ModItems.ajro_plate, ajr * chest);
		registerHazmat(ModItems.ajro_legs, ajr * legs);
		registerHazmat(ModItems.ajro_boots, ajr * boots);

		registerHazmat(ModItems.bj_helmet, bj * helmet);
		registerHazmat(ModItems.bj_plate, bj * chest);
		registerHazmat(ModItems.bj_plate_jetpack, bj * chest);
		registerHazmat(ModItems.bj_legs, bj * legs);
		registerHazmat(ModItems.bj_boots, bj * boots);

		HazmatRegistry.registerHazmat(ModItems.steamsuit_helmet, 1.3 * helmet);
		HazmatRegistry.registerHazmat(ModItems.steamsuit_plate, 1.3 * chest);
		HazmatRegistry.registerHazmat(ModItems.steamsuit_legs, 1.3 * legs);
		HazmatRegistry.registerHazmat(ModItems.steamsuit_boots, 1.3 * boots);

		HazmatRegistry.registerHazmat(ModItems.hev_helmet, hev * helmet);
		HazmatRegistry.registerHazmat(ModItems.hev_plate, hev * chest);
		HazmatRegistry.registerHazmat(ModItems.hev_legs, hev * legs);
		HazmatRegistry.registerHazmat(ModItems.hev_boots, hev * boots);

		HazmatRegistry.registerHazmat(ModItems.rpa_helmet, rpa * helmet);
		HazmatRegistry.registerHazmat(ModItems.rpa_plate, rpa * chest);
		HazmatRegistry.registerHazmat(ModItems.rpa_legs, rpa * legs);
		HazmatRegistry.registerHazmat(ModItems.rpa_boots, rpa * boots);

		HazmatRegistry.registerHazmat(ModItems.fau_helmet, fau * helmet);
		HazmatRegistry.registerHazmat(ModItems.fau_plate, fau * chest);
		HazmatRegistry.registerHazmat(ModItems.fau_legs, fau * legs);
		HazmatRegistry.registerHazmat(ModItems.fau_boots, fau * boots);

		registerHazmat(ModItems.dns_helmet, dns * helmet);
		registerHazmat(ModItems.dns_plate, dns * chest);
		registerHazmat(ModItems.dns_legs, dns * legs);
		registerHazmat(ModItems.dns_boots, dns * boots);

		registerHazmat(ModItems.paa_plate, paa * chest);
		registerHazmat(ModItems.paa_legs, paa * legs);
		registerHazmat(ModItems.paa_boots, paa * boots);

		registerHazmat(ModItems.hazmat_paa_helmet, paa * helmet);
		registerHazmat(ModItems.hazmat_paa_plate, paa * chest);
		registerHazmat(ModItems.hazmat_paa_legs, paa * legs);
		registerHazmat(ModItems.hazmat_paa_boots, paa * boots);

		registerHazmat(ModItems.security_helmet, security * helmet);
		registerHazmat(ModItems.security_plate, security * chest);
		registerHazmat(ModItems.security_legs, security * legs);
		registerHazmat(ModItems.security_boots, security * boots);

		registerHazmat(ModItems.starmetal_helmet, star * helmet);
		registerHazmat(ModItems.starmetal_plate, star * chest);
		registerHazmat(ModItems.starmetal_legs, star * legs);
		registerHazmat(ModItems.starmetal_boots, star * boots);

		registerHazmat(ModItems.jackt, 0.1);
		registerHazmat(ModItems.jackt2, 0.1);

		registerHazmat(ModItems.gas_mask, 0.07);
		registerHazmat(ModItems.gas_mask_m65, 0.095);

		registerHazmat(ModItems.steel_helmet, steel * helmet);
		registerHazmat(ModItems.steel_plate, steel * chest);
		registerHazmat(ModItems.steel_legs, steel * legs);
		registerHazmat(ModItems.steel_boots, steel * boots);

		registerHazmat(ModItems.titanium_helmet, titanium * helmet);
		registerHazmat(ModItems.titanium_plate, titanium * chest);
		registerHazmat(ModItems.titanium_legs, titanium * legs);
		registerHazmat(ModItems.titanium_boots, titanium * boots);

		registerHazmat(ModItems.cobalt_helmet, cobalt * helmet);
		registerHazmat(ModItems.cobalt_plate, cobalt * chest);
		registerHazmat(ModItems.cobalt_legs, cobalt * legs);
		registerHazmat(ModItems.cobalt_boots, cobalt * boots);

		registerHazmat(Items.iron_helmet, iron * helmet);
		registerHazmat(Items.iron_chestplate, iron * chest);
		registerHazmat(Items.iron_leggings, iron * legs);
		registerHazmat(Items.iron_boots, iron * boots);

		registerHazmat(Items.golden_helmet, gold * helmet);
		registerHazmat(Items.golden_chestplate, gold * chest);
		registerHazmat(Items.golden_leggings, gold * legs);
		registerHazmat(Items.golden_boots, gold * boots);

		registerHazmat(ModItems.alloy_helmet, alloy * helmet);
		registerHazmat(ModItems.alloy_plate, alloy * chest);
		registerHazmat(ModItems.alloy_legs, alloy * legs);
		registerHazmat(ModItems.alloy_boots, alloy * boots);

		registerHazmat(ModItems.cmb_helmet, cmb * helmet);
		registerHazmat(ModItems.cmb_plate, cmb * chest);
		registerHazmat(ModItems.cmb_legs, cmb * legs);
		registerHazmat(ModItems.cmb_boots, cmb * boots);

		registerHazmat(ModItems.schrabidium_helmet, schrab * helmet);
		registerHazmat(ModItems.schrabidium_plate, schrab * chest);
		registerHazmat(ModItems.schrabidium_legs, schrab * legs);
		registerHazmat(ModItems.schrabidium_boots, schrab * boots);

		registerHazmat(ModItems.euphemium_helmet, euph * helmet);
		registerHazmat(ModItems.euphemium_plate, euph * chest);
		registerHazmat(ModItems.euphemium_legs, euph * legs);
		registerHazmat(ModItems.euphemium_boots, euph * boots);
		
		registerHazmat(ModItems.ferrouranium_helmet, hazRed * helmet);
		registerHazmat(ModItems.ferrouranium_plate, hazRed * chest);
		registerHazmat(ModItems.ferrouranium_legs, hazRed * legs);
		registerHazmat(ModItems.ferrouranium_boots, hazRed * boots);
	}
	
	private static HashMap<Item, Double> entries = new HashMap<Item, Double>();
	
	public static void registerHazmat(Item item, double resistance) {
		
		entries.put(item, resistance);
	}
	
	public static double getResistance(ItemStack stack) {
		
		if(stack == null)
			return 0;
		
		double cladding = getCladding(stack);
		
		Double f = entries.get(stack.getItem());
		
		if(f != null)
			return f + cladding;
		
		return cladding;
	}
	
	public static double getCladding(ItemStack stack) {

		if(stack.hasTagCompound() && stack.stackTagCompound.getFloat("hfr_cladding") > 0)
			return stack.stackTagCompound.getFloat("hfr_cladding");
		
		if(ArmorModHandler.hasMods(stack)) {
			
			ItemStack[] mods = ArmorModHandler.pryMods(stack);
			ItemStack cladding = mods[ArmorModHandler.cladding];
			
			if(cladding != null && cladding.getItem() instanceof ItemModCladding) {
				return ((ItemModCladding)cladding.getItem()).rad;
			}
		}
		
		return 0;
	}
	
	public static float getResistance(EntityPlayer player) {
		
		float res = 0.0F;
		
		if(player.getUniqueID().toString().equals(Library.Pu_238)) {
			res += 0.4F;
		}
		
		for(int i = 0; i < 4; i++) {
			res += getResistance(player.inventory.armorInventory[i]);
		}
		
		if(player.isPotionActive(HbmPotion.radx))
			res += 0.2F;
		
		return res;
		
	}

}