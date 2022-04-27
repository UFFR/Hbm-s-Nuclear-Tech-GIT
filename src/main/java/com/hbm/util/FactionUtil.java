package com.hbm.util;

public class FactionUtil
{

	public enum EnumNation
	{
		UCFS,
		UFFR,
		LUNAR,
		CMB;
		public String toLoc()
		{
			return I18nUtil.resolveKey("factions.nation." + toString().toLowerCase());
		}
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
