package com.hbm.items.weapon;

import java.util.EnumMap;
import java.util.List;

import com.hbm.calc.EasyLocation;
import com.hbm.entity.grenade.EntityGrenadeBouncyBaseNT;
import com.hbm.explosion.*;
import com.hbm.items.ItemAmmoEnums.AmmoHandGrenade;
import com.hbm.items.ItemEnumMulti;
import com.hbm.items.special.ItemCustomLore;
import com.hbm.lib.Library;
import com.hbm.packet.AuxParticlePacketNT;
import com.hbm.packet.PacketDispatcher;
import com.hbm.potion.HbmPotion;
import com.hbm.util.EnumUtil;
import com.hbm.util.I18nUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemGrenadeEnum extends ItemEnumMulti
{
	@FunctionalInterface
	public interface GrenadeLogic
	{
		public void explode(EntityLivingBase thrower, EasyLocation loc, EntityGrenadeBouncyBaseNT grenade);
	}

	private static final EnumMap<AmmoHandGrenade, GrenadeLogic> logicMap = new EnumMap<>(AmmoHandGrenade.class);
	static
	{
		logicMap.put(AmmoHandGrenade.GENERIC, (thrower, loc, nade) ->
		{
			loc.world.get().createExplosion(nade, loc.posX, loc.posY, loc.posZ, 2.0F, true);
		});
		logicMap.put(AmmoHandGrenade.SMOKE, (thrower, loc, nade) ->
		{
			final int radius = 20;
			List<Entity> hit = loc.world.get().getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(loc.posX - radius, loc.posY - radius, loc.posZ - radius, loc.posX + radius, loc.posY + radius, loc.posZ + radius));
			
			for(Entity e : hit) {
				
				if(!Library.isObstructed(loc.world.get(), loc.posX, loc.posY, loc.posZ, e.posX, e.posY + e.getEyeHeight(), e.posZ)) {
					e.setFire(5);
					
					if(e instanceof EntityLivingBase) {
						
						PotionEffect eff = new PotionEffect(HbmPotion.phosphorus.id, 120 * 20, 0, true);
						eff.getCurativeItems().clear();
						((EntityLivingBase)e).addPotionEffect(eff);
					}
				}
			}
			
			NBTTagCompound data = new NBTTagCompound();
			data.setString("type", "vanillaburst");
			data.setString("mode", "flame");
			data.setInteger("count", 200);
			data.setDouble("motion", 0.5);
			
			
			NBTTagCompound haze = new NBTTagCompound();
			haze.setString("type", "haze");
			for (byte i = 0; i < 5; i++)
			{
				PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(data, loc.posX, loc.posY, loc.posZ), Library.easyTargetPoint(loc, 50));
				PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(haze, loc.posX, loc.posY, loc.posZ), Library.easyTargetPoint(loc, 150));
			}
		});
		logicMap.put(AmmoHandGrenade.STRONG, (thrower, loc, nade) ->
		{
			ExplosionLarge.explode(loc.world.get(), loc.posX, loc.posY, loc.posZ, 5.0F, true, false, false);
		});
		logicMap.put(AmmoHandGrenade.FRAG, (thrower, loc, nade) ->
		{
			World world = loc.world.get();
			if(nade.isBurning())
        	{
        		ExplosionChaos.frag(world, (int)loc.posX, (int)loc.posY, (int)loc.posZ, 100, true, thrower);
                ExplosionChaos.burn(world, (int)loc.posX, (int)loc.posY, (int)loc.posZ, 5);
                ExplosionChaos.flameDeath(loc.world.get(), (int)loc.posX, (int)loc.posY, (int)loc.posZ, 15);
        		loc.world.get().playSoundEffect((int)loc.posX, (int)loc.posY, (int)loc.posZ, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        	} else {
        		ExplosionChaos.frag(world, (int)loc.posX, (int)loc.posY, (int)loc.posZ, 100, false, thrower);
        		loc.world.get().playSoundEffect((int)loc.posX, (int)loc.posY, (int)loc.posZ, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        	}
		});
		logicMap.put(AmmoHandGrenade.FIRE, (thrower, loc, nade) ->
		{
			World world = loc.world.get();
            ExplosionChaos.frag(world, (int)loc.posX, (int)loc.posY, (int)loc.posZ, 100, true, thrower);
            ExplosionChaos.burn(world, (int)loc.posX, (int)loc.posY, (int)loc.posZ, 5);
            ExplosionChaos.flameDeath(world, (int)loc.posX, (int)loc.posY, (int)loc.posZ, 15);
            world.playSoundEffect((int)loc.posX, (int)loc.posY, (int)loc.posZ, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		});
		logicMap.put(AmmoHandGrenade.SHRAPNEL, (thrower, loc, nade) ->
		{
			World world = loc.world.get();
			world.createExplosion(nade, loc.posX, loc.posY, loc.posZ, 2.0F, true);
			for (byte i = 0; i < 5; i++) 
				ExplosionLarge.spawnShrapnels(world, loc.posX, loc.posY, loc.posZ, 5);
		});
		logicMap.put(AmmoHandGrenade.CLUSTER, (thrower, loc, nade) ->
		{
			World world = loc.world.get();
            ExplosionChaos.cluster(world, (int)loc.posX, (int)loc.posY, (int)loc.posZ, 10, 50);
            world.createExplosion(nade, loc.posX, loc.posY, loc.posZ, 1.5F, true);
		});
		logicMap.put(AmmoHandGrenade.FLARE, (thrower, loc, nade) ->
		{
			if (nade.ticksExisted > 250)
				nade.setDead();
		});
		logicMap.put(AmmoHandGrenade.ELECTRIC, (thrower, loc, nade) ->
		{
			World world = loc.world.get();
            world.createExplosion(nade, loc.posX, loc.posY, loc.posZ, 2.0F, true);
            loc.spawnEntityAtCoord(new EntityLightningBolt(world, loc.posX, loc.posY, loc.posZ));
		});
		logicMap.put(AmmoHandGrenade.POISON, (thrower, loc, nade) ->
		{
			World world = loc.world.get();
			world.createExplosion(nade, loc.posX, loc.posY, loc.posZ, 2.0F, true);
			ExplosionNukeGeneric.wasteNoSchrab(world, (int) loc.posX, (int) loc.posY, (int) loc.posZ, 10);
		});
		logicMap.put(AmmoHandGrenade.GAS, (thrower, loc, nade) ->
		{
			World world = loc.world.get();
			world.createExplosion(nade, loc.posX, loc.posY, loc.posZ, 0.0F, true);
			ExplosionChaos.spawnChlorine(world, loc.posX, loc.posY, loc.posZ, 50, 1.25, 0);
		});
		logicMap.put(AmmoHandGrenade.PULSE, (thrower, loc, nade) ->
		{
			World world = loc.world.get();
			ExplosionChaos.pulse(world, (int) loc.posX, (int) loc.posY, (int) loc.posZ, 7);
    		world.playSoundEffect((int)loc.posX, (int)loc.posY, (int)loc.posZ, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
    		ExplosionLarge.spawnShock(world, loc.posX, loc.posY, loc.posZ, 24, 2);
		});
		logicMap.put(AmmoHandGrenade.PLASMA, (thrower, loc, nade) ->
		{
			World world = loc.world.get();
			world.createExplosion(nade, loc.posX, loc.posY, loc.posZ, 2.0F, true);
			ExplosionChaos.plasma(world, (int) loc.posX, (int) loc.posY, (int) loc.posZ, 7);
		});
	}
	public ItemGrenadeEnum()
	{
		super(AmmoHandGrenade.class, true, true);
	}

	private static String translateFuse(int fuse)
	{
		switch (fuse)
		{
		case -1:
			return I18nUtil.resolveKey("desc.item.grenade.fuseImpact");
		case 0:
			return I18nUtil.resolveKey("desc.item.grenade.fuseInstant");
		default:
			return fuse + "s";
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean b)
	{
		AmmoHandGrenade nade = EnumUtil.grabEnumSafely(theEnum, stack.getItemDamage());
		tooltip.add(I18nUtil.resolveKey("desc.item.grenade.fuse", translateFuse(nade.fuse)));
		if (ItemCustomLore.getHasLore(stack))
			tooltip.add("");
		super.addInformation(stack, player, tooltip, b);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		AmmoHandGrenade nade = EnumUtil.grabEnumSafely(theEnum, stack.getItemDamage());
		
		if (!player.capabilities.isCreativeMode)
			stack.stackSize--;
		
		world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		
		if (!world.isRemote && logicMap.containsKey(nade))
			world.spawnEntityInWorld(new EntityGrenadeBouncyBaseNT(world, player, nade.fuse * 20, logicMap.get(nade)));
		
		return stack;
	}

}
