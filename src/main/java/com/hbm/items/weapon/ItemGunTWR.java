package com.hbm.items.weapon;

import java.util.HashSet;

import com.google.common.annotations.Beta;
import com.hbm.calc.EasyLocation;
import com.hbm.handler.GunConfiguration;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.lib.Library;
import com.hbm.util.BobMathUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemGunTWR extends ItemGunBase
{
	public ItemGunTWR(GunConfiguration config)
	{
		super(config);
		setFull3D();
	}
	
	// Not too small otherwise it doesn't work as a sniper, not too big otherwise your computer dies. (Deprecated)
//	private static final byte MAX_RANGE = 100;
	
	@Beta
	@Override
	protected void spawnProjectile(World world, EntityPlayer player, ItemStack stack, int config)
	{
		// Get player's location conveniently (more readable this way)
		final ILocationProvider playerLoc = ILocationProvider.wrap(player, true);/*new EasyLocation(Library.getPosition(1, player)).modifyCoord(0, player.getEyeHeight(), 0);*/
		// Set for entities
		final HashSet<Entity> entities = new HashSet<>();
		// Unit vector for player
		final Vec3 step = BobMathUtil.entityUnitVector(player);
		// First block in the way
		final double blockLoc = ILocationProvider.distanceSq(playerLoc, ILocationProvider.wrap(Library.rayTrace(player, 500, 1)));
		
		// LEGACY RAY-MARCHING CODE; KNOWN TO WORK, QUESTIONABLE EFFICIENCY
//		final EasyLocation checkCoord = playerLoc.clone().setWorld(world);
//		BobMathUtil.offsetForHand(player, checkCoord);
//		do
//		{
//			checkCoord.addCoord(step);
//			entities.addAll(world.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(checkCoord.posX - 1, checkCoord.posY - 1, checkCoord.posZ - 1, checkCoord.posX + 1, checkCoord.posY + 1, checkCoord.posZ + 1)));
//		} while (!checkCoord.getBlock().isBlockNormalCube() && ILocationProvider.distance(checkCoord, playerLoc) < 500);
		
		// EXPERIMENTAL SLAB-LIKE RAY CHECKING; WORKS, BUT REQUIRES MORE PRECISE AIM AND DIFFERENT BLOCK CHECKING
		// Deprecated
//		entities.addAll(world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(MAX_RANGE, MAX_RANGE, MAX_RANGE)));
		// Get all entities since we don't need to ray-march anymore.
		entities.addAll(world.loadedEntityList);
		// Remove player so they don't commit self-removal
		entities.remove(player);
		// Remove invalid targets
		entities.removeIf(e ->
		{
			// Save entity location conveniently
			final ILocationProvider entLoc = ILocationProvider.wrap(e, false);
			// Check if the block checked earlier is in the way							Check if the ray doesn't intersect
			return blockLoc < ILocationProvider.distanceSq(playerLoc, entLoc) || !BobMathUtil.rayIntersects(step, playerLoc, e.boundingBox);
		});
		
		// Loop over all entities
		for (Entity entity : entities)
		{
			// If normal living entity, otherwise just kill
			if (entity instanceof EntityLivingBase)
			{
				// Cast
				final EntityLivingBase livingEntity = (EntityLivingBase) entity;
				// Funky damage + random message
				final DamageSource dmgSrc = new EntityDamageSourceIndirect("twr" + (world.rand.nextInt(4) + 4), livingEntity, player).setDamageIsAbsolute().setDamageBypassesArmor().setDamageAllowedInCreativeMode();
				// Try attack and enforce compliance if not cooperative
				if (!livingEntity.attackEntityFrom(dmgSrc, 750))
					livingEntity.setHealth(livingEntity.getHealth() - 750);
				if (livingEntity.isDead || livingEntity.getHealth() <= 0)
				{
					// Theoretically supposed to tell the game who killed the entity if forcefully killed, but doesn't seem to work.
					// Not a huge thing, but curious.
					livingEntity.setLastAttacker(player);
					livingEntity.onDeath(dmgSrc);
				}
			} else
				entity.setDead();
		}
	}
	
	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_)
	{
		return EnumRarity.epic;
	}
}
