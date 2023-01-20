package com.hbm.entity.mob.ai.squad;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.apache.logging.log4j.Logger;

import com.google.common.annotations.Beta;
import com.hbm.blocks.ModBlocks;
import com.hbm.calc.EasyLocation;
import com.hbm.entity.grenade.EntityGrenadeBouncyBaseNT;
import com.hbm.entity.mob.ai.task.TaskHelper;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.interfaces.IByteSerializable;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.lib.Library;
import com.hbm.main.DeserializationException;
import com.hbm.main.MainRegistry;
import com.hbm.util.lists.ArraySet;
import com.hbm.util.wrappers.Wrapper;

import api.hbm.faction.Faction;
import api.hbm.faction.Faction.EnumTreaty;
import bytes.ByteSequence;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

/**
 * Base class for handling {@link EntitySmartNPCBase} entities in squad behavior.<br>
 * May also be for single NPCs, for whatever reason.
 * @author UFFR
 *
 */
@Beta
public abstract class SquadBase implements IByteSerializable
{
	/**Reference to the main logger**/
	protected static final Logger LOGGER = MainRegistry.logger;
	protected static final Set<Wrapper<Block>> HAZARDOUS_BLOCKS = new HashSet<Wrapper<Block>>();
	protected transient World world;
	protected final List<EntitySmartNPCBase> members = new ArrayList<EntitySmartNPCBase>();
	protected final Set<String> registeredNames = new HashSet<String>();
	protected transient final Map<EntitySmartNPCBase, AxisAlignedBB> memberSightMap = new HashMap<EntitySmartNPCBase, AxisAlignedBB>(), memberProximityMap = new HashMap<EntitySmartNPCBase, AxisAlignedBB>();
	protected final List<EntityLivingBase> targets = new ArraySet<EntityLivingBase>();
	protected transient final Map<EntityLivingBase, EntityStats> targetStatMap = new HashMap<EntityLivingBase, EntityStats>();
	protected transient final Map<EntitySmartNPCBase, Set<ILocationProvider>> localHazards = new HashMap<EntitySmartNPCBase, Set<ILocationProvider>>();
	protected transient final Consumer<IChatComponent> worldChat;
	protected transient final Function<String, IChatComponent> chatSupplier = ChatComponentText::new;
	protected Optional<EntitySmartNPCBase> leader = Optional.empty();
	protected Optional<TaskHelper> taskHelper = Optional.empty();
	protected SquadTask task = SquadTask.PATROL;
	protected ILocationProvider taskLocation;
	protected String name;
	
	static
	{
		// TODO Add all "hazardous" blocks, the current list should give an idea. Just blocks that are obviously bad.
		addHazardousBlocks(Blocks.fire, Blocks.lava, Blocks.flowing_lava, ModBlocks.balefire, ModBlocks.fire_digamma,
				ModBlocks.mine_ap, ModBlocks.mine_fat, ModBlocks.mine_he, ModBlocks.mine_shrap, ModBlocks.sand_gold,
				ModBlocks.sand_gold198, ModBlocks.sulfuric_acid_block, ModBlocks.broadcaster_pc, ModBlocks.barbed_wire,
				ModBlocks.barbed_wire_acid, ModBlocks.barbed_wire_fire, ModBlocks.barbed_wire_poison,
				ModBlocks.barbed_wire_ultradeath, ModBlocks.barbed_wire_wither, ModBlocks.spikes, ModBlocks.taint,
				ModBlocks.machine_sawmill, ModBlocks.machine_crucible, ModBlocks.vitrified_barrel, ModBlocks.yellow_barrel);
	}
	
	public SquadBase(World world)
	{
		this.world = world;
		
		worldChat = MinecraftServer.getServer().getConfigurationManager()::sendChatMsg;
		
		getFaction().addSquad(this);
	}
	
	public abstract int preferredSize();
	public abstract void update();
	public abstract void updateMembers();
	public abstract boolean isTaskComplete();
	public abstract boolean strictSize();
	public abstract void notifyUpdatedTask(SquadTask task);
	public abstract Faction getFaction();
	
