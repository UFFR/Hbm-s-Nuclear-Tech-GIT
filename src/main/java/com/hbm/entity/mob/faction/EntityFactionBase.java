package com.hbm.entity.mob.faction;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.google.common.annotations.Beta;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.items.ItemAmmoEnums.AmmoHandGrenade;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

@Beta
public abstract class EntityFactionBase extends EntityLivingBase implements IFactionDataProvider
{
	protected final ArrayList<EntityItem> interestedItems = new ArrayList<>();
	public EntityFactionBase(World world)
	{
		super(world);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean hasSquad()
	{
		return true;
	}
	
	@Override
	public boolean rankApplicable()
	{
		return true;
	}
	
	@OverridingMethodsMustInvokeSuper
	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();
		final List<EntityItem> nearbyItems = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(10, 2, 10, -10, -2, -10));
		interestedItems.clear();
		nearbyItems.forEach(entityItem ->
		{
			final ItemStack stack = entityItem.getEntityItem();
			for (AStack aStack : interestedStacks())
				if (aStack.isApplicable(stack))
					interestedItems.add(entityItem);
		});
	}
	
	
	/** -1 for infinite **/
	public abstract byte grenadeCount();
	public abstract AmmoHandGrenade grenadeType();
	public abstract AStack[] interestedStacks();
}
