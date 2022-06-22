package com.hbm.config;

import static com.hbm.config.CommonConfig.CATEGORY_FACTIONS;
import static com.hbm.util.FactionUtil.factionRegistry;

import net.minecraftforge.common.config.Configuration;

public class FactionConfig
{
	public static boolean enableFactionMode = false;
	public static boolean enableFriendlyFire = false;
	public static boolean enableDefaultFriendlyEarth = true;
	public static final boolean[] enableFactions = new boolean[factionRegistry.size()];
	
	public static void loadFromConfig(Configuration config)
	{
		enableFactionMode = config.getBoolean(CATEGORY_FACTIONS, "14.00_enableFactionMode", false, "Enables faction mode. Think of it like a more intuitive Seige Mode, but you also have NPC friendlies.");
		enableFriendlyFire = config.getBoolean(CATEGORY_FACTIONS, "14.01_enableFriendlyFire", false, "Allows members of a faction to accidentally injure each other with direct attacks. Keep in mind, this includes *you*.");
		enableDefaultFriendlyEarth = config.getBoolean(CATEGORY_FACTIONS, "14.02_enableDefaultFriendlyEarth", true, "When enabled, the \"United Earth\" faction is by friendly with players right off the bat. Otherwise, they are neutral.");
		
		for (int i = 1; i < factionRegistry.size(); i++)
			enableFactions[i - 1] = config.getBoolean(CATEGORY_FACTIONS, String.format("14.F%s_%s", i, factionRegistry.get(i).toString()), true, String.format("Enables the: [%s] faction", factionRegistry.get(i).toLoc()));
	}
}
