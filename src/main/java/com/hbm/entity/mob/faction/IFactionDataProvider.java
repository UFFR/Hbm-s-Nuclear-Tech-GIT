package com.hbm.entity.mob.faction;

import com.hbm.entity.mob.faction.NameUtil.EnumRank;
import com.hbm.util.FactionUtil.Faction;

public interface IFactionDataProvider
{
	public String getName();
	public Faction getFaction();
	public boolean hasSquad();
	public String getSquad();
	public String getSquadName();
	public boolean rankApplicable();
	public EnumRank getRank();
}
