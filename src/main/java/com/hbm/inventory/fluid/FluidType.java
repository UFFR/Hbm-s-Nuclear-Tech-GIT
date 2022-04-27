package com.hbm.inventory.fluid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.hbm.inventory.FluidTank;
import com.hbm.lib.RefStrings;
import com.hbm.render.util.EnumSymbol;
import com.hbm.util.I18nUtil;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class FluidType {

	//The numeric ID of the fluid
	private int id;
	//Approximate HEX Color of the fluid, used for pipe rendering
	private int color;
	//Unlocalized string ID of the fluid
	private String unlocalized;
	
	public int poison;
	public int flammability;
	public int reactivity;
	public EnumSymbol symbol;
	public int temperature;
	public Set<FluidTrait> traits = new HashSet<>();
	private String stringId;
	
	private ResourceLocation texture;
	
	public FluidType(String name, int color, int p, int f, int r, EnumSymbol symbol) {
		this.stringId = name;
		this.color = color;
		this.unlocalized = "hbmfluid.".concat(name);
		this.poison = p;
		this.flammability = f;
		this.reactivity = r;
		this.symbol = symbol;
		this.texture = new ResourceLocation(RefStrings.MODID + ":textures/gui/fluids/" + name.toLowerCase() + ".png");
		Fluids.registerSelf(this);
	}
	
	public FluidType(int forcedId, String name, int color, int p, int f, int r, EnumSymbol symbol) {
		this(name, color, p, f, r, symbol);
		
		if(this.id != forcedId) {
			throw new IllegalStateException("Howdy! I am a safeguard put into place by Bob to protect you, the player, from Bob's dementia. For whatever reason, Bob decided to either add or remove a fluid in a way that shifts the IDs, despite the entire system being built to prevent just that. Instead of people's fluids getting jumbled for the 500th time, I am here to prevent the game from starting entirely. The expected ID was " + forcedId + ", but turned out to be " + this.id + ".");
		}
	}
	
	public FluidType setTemp(int temperature) {
		this.temperature = temperature;
		if (temperature > 0 && !this.traits.contains(FluidTrait.HOT))
			this.traits.add(FluidTrait.HOT);
		else if (temperature < 0 && !this.traits.contains(FluidTrait.CRYO))
			this.traits.add(FluidTrait.CRYO);
		return this;
	}
	
	public FluidType addTraits(FluidTrait... traitsIn) {
		Collections.addAll(this.traits, traitsIn);
		return this;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.stringId;
	}

	public int getColor() {
		return this.color;
	}
	public ResourceLocation getTexture() {
		return this.texture;
	}
	public String getUnlocalizedName() {
		return this.unlocalized;
	}
	public String getLocalizedName()
	{
		return I18nUtil.resolveKey(getUnlocalizedName().toLowerCase());
	}
	public String getDict(int quantity) {
		return "container" + quantity + this.stringId.replace("_", "");
	}
	
	public boolean isHot() {
		return this.temperature >= 100;
	}
	public boolean isCorrosive() {
		return this.traits.contains(FluidTrait.CORROSIVE) || this.traits.contains(FluidTrait.CORROSIVE_2);
	}
	public boolean isAntimatter() {
		return this.traits.contains(FluidTrait.AMAT);
	}
	public boolean hasNoContainer() {
		return this.traits.contains(FluidTrait.NO_CONTAINER);
	}
	public boolean hasNoID() {
		return this.traits.contains(FluidTrait.NO_ID);
	}
	public boolean needsLeadContainer() {
		return this.traits.contains(FluidTrait.LEAD_CONTAINER);
	}

	/**
	 * Called when the tile entity is broken, effectively voiding the fluids.
	 * @param te
	 * @param tank
	 */
	public void onTankBroken(TileEntity te, FluidTank tank) { }
	/**
	 * Called by the tile entity's update loop. Also has an arg for the fluid tank for possible tanks using child-classes that are shielded or treated differently.
	 * @param te
	 * @param tank
	 */
	public void onTankUpdate(TileEntity te, FluidTank tank) { }
	/**
	 * For when the tile entity is releasing this fluid into the world, either by an overflow or (by proxy) when broken.
	 * @param te
	 * @param tank
	 * @param overflowAmount
	 */
	public void onFluidRelease(TileEntity te, FluidTank tank, int overflowAmount) { }
	//public void onFluidTransmit(FluidNetwork net) { }
	
	public void addInfo(List<String> info) {
		// Little funky, but it's better than before I guess
		ArrayList<FluidTrait> sortedTraits = new ArrayList<>(getTraitSet());
		Collections.sort(sortedTraits);
		for (FluidTrait trait : sortedTraits)
		{
			String uloc = "desc.fTrait.".concat(trait.toString().toLowerCase());
			if (trait == FluidTrait.NO_ID)
				continue;
			else if (trait == FluidTrait.CRYO || trait == FluidTrait.HOT)
				info.add(I18nUtil.resolveKey(uloc, temperature));
			else
				info.add(I18nUtil.resolveKey(uloc));
		}
	}
	
	public static enum FluidTrait {
		CRYO,
		HOT,
		AMAT,
		CORROSIVE,
		CORROSIVE_2,
		RADIOACTIVE,
		BIOHAZARD,
		CHEMICAL,
		NO_CONTAINER,
		LEAD_CONTAINER,
		NO_ID;
	}
	
	//shitty wrapper delegates, go!
	//only used for compatibility purposes, these will be removed soon
	//don't use these, dumbfuck
	@Deprecated //reason: not an enum, asshole, use the registry
	public static FluidType getEnum(int i) {
		return Fluids.fromID(i);
	}
	@Deprecated //reason: the more time you waste reading this the less time is there for you to use that fucking registry already
	public static FluidType getEnumFromName(String s) {
		return Fluids.fromName(s);
	}
	@Deprecated //reason: not an enum, again, fuck you
	public int ordinal() {
		return this.getID();
	}

	public Set<FluidTrait> getTraitSet()
	{
		return ImmutableSet.copyOf(traits);
	}
	@Deprecated
	public int getMSAColor() {
		return this.color;
	}
	@Deprecated
	public String name() {
		return this.stringId;
	}
}