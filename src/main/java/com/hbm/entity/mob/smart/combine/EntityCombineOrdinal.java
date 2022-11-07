package com.hbm.entity.mob.smart.combine;

import java.util.List;

import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.entity.mob.smart.NPCTask;
import com.hbm.entity.mob.smart.combine.CombineCommandoSoundUtil.LineCategory;
import com.hbm.entity.mob.smart.combine.CombineCommandoSoundUtil.UnitType;
import com.hbm.entity.mob.smart.combine.CombineCommandoSoundUtil.VoiceLine;
import com.hbm.handler.BulletConfiguration;
import com.hbm.handler.GunConfiguration;
import com.hbm.handler.guncfg.GunOSIPRFactory;
import com.hbm.interfaces.ILocationProvider;
import com.hbm.items.ItemAmmoEnums.AmmoHandGrenade;
import com.hbm.items.ModItems;
import com.hbm.render.entity.mob.DeathPose;

import api.hbm.faction.EnumRank;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityCombineOrdinal extends EntityCombineBase
{
	private static final int[] FRONT_ARMOR = {3, 25, 5, 2}, BACK_ARMOR = {3, 15, 2, 2};
	public EntityCombineOrdinal(World world, SquadBase squad, String name)
	{
		super(world, squad, name);
		heldGun = new ItemStack(ModItems.gun_osipr);
	}

	public EntityCombineOrdinal(World world, SquadBase squad)
	{
		super(world, squad);
		heldGun = new ItemStack(ModItems.gun_osipr);
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.5);
		
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4);
	}

	@Override
	public String getSquadName()
	{
		return squad.getName();
	}

	@Override
	public String getMySquadName()
	{
		return "Ordinal";
	}

	@Override
	public EnumRank getRank()
	{
		return EnumRank.SERGEANT_FCLASS;
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
	public ItemStack getHeldItem()
	{
		return heldGun;
	}
	
	@Override
	public void overrideHeldItem(ItemStack stack)
	{
		LOGGER.debug("This NPC is not designed to have its held item be overridden.");
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
		return 2;
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
		return GunOSIPRFactory.getOSIPRConfig();
	}

	@Override
	public BulletConfiguration getMainBulletConfiguration()
	{
		return GunOSIPRFactory.getPulseConfig();
	}

	@Override
	public int getMainMaxAmmoCount()
	{
		return -1;
	}

	@Override
	public GunConfiguration getAltGunConfiguration()
	{
		return GunOSIPRFactory.getAltConfig();
	}

	@Override
	public BulletConfiguration getAltBulletConfiguration()
	{
		return GunOSIPRFactory.getPulseChargedConfig();
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
		say("Reloading...");
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
	}
	
	@Override
	protected void resetAI()
	{
		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
	}

	@Override
	public void onIdle()
	{
		// TODO Auto-generated method stub
		say("Idling...");
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
	public void onKilledEnemy(EntityLivingBase entity)
	{
		// TODO Auto-generated method stub
		say("Kill confirmed.");
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
		say("HVT is contained.");
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
	public void onSquadMemberAlmostDead(EntitySmartNPCBase entity)
	{
		// TODO Auto-generated method stub
		say("Medical assist on " + entity.getMySquadName() + '.');
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
		say("Provisions low, recharging.");
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
		dropItem(ModItems.gun_osipr, 0);
	}

	@Override
	protected void dropFewItems(boolean byPlayer, int lootLevel)
	{
		dropItem(ModItems.syringe_metal_stimpak, 0);
	}

	@Override
	protected void dropRareDrop(int i)
	{
		dropItem(ModItems.grenade, AmmoHandGrenade.IF_GENERIC.ordinal());
	}

	@Override
	protected String getLivingSound()
	{
		return CombineCommandoSoundUtil.generateVoiceLine(UnitType.ORDINAL, CombineCommandoSoundUtil.randomLine(LineCategory.IDLE));
	}

	@Override
	protected String getHurtSound()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDeathSound()
	{
		return CombineCommandoSoundUtil.generateVoiceLine(UnitType.ORDINAL, VoiceLine.DEATH);
	}
	
	@Override
	protected void say(String string)
	{
		super.say("[Ordinal: " + string + ']');
	}

}
