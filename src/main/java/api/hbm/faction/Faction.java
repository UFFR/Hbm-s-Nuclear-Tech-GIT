package api.hbm.faction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.annotations.Beta;
import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.main.MainRegistry;
import com.hbm.util.ArraySet;
import com.hbm.util.I18nUtil;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Nation states, rouge nations, or other organized groups.<br>
 * Have relations and possible treaties with each other and players.
 * @author UFFR
 *
 */
@Beta
public abstract class Faction implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2881160717854004206L;

	public enum EnumTreaty
	{
		NONE,
		NO_AGGRESSION,
		PEACE,
		TRADE,
		DEFENSE,
		OFFENSE,
		BROTHERS;
		public String toLoc()
		{
			return I18nUtil.resolveKey("factions.treaty." + toString().toLowerCase());
		}
	}

	public enum EnumRelation
	{
		VITRIOL,
		HATE,
		RIVAL,
		NEGATIVE,
		NEUTRAL,
		POSITIVE,
		FRIEND,
		ALLY,
		BROTHER;
		public String toLoc()
		{
			return I18nUtil.resolveKey("factions.relation." + toString().toLowerCase());
		}
	}
	
	private static final Set<String> REGISTERED_SET = new HashSet<String>();
	private static final Map<FactionPair, EnumTreaty> TREATY_MAP = new HashMap<FactionPair, EnumTreaty>();
	
	protected final Set<String> registeredNames = new HashSet<String>();
	protected final List<SquadBase> activeSquads = new ArraySet<SquadBase>();
	private final Map<Faction, EnumRelation> relationMap = new HashMap<Faction, EnumRelation>();
	private final Map<UUID, EnumRelation> playerRelationMap = new HashMap<UUID, EnumRelation>();
	private final String registryName;
	protected World world;
	public Faction(String registryName, World world)
	{
		if (REGISTERED_SET.contains(registryName))
			throw new IllegalStateException("Name [" + registryName + "] already registered!");
		else
			REGISTERED_SET.add(registryName);
		
		this.registryName = registryName;
		this.world = world;
	}
	
	public abstract void constructAndDeploySquad(ILocationProvider loc);
	public abstract List<SquadBuilder> getSquadBuilders();
	public abstract boolean isValidTarget(EntityLivingBase entity);
	
	public String getRegistryName()
	{
		return registryName;
	}
	
	public Set<String> getRegisteredNames()
	{
		return registeredNames;
	}
	
	private void checkInvalid()
	{
		if (relationMap.containsKey(this))
		{
			MainRegistry.logger.warn(new IllegalStateException("Relationship map has self-reference, this should not be possible!"));
			relationMap.remove(this);
		}
	}
	
	public Faction setRelationship(Faction faction, EnumRelation relation)
	{
		relationMap.put(faction, relation);
		checkInvalid();
		return this;
	}
	
	public EnumRelation getRelationship(Faction faction)
	{
		relationMap.putIfAbsent(faction, EnumRelation.NEUTRAL);
		checkInvalid();
		return relationMap.get(faction);
	}
	
	public Faction setPlayerRelation(UUID id, EnumRelation relation)
	{
		playerRelationMap.put(id, relation);
		return this;
	}
	
	public Faction setPlayerRelation(EntityPlayer player, EnumRelation relation)
	{
		return setPlayerRelation(player.getPersistentID(), relation);
	}
	
	public EnumRelation getPlayerRelationship(UUID id)
	{
		playerRelationMap.putIfAbsent(id, EnumRelation.NEUTRAL);
		return playerRelationMap.get(id);
	}
	
	public EnumRelation getPlayerRelationship(EntityPlayer player)
	{
		return getPlayerRelationship(player.getPersistentID());
	}
	
	protected boolean shouldTargetPlayer(EntityPlayer player)
	{
		return getPlayerRelationship(player).ordinal() < EnumRelation.NEUTRAL.ordinal();
	}
	
	// TODO
	@Override
	public abstract boolean equals(Object obj);
	@Override
	public abstract int hashCode();
	@Override
	public abstract String toString();
	
	public static void setTreaty(Faction faction1, Faction faction2, EnumTreaty treaty)
	{
		TREATY_MAP.put(new FactionPair(faction1, faction2), treaty);
	}
	
	public static EnumTreaty getTreaty(Faction faction1, Faction faction2)
	{
		final FactionPair pair = new FactionPair(faction1, faction2);
		TREATY_MAP.putIfAbsent(pair, EnumTreaty.NONE);
		return TREATY_MAP.get(pair);
	}
	
}
