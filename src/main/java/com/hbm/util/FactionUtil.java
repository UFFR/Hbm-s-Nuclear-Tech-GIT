package com.hbm.util;

import java.util.ArrayList;

import com.google.common.annotations.Beta;

public class FactionUtil
{
	public static final ArrayList<Faction> factionRegistry = new ArrayList<>();
//	public enum EnumNation
//	{
//		NONE,
//		UCFS,
//		UFFR,
//		LUNAR,
//		CMB;
//		public String toLoc()
//		{
//			return I18nUtil.resolveKey("factions.nation." + toString().toLowerCase());
//		}
//	}
	
	@Beta
	public static abstract class Faction
	{
		public final String unlocalized;
		public Faction(String uloc)
		{
			unlocalized = uloc;
		}
		
		public final String toLoc()
		{
			return I18nUtil.resolveKey("factions.nation." + unlocalized.toLowerCase());
		}
		
		@Override
		public String toString()
		{
			return unlocalized;
		}
		
		public abstract String randomDivision();
	}

	public enum EnumTreaty
	{
		NONE,
		NO_AGGRESSION,
		PEACE,
		TRADE,
		DEFENSE,
		OFFENSE,
		BROTHERS;
		public String toLoc()
		{
			return I18nUtil.resolveKey("factions.treaty." + toString().toLowerCase());
		}
	}

	public enum EnumRelation
	{
		VITRIOL,
		HATE,
		RIVAL,
		NEGATIVE,
		NEUTRAL,
		POSITIVE,
		FRIEND,
		ALLY,
		BROTHER;
		public String toLoc()
		{
			return I18nUtil.resolveKey("factions.relation." + toString().toLowerCase());
		}
	}

}
