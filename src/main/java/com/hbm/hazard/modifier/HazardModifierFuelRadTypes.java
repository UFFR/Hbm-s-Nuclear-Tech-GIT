package com.hbm.hazard.modifier;

import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.items.machine.ItemFuelRod;
import com.hbm.items.machine.ItemRBMKPellet;
import com.hbm.items.machine.ItemRBMKRod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class HazardModifierFuelRadTypes extends HazardModifier
{
	private final byte[] target;
	
	public HazardModifierFuelRadTypes(byte[] target)
	{
		this.target = target;
	}
	
	public HazardModifierFuelRadTypes(float target)
	{
		this.target = HazardSystem.decompactRadTypes(target);
	}
	
	@Override
	public float modify(ItemStack stack, EntityLivingBase holder, float level)
	{
		final byte[] newTypes = new byte[4], currentTypes = HazardSystem.decompactRadTypes(level);
		if (stack.getItem() instanceof ItemFuelRod)
		{
			final double depletion = ((ItemFuelRod) stack.getItem()).getDurabilityForDisplay(stack);
			for (byte i = 0; i < 4; i++)
				newTypes[i] = (byte) (currentTypes[i] + (this.target[i] * depletion));
		} else if (stack.getItem() instanceof ItemRBMKRod)
		{
			final double depletion = 1D - ItemRBMKRod.getEnrichment(stack);
			final double xenon = ItemRBMKRod.getPoisonLevel(stack);
			
			for (byte i = 0; i < 4; i++)
				newTypes[i] = (byte) (currentTypes[i] + (this.target[i] * depletion));
			
			newTypes[1] += MathHelper.clamp_int(Float.floatToRawIntBits((float) (HazardRegistry.xe135 * xenon)), 0, 50);
			
		} else if (stack.getItem() instanceof ItemRBMKPellet)
		{
			
			final double depletion = (ItemRBMKPellet.rectify(stack.getItemDamage()) % 5) / 4F;
			for (byte i = 0; i < 4; i++)
				newTypes[i] = (byte) (currentTypes[i] + (this.target[i] * depletion));
			
			newTypes[1] += MathHelper.clamp_int(Float.floatToRawIntBits(HazardRegistry.xe135 * HazardRegistry.nugget), 0, 50);
		}
		
		
		return HazardSystem.compactRadTypes(newTypes);
	}

}
