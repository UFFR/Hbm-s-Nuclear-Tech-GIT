package com.hbm.entity.mob.smart;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.apache.logging.log4j.Logger;

import com.google.common.annotations.Beta;
import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.entity.projectile.EntityBulletBase;
import com.hbm.handler.BulletConfigSyncingUtil;
import com.hbm.handler.BulletConfiguration;
import com.hbm.handler.GunConfiguration;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.items.ItemAmmoEnums.AmmoHandGrenade;
import com.hbm.main.DeserializationException;
import com.hbm.main.MainRegistry;
import com.hbm.particle.SpentCasingConfig;
import com.hbm.render.entity.mob.DeathPose;
import com.hbm.util.CacheList;
import com.hbm.util.ItemStackSerialization;
import com.hbm.util.ParticleUtil;

import api.hbm.entity.IDefenseAttributes;
import api.hbm.faction.Faction;
import api.hbm.faction.Faction.EnumRelation;
import api.hbm.faction.Faction.EnumTreaty;
import api.hbm.faction.IEntityDataProvider;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Intended to be a base class for "smart" NPCs/mobs, particularly faction based ones.
 * @author UFFR
 *
 */
@Beta
public abstract class EntitySmartNPCBase extends EntityCreature implements IEntityDataProvider, IDefenseAttributes
{
	/**Reference to the main logger**/
	protected static final Logger LOGGER = MainRegistry.logger;
	protected final ItemStack[] equipmentSlots = new ItemStack[5], myInventory = new ItemStack[16];
	protected final List<ItemStack> previousStacks = new CacheList<ItemStack>(10);
	protected transient final ILocationProvider myLocation = ILocationProvider.wrap(this, false);
	protected transient final Consumer<IChatComponent> worldChat;
	protected transient final Function<String, IChatComponent> chatSupplier = ChatComponentText::new;
	protected transient AxisAlignedBB spotBox, proximityBox;
	protected NPCTask currentTask = NPCTask.HOLD;
	protected boolean isSquadLeader, isSolo;
	protected SquadBase squad;
	protected String name, squadName;
	protected int regenTimer, mainRoFCooldown, reloadTimer, altRoFCooldown, mainMagAmmo, mainAmmo, altAmmo;
	protected boolean isSneaking = false;
	
	public EntitySmartNPCBase(World world)
	{
		this(world, null, "", "");// TODO
	}
	
	public EntitySmartNPCBase(World world, SquadBase squad, String name, String squadName)
	{
		super(world);
		this.squad = squad;
		this.name = name;
		this.squadName = squadName;
		
		worldChat = MinecraftServer.getServer().getConfigurationManager()::sendChatMsg;
		regenTimer = regenDelay();
		func_110163_bv();
		setCanPickUpLoot(true);
		preventEntitySpawning = true;
		setSize(getWidth(), getHeight());
		initializeAI();
		spotBox = AxisAlignedBB.getBoundingBox(posX + spotDistance(), posY + spotDistance(), posZ + spotDistance(), posX - spotDistance(), posY - spotDistance(), posZ - spotDistance());
		proximityBox = AxisAlignedBB.getBoundingBox(posX + proximityDistance(), posY + proximityDistance(), posZ + proximityDistance(), posX - proximityDistance(), posY - proximityDistance(), posZ - proximityDistance());
		
		if (squad != null)
			squad.addMember(this);
		
		LOGGER.info("Spawned SNPC:\n" + toDisplay());
	}
	
