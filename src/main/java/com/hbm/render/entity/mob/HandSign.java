package com.hbm.render.entity.mob;

/**
 * Hand signal animations that commanding smart NPCs may perform in combat while giving out orders.
 * @author UFFR
 *
 */
public enum HandSign
{
	/**Orders units to attack or charge**/
	ATTACK,
	/**Tells units to move somewhere, but not necessarily attack**/
	FORWARD,
	/**Move or point out left**/
	LEFT,
	/**Move or point out right**/
	RIGHT,
	/**Stop moving**/
	STOP,
	/**Group up around commander**/
	GROUP,
	/**Take cover from danger or enemies**/
	TAKE_COVER;
}
