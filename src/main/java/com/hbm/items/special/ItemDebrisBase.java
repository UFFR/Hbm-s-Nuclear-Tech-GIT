package com.hbm.items.special;

import java.lang.reflect.InvocationTargetException;

import com.hbm.calc.EasyLocation;
import com.hbm.entity.projectile.EntityDebrisBase;
import com.hbm.entity.projectile.EntityDebrisBase.DebrisType;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDebrisBase extends Item
{
	private final DebrisType type;
	private final Class<? extends EntityDebrisBase> entityClazz;
	public ItemDebrisBase(Class<? extends EntityDebrisBase> entityClazz, DebrisType type)
	{
		this.entityClazz = entityClazz;
		this.type = type;
	}
	
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		EntityDebrisBase ent = null;
		try
		{
			ent = EntityDebrisBase.class.asSubclass(entityClazz).getConstructor(World.class).newInstance(world);
			ent.setType(type);
			ent.posX = location.posX;
			ent.posY = location.posY;
			ent.posZ = location.posZ;
			return ent;
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
		
		return ent;
	}
}