	/// Basic behavior and details
	public abstract float getWidth();
	public abstract float getHeight();
	/**
	 * Replace the held item with something else.
	 * @param stack The new ItemStack or null.
	 */
	public abstract void overrideHeldItem(@Nullable ItemStack stack);
	@Override
	protected abstract int getExperiencePoints(EntityPlayer player);
	/**
	 * The max distance this NPC can see and spot targets.
	 * @return An integer for the distance this NPC can detect targets.
	 */
	public abstract int spotDistance();
	/**
	 * The distance this NPC prefers to engage enemies at, can be closer or further, but is most effective around this distance.
	 * @return An integer for the distance this NPC prefers to engage at.
	 */
	public abstract int preferredDistance();
	/**
	 * The distance at which this NPC detects a threat as an immediate emergency.
	 * @return An integer for the distance this NPC finds threats to be "too close".
	 */
	public abstract int proximityDistance();
	/**
	 * Set or reset this NPC's activity task.
	 * @param task The new task to apply.
	 */
	public abstract void applyTask(NPCTask task);
	/**
	 * The timer in ticks before another regeneration cycle, seen in {@link #regenAmount()}.
	 * @return Ticks before regenerating again.
	 */
	@Nonnegative
	public abstract int regenDelay();
	/**
	 * The amount of health replenished every cycle or none.
	 * @return A nonnegative integer for regenerating health or zero for none.
	 */
	@Nonnegative
	public abstract int regenAmount();
	/**
	 * The main gun configuration.
	 * @return The {@code GunConfiguration} for this NPC's main gun mode or null.
	 */
	@Nullable
	@CheckForNull
	public abstract GunConfiguration getMainGunConfiguration();
	@Nullable
	@CheckForNull
	/**
	 * The main gun's bullet configuration.
	 * @return The {@code BulletConfiguration} for this NPC's main gun mode or null.
	 */
	public abstract BulletConfiguration getMainBulletConfiguration();
	/**
	 * The maximum amount of main ammo this NPC carries.
	 * @return The integer amount of ammo or -1 for infinite.
	 */
	public abstract int getMainMaxAmmoCount();
	/**
	 * The alternate gun configuration.
	 * @return The {@code GunConfiguration} for this NPC's alternate file or null.
	 */
	@Nullable
	@CheckForNull
	public abstract GunConfiguration getAltGunConfiguration();
	/**
	 * The alternate fire's bullet configuration.
	 * @return The {@code BulletConfiguration} for this NPC's alternate fire or null.
	 */
	@Nullable
	@CheckForNull
	public abstract BulletConfiguration getAltBulletConfiguration();
	/**
	 * The maximum amount of alternate ammo this NPC carries.
	 * @return The integer amount of ammo or -1 for infinite.
	 */
	public abstract int getAltMaxAmmoCount();
	/**
	 * If this NPC can use grenades.
	 * @return True if this NPC throws hand grenades, false otherwise.
	 */
	public abstract boolean usesGrenades();
	/**
	 * The amount of grenades this NPC carries.
	 * @return The integer amount of grenades or -1 for infinite.
	 */
	public abstract int grenadeCount();
	/**
	 * The hand grenade enumerate type.
	 * @return A nonnull enumerate for the hand grenade type.
	 */
	@Nullable
	@CheckForNull
	public abstract AmmoHandGrenade grenadeType();
	/**
	 * Level at which the NPC will be warned of low ammo. May cause premature reload if possible.
	 * @return Integer threshold of magazine ammo that will warn this NPC.
	 */
	public abstract int lowAmmoThreshold();
	/**
	 * Items that will be carried on the corpse on death. May include weapon, leftover ammo, inventory, and additional loot.
	 * @return An array of {@code ItemStack} objects to be in the corpse block for looting.
	 */
	public abstract ItemStack[] deathLootStacks();
	/**
	 * Gets the death pose given the type of death.
	 * @param source The type of damage received.
	 * @param amount The amount of damage.
	 * @return The pose given the parameters.
	 */
	public abstract DeathPose poseFromDeathType(DamageSource source, float amount);
	/**Trigger a reload operation, preventing firing until complete.**/
	public abstract void reload();
	/**
	 * If this NPC is allowed to know the location of all enemies without having direct visual.
	 * @return True if this NPC is omniscient, false otherwise.
	 */
	public abstract boolean isOmniscient();
	protected abstract void initializeAI();
	protected abstract void resetAI();
	protected abstract boolean usesCasings();
	protected abstract SpentCasingConfig casingConfig();
	
