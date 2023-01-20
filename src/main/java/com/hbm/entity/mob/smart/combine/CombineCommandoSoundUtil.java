package com.hbm.entity.mob.smart.combine;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.hbm.lib.Library;

public class CombineCommandoSoundUtil
{
	private static final Map<LineCategory, Set<VoiceLine>> CATEGORY_MAP = new EnumMap<>(LineCategory.class);
	
	static
	{
		for (LineCategory category : LineCategory.values())
			CATEGORY_MAP.put(category, EnumSet.noneOf(VoiceLine.class));
		
		for (VoiceLine line : VoiceLine.values())
			CATEGORY_MAP.get(line.category).add(line);
	}
	
	/**
	 * Each type of unit has different recorded lines, but the contents are roughly the same.<br>
	 * {@link #APF} {@link #ECHO} {@link #ORDINAL} {@link #WALLHAMMER}
	 * @author UFFR
	 * 
	 */
	public enum UnitType
	{
		/**(Short for "Antibody Protection Force") Carry HMGs, very heavy armor, role of suppressor. Limited to one per squad.**/
		APF,
		/**Carry SMGs, no armor except of the head. Bulk of the squad, at least two per.**/
		ECHO,
		/**Carry the OSIPR, medium armor. Commander of the squad.**/
		ORDINAL,
		/**Carry heavy shotguns, very heavy armor, role of tank and charger. Limited to one per squad.**/
		WALLHAMMER;
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
		/**Not necessarily literal, could just be pointing out hostile weaknesses to the squad**/
		TAUNT,
		/**When none of the above apply**/
		MISC;
//		final Set<VoiceLine> lines = EnumSet.noneOf(VoiceLine.class);
	}
	/**
	 * Most units have the same voice lines, but check first.<br>
	 * JavaDoc should have the approximate caption.<br>
	 * @author UFFR
	 *
	 */
	public enum VoiceLine
	{
		/**"Overwatch, we have active hostiles, advise preparations"**/
		ACTIVE_HOSTILES(LineCategory.COMBAT_IDLE, "Overwatch, we have active hostiles."),
		/**"Suspect is above legal aggression threshold, taking advantage" - Your guess is as good as mine**/
		AGGRESSION_THRESHOLD(LineCategory.TAUNT, "Suspect is above legal aggression threshold."),
		/**"Callout code: Delta Echo 7 Niner Alpha 2 Winder 0 (DE79A2W0), confirmed"**/
		CALLOUT_CODE(LineCategory.IDLE, "Delta Echo 7 9 Alpha Winder 0, confirmed."),
		/**"14, 1, 6, 3, 11, and your turn, over"**/
		CHECK_IN(LineCategory.IDLE, "*Counting*, and your turn, over."),
		/**"Standing down, code 10-40" - 10-40 means "silent running", could have multiple meanings given that**/
		CODE_10_40(LineCategory.TAUNT, "Standing down, code 10-40."),
		/**"Activating code 10-108, standing down" - 10-108 means "officer down", so the use is somewhat unusual**/
		CODE_10_108(LineCategory.TAUNT, "Activating code 10-108, standing down."),
		/**"Copy that, forming Condition Alpha" - Don't know exactly what it means**/
		CONDITION_ALPHA(LineCategory.COMBAT_IDLE, "Forming Condition Alpha."),
		/**"Copy that, contain and control is underway"**/
		CONTAIN_CONTROL(LineCategory.COMBAT_IDLE, "Contain and control is underway."),
		/**"Copy that"**/
		COPY(LineCategory.IDLE, "Copy that."),
		/**[Death]**/
		DEATH(LineCategory.MISC, "*Flatline*"),
		/**"Disregarding last call" - Not sure what that means or when it would be brought up**/
		DISREGARDING(LineCategory.COMBAT_IDLE, ""),
		/**"Extending conflict duration" - Not sure what it means, perhaps when an engagement exceeds the predicted duration?**/
		EXTENDING_DURATION(LineCategory.COMBAT_IDLE, "Extending conflict duration."),
		/**"Forming tactical advantage"**/
		FORMING_ADVANTAGE(LineCategory.COMBAT_IDLE, "Forming tactical advantage."),
		/**"Still holding" - For when engagement hasn't happened for a while I assume**/
		HOLDING(LineCategory.COMBAT_IDLE, "Still holding."),
		/**"Holding for biodat upload" - Requesting hostile information I assume**/
		HOLDING_UPLOAD(LineCategory.COMBAT_IDLE, "Holding for biodat upload"),
		/**[Hurt/pain]**/
		HURT(LineCategory.MISC, "*Hurt*"),
		/**"Negative, no daggers needed" - Has sufficient ammo**/
		NO_AMMO_NEEDED(LineCategory.COMBAT_IDLE, "Negative, no daggers needed."),
		/**"Negative movement"**/
		NO_MOVEMENT(LineCategory.IDLE, "Negative movement."),
		/**"Nothing to report, over"**/
		NO_REPORT(LineCategory.IDLE, "Nothing to report, over."),
		/**"Preparing comms for extended conflict" - For long engagements I guess?**/
		PREPARING_COMMS(LineCategory.COMBAT_IDLE, "Preparing comms for extended conflict."),
		/**"Preparing for Sundown" - Not sure, maybe literal?**/
		PREPARING_SUNDOWN(LineCategory.COMBAT_IDLE, "Preparing for Sundown."),
		/**"10-4, negative on dagger renew, locked and ready"**/
		READY(LineCategory.COMBAT_IDLE, "10-4, negative on dagger renew, locked and ready."),
		/**"Confirmed, recalibrating" - Updating for new tactics I assume**/
		RECALIBRATE(LineCategory.COMBAT_IDLE, "Confirmed, recalibrating..."),
		/**"Copy, accepted, updating cognitive suppression biodats. Update complete in T-3 seconds... dissonance resolved"**/
		REFRESH(LineCategory.IDLE, "Updating congitive suppression biodats..."),
		/**"Overwatch, 10-niner (10-9)" - Perhaps for bad radio transmission?**/
		REPEAT(LineCategory.COMBAT_IDLE, "Overwatch, 10-9."),
		/**"Sector is tracking at 43%, stable. Still holding for transmission, over"<br>"Stabilization Team holding position, lightviz(?) 73, audibles at 15. Overwatch confirms HVT is perimeter inbound, stay alert"**/
		REPORTING(LineCategory.IDLE, "Sector stable. Still holding."),
		/**"Overwatch, requesting proximity data on hostiles"**/
		REQUESTING_DATA(LineCategory.IDLE, "Overwatch, requesting proximity data on hostiles."),
		/**"Overwatch is requesting all units refresh and resolve. Cognitive dissonance will not be tolerated. Negative, memories included. Update is live, acceptance is mandatory"**/
		REQUEST_REFRESH(LineCategory.IDLE, "Overwratch is requesting all units refresh and resolve."),
		/**"Resolving" - Engaging enemy**/
		RESOLVING(LineCategory.COMBAT_IDLE, "Resolving..."),
		/**"Scanning hostile biodats" - Checking the info on hostile(s)**/
		SCANNING(LineCategory.COMBAT_IDLE, "Scanning hostile biodats."),
		/**"Overwatch is confirming sector lockdown, keep sightlines open and sound on"**/
		SECTOR_LOCKDOWN(LineCategory.IDLE, "Overwatch is confirming sector lockdown, keep sightlines open and sound on."),
		/**"Copy, setting up position" - Given a location for tactical advantage**/
		SETTING_POSITION(LineCategory.COMBAT_IDLE, "Copy, setting up position."),
		/**"Affirmative, low sound, standing by for opportunities"**/
		STANDBY(LineCategory.IDLE, "Affirmative, low sound, standing by for opportunities."),
		/**"Roger that, still here"**/
		STILL_HERE(LineCategory.IDLE, "Roger that, still here."),
		/**"Negative, target is still active" - Hostile is still alive**/
		TARGET_ACTIVE(LineCategory.COMBAT_IDLE, "Negative, target is still active."),
		/**"Affirmative, outbreak status is code Delta 7, Team Sundown is on contact overrun"**/
		TEAM_SUNDOWN(LineCategory.IDLE, "Affirmative, outbreak status is Delta 7. Team Sundown is on contact overrun."),
		/**"They're still alive" - Could be referring to how the hostile is still alive, but it could also be used for allied forces that lost contact**/
		THEYRE_ALIVE(LineCategory.COMBAT_IDLE, "They're still alive."),
		/**"Transmission clear, negative on Condition Quicksand. Radials clear, over"**/
		TRANSMISSION_CLEAR(LineCategory.IDLE, "Transmission clear, negative on Condition Quicksand. Radials clear, over."),
		/**"Audibles 3, lightviz 15"**/
		UNIT_REPORT(LineCategory.IDLE, "Audibles 3, lightviz 15."),
		/**"Copy that, sustaining viscon in sector Nomad Kilo 3 - 6 (NK3-6)"**/
		VISCON(LineCategory.IDLE, "Copy that, sustaining viscon."),
		/**"Copy that, no sector containment, no confirmation on target 1 vector. Waiting for secondary parameter codes"**/
		WAITING_FOR_CODES(LineCategory.IDLE, "Copy that, no sector containment, no confirmation on Target 1 vector. Waiting for secondary parameter codes."),
		/**"Sending viscerator to Apex 5, we have biotics at the perimeter, over"**/
		WAITING_FOR_CONTACT(LineCategory.IDLE, "Sending viscerator to Apex 5, we have biotics at the perimeter, over.");
		public final LineCategory category;
		public final String caption;
		private VoiceLine(LineCategory category, String caption)
		{
			this.category = category;
			this.caption = caption;
		}
	}

	public static String generateVoiceLine(UnitType type, VoiceLine line)
	{
		if (line.category == LineCategory.MISC)
			return String.format("hbm:entity.cmb.%s.%s", type.toString().toLowerCase(), line.toString().toLowerCase());
		else
			return String.format("hbm:entity.cmb.%s.%s.%s", type.toString().toLowerCase(), line.category.toString().toLowerCase(), line.toString().toLowerCase());
	}
	/**
	 * Get a random line from a category (use with the appropriate category, such as idle)
	 * @param cat The desired category
	 * @return A random VoiceLine from that category
	 */
	public static VoiceLine randomLine(LineCategory cat)
	{
		return Library.randomFromCollection(CATEGORY_MAP.get(cat));
	}
}
