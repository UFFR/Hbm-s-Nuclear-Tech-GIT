package com.hbm.handler;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.hbm.entity.mob.*;
import com.hbm.entity.mob.botprime.*;
import com.hbm.entity.mob.siege.*;
import com.hbm.entity.mob.smart.combine.*;

import api.hbm.entity.EnumSpeciesType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Registry for entity/NPC classes, vanilla and NTM NPCs already handled.<br>
 * If registered correctly, smarter NPCs will identify these traits and change tactics to better combat them, if applicable<br>
 * Players have special behavior, their species traits can be changed, for support with other mods that may do that. Is a plain humanoid by default.
 * @author UFFR
 *
 */
public class SpeciesRegistry
{
	private static final Map<Class<? extends EntityLivingBase>, Set<EnumSpeciesType>> REGISTRY = new HashMap<Class<? extends EntityLivingBase>, Set<EnumSpeciesType>>();
	private static final Map<UUID, Set<EnumSpeciesType>> PLAYER_REGISTRY = new HashMap<UUID, Set<EnumSpeciesType>>();
	private static final Set<EnumSpeciesType> DEFAULT_SET = Sets.immutableEnumSet(EnumSpeciesType.OTHER), PLAYER_SET = Sets.immutableEnumSet(EnumSpeciesType.HUMANOID);
	private static final EnumSpeciesType[] GENERIC_INORGANIC = {EnumSpeciesType.INORGANIC, EnumSpeciesType.OTHER}, AIRCRAFT = {EnumSpeciesType.FLYING, EnumSpeciesType.INORGANIC};
	
