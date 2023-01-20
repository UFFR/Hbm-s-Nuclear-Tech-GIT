package com.hbm.entity.mob.smart.combine;

import java.util.List;

import com.hbm.entity.mob.ai.snpc.solo.EntityAILookAtTarget;
import com.hbm.entity.mob.ai.snpc.solo.EntityAIRangePrimaryAttack;
import com.hbm.entity.mob.ai.snpc.squad.EntityAIEnemyProximitySquad;
import com.hbm.entity.mob.ai.snpc.squad.EntityAINearestTargetSquad;
import com.hbm.entity.mob.ai.squad.CombineCommandoSquad;
import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.entity.mob.smart.NPCTask;
import com.hbm.entity.mob.smart.NameUtil;
import com.hbm.entity.mob.smart.combine.CombineCommandoSoundUtil.LineCategory;
import com.hbm.entity.mob.smart.combine.CombineCommandoSoundUtil.UnitType;
import com.hbm.entity.mob.smart.combine.CombineCommandoSoundUtil.VoiceLine;
import com.hbm.faction.FactionCombine;
import com.hbm.handler.BulletConfigSyncingUtil;
import com.hbm.handler.GunConfiguration;
import com.hbm.handler.guncfg.GunOSIPRFactory;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.items.ItemAmmoEnums.AmmoHandGrenade;
import com.hbm.items.ModItems;
import com.hbm.particle.SpentCasingConfig;
import com.hbm.render.entity.mob.DeathPose;
import com.hbm.util.ParticleUtil;

import api.hbm.faction.EnumRank;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityCombineOrdinal extends EntityCombineCommando
{
	private static final int[] FRONT_ARMOR = {3, 25, 5, 2}, BACK_ARMOR = {3, 15, 2, 2};
	private boolean hasManhack = true;
	
	public EntityCombineOrdinal(World world)
	{
		this(world, new CombineCommandoSquad(world));
	}
	
	public EntityCombineOrdinal(World world, SquadBase squad, String name)
	{
		super(world, squad == null ? new CombineCommandoSquad(world) : squad, name, "Ordinal");
		
		setCurrentItemOrArmor(0, new ItemStack(ModItems.gun_osipr));
	}

	public EntityCombineOrdinal(World world, SquadBase squad)
	{
		super(world, squad == null ? new CombineCommandoSquad(world) : squad,
				NameUtil.generateNameFromDefined(NameUtil::generateCombineName, FactionCombine.getCombine().getRegisteredNames(), Short.MAX_VALUE), "Ordinal");
		
		setCurrentItemOrArmor(0, new ItemStack(ModItems.gun_osipr));
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.55);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4);
		