	/// Squad and smart behavior
	/**Non-specific behavior when not in combat, implementation is allowed to vary.**/
	public abstract void onIdle();
	/**
	 * When a previously unspotted enemy is seen.
	 * @param entity The entity in question.
	 */
	public abstract void onNewSpotting(EntityLivingBase entity);
	/**
	 * When the NPC is engaging a target.
	 * @param entity The entity in question.
	 */
	public abstract void onBeginAttack(EntityLivingBase entity);
	/**
	 * An enemy is in cover.
	 * @param entity The entity in cover.
	 * @param loc The general location of the cover.
	 */
	public abstract void notifyEnemyInCover(EntityLivingBase entity, ILocationProvider loc);
	/**
	 * When an enemy is near death.
	 * @param entity The entity in question.
	 */
	public abstract void onEnemyAlmostDead(EntityLivingBase entity);
	/**
	 * When this NPC successfully kills an enemy.
	 * @param entity The killed entity.
	 */
	@Override
	public abstract void onKillEntity(EntityLivingBase entity);
	/**
	 * When a previously unspotted player (enemy) is seen.
	 * @param player The player in question.
	 */
	public abstract void onSpotPlayer(EntityPlayer player);
	/**
	 * When a player (enemy) is near death.
	 * @param player The player in question.
	 */
	public abstract void onPlayerAlmostDead(EntityPlayer player);
	/**
	 * When this NPC successfully kills a player (enemy).
	 * @param player The killed player.
	 */
	public abstract void onKilledPlayer(EntityPlayer player);
	/**When this NPC is injured, but only lightly. Health is still >80%.**/
	public abstract void onDamagedMinor();
	/**When this NPC is severely injured. Health is <20%.**/
	public abstract void onDamagedMajor();
	/**When this NPC is extremely injured. Health is <10%.**/
	public abstract void onNearDeath();
	/**
	 * When an enemy is within {@link #proximityDistance()}.
	 * @param entity The entity in question.
	 */
	public abstract void notifyEnemyTooClose(EntityLivingBase entity);
	/**When an enemy hasn't been seen for a few seconds.**/
	public abstract void onLostEnemy();
	/**When an enemy hasn't been seen for several seconds.**/
	public abstract void onLostEnemyLong();
	/**
	 * When an enemy that had previously been lost is seen again.
	 * @param entity The entity in question.
	 */
	public abstract void onReestablishContact(EntityLivingBase entity);
	/**
	 * A squadmate is almost dead. For covering fire or medical assistance.
	 * @param entity The squad member in question.
	 */
	public abstract void onSquadMemberAlmostDead(EntitySmartNPCBase entity);
	/**
	 * A squadmate has been killed.
	 * @param entity The squad member in question.
	 */
	public abstract void onLostSquadMember(EntitySmartNPCBase entity);
	/**
	 * The squad's leader has been killed.
	 * @param entity The leader.
	 */
	public abstract void onLostSquadLeader(EntitySmartNPCBase entity);
	/**When all squad members besides this one is almost dead.**/
	public abstract void onLastMemberRemaining();
	/**
	 * When a friendly non-squadmate is almost dead.
	 * @param entity The entity in question.
	 */
	public abstract void onFriendAlmostDead(EntityLivingBase entity);
	/**
	 * When a friendly non-squadmate is killed.
	 * @param entity The entity in question.
	 */
	public abstract void onLostFriend(EntityLivingBase entity);
	/**Paired with {@link #lowAmmoThreshold()}, notifies this NPC they are low on ammo.**/
	public abstract void notifyLowAmmunition();
	/**
	 * Notifies this NPC of a nearby danger, such as fire or another dangerous block.
	 * @param loc The general location of the danger.
	 */
	public abstract void notifyDangerGeneric(ILocationProvider loc);
	/**
	 * Notifies this NPC of a nearby hostile grenade.
	 * @param loc The location of the grenade.
	 * @param type The type of grenade.
	 */
	public abstract void notifyDangerGrenade(ILocationProvider loc, AmmoHandGrenade type);
	/**
	 * Warn this NPC that an enemy is looking directly at them, being aimed at for instance.
	 * @param entity The enemy in question.
	 * @param vector The angle of their crosshair.
	 */
	public abstract void notifyEnemyCrosshair(EntityLivingBase entity, Vec3 vector);
	/**
	 * Notifies this NPC of nearby dropped items for looting or inspection.
	 * @param items The list of dropped items.
	 */
	public abstract void notifyNearbyItems(List<EntityItem> items);
	/**
	 * Tell this NPC to try and throw a grenade at a particular location.
	 * @param loc The intended target location.
	 */
	public abstract void tryThrowGrenade(ILocationProvider loc);
	
	/// Vanilla behavior
	@Override
	protected abstract void dropEquipment(boolean byPlayer, int lootLevel);
	@Override
	protected abstract void dropFewItems(boolean byPlayer, int lootLevel);
	@Override
	protected abstract void dropRareDrop(int i);
	@Override
	protected abstract String getLivingSound();
	@Override
	protected abstract String getHurtSound();
	@Override
	protected abstract String getDeathSound();
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
//		setDead();
		if (hasSquad())
			squad.update();
		
		if (regenAmount() > 0)
		{
			if (regenTimer > 0)
				regenTimer--;
			else
			{
				heal(regenAmount());
				regenTimer = regenDelay();
			}
		}
		
