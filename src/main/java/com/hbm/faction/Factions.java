package com.hbm.faction;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import api.hbm.faction.Faction;
import net.minecraft.world.World;

public class Factions
{
	private static final Map<String, Faction> FACTION_MAP = new HashMap<String, Faction>();
	
	static Faction COMBINE;
	
	public static void initialize()
	{
		COMBINE = registerFaction(FactionCombine.getCombine());
		
	}
	
	public static void prepare(World world)
	{
		for (Faction faction : FACTION_MAP.values())
			faction.setWorld(world);
	}
	
	private static Faction registerFaction(Faction faction)
	{
		FACTION_MAP.put(faction.getRegistryName(), faction);
		return faction;
	}
	
	public static void updateFactions()
	{
		FACTION_MAP.values().forEach(Faction::update);
	}

	public static Faction getFaction(String key)
	{
		return FACTION_MAP.get(key);
	}
	
	public static Map<String, Faction> getFactionMap()
	{
		return ImmutableMap.copyOf(FACTION_MAP);
	}
}