	public void checkInvalidMembers()
	{
		if (isSquadDead())
			return;
		
		if (members.removeIf(Objects::isNull))
			LOGGER.warn("Null element(s) found in squad list, this should not be possible!");
		if (members.removeIf(m -> m.getSquad() != this))
			LOGGER.warn("Squad contained member(s) from wrong squad, this should not be possible!");
		if (members.removeIf(m -> Faction.getTreaty(getFaction(), m.getFaction()).ordinal() < EnumTreaty.DEFENSE.ordinal()))
			LOGGER.catching(new IllegalStateException("Unallied NPC(s) included in squad, this should not be possible!"));
		
		members.sort(Comparator.comparing(EntitySmartNPCBase::getRank));
		final List<EntitySmartNPCBase> leaders = members.stream().filter(EntitySmartNPCBase::isSquadLeader).collect(ArrayList::new, List::add, List::addAll);
		
		if (leaders.size() > 1)
		{
			LOGGER.catching(new IllegalStateException("Squad has more than 1 leader, this should not be possible!"));
			for (int i = 1; i < leaders.size(); i++)
				leaders.get(i).setSquadLeader(false);
		}
	}
	
	public void checkDeadMembers()
	{
		if (isSquadDead())
			return;
		
		members.sort(Comparator.comparing(EntitySmartNPCBase::getRank));
		
		final List<EntitySmartNPCBase> dead = members.stream().filter(EntitySmartNPCBase::isDead).collect(ArrayList::new, List::add, List::addAll);
		
		members.removeAll(dead);
		
		if (isSquadDead())
			return;
		
		for (EntitySmartNPCBase member : dead)
		{
			if (member.isSquadLeader())
			{
				setLeader(null);
				getLeaderOrNextRanked().onLostSquadLeader(member);
			} else
				getLeaderOrNextRanked().onLostSquadMember(member);
		}
	}
	
	public void notifyDead(EntitySmartNPCBase member)
	{
		if (!members.contains(member))
			return;
		
		members.remove(member);
		
		if (isSquadDead())
			return;
		
		if (member.isSquadLeader())
		{
			setLeader(null);
			getLeaderOrNextRanked().onLostSquadLeader(member);
		} else
			getLeaderOrNextRanked().onLostSquadMember(member);
	}
	
	public void refreshMemberSightlines()
	{
		memberSightMap.clear();
		for (EntitySmartNPCBase member : members)
			memberSightMap.put(member, member.getSpottingBox());
	}
	
	public void refreshMemberProximities()
	{
		memberProximityMap.clear();
		for (EntitySmartNPCBase member : members)
			memberProximityMap.put(member, member.getProximityBox());
	}
	
