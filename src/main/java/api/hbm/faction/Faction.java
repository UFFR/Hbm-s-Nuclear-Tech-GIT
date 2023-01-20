package api.hbm.faction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.apache.logging.log4j.Logger;

import com.google.common.annotations.Beta;
import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.faction.Factions;
import com.hbm.interfaces.IByteSerializable;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.lib.Library;
import com.hbm.main.DeserializationException;
import com.hbm.main.MainRegistry;
import com.hbm.util.I18nUtil;
import com.hbm.util.lists.ArraySet;

import bytes.ByteSequence;
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
public abstract class Faction implements IByteSerializable
{
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
	protected static final Logger LOGGER = MainRegistry.logger;
	private static final Map<String, Faction> REGISTRY = new HashMap<String, Faction>();
	private static final Map<FactionPair, EnumTreaty> TREATY_MAP = new HashMap<FactionPair, EnumTreaty>();
	
	protected final Set<String> registeredNames = new HashSet<String>();
	protected final List<SquadBase> activeSquads = new ArraySet<SquadBase>();
	protected final Map<String, SquadBase> squadNameRegistry = new HashMap<String, SquadBase>();
	private final Map<Faction, EnumRelation> relationMap = new HashMap<Faction, EnumRelation>();
	private final Map<UUID, EnumRelation> playerRelationMap = new HashMap<UUID, EnumRelation>();
	private final String registryName;
	protected World world;
	public Faction(String registryName)
	{
		if (REGISTRY.containsKey(registryName))
			throw new IllegalStateException("Name [" + registryName + "] already registered!");
		else
			REGISTRY.put(registryName, this);
		
		this.registryName = registryName;
	}
	
	public abstract void constructAndDeploySquad(ILocationProvider loc);
	public abstract List<SquadBuilder> getSquadBuilders();
	public abstract boolean isValidTarget(EntityLivingBase entity);
	public abstract boolean isBadTarget(EntityLivingBase entity);
	public abstract String defaultSquadName();
	public abstract void regenerateSquads(ByteSequence data, List<String> classes);
	public abstract String generateSquadName(Class<? extends SquadBase> clazz, Set<String> alreadyRegisteredNames);
	
	@OverridingMethodsMustInvokeSuper
	public void update()
	{
//		activeSquads.forEach(SquadBase::update);
		
		activeSquads.iterator().forEachRemaining(SquadBase::update);
		
		if (activeSquads.removeIf(SquadBase::isSquadDead))
			squadNameRegistry.keySet().retainAll(activeSquads.stream().map(SquadBase::getName).collect(Collectors.toList()));
	}
	
	public void setWorld(World world)
	{
		this.world = world;
	}
	
	public void addSquad(SquadBase squad)
	{
		activeSquads.add(squad.setName(generateSquadName(squad.getClass(), squadNameRegistry.keySet())));
		squadNameRegistry.put(squad.getName(), squad);
	}
	
	public SquadBase getSquad(String name)
	{
		return squadNameRegistry.get(name);
	}
	
	public void removeSquad(SquadBase squad)
	{
		activeSquads.remove(squad);
		squadNameRegistry.remove(squad.getName());
	}
	
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
			LOGGER.warn(new IllegalStateException("Relationship map has self-reference, this should not be possible!"));
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
	
	public void removeInvalid(Collection<? extends EntityLivingBase> entities)
	{
		final Iterator<? extends EntityLivingBase> iterator = entities.iterator();
		while (iterator.hasNext())
		{
			final EntityLivingBase entity = iterator.next();
			if (isBadTarget(entity) || entity.isDead || !entity.isEntityAlive())
				iterator.remove();
		}
	}
	
	@Override
	@OverridingMethodsMustInvokeSuper
	public void readFromBytes(ByteSequence buf)
	{
		// TODO Auto-generated method stub
		relationMap.clear();
		playerRelationMap.clear();
		registeredNames.clear();
		
		final byte[] relationBytes, playerRelationBytes, nameBytes, squadClassBytes;
		
		try
		{
			relationBytes = new byte[buf.readInt()];
			playerRelationBytes = new byte[buf.readInt()];
			nameBytes = new byte[buf.readInt()];
			squadClassBytes = new byte[buf.readInt()];
			
			buf.readBytes(relationBytes);
			buf.readBytes(playerRelationBytes);
			buf.readBytes(nameBytes);
			buf.readBytes(squadClassBytes);
			
			deserializeRelationMap(relationBytes, relationMap);
			deserializePlayerRelationMap(playerRelationBytes, playerRelationMap);
			registeredNames.addAll(Library.deserializeStringCollection(nameBytes));
			
			regenerateSquads(buf, Library.deserializeStringCollection(squadClassBytes));
		} catch (Exception e)
		{
			LOGGER.warn("Caught exception while deserializing faction data, must skip.");
			if (e instanceof DeserializationException)
				LOGGER.catching(e);
			else
				LOGGER.catching(new DeserializationException(e));
		}
	}
	