		if (reloadTimer > 0)
			reloadTimer--;
		if (mainRoFCooldown > 0)
			mainRoFCooldown--;
		if (altRoFCooldown > 0)
			altRoFCooldown--;
		
		if (mainMagAmmo <= 0)
			reload();
		else if (mainMagAmmo <= lowAmmoThreshold())
			notifyLowAmmunition();
	}
	
	// TODO Implement some kind of config maybe and/or more detailed relations with unallied NPCs.
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source.getEntity() instanceof EntitySmartNPCBase)
		{
			final EntitySmartNPCBase entity = (EntitySmartNPCBase) source.getEntity();
			if (entity.hasFaction() && entity.getFaction() == getFaction()
					|| entity.getFaction().getRelationship(getFaction()).ordinal() >= EnumRelation.NEUTRAL.ordinal()
					|| Faction.getTreaty(getFaction(), entity.getFaction()).ordinal() >= EnumTreaty.NO_AGGRESSION.ordinal())
				return false;
		}
		if (source.getEntity() instanceof EntityPlayer)
		{
			final EntityPlayer player = (EntityPlayer) source.getEntity();
			if (getFaction().getPlayerRelationship(player).ordinal() >= EnumRelation.POSITIVE.ordinal())
				return false;
		}
		
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	protected boolean isAIEnabled()
	{
		return true;
	}
	
	public void refreshSpottingBox()
	{
		spotBox = AxisAlignedBB.getBoundingBox(posX + spotDistance(), posY + spotDistance(), posZ + spotDistance(), posX - spotDistance(), posY - spotDistance(), posZ - spotDistance());
	}
	
	public AxisAlignedBB getSpottingBox()
	{
		return spotBox;
	}
	
	public void refreshProximityBox()
	{
		proximityBox = AxisAlignedBB.getBoundingBox(posX + proximityDistance(), posY + proximityDistance(), posZ + proximityDistance(), posX - proximityDistance(), posY - proximityDistance(), posZ - proximityDistance());
	}
	
	public AxisAlignedBB getProximityBox()
	{
		return proximityBox;
	}
	
	@Beta
	public void primaryFire()
	{
		if (mainMagAmmo <= 0)
		{
			reload();
			return;
		}
		
		if (getMainGunConfiguration() != null && getMainBulletConfiguration() != null && reloadTimer <= 0 && mainRoFCooldown <= 0)
		{
			final GunConfiguration gunConfiguration = getMainGunConfiguration();
			final BulletConfiguration bulletConfiguration = getMainBulletConfiguration();
			final int amountToFire = bulletConfiguration.bulletsMin + rand.nextInt(bulletConfiguration.bulletsMax - bulletConfiguration.bulletsMin),
					key = BulletConfigSyncingUtil.getKey(bulletConfiguration);
			
			mainMagAmmo -= gunConfiguration.roundsPerCycle;
			
			for (int i = 0; i < amountToFire; i++)
				worldObj.spawnEntityInWorld(new EntityBulletBase(worldObj, key, this, getAttackTarget(), 1, 0));
			
			if (usesCasings() && !casingConfig().isAfterReload())
				ParticleUtil.spawnCasing(getMyLocation(), casingConfig(), (float) Math.toRadians(rotationPitch), (float) Math.toRadians(rotationYaw), getEyeHeight(), isSneaking);
		}
	}
	
	@Override
	public SquadBase getSquad()
	{
		return squad;
	}
	
	public void setSquad(@Nullable SquadBase squad)
	{
		// TODO Implement single member squad types.
		this.squad = squad;
	}
	
	@Override
	public ItemStack getHeldItem()
	{
		return equipmentSlots[0];
	}
	
	@Override
	public ItemStack getEquipmentInSlot(int slot)
	{
		return equipmentSlots[slot];
	}
	
	@Override
	public void setCurrentItemOrArmor(int slot, ItemStack stack)
	{
		equipmentSlots[slot] = stack;
	}
	
	public ItemStack[] getArmorSlots()
	{
		return Arrays.copyOfRange(equipmentSlots, 1, 5);
	}
	
	public NPCTask getCurrentTask()
	{
		return currentTask;
	}
	
	public boolean isSquadLeader()
	{
		return isSquadLeader;
	}
	
	public void setSquadLeader(boolean isSquadLeader)
	{
		this.isSquadLeader = isSquadLeader;
	}
	
	public boolean isSolo()
	{
		return isSolo;
	}
	
	public void setSolo(boolean isSolo)
	{
		this.isSolo = isSolo;
	}
	
	public final ILocationProvider getMyLocation()
	{
		return myLocation;
	}