	static
	{
		register(EntityBat.class, EnumSpeciesType.BEAST, EnumSpeciesType.FLYING);
		register(EntityChicken.class, EnumSpeciesType.BEAST);
		register(EntityDuck.class, EnumSpeciesType.BEAST);
		register(EntityQuackos.class, EnumSpeciesType.DIVINE, EnumSpeciesType.OTHER);
		register(EntityCow.class, EnumSpeciesType.BEAST);
		register(EntityMooshroom.class, EnumSpeciesType.BEAST, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntityHorse.class, EnumSpeciesType.BEAST);
		register(EntityPig.class, EnumSpeciesType.BEAST);
		register(EntitySheep.class, EnumSpeciesType.BEAST);
		register(EntityOcelot.class, EnumSpeciesType.BEAST);
		register(EntityWolf.class, EnumSpeciesType.BEAST);
		register(EntityVillager.class, EnumSpeciesType.HUMANOID, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntitySiegeTunneler.class, GENERIC_INORGANIC);
		register(EntityBOTPrimeBody.class, GENERIC_INORGANIC);
		register(EntityBOTPrimeHead.class, GENERIC_INORGANIC);
		register(EntityGhost.class, EnumSpeciesType.GHOST, EnumSpeciesType.YOUKAI);
		register(EntityIronGolem.class, EnumSpeciesType.HUMANOID, EnumSpeciesType.INORGANIC);
		register(EntitySnowman.class, GENERIC_INORGANIC);
		register(EntityBlaze.class, EnumSpeciesType.BEAST, EnumSpeciesType.INORGANIC, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntityCreeper.class, EnumSpeciesType.OTHER, EnumSpeciesType.PLANT, EnumSpeciesType.YOUKAI);
		register(EntityCyberCrab.class, GENERIC_INORGANIC);
		register(EntityTaintCrab.class, GENERIC_INORGANIC);
		register(EntityTeslaCrab.class, GENERIC_INORGANIC);
		register(EntityEnderman.class, EnumSpeciesType.HUMANOID, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntityFBI.class, EnumSpeciesType.HUMANOID);
		register(EntityMaskMan.class, EnumSpeciesType.HUMANOID, EnumSpeciesType.INORGANIC, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntityNuclearCreeper.class, EnumSpeciesType.OTHER, EnumSpeciesType.PLANT, EnumSpeciesType.YOUKAI);
		register(EntityRADBeast.class, EnumSpeciesType.BEAST, EnumSpeciesType.INORGANIC, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntitySiegeSkeleton.class, EnumSpeciesType.HUMANOID, EnumSpeciesType.INORGANIC, EnumSpeciesType.OTHER);
		register(EntitySiegeZombie.class, EnumSpeciesType.HUMANOID, EnumSpeciesType.INORGANIC, EnumSpeciesType.OTHER);
		register(EntitySilverfish.class, EnumSpeciesType.BEAST, EnumSpeciesType.OTHER);
		register(EntitySkeleton.class, EnumSpeciesType.HUMANOID, EnumSpeciesType.YOUKAI);
		register(EntitySpider.class, EnumSpeciesType.BEAST, EnumSpeciesType.INSECT, EnumSpeciesType.YOUKAI);
		register(EntityCaveSpider.class, EnumSpeciesType.BEAST, EnumSpeciesType.INSECT, EnumSpeciesType.YOUKAI);
		register(EntityTaintedCreeper.class, EnumSpeciesType.OTHER, EnumSpeciesType.PLANT, EnumSpeciesType.YOUKAI);
		register(EntityWitch.class, EnumSpeciesType.HUMANOID, EnumSpeciesType.YOUKAI);
		register(EntityWither.class, EnumSpeciesType.BEAST, EnumSpeciesType.FLYING, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntityZombie.class, EnumSpeciesType.HUMANOID, EnumSpeciesType.YOUKAI);
		register(EntityPigZombie.class, EnumSpeciesType.BEAST, EnumSpeciesType.HUMANOID, EnumSpeciesType.YOUKAI);
		register(EntitySquid.class, EnumSpeciesType.AQUATIC, EnumSpeciesType.BEAST);
		register(EntityDragon.class, EnumSpeciesType.BEAST, EnumSpeciesType.DRAGON, EnumSpeciesType.FLYING, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntityGhast.class, EnumSpeciesType.FLYING, EnumSpeciesType.GHOST, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntityHunterChopper.class, AIRCRAFT);
		register(EntityUFO.class, AIRCRAFT);
		register(EntitySiegeCraft.class, AIRCRAFT);
		register(EntitySiegeUFO.class, AIRCRAFT);
		register(EntitySlime.class, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		register(EntityMagmaCube.class, EnumSpeciesType.INORGANIC, EnumSpeciesType.OTHER, EnumSpeciesType.YOUKAI);
		
		register(EntityCombineOrdinal.class, EnumSpeciesType.HUMANOID);
	}
	
	@SafeVarargs
	public static void register(Class<? extends EntityLivingBase> clazz, EnumSpeciesType... types)
	{
		REGISTRY.put(clazz, Sets.immutableEnumSet(Arrays.asList(types)));
	}
	
	public static Set<EnumSpeciesType> getTypes(Class<? extends EntityLivingBase> clazz)
	{
		return REGISTRY.getOrDefault(clazz, DEFAULT_SET);
	}
	
	public static Set<EnumSpeciesType> getTypes(EntityLivingBase entity)
	{
		if (entity instanceof EntityPlayer)
			return getPlayerTypes(entity.getPersistentID());
		return getTypes(entity.getClass());
	}
	
	@SafeVarargs
	public static void registerPlayer(UUID id, EnumSpeciesType... types)
	{
		PLAYER_REGISTRY.put(id, EnumSet.copyOf(Arrays.asList(types)));
	}
	
	@SafeVarargs
	public static void registerPlayer(EntityPlayer player, EnumSpeciesType... types)
	{
		registerPlayer(player.getPersistentID(), types);
	}
	
	public static Set<EnumSpeciesType> getPlayerTypes(UUID id)
	{
		PLAYER_REGISTRY.putIfAbsent(id, PLAYER_SET);
		return PLAYER_REGISTRY.get(id);
	}
	
	public static Set<EnumSpeciesType> getPlayerTypes(EntityPlayer player)
	{
		return getPlayerTypes(player.getPersistentID());
	}
	
	public static Map<Class<? extends EntityLivingBase>, Set<EnumSpeciesType>> getRegistry()
	{
		return ImmutableMap.copyOf(REGISTRY);
	}
	
	public static Map<UUID, Set<EnumSpeciesType>> getPlayerRegistry()
	{
		return ImmutableMap.copyOf(PLAYER_REGISTRY);
	}
	
}