//		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4);
	}
	
	@Override
	protected int getExperiencePoints(EntityPlayer player)
	{
		return 40;
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
//		if (name == null)
//			name = NameUtil.generateNameFromDefined(NameUtil::generateCombineName, Factions.COMBINE.getRegisteredNames(), Short.MAX_VALUE);
//		if (squadName == null)
//			squadName = "Ordinal";
	}

	@Override
	public EnumRank getRank()
	{
		return EnumRank.SERGEANT;
	}

	@Override
	public boolean hasSquad()
	{
		return true;
	}

	@Override
	public boolean hasRank()
	{
		return true;
	}

	@Override
	public int getArmorValue()
	{
		return 30;
	}

	@Override
	public int[] getFrontArmorValues()
	{
		return FRONT_ARMOR;
	}
	
	@Override
	public int[] getBackArmorValues()
	{
		return BACK_ARMOR;
	}

	@Override
	public float getDamageThreshold()
	{
		return 2;
	}

	@Override
	public float getDamageCap()
	{
		return -1;
	}

	@Override
	public float getDamageModifier()
	{
		return 1;
	}
	
	@Override
	public float getProjectileResist()
	{
		return 0.75f;
	}

	@Override
	public float getBlastResist()
	{
		return 0.8f;
	}

	@Override
	public float getFireResist()
	{
		return 0.85f;
	}

	@Override
	public float getRadResist()
	{
		return 0;
	}

	@Override
	public void overrideHeldItem(ItemStack stack)
	{
		if (stack != null && stack.getItem() == ModItems.gun_osipr)
			equipmentSlots[0] = stack;
		else
			LOGGER.debug("This NPC is not designed to have its held item be overridden, cannot comply.");
	}

	@Override
	public int spotDistance()
	{
		return 100;
	}

	@Override
	public int preferredDistance()
	{
		return 40;
	}
	
	@Override
	public int proximityDistance()
	{
		return 5;
	}

	@Override
	public void applyTask(NPCTask task)
	{
		// TODO Auto-generated method stub
		say("Accepting task " + task + '.');
	}

	@Override
	public int regenDelay()
	{
		return 20 * 60;
	}

	@Override
	public int regenAmount()
	{
		return 1;
	}

	@Override
	public GunConfiguration getMainGunConfiguration()
	{
		return OSIPR_CONFIG;
	}

	@Override
	public int getMainBulletConfiguration()
	{
		return BulletConfigSyncingUtil.SPECIAL_OSIPR;
	}

	@Override
	public int getMainMaxAmmoCount()
	{
		return -1;
	}

	@Override
	public GunConfiguration getAltGunConfiguration()
	{
		return OSIPR_ALT_CONFIG;
	}

	@Override
	public int getAltBulletConfiguration()
	{
		return BulletConfigSyncingUtil.SPECIAL_OSIPR_CHARGED;
	}

	@Override
	public int getAltMaxAmmoCount()
	{
		return 6;
	}

	@Override
	public boolean usesGrenades()
	{
		return true;
	}

	@Override
	public int grenadeCount()
	{
		return 1;
	}

	@Override
	public AmmoHandGrenade grenadeType()
	{
		return worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") ? AmmoHandGrenade.IF_GENERIC : AmmoHandGrenade.IF_CONCUSSION;
	}

	@Override
	public int lowAmmoThreshold()
	{
		return 10;
	}

	@Override
	public ItemStack[] deathLootStacks()
	{
		return null;
	}

	@Override
	public DeathPose poseFromDeathType(DamageSource source, float amount)
	{
		// TODO Auto-generated method stub
		return DeathPose.FAMILY_GUY;
	}

	@Override
	public void reload()
	{
		// TODO Auto-generated method stub
		say("Provisions low, reloading...");
		mainMagAmmo = 30;
		ParticleUtil.spawnCasing(this, casingConfig(), (float) Math.toRadians(rotationPitch), (float) Math.toRadians(rotationYaw), getEyeHeight(), isSneaking());
	}

	@Override
	public boolean isOmniscient()
	{
		return false;
	}
	
	@Override
	protected void initializeAI()
	{
		// TODO Auto-generated method stub
		getNavigator().setAvoidsWater(true);
		getNavigator().setCanSwim(true);
		getNavigator().setEnterDoors(true);
		getNavigator().setBreakDoors(true);
		
		tasks.addTask(1, new EntityAIRangePrimaryAttack(this));
		tasks.addTask(1, new EntityAILookAtTarget(this, this::getAttackTarget));
		tasks.addTask(2, new EntityAIWander(this, getAIMoveSpeed() * 0.75));
		
		targetTasks.addTask(0, new EntityAIEnemyProximitySquad(this));
		targetTasks.addTask(1, new EntityAINearestTargetSquad(this));
	}
	
	@Override
	protected void resetAI()
	{
		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
	}
	
	@Override
	protected boolean usesCasings()
	{
		return true;
	}
	
	@Override
	protected SpentCasingConfig casingConfig()
	{
		return GunOSIPRFactory.CASING_AR2;
	}

	@Override
	public void onIdle()
	{
		// TODO Auto-generated method stub
		if (currentTask == NPCTask.HOLD)
		{
			say(CombineCommandoSoundUtil.randomLine(LineCategory.IDLE));
		} else
		{
			say(CombineCommandoSoundUtil.randomLine(LineCategory.COMBAT_IDLE));
		}
	}

	@Override
	public void onNewSpotting(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub
		if (entity.isEntityUndead())
			say("Call contact: necrotics.");
		else if (entity instanceof EntitySmartNPCBase)
			say("Outbreak.");
		else
			say("Visual on exogen.");
		
		if (getAttackTarget() == null)
			setAttackTarget(entity);
	}

	@Override
	public void onBeginAttack(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub
		say("Applying pressure...");
	}
	
	@Override
	public void notifyEnemyInCover(EntityLivingBase entity, ILocationProvider loc)
	{
		// TODO Auto-generated method stub
		say("Code Breakdown on grid " + loc + '.');
	}

	@Override
	public void onEnemyAlmostDead(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub
		say("Target compromised.");
	}

	@Override
	public void onKillEntity(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub
		if (entity instanceof EntityPlayer)
			onKilledPlayer((EntityPlayer) entity);
		else if (entity.isEntityUndead())
			say("Tag one necrotic.");
		else if (entity instanceof EntitySmartNPCBase)
			say("Kill confirmed.");
		else
			say("One contained.");
	}

	@Override
	public void onSpotPlayer(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		say("Designate target as Priority One objective.");
	}

	@Override
	public void onPlayerAlmostDead(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		say("That's it, wrap it up.");
	}

	@Override
	public void onKilledPlayer(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		super.onKilledPlayer(player);
		say("Overwatch, HVT is contained.");
	}
	
	@Override
	public void onDamageBlocked(DamageSource source, float originalAmount)
	{
		// TODO Auto-generated method stub
		say("Target ineffective.");
	}

	@Override
	public void onDamagedMinor()
	{
		// TODO Auto-generated method stub
		say("Bodypack holding.");
	}

	@Override
	public void onDamagedMajor()
	{
		// TODO Auto-generated method stub
		say("Medical assist on Ordinal.");
	}

	@Override
	public void onNearDeath()
	{
		// TODO Auto-generated method stub
		say("Ordinal is life limited.");
	}

	@Override
	public void notifyEnemyTooClose(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub
		say("Proximity violation. Amending.");
	}

	@Override
	public void onLostEnemy()
	{
		// TODO Auto-generated method stub
		say("Contaminant is not visible.");
	}

	@Override
	public void onLostEnemyLong()
	{
		// TODO Auto-generated method stub
		say("Requesting last known sector.");
	}

	@Override
	public void onReestablishContact(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub
		say("Sightline confirmed.");
	}

	@Override
	public void onSquadMemberNeedHelp(EntitySmartNPCBase entity)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onSquadMemberAlmostDead(EntitySmartNPCBase entity)
	{
		// TODO Auto-generated method stub
		say("Medical assist on " + entity.getSquadName() + '.');
	}

	@Override
	public void onLostSquadMember(EntitySmartNPCBase entity)
	{
		// TODO Auto-generated method stub
		say("Overwatch, request reserve activation.");
	}

	@Override
	public void onLostSquadLeader(EntitySmartNPCBase entity)
	{
		// TODO Auto-generated method stub
		onLastMemberRemaining();
	}

	@Override
	public void onLastMemberRemaining()
	{
		// TODO Auto-generated method stub
		say("Overwatch, outbreak is uncontained.");
	}
	
	@Override
	public void onFriendNeedHelp(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFriendAlmostDead(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onLostFriend(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyLowAmmunition()
	{
		// TODO Auto-generated method stub
		say("Provisions low.");
	}

	@Override
	public void notifyDangerGeneric(ILocationProvider loc)
	{
		// TODO Auto-generated method stub
		say("All units evade.");
	}

	@Override
	public void notifyDangerGrenade(ILocationProvider loc, AmmoHandGrenade type)
	{
		// TODO Auto-generated method stub
		say("All units, bouncer proximity.");
	}

	@Override
	public void notifyEnemyCrosshair(EntityLivingBase entity, Vec3 vector)
	{
		// TODO Auto-generated method stub
		say("Ordinal requesting shadow.");
	}

	@Override
	public void notifyNearbyItems(List<EntityItem> items)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void tryThrowGrenade(ILocationProvider loc)
	{
		// TODO Auto-generated method stub
		say("All units stay low, extractor live, sharp zone.");
	}

	@Override
	protected void dropEquipment(boolean byPlayer, int lootLevel)
	{
		final ItemStack gunStack = new ItemStack(ModItems.gun_osipr), ammoStack = new ItemStack(ModItems.gun_osipr_ammo, mainAmmo / 30);
		gunStack.stackTagCompound = getStackNBTForGun();
		entityDropItem(gunStack, rand.nextFloat());
		entityDropItem(ammoStack, rand.nextFloat());
	}

	@Override
	protected void dropFewItems(boolean byPlayer, int lootLevel)
	{
		dropItem(ModItems.syringe_metal_stimpak, rand.nextInt(MathHelper.clamp_int(lootLevel, 1, 64)));
	}

	@Override
	protected void dropRareDrop(int i)
	{
		if (rand.nextBoolean())
			entityDropItem(ModItems.grenade.stackFromEnum(AmmoHandGrenade.IF_GENERIC), rand.nextFloat());
		else
			dropItem(ModItems.gun_osipr_ammo2, 1);
	}

	@Override
	protected String getLivingSound()
	{
//		return CombineCommandoSoundUtil.generateVoiceLine(UnitType.ORDINAL, CombineCommandoSoundUtil.randomLine(LineCategory.IDLE));
		return null;
	}

	@Override
	protected String getHurtSound()
	{
		say("*Hurt*");
		return CombineCommandoSoundUtil.generateVoiceLine(UnitType.ORDINAL, VoiceLine.HURT);
	}

	@Override
	protected String getDeathSound()
	{
		say("*Flatline*");
		return CombineCommandoSoundUtil.generateVoiceLine(UnitType.ORDINAL, VoiceLine.DEATH);
	}
	
	protected void say(VoiceLine line)
	{
		say(line.caption);
		worldObj.playSoundAtEntity(this, CombineCommandoSoundUtil.generateVoiceLine(UnitType.ORDINAL, line), 2, 1);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("hasManhack", hasManhack);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		hasManhack = nbt.getBoolean("hasManhack");
	}
}