	@Override
	@OverridingMethodsMustInvokeSuper
	public void writeToBytes(ByteSequence buf)
	{
		// TODO Auto-generated method stub
		final byte[] relationBytes, playerRelationBytes, nameBytes, squadClassBytes;
		
		relationBytes = serializeRelationMap(relationMap);
		playerRelationBytes = serializePlayerRelationMap(playerRelationMap);
		nameBytes = Library.serializeStringCollection(registeredNames);
		squadClassBytes = Library.serializeStringCollection(squadClassNames(activeSquads));
		
		buf.writeInt(relationBytes.length).writeInt(playerRelationBytes.length).writeInt(nameBytes.length).writeInt(squadClassBytes.length);
		buf.writeBytes(relationBytes).writeBytes(playerRelationBytes).writeBytes(nameBytes).writeBytes(squadClassBytes);
		
		for (SquadBase squad : activeSquads)
			squad.writeToBytes(buf);
	}
	
	@Override
	public abstract String toString();
	
	private static List<String> squadClassNames(List<SquadBase> squads)
	{
		final List<String> names = new ArrayList<String>(squads.size());
		for (SquadBase squad : squads)
			names.add(squad.getClass().getCanonicalName());
		return names;
	}
	
	private static byte[] serializeRelationMap(Map<Faction, EnumRelation> relationMap)
	{
		final ByteSequence buf = ByteSequence.allocate(true);
		
		buf.writeInt(relationMap.size());
		for (Entry<Faction, EnumRelation> entry : relationMap.entrySet())
		{
			final byte[] factionKeyBytes = entry.getKey().registryName.getBytes();
			buf.writeInt(factionKeyBytes.length);
			buf.writeBytes(factionKeyBytes);
			buf.writeByte((byte) entry.getValue().ordinal());
		}
		
		return buf.array();
	}
	
	private static void deserializeRelationMap(byte[] bytes, Map<Faction, EnumRelation> relationMap)
	{
		final ByteSequence buf = ByteSequence.wrapReadOnly(bytes);
		final int length = buf.readInt();
		for (int i = 0; i < length; i++)
		{
			final byte[] factionKeyBytes = new byte[buf.readInt()];
			buf.readBytes(factionKeyBytes);
			relationMap.put(Factions.getFaction(new String(factionKeyBytes)), EnumRelation.values()[buf.readByte()]);
		}
	}
	
	private static byte[] serializePlayerRelationMap(Map<UUID, EnumRelation> playerRelationMap)
	{
		final ByteSequence buf = ByteSequence.allocate(true);
		
		buf.writeInt(playerRelationMap.size());
		for (Entry<UUID, EnumRelation> entry : playerRelationMap.entrySet())
			buf.writeLong(entry.getKey().getMostSignificantBits()).writeLong(entry.getKey().getLeastSignificantBits()).writeByte((byte) entry.getValue().ordinal());
		
		return buf.array();
	}
	
	private static void deserializePlayerRelationMap(byte[] bytes, Map<UUID, EnumRelation> playerRelationMap)
	{
		final ByteSequence buf = ByteSequence.wrapReadOnly(bytes);
		final int length = buf.readInt();
		
		for (int i = 0; i < length; i++)
			playerRelationMap.put(new UUID(buf.readLong(), buf.readLong()), EnumRelation.values()[buf.readByte()]);
	}
	
	public static byte[] serializeFactionTreaties()
	{
		final ByteSequence buf = ByteSequence.allocate(true);
		
		buf.writeInt(TREATY_MAP.size());
		for (Entry<FactionPair, EnumTreaty> entry : TREATY_MAP.entrySet())
			buf.writeString(entry.getKey().getFaction1().registryName).writeString(entry.getKey().getFaction2().registryName).writeByte(entry.getValue().ordinal());
		
		return buf.array();
	}
	
	public static void deserializeFactionTreaties(byte[] bytes)
	{
		final ByteSequence buf = ByteSequence.wrapReadOnly(bytes);
		final int length = buf.readInt();
		
		for (int i = 0; i < length; i++)
		{
			final String faction1Reg, faction2Reg;
			final Faction faction1, faction2;
			final EnumTreaty treaty;
			
			try
			{
				faction1Reg = buf.readString();
				faction2Reg = buf.readString();
				
				faction1 = REGISTRY.get(faction1Reg);
				faction2 = REGISTRY.get(faction2Reg);
				
				treaty = EnumTreaty.values()[buf.readByte()];
			} catch (Exception e)
			{
				LOGGER.warn("Caught exception when deserializing faction treaty registry, must abort.");
				LOGGER.catching(e);
				return;
			}
			
			if (faction1 == null)
				LOGGER.warn('[' + faction1Reg + "] returned null in registry, must skip this entry.");
			if (faction2 == null)
				LOGGER.warn('[' + faction2Reg + "] returned null in registry, must skip this entry.");
			
			if (faction1 == null || faction2 == null)
				continue;
			else
				TREATY_MAP.put(new FactionPair(faction1, faction2), treaty);
		}
	}
	
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