//	@Override
//	public void setCurrentItemOrArmor(int slot, ItemStack stack)
//	{
//		if (slot == 0)
//			overrideHeldItem(stack);
//		else
//			armorSlots[slot] = stack;
//	}

	@Override
	public ItemStack[] getLastActiveItems()
	{
		return previousStacks.toArray(new ItemStack[previousStacks.size()]);
	}
	
	public boolean isDead()
	{
		return isDead || getHealth() <= 0;
	}
	
//	@Override
//	@OverridingMethodsMustInvokeSuper
//	public void writeToNBT(NBTTagCompound nbt)
//	{
//		super.writeToNBT(nbt);
//		writeEntityToNBT(nbt);
//	}
//	
//	@Override
//	@OverridingMethodsMustInvokeSuper
//	public void readFromNBT(NBTTagCompound nbt)
//	{
//		super.readFromNBT(nbt);
//		readEntityFromNBT(nbt);
//	}
//	
	@Override
	@OverridingMethodsMustInvokeSuper
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByteArray("armorSlots", ItemStackSerialization.serializeArray(equipmentSlots));
		nbt.setByteArray("lastItems", ItemStackSerialization.serializeArray(previousStacks.toArray(new ItemStack[previousStacks.size()])));
		nbt.setByteArray("inventory", ItemStackSerialization.serializeArray(myInventory));
		nbt.setString("name", name);
		nbt.setString("squadName", squadName);
		nbt.setByte("task", (byte) currentTask.ordinal());
		nbt.setBoolean("isLeader", isSquadLeader);
		nbt.setBoolean("isSolo", isSolo);
		nbt.setInteger("regenTimer", regenTimer);
		nbt.setInteger("mainRoFCooldown", mainRoFCooldown);
		nbt.setInteger("reloadTimer", reloadTimer);
		nbt.setInteger("altRoFCooldown", altRoFCooldown);
		nbt.setInteger("mainMagAmmo", mainMagAmmo);
		nbt.setInteger("mainAmmo", mainAmmo);
		nbt.setInteger("altAmmo", altAmmo);
		nbt.setBoolean("isSneaking", isSneaking);
	}
	
	@Override
	@OverridingMethodsMustInvokeSuper
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		try
		{
			final ItemStack[] savedArmorSlots = ItemStackSerialization.deserializeItemStacks(nbt.getByteArray("armorSlots")),
							  lastStacks = ItemStackSerialization.deserializeItemStacks(nbt.getByteArray("lastItems")),
							  inventoryStacks = ItemStackSerialization.deserializeItemStacks(nbt.getByteArray("inventory"));
			for (byte i = 0; i < 4; i++)
				equipmentSlots[i] = savedArmorSlots[i];
			for (byte i = 0; i < 16; i++)
				myInventory[i] = inventoryStacks[i];
			previousStacks.clear();
			Collections.addAll(previousStacks, lastStacks);
		} catch (DeserializationException e)
		{
			LOGGER.catching(e);
		}
		name = nbt.getString("name");
		squadName = nbt.getString("squadName");
		currentTask = NPCTask.values()[nbt.getByte("task")];
		isSquadLeader = nbt.getBoolean("isLeader");
		isSolo = nbt.getBoolean("isSolo");
		regenTimer = nbt.getInteger("regenTimer");
		mainRoFCooldown = nbt.getInteger("mainRoFCooldown");
		reloadTimer = nbt.getInteger("reloadTimer");
		altRoFCooldown = nbt.getInteger("altRoFCooldown");
		mainMagAmmo = nbt.getInteger("mainMagAmmo");
		mainAmmo = nbt.getInteger("mainAmmo");
		altAmmo = nbt.getInteger("altAmmo");
		isSneaking = nbt.getBoolean("isSneaking");
	}
	
	protected void say(String string)
	{
		worldChat.accept(chatSupplier.apply('[' + getSquadName() + ": " + string + ']'));
	}
	
	@Override
	public void setDead()
	{
		super.setDead();
		if (hasFaction())
			getFaction().getRegisteredNames().remove(getName());
		if (hasSquad())
			getSquad().getRegisteredNames().remove(getSquadName());
	}
}
