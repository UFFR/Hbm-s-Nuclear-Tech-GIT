package api.hbm.faction;

import javax.annotation.CheckForNull;

import com.hbm.entity.mob.ai.squad.SquadBase;

public interface IEntityDataProvider
{
	/**
	 * Gets the NPC's individual name.
	 * @return The name in string format.
	 */
	public String getName();
	/**
	 * Gets the squad's name, not to be confused with {@link #getSquadName()}.
	 * @return The squad the NPC is a part of or null if not using a squad.
	 */
	public SquadBase getSquad();
	/**
	 * Gets this NPC's name within the squad, if applicable, not to be confused with {@link #getSquad()}.
	 * @return This NPC's name within its squad or empty if N/A.
	 */
	public String getSquadName();
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
	 * @return True if {@link #getSquad()} and {@link #getSquadName()} will return non-empty strings or nonnull, false if not in a squad.
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
		builder.append("Rank: ").append(hasRank() ? getRank().abbreviation : "[N/A]").append('\n');
		builder.append("Squad: ").append(hasSquad() && getSquad() != null ? getSquad().getName() : "[None]").append('\n');
		builder.append("Squad Name: ").append(hasSquad() ? getSquadName() : "[N/A]").append('\n');
		return builder.toString();
	}
	
	public default String toDisplayShort()
	{
		final StringBuilder builder = new StringBuilder();
		if (hasRank())
			builder.append(getRank().abbreviation).append(' ').append(getName());
		else
			builder.append(getName());
		builder.append('\n').append(hasFaction() ? getFaction() : "[No faction]");
		return builder.toString();
	}
	
}
