package com.hbm.entity.mob.ai.squad;

public enum SquadTask
{
	/**Scan area for enemies and/or player (relation irrelevant)**/
	PATROL,
	/**Protect an area from enemies and/or hostile player**/
	GUARD,
	/**Kill all enemies in area, possibly including player**/
	SECURE,
	/**Specifically target an enemy, usually player**/
	ELIMINATE;
}
