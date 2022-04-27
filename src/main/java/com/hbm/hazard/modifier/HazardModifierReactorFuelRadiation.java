package com.hbm.hazard.modifier;

import com.hbm.items.machine.ItemFuelRod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class HazardModifierReactorFuelRadiation extends HazardModifier
{
	float target;
	public HazardModifierReactorFuelRadiation(float target)
	{
		this.target = target;
	}

	@Override
	public float modify(ItemStack stack, EntityLivingBase holder, float level)
	{
		if (stack.getItem() instanceof ItemFuelRod)
		{
//			float depletion = ItemFuelRod.getLifeTime(stack) / ((ItemFuelRod) stack.getItem()).lifeTime;
			double depletion = ((ItemFuelRod) stack.getItem()).getDurabilityForDisplay(stack);
			level = (float) (level + (this.target * depletion));
		}
		
		return level;
	}

}
