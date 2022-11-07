package com.hbm.entity.mob.ai.squad;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.google.common.annotations.Beta;
import com.hbm.blocks.ModBlocks;
import com.hbm.calc.EasyLocation;
import com.hbm.entity.grenade.EntityGrenadeBouncyBaseNT;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.lib.Library;
import com.hbm.main.MainRegistry;
import com.hbm.util.wrappers.Wrapper;

import api.hbm.faction.Faction;
import api.hbm.faction.Faction.EnumRelation;
import api.hbm.faction.Faction.EnumTreaty;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
public abstract class SquadBase implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 137261370580268948L;
	/**Reference to the main logger**/
	protected static final Logger LOGGER = MainRegistry.logger;
	protected static final Set<Wrapper<Block>> HAZARDOUS_BLOCKS = new HashSet<Wrapper<Block>>();
	protected transient World world;
	protected final List<EntitySmartNPCBase> members = new ArrayList<EntitySmartNPCBase>();
	protected final Set<String> registeredNames = new HashSet<String>();
	protected transient final Map<EntitySmartNPCBase, AxisAlignedBB> memberSightMap = new HashMap<EntitySmartNPCBase, AxisAlignedBB>(), memberProximityMap = new HashMap<EntitySmartNPCBase, AxisAlignedBB>();
	protected transient final Set<EntityLivingBase> targets = new HashSet<EntityLivingBase>();
	protected transient final Map<EntityLivingBase, EntityStats> targetStatMap = new HashMap<EntityLivingBase, EntityStats>();
	protected transient final Map<EntitySmartNPCBase, Set<ILocationProvider>> localHazards = new HashMap<EntitySmartNPCBase, Set<ILocationProvider>>();
	protected transient final Consumer<IChatComponent> worldChat;
	protected transient final Function<String, IChatComponent> chatSupplier = ChatComponentText::new;
	protected Optional<EntitySmartNPCBase> leader = Optional.empty();
	protected SquadTask task = SquadTask.PATROL;
	protected ILocationProvider taskLocation;
	
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
	}
	
	public abstract String getName();
	public abstract int preferredSize();
	public abstract void update();
	public abstract void updateMembers();
	public abstract boolean isTaskComplete();
	public abstract void notifyUpdatedTask(SquadTask task);
	public abstract Faction getFaction();
	
	private void writeObject(ObjectOutputStream stream) throws IOException
	{
		stream.defaultWriteObject();
		stream.writeObject(members.stream().mapToInt(EntitySmartNPCBase::hashCode).toArray());
		stream.writeObject(targetStatMap.keySet().stream().collect(Collectors.toMap(EntityLivingBase::hashCode, targetStatMap::get)));
	}
	
	private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException
	{
		stream.defaultReadObject();
		world = MinecraftServer.getServer().getEntityWorld();
		final int[] memberIDs = (int[]) stream.readObject();
		for (int id : memberIDs)
		{
			final Entity entity = world.getEntityByID(id);
			if (entity instanceof EntitySmartNPCBase)
				members.add((EntitySmartNPCBase) entity);
			else
				LOGGER.warn("Saved member either null or wrong class!");
		}
		final Map<Integer, EntityStats> savedStats = (Map<Integer, EntityStats>) stream.readObject();
		final Map<Entity, EntityStats> translatedStats = savedStats.keySet().stream().collect(Collectors.toMap(world::getEntityByID, savedStats::get));
		for (Entry<Entity, EntityStats> entry : translatedStats.entrySet())
		{
			if (entry.getKey() instanceof EntityLivingBase)
				targetStatMap.put((EntityLivingBase) entry.getKey(), entry.getValue());
			else
				LOGGER.warn("Saved key: [" + entry.getKey() + "] is not an instance of EntityLivingBase!");
		}
	}
	
	public void checkDeadOrInvalidMembers()
	{
		if (members.removeIf(Objects::isNull))
			LOGGER.warn("Null element(s) found in squad list, this should not be possible!");
		if (members.removeIf(m -> m.getSquad() != this))
			LOGGER.warn("Squad contained member(s) from wrong squad, this should not be possible!");
		if (members.removeIf(m -> Faction.getTreaty(getFaction(), m.getFaction()).ordinal() < EnumTreaty.DEFENSE.ordinal()))
			LOGGER.catching(new IllegalStateException("Unallied NPC(s) included in squad, this should not be possible!"));
		
		if (isSquadDead())
			return;
		
		members.sort(Comparator.comparing(EntitySmartNPCBase::getRank));
		
		final List<EntitySmartNPCBase> leaders = members.stream().filter(EntitySmartNPCBase::isSquadLeader).collect(ArrayList::new, List::add, List::addAll);
		
		if (leaders.size() > 1)
		{
			LOGGER.catching(new IllegalStateException("Squad has more than 1 leader, this should not be possible!"));
			for (int i = 1; i < leaders.size(); i++)
				leaders.get(i).setSquadLeader(false);
		}
		
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
	
	public void getMemberSightlines()
	{
		memberSightMap.clear();
		for (EntitySmartNPCBase member : members)
			memberSightMap.put(member, member.getSpottingBox());
	}
	
	public void getMemberProximities()
	{
		memberProximityMap.clear();
		for (EntitySmartNPCBase member : members)
			memberProximityMap.put(member, member.getProximityBox());
	}
	
	public void checkMemberSightlines()
	{
		for (EntitySmartNPCBase member : members)
		{
			final AxisAlignedBB box = memberSightMap.get(member);
			final List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
			entities.remove(member);
			entities.removeIf(this::badTarget);
			entities.removeAll(getMembers());
			if (entities.isEmpty())
				continue;
			
			entities.sort(Comparator.comparing(e -> ILocationProvider.distance(ILocationProvider.wrap(e, false), member.getMyLocation())));
			targets.addAll(entities);
			if (member.getAttackTarget() == null)
				member.setAttackTarget(entities.get(0));
		}
	}
	
	public void recheckLocalArea()
	{
		final List<ILocationProvider> blockCache = new ArrayList<ILocationProvider>();
		getMemberSightlines();
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
			blockCache.removeIf(l -> Library.isObstructed(world, l, member.getMyLocation()));
			localHazards.put(member, new HashSet<>(blockCache));
			if (blockCache.isEmpty())
				continue;
			
			blockCache.sort(Comparator.comparing(l -> ILocationProvider.distance(l, member.getMyLocation())));
			member.notifyDangerGeneric(blockCache.get(0));
			
			final List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, spotBox);
			if (!items.isEmpty())
				member.notifyNearbyItems(items);
			
			final List<EntityGrenadeBouncyBaseNT> nades = world.getEntitiesWithinAABB(EntityGrenadeBouncyBaseNT.class, proximityBox);
			nades.removeIf(g -> badTarget(g.getThrower()));
			if (!nades.isEmpty())
			{
				nades.sort(Comparator.comparing(nade -> ILocationProvider.distance(ILocationProvider.wrap(nade, false), member.getMyLocation())));
				member.notifyDangerGrenade(ILocationProvider.wrap(nades.get(0), false), nades.get(0).getType());
			}
			
			final List<EntityLivingBase> closeEnemies = world.getEntitiesWithinAABB(EntityLivingBase.class, proximityBox);
			closeEnemies.removeIf(this::badTarget);
			closeEnemies.removeAll(getMembers());
			if (!closeEnemies.isEmpty())
			{
				closeEnemies.sort(Comparator.comparing(e -> ILocationProvider.distance(ILocationProvider.wrap(e, false), member.getMyLocation())));
				member.notifyEnemyTooClose(closeEnemies.get(0));
			}
		}
	}
	
	public SquadBase setTaskAndLocation(SquadTask task, @Nullable ILocationProvider loc)
	{
		checkDeadOrInvalidMembers();
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
		checkDeadOrInvalidMembers();
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
		if (npc == null || npc.getFaction() != getFaction() && Faction.getTreaty(getFaction(), npc.getFaction()).ordinal() < EnumTreaty.DEFENSE.ordinal())
			return false;
		
		members.add(npc);
		registeredNames.add(npc.getMySquadName());
		checkDeadOrInvalidMembers();
		return true;
	}
	
	public boolean addTarget(EntityLivingBase entity)
	{
		if (entity == null || members.contains(entity))
			return false;
		
		if (entity instanceof EntitySmartNPCBase)
			if (Faction.getTreaty(((EntitySmartNPCBase) entity).getFaction(), getFaction()).ordinal() >= EnumTreaty.NO_AGGRESSION.ordinal())
				return false;
		return targets.add(entity);
	}
	
	public Set<EntityLivingBase> getTargets()
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
		checkDeadOrInvalidMembers();
		return members.get(index);
	}

	public int size()
	{
		checkDeadOrInvalidMembers();
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
	
	public boolean badTarget(EntityLivingBase entity)
	{
		if (entity.isDead)
			return true;
		if (entity instanceof EntitySmartNPCBase)
			return ((EntitySmartNPCBase) entity).hasFaction() && getFaction().getRelationship(((EntitySmartNPCBase) entity).getFaction()).ordinal() >= EnumRelation.NEUTRAL.ordinal() || Faction.getTreaty(((EntitySmartNPCBase) entity).getFaction(), getFaction()).ordinal() >= EnumTreaty.NO_AGGRESSION.ordinal();
		if (entity instanceof EntityPlayer)
			return getFaction().getPlayerRelationship((EntityPlayer) entity).ordinal() >= EnumRelation.NEUTRAL.ordinal();
		return entity.getClass().getCanonicalName().contains("passive");
	}
	
	protected EnumDifficulty getWorldDifficulty()
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
