package com.hbm.render.entity.mob;

/**
 * Poses for corpses.<br>
 * "Decapitated" means missing or mostly missing a head, usually from headshots.<br>
 * "Damaged" just means the body is severely damaged, usually from something like an explosion. May be minor like missing limbs to major like significant destruction of the torso.<br>
 * {@link #OVER_WALL} and the damaged types may also apply to hanging out of windows.<br>
 * {@link #NONE} means that there will be no corpse at all.
 * @author UFFR
 *
 */
public enum DeathPose
{
	NONE,
	
	AGAINST_WALL,
	AGAINST_WALL_DECAPITATED,
	AGAINST_WALL_DAMAGED,
	
	ON_SIDE,
	ON_SIDE_DECAPITATED,
	ON_SIDE_DAMAGED,
	
	ON_BACK,
	ON_BACK_DECAPITATED,
	ON_BACK_DAMAGED,
	
	ON_STOMACH,
	ON_STOMACH_DECAPITATED,
	ON_STOMACH_MUTIALTED,
	
	OVER_WALL,
	OVER_WALL_DECAPITATED,
	OVER_WALL_DAMAGED,
	
	OVER_LEDGE,
	OVER_LEDGE_DECAPITATED,
	OVER_LEDGE_DAMAGED,
	
	FAMILY_GUY;
}
