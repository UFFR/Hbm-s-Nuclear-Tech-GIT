package com.hbm.entity.mob.smart;

/**
 * Basic AI core tasks.
 * @author UFFR
 *
 */
public enum NPCTask
{
	/// Non-combat tasks, interrupted when combat occurs
	/**Stay in a general place**/
	HOLD,
	/**Look around in a general place to ensure security**/
	PATROL,
	/**Move to some place**/
	ADVANCE,
	/**Stay in a very specific place, persists through combat**/
	GUARD,
	/**Unidentified sound or movement, may be an enemy or nothing**/
	INVESTIGATE,
	
	/**Group up around commander, if applicable**/
	GROUP,
	
	/// Combat tasks, conclude when all enemies dead or cannot be found for a long time
	/**Shoot at enemy, may be done behind cover**/
	TARGET,
	/**Try to find cover and/or stay there**/
	COVER,
	/**Shoot at enemy whether or not hitting is guaranteed**/
	SUPPRESS,
	/**Attempt to get behind enemy and around cover**/
	FLANK,
	/**Break down door or wall**/
	BREACH,
	/**Attempt to destroy the enemy's cover**/
	DESTROY_COVER,
	/**Attempt to force enemy out of cover with a grenade or alt-fire**/
	FLUSH,
	/**Charge in on enemy**/
	PRESSURE,
	/**Pull back, in case of danger or enemy getting too close**/
	RIPCORD,
	/**Hold position and fire until enemy exposes self**/
	OVERWATCH,
	
	/// Idle combat tasks, no direct engagement but is ready
	/**Search for lost enemy**/
	SWEEP;
}
