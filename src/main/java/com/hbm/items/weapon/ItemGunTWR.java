package com.hbm.items.weapon;

import java.util.HashSet;

import com.google.common.annotations.Beta;
import com.hbm.calc.EasyLocation;
import com.hbm.handler.GunConfiguration;
import com.hbm.lib.Library;
import com.hbm.util.BobMathUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.World;

public class ItemGunTWR extends ItemGunBase
{
	public ItemGunTWR(GunConfiguration config)
	{
		super(config);
	}
	
	@Beta
	@Override
	protected void spawnProjectile(World world, EntityPlayer player, ItemStack stack, int config)
	{
		EasyLocation playerLoc = new EasyLocation(Library.getPosition(1, player)).modifyCoord(0, player.getEyeHeight(), 0);
//		Vec3 playerLookVec = player.getLookVec();
		EasyLocation checkCoord = playerLoc.clone().setWorld(world);
		HashSet<Entity> entities = new HashSet<>();
		BobMathUtil.offsetForHand(player, checkCoord);
		EasyLocation step = new EasyLocation
				(
					-(Math.sin(player.rotationYaw / 180 * Math.PI) * Math.cos(player.rotationPitch / 180 * Math.PI)),
					-Math.sin(player.rotationPitch / 180 * Math.PI),
					Math.cos(player.rotationYaw / 180 * Math.PI) * Math.cos(player.rotationPitch / 180 * Math.PI)
				);
		do
		{
			checkCoord.addCoord(step);
			entities.addAll(world.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(checkCoord.posX - 1, checkCoord.posY - 1, checkCoord.posZ - 1, checkCoord.posX + 1, checkCoord.posY + 1, checkCoord.posZ + 1)));
		} while (!checkCoord.getBlockAtCoord().isBlockNormalCube() && checkCoord.distance(playerLoc) < 500);
		for (Entity entity : entities)
		{
			if (entity instanceof EntityLivingBase)
			{
				EntityLivingBase livingEntity = (EntityLivingBase) entity;
				DamageSource dmgSrc = new EntityDamageSourceIndirect("twr" + world.rand.nextInt(2) + 2, player, livingEntity).setDamageIsAbsolute().setDamageBypassesArmor().setDamageAllowedInCreativeMode();
				if (!livingEntity.attackEntityFrom(dmgSrc, 750))
					livingEntity.setHealth(livingEntity.getHealth() - 750);
				livingEntity.setLastAttacker(player);
				livingEntity.onDeath(dmgSrc);
			}
			else
				entity.setDead();
		}		
	}
	
	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_)
	{
		return EnumRarity.epic;
	}
}
