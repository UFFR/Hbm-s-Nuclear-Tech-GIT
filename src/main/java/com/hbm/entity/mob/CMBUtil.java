package com.hbm.entity.mob;

import java.util.HashSet;

import com.hbm.lib.Library;

public class CMBUtil
{
	/**
	 * Each type of unit has different lines<br>
	 * {@link #APF} {@link #ECHO} {@link #ORDINAL} {@link #WALLHAMMER}
	 * @author UFFR
	 * 
	 */
	public enum UnitType
	{
		/**(Short for "Antibody Protection Force") Carry HMGs, very heavy armor, role of suppressor. Limited to one per squad.**/
		APF(50, 50),
		/**Carry SMGs, no armor except of the head. Bulk of the squad, comparatively unskilled, at least two per.**/
		ECHO(20, 5),
		/**Carry the OSIPR, medium armor. Commander of the squad.**/
		ORDINAL(25, 20),
		/**Carry heavy shotguns, very heavy armor, role of tank and charger. Limited to one per squad.**/
		WALLHAMMER(75, 70);
		private final int health;
		private final int armor;
		private UnitType(int hp, int ar)
		{
			health = hp;
			armor = ar;
		}
		public int getHealth()
		{
			return health;
		}
		public int getArmor()
		{
			return armor;
		}
	}
	/**
	 * {@link #CALLOUT} {@link #COMBAT_IDLE} {@link #IDLE} {@link #MISC}
	 * @author UFFR
	 *
	 */
	public enum LineCategory
	{
		/**Describing engaged entities or other information**/
		CALLOUT,
		/**In a fight, but not actively engaging yet**/
		COMBAT_IDLE,
		/**Not doing anything, just waiting for orders or hostiles**/
		IDLE,
		/**When none of the above apply**/
		MISC;
		public final HashSet<VoiceLine> lines = new HashSet<>();
	}
	/**
	 * Most units have the same voice lines<br>
	 * @author UFFR
	 *
	 */
	public enum VoiceLine
	{
		/**"Overwatch, we have active hostiles, advise preparations"**/
		ACTIVE_HOSTILES(LineCategory.COMBAT_IDLE),
		/**"Callout code: Delta Echo 7 Niner Alpha 2 Winder 0 (DE79A2W0), confirmed"**/
		CALLOUT_CODE(LineCategory.IDLE),
		/**"14, 1, 6, 3, 11, and your turn, over"**/
		CHECK_IN(LineCategory.IDLE),
		/**"Copy that, forming Condition Alpha" - Don't know exactly what it means**/
		CONDITION_ALPHA(LineCategory.COMBAT_IDLE),
		/**"Copy that, contain and control is underway"**/
		CONTAIN_CONTROL(LineCategory.COMBAT_IDLE),
		/**"Copy that"**/
		COPY(LineCategory.IDLE),
		/**"Disregarding last call" - Not sure what that means or when it would be brought up**/
		DISREGARDING(LineCategory.COMBAT_IDLE),
		/**"Extending conflict duration" - Not sure what it means, perhaps when an engagement exceeds the predicted duration?**/
		EXTENDING_DURATION(LineCategory.COMBAT_IDLE),
		/**"Forming tactical advantage"**/
		FORMING_ADVANTAGE(LineCategory.COMBAT_IDLE),
		/**"Still holding" - For when engagement hasn't happened for a while I assume**/
		HOLDING(LineCategory.COMBAT_IDLE),
		/**"Holding for biodat upload" - Requesting hostile information I assume**/
		HOLDING_UPLOAD(LineCategory.COMBAT_IDLE),
		/**"Negative, no daggers needed" - Has sufficient ammo**/
		NO_AMMO_NEEDED(LineCategory.COMBAT_IDLE),
		/**"Negative movement"**/
		NO_MOVEMENT(LineCategory.IDLE),
		/**"Nothing to report, over"**/
		NO_REPORT(LineCategory.IDLE),
		/**"Preparing comms for extended conflict" - For long engagements I guess?**/
		PREPARING_COMMS(LineCategory.COMBAT_IDLE),
		/**"Preparing for Sundown" - Not sure, maybe literal?**/
		PREPARING_SUNDOWN(LineCategory.COMBAT_IDLE),
		/**"10-4, negative on dagger renew, locked and ready"**/
		READY(LineCategory.COMBAT_IDLE),
		/**"Confirmed, recalibrating" - Updating for new tactics I assume**/
		RECALIBRATE(LineCategory.COMBAT_IDLE),
		/**"Copy, accepted, updating cognitive suppression biodats. Update complete in T-3 seconds... dissonance resolved"**/
		REFRESH(LineCategory.IDLE),
		/**"Overwatch, 10-niner (10-9)" - Perhaps for bad radio transmission?**/
		REPEAT(LineCategory.COMBAT_IDLE),
		/**"Sector is tracking at 43%, stable. Still holding for transmission, over"<br>"Stabilization Team holding position, lightviz(?) 73, audibles at 15. Overwatch confirms HVT is perimeter inbound, stay alert"**/
		REPORTING(LineCategory.IDLE),
		/**"Overwatch, requesting proximity data on hostiles"**/
		REQUESTING_DATA(LineCategory.IDLE),
		/**"Overwatch is requesting all units refresh and resolve. Cognitive dissonance will not be tolerated. Negative, memories included. Update is live, acceptance is mandatory"**/
		REQUEST_REFRESH(LineCategory.IDLE),
		/**"Resolving" - Engaging enemy**/
		RESOLVING(LineCategory.COMBAT_IDLE),
		/**"Scanning hostile biodats" - Checking the info on hostile(s)**/
		SCANNING(LineCategory.COMBAT_IDLE),
		/**"Overwatch is confirming sector lockdown, keep sightlines open and sound on"**/
		SECTOR_LOCKDOWN(LineCategory.IDLE),
		/**"Copy, setting up position" - Given a location for tactical advantage**/
		SETTING_POSITION(LineCategory.COMBAT_IDLE),
		/**"Affirmative, low sound, standing by for opportunities"**/
		STANDBY(LineCategory.IDLE),
		/**"Roger that, still here"**/
		STILL_HERE(LineCategory.IDLE),
		/**"Negative, target is still active" - Hostile is still alive**/
		TARGET_ACTIVE(LineCategory.COMBAT_IDLE),
		/**"Affirmative, outbreak status is code Delta 7, Team Sundown is on contact overrun"**/
		TEAM_SUNDOWN(LineCategory.IDLE),
		/**"They're still alive" - Could be referring to how the hostile is still alive, but it could also be used for allied forces that lost contact**/
		THEYRE_ALIVE(LineCategory.COMBAT_IDLE),
		/**"Transmission clear, negative on Condition Quicksand. Radials clear, over"**/
		TRANSMISSION_CLEAR(LineCategory.IDLE),
		/**"Audibles 3, lightviz 15"**/
		UNIT_REPORT(LineCategory.IDLE),
		/**"Copy that, sustaining viscon in sector Nomad Kilo 3 - 6 (NK3-6)"**/
		VISCON(LineCategory.IDLE),
		/**"Copy that, no sector containment, no confirmation on target 1 vector. Waiting for seconday parameter codes"**/
		WAITING_FOR_CODES(LineCategory.IDLE),
		/**"Sending vicerator to Apex 5, we have biotics at the perimeter, over"**/
		WAITING_FOR_CONTAICT(LineCategory.IDLE);
		public final LineCategory category;
		private VoiceLine(LineCategory cat)
		{
			category = cat;
			cat.lines.add(this);
		}
	}

	public static String generateVoiceLine(UnitType type, VoiceLine line)
	{
		if (line.category == LineCategory.MISC)
			return String.format("entity.cmb.%s.%s", type.toString().toLowerCase(), line.toString().toLowerCase());
		else
			return String.format("entity.cmb.%s.%s.%s", type.toString().toLowerCase(), line.category.toString().toLowerCase(), line.toString().toLowerCase());
	}
	/**
	 * Get a random line from a category (use with the appropriate category)
	 * @param cat The desired category
	 * @return A random VoiceLine from that category
	 */
	public static VoiceLine randomLine(LineCategory cat)
	{
		return Library.randomFromCollection(cat.lines);
	}
}
