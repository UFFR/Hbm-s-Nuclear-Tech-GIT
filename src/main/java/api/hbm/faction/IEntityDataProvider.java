package api.hbm.faction;

import javax.annotation.CheckForNull;

public interface IEntityDataProvider
{
	/**
	 * Gets the NPC's individual name.
	 * @return The name in string format.
	 */
	public String getName();
	/**
	 * Gets the squad's name, not to be confused with {@link #getMySquadName()}.
	 * @return The squad's name in string format or empty if N/A.
	 */
	public String getSquadName();
	/**
	 * Gets this NPC's name within the squad, if applicable, not to be confused with {@link #getSquadName()}.
	 * @return This NPC's name within its squad or empty if N/A.
	 */
	public String getMySquadName();
	/**
	 * Gets this NPC's rank, if applicable.
	 * @return The rank or none.
	 */
	@CheckForNull
	public EnumRank getRank();
	/**
	 * Gets this NPC's faction, if applicable.
	 * @return The faction reference or null.
	 */
	@CheckForNull
	public Faction getFaction();
	/**
	 * If this NPC is currently in a squad.
	 * @return True if {@link #getSquadName()} and {@link #getMySquadName()} will return non-empty strings, false if not in a squad.
	 */
	public boolean hasSquad();
	/**
	 * If this NPC bears a (standard) rank.
	 * @return True if {@link #getRank()} is non-null, false otherwise.
	 */
	public boolean hasRank();
	/**
	 * If this NPC is in a faction
	 * @return True if {@link #getFaction()} is an enlisted rank, false if otherwise.
	 */
	public boolean hasFaction();
	
	public default String toDisplay()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("Name: ").append(getName()).append('\n');
		builder.append("Faction: ").append(hasFaction() ? getFaction() : "[None]").append('\n');
		builder.append("Rank: ").append(hasRank() ? getRank() : "[N/A]").append('\n');
		builder.append("Squad: ").append(hasSquad() ? getSquadName() : "[None]").append('\n');
		builder.append("Squad Name: ").append(hasSquad() ? getMySquadName() : "[N/A]").append('\n');
		return builder.toString();
	}
	
	public default String toDisplayShort()
	{
		final StringBuilder builder = new StringBuilder();
		if (hasRank())
			builder.append(getRank()).append(' ').append(getName());
		else
			builder.append(getName());
		builder.append('\n').append(hasFaction() ? getFaction() : "[No faction]");
		return builder.toString();
	}
	
}