	public List<EntityLivingBase> checkMemberSightlines()
	{
		final List<EntityLivingBase> allEntities = new ArraySet<EntityLivingBase>();
		if (!world.isRemote)
		{
			for (EntitySmartNPCBase member : members)
			{
				final AxisAlignedBB box = memberSightMap.get(member);
				final List<EntityLivingBase> entities = member.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, box);
				
//				System.out.println(entities);
				
				entities.removeIf(getFaction()::isBadTarget);
				entities.removeAll(getMembers());
				if (entities.isEmpty())
					continue;
				
				ILocationProvider.sortEntitiesByDistanceFrom(entities, member);
				allEntities.addAll(entities);
				
//				System.out.println(entities);
			}
		}
		return allEntities;
	}
	
	public void recheckLocalArea()
	{
		final List<ILocationProvider> blockCache = new ArrayList<ILocationProvider>();
		refreshMemberSightlines();
		localHazards.clear();
		for (EntitySmartNPCBase member : members)
		{
			blockCache.clear();
			final AxisAlignedBB spotBox = memberSightMap.get(member), proximityBox = memberProximityMap.get(member);
			for (int x = (int) spotBox.minX; x < spotBox.maxX; x++)
				for (int y = (int) spotBox.minY; y < spotBox.maxY; y++)
					for (int z = (int) spotBox.minZ; z < spotBox.maxZ; z++)
						if (isBlockHazardous(world.getBlock(x, y, z)))
							blockCache.add(new EasyLocation(x, y, z));
			blockCache.removeIf(l -> Library.isObstructed(world, l, member));
			localHazards.put(member, new HashSet<>(blockCache));
			if (blockCache.isEmpty())
				continue;
			
			ILocationProvider.sortEntitiesByDistanceFrom(members, member);
			member.notifyDangerGeneric(blockCache.get(0));
			
			final List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, spotBox);
			if (!items.isEmpty())
				member.notifyNearbyItems(items);
			
			final List<EntityGrenadeBouncyBaseNT> nades = world.getEntitiesWithinAABB(EntityGrenadeBouncyBaseNT.class, proximityBox);
			nades.removeIf(g -> getFaction().isBadTarget(g.getThrower()));
			if (!nades.isEmpty())
			{
				ILocationProvider.sortEntitiesByDistanceFrom(nades, member);
				member.notifyDangerGrenade(ILocationProvider.wrap(nades.get(0), false), nades.get(0).getType());
			}
			
			final List<EntityLivingBase> closeEnemies = world.getEntitiesWithinAABB(EntityLivingBase.class, proximityBox);
			closeEnemies.removeIf(getFaction()::isBadTarget);
			closeEnemies.removeAll(getMembers());
			if (!closeEnemies.isEmpty())
			{
				ILocationProvider.sortEntitiesByDistanceFrom(closeEnemies, member);
				member.notifyEnemyTooClose(closeEnemies.get(0));
			}
		}
	}
	
	public SquadBase setTaskAndLocation(SquadTask task, @Nullable ILocationProvider loc)
	{
		checkDeadMembers();
		this.task = task;
		taskLocation = loc == null ? ILocationProvider.wrap(getLeaderOrNextRanked(), false) : loc;
		return this;
	}
	
	public ILocationProvider getTaskLocation()
	{
		return taskLocation == null ? taskLocation = ILocationProvider.wrap(getLeaderOrNextRanked(), true) : taskLocation;
	}
	
	public final Optional<EntitySmartNPCBase> getLeader()
	{
		return leader;
	}
	
	public EntitySmartNPCBase getLeaderOrNextRanked()
	{
		checkDeadMembers();
		return leader.orElse(members.get(0));
	}
	
	public boolean setLeader(@Nullable EntitySmartNPCBase leader)
	{
		if (leader != null && !members.contains(leader))
			return false;
		this.leader = Optional.ofNullable(leader);
		return true;
	}
	
	public List<EntitySmartNPCBase> getMembers()
	{
		return members;
	}
	
	public boolean addMember(@Nonnull EntitySmartNPCBase npc)
	{
		if (strictSize())
			return false;
		
		if (npc == null || npc.getFaction() != getFaction() && Faction.getTreaty(getFaction(), npc.getFaction()).ordinal() < EnumTreaty.DEFENSE.ordinal())
			return false;
		
		members.add(npc);
		registeredNames.add(npc.getSquadName());
		checkDeadMembers();
		return true;
	}
	
	public boolean addTarget(EntityLivingBase entity)
	{
		if (entity == null || members.contains(entity) || getFaction().isBadTarget(entity))
			return false;
		
//		if (entity instanceof EntitySmartNPCBase)
//			if (Faction.getTreaty(((EntitySmartNPCBase) entity).getFaction(), getFaction()).ordinal() >= EnumTreaty.NO_AGGRESSION.ordinal())
//				return false;
		return targets.add(entity);
	}
	
	public List<EntityLivingBase> getTargets()
	{
		return targets;
	}
	
	public Set<String> getRegisteredNames()
	{
		return registeredNames;
	}
	
	public void setWorld(World world)
	{
		this.world = world;
	}
	
	public World getWorld()
	{
		return world;
	}

	public EntitySmartNPCBase getMember(int index) throws IndexOutOfBoundsException
	{
		checkDeadMembers();
		return members.get(index);
	}

	public int size()
	{
		checkDeadMembers();
		return members.size();
	}
	
	public SquadTask getTask()
	{
		return task;
	}
	
	public void setTask(SquadTask task)
	{
		this.task = task;
	}
	
	public boolean isSquadDead()
	{
		return members.isEmpty();
	}
	
	public boolean inCombat()
	{
		return !targets.isEmpty();
	}

	public String getName()
	{
		return name;
	}
	
	public SquadBase setName(String name)
	{
		this.name = name;
		return this;
	}
	
	@Override
	@OverridingMethodsMustInvokeSuper
	public void writeToBytes(ByteSequence buf)
	{
		// TODO Auto-generated method stub
		final byte[] memberBytes, targetBytes, nameBytes;
		
		memberBytes = serializeEntityList(members);
		targetBytes = serializeEntityList(targets);
		nameBytes = Library.serializeStringCollection(registeredNames);
		
		buf.writeInt(memberBytes.length).writeInt(targetBytes.length).writeInt(nameBytes.length);
		buf.writeBytes(memberBytes).writeBytes(targetBytes).writeBytes(nameBytes);
		buf.writeByte(task.ordinal());
		buf.writeString(name);
	}
	
	@Override
	@OverridingMethodsMustInvokeSuper
	public void readFromBytes(ByteSequence buf) throws DeserializationException
	{
		// TODO Auto-generated method stub
		members.clear();
		targets.clear();
		registeredNames.clear();
		
		final byte[] memberBytes, targetBytes, nameBytes;
		final List<UUID> memberUuids, targetUuids;

		memberBytes = new byte[buf.readInt()];
		targetBytes = new byte[buf.readInt()];
		nameBytes = new byte[buf.readInt()];
		
		buf.readBytes(memberBytes);
		buf.readBytes(targetBytes);
		buf.readBytes(nameBytes);
		
		memberUuids = deserializeEntityUUIDList(memberBytes);
		targetUuids = deserializeEntityUUIDList(targetBytes);
		
		registeredNames.addAll(Library.deserializeStringCollection(nameBytes));
		for (UUID uuid : memberUuids)
		{
			final Entity entity = Library.findEntityWithUUID(uuid, world);
			if (entity instanceof EntitySmartNPCBase)
				members.add((EntitySmartNPCBase) entity);
			else
				LOGGER.warn("Saved member UUID [" + uuid + "] associated with either null or wrong class!");
		}
		for (UUID uuid : targetUuids)
		{
			final Entity entity = Library.findEntityWithUUID(uuid, world);
			if (entity instanceof EntityLivingBase)
				targets.add((EntityLivingBase) entity);
			else
				LOGGER.warn("Saved target UUID [" + uuid + "] associated with either null or wrong class!");
		}
		task = SquadTask.values()[buf.readByte()];
		name = buf.readString();
		for (EntitySmartNPCBase member : members)
			member.setSquad(this);
		refreshMemberProximities();
		refreshMemberSightlines();
	}
	
	protected static byte[] serializeEntityList(List<? extends Entity> entities)
	{
		final ByteSequence buf = ByteSequence.allocate(entities.size() * 16, true);
		
		for (UUID uuid : entities.stream().map(Entity::getUniqueID).collect(Collectors.toList()))
			buf.writeLong(uuid.getMostSignificantBits()).writeLong(uuid.getLeastSignificantBits());
		
		return buf.array();
	}
	
	protected static List<UUID> deserializeEntityUUIDList(byte[] bytes) throws DeserializationException
	{
		try
		{
			final List<UUID> uuids = new ArrayList<UUID>();
			final ByteSequence buf = ByteSequence.wrapReadOnly(bytes);
			while (buf.getRemainingSize() > 0)
				uuids.add(new UUID(buf.readLong(), buf.readLong()));
			return uuids;
		} catch (Exception e)
		{
			throw new DeserializationException(e);
		}
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(memberSightMap, leader, members, targets, task, taskLocation);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof SquadBase))
			return false;
		final SquadBase other = (SquadBase) obj;
		return Objects.equals(memberSightMap, other.memberSightMap)
				&& Objects.equals(leader, other.leader) && Objects.equals(members, other.members)
				&& Objects.equals(targets, other.targets) && task == other.task && Objects.equals(taskLocation, other.taskLocation);
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
//	public boolean badTarget(EntityLivingBase entity)
//	{
//		if (entity.isDead)
//			return true;
//		if (entity instanceof EntitySmartNPCBase)
//			return ((EntitySmartNPCBase) entity).hasFaction() && getFaction().getRelationship(((EntitySmartNPCBase) entity).getFaction()).ordinal() >= EnumRelation.NEUTRAL.ordinal() || Faction.getTreaty(((EntitySmartNPCBase) entity).getFaction(), getFaction()).ordinal() >= EnumTreaty.NO_AGGRESSION.ordinal();
//		if (entity instanceof EntityPlayer)
//			return getFaction().getPlayerRelationship((EntityPlayer) entity).ordinal() >= EnumRelation.NEUTRAL.ordinal();
//		return entity.getClass().getCanonicalName().contains("passive");
//	}
	
	protected final EnumDifficulty getWorldDifficulty()
	{
		return world.difficultySetting;
	}
	
	public static void addHazardousBlocks(Block... blocks)
	{
		for (Block block : blocks)
			addHazardousBlock(block);
	}

	public static void addHazardousBlock(Block block)
	{
		Wrapper.addWrappedBlock(block, HAZARDOUS_BLOCKS);
	}
	
	public static boolean isBlockHazardous(Block block)
	{
		return Wrapper.doesContain(block, HAZARDOUS_BLOCKS);
	}
	
}
