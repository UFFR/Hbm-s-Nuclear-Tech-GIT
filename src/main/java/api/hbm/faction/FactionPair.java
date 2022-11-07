package api.hbm.faction;

import java.util.Objects;

public class FactionPair
{
	private final Faction faction1, faction2;

	public FactionPair(Faction faction1, Faction faction2)
	{
		this.faction1 = faction1;
		this.faction2 = faction2;
	}
	
	public Faction getFaction1()
	{
		return faction1;
	}
	
	public Faction getFaction2()
	{
		return faction2;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(faction1, faction2);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof FactionPair))
			return false;
		final FactionPair other = (FactionPair) obj;
		return (Objects.equals(faction1, other.faction1) && Objects.equals(faction2, other.faction2))
				|| (Objects.equals(faction1, other.faction2) && Objects.equals(faction2, other.faction1));
	}
	
	@Override
	public String toString()
	{
		return faction1.getRegistryName() + '&' + faction2.getRegistryName();
	}
}
