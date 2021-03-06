package com.hbm.handler.guncfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.hbm.entity.projectile.EntityBulletBase;
import com.hbm.explosion.ExplosionChaos;
import com.hbm.explosion.ExplosionLarge;
import com.hbm.handler.BulletConfigSyncingUtil;
import com.hbm.handler.BulletConfiguration;
import com.hbm.handler.GunConfiguration;
import com.hbm.handler.GunConfigurationEnergy;
import com.hbm.interfaces.IBulletImpactBehavior;
import com.hbm.items.ModItems;
import com.hbm.main.MainRegistry;
import com.hbm.packet.AuxParticlePacketNT;
import com.hbm.packet.PacketDispatcher;
import com.hbm.potion.HbmPotion;
import com.hbm.render.util.RenderScreenOverlay.Crosshair;
import com.hbm.util.I18nUtil;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

public class GunEnergyFactory {
	private static Random rand = new Random();
	public static GunConfiguration getEMPConfig() {
		
		GunConfiguration config = new GunConfiguration();
		
		config.rateOfFire = 30;
		config.roundsPerCycle = 1;
		config.gunMode = GunConfiguration.MODE_NORMAL;
		config.firingMode = GunConfiguration.FIRE_MANUAL;
		config.reloadDuration = 10;
		config.firingDuration = 0;
		config.ammoCap = 0;
		config.durability = 1500;
		config.reloadType = GunConfiguration.RELOAD_NONE;
		config.allowsInfinity = true;
		config.crosshair = Crosshair.L_SPLIT;
		config.firingSound = "hbm:weapon.teslaShoot";
		
		config.name = "EMP Orb Projector";
		config.manufacturer = "MWT Prototype Labs";
		
		config.config = new ArrayList<Integer>();
		config.config.add(BulletConfigSyncingUtil.SPECIAL_EMP);
		
		return config;
	}
	
	public static GunConfiguration getFlamerConfig() {
		
		GunConfiguration config = new GunConfiguration();
		
		config.rateOfFire = 1;
		config.roundsPerCycle = 1;
		config.gunMode = GunConfiguration.MODE_NORMAL;
		config.firingMode = GunConfiguration.FIRE_AUTO;
		config.reloadDuration = 20;
		config.reloadSoundEnd = false;
		config.firingDuration = 0;
		config.ammoCap = 100;
		config.durability = 1000;
		config.reloadType = GunConfiguration.RELOAD_FULL;
		config.allowsInfinity = true;
		config.crosshair = Crosshair.L_CIRCLE;
		config.firingSound = "hbm:weapon.flamethrowerShoot";
		config.reloadSound = "hbm:weapon.flamerReload";
		
		config.name = "Heavy Duty Flamer";
		config.manufacturer = "MWT Prototype Labs";

		config.comment.add("Dragon-slaying: Advanced techniques, part 1:");
		config.comment.add("Try not to get eaten by the dragon.");
		config.comment.add("");
		config.comment.add("Hope that helps.");
		
		config.config = new ArrayList<Integer>();
		config.config.add(BulletConfigSyncingUtil.FLAMER_NORMAL);
		config.config.add(BulletConfigSyncingUtil.FLAMER_NAPALM);
		config.config.add(BulletConfigSyncingUtil.FLAMER_WP);
		config.config.add(BulletConfigSyncingUtil.FLAMER_VAPORIZER);
		config.config.add(BulletConfigSyncingUtil.FLAMER_GAS);
		
		return config;
	}
	
	public static GunConfiguration getZOMGConfig() {
		
		GunConfiguration config = new GunConfiguration();
		
		config.rateOfFire = 1;
		config.roundsPerCycle = 1;
		config.gunMode = GunConfiguration.MODE_NORMAL;
		config.firingMode = GunConfiguration.FIRE_AUTO;
		config.reloadDuration = 10;
		config.reloadSoundEnd = false;
		config.firingDuration = 0;
		config.durability = 100000;
		config.reloadType = GunConfiguration.RELOAD_FULL;
		config.ammoCap = 1000;
		config.allowsInfinity = true;
		config.crosshair = Crosshair.L_ARROWS;
		config.firingSound = "hbm:weapon.zomgShoot";
		config.reloadSound = "hbm:weapon.b92Reload";
		
		config.name = "EMC101 Prismatic Negative Energy Cannon";
		config.manufacturer = "MWT Prototype Labs";

		config.comment.add("Taste the rainbow!");
		
		config.config = new ArrayList<Integer>();
		config.config.add(BulletConfigSyncingUtil.ZOMG_BOLT);
		
		return config;
	}
	
	public static GunConfiguration getVortexConfig() {
		
		GunConfiguration config = new GunConfiguration();
		
		return config;
		
	}
	
	public static GunConfiguration getTWRConfig()
	{
		GunConfiguration config = new GunConfiguration();
		int randLore = rand.nextInt(7);

		config.rateOfFire = 10;
		config.roundsPerCycle = 1;
		config.gunMode = GunConfiguration.MODE_NORMAL;
		config.firingMode = GunConfiguration.FIRE_MANUAL;
		config.durability = 1000000000;
		//config.hasSights = false;
		config.reloadSound = "hbm:weapon.b92Reload";
		config.firingSound = "hbm:weapon.zomgShoot";
		config.ammoCap = 16;// Subject to change
		config.reloadType = GunConfiguration.RELOAD_FULL;
		config.allowsInfinity = false;
		config.damage = EnumChatFormatting.BOLD + "A lot";
		config.crosshair = Crosshair.L_SPLIT;
		
		config.config = new ArrayList<Integer>();
		config.config.add(BulletConfigSyncingUtil.TWR_RAY);
		
		config.name = "Time Warp Rifle";
		config.manufacturer = "Lunar Defense Corp";
		String[] lore = I18nUtil.resolveKeyArray("item.gun_twr.desc." + randLore);
		for (String s : lore)
			config.comment.add(EnumChatFormatting.ITALIC + s);
		// May remove if it defeats the purpose of a semi-obscure reference
		config.advLore.add("Born from the Lunarian's vastly superior technology, the Time Warp Rifle (TWR)");
		config.advLore.add("is no ordinary sniper rifle. It was meant to be a weapon to eliminate targets of");
		config.advLore.add("extremely high priority, since very few beings could ever hope of surviving a shot");
		config.advLore.add("and even fewer (exactly none) could evade it. The only known TWR to be manufactured");
		config.advLore.add("and put into service was weilded by Reisen 2, sent by the Watatsuki sisters to eliminate");
		config.advLore.add("Yakumo Yukari to ensure that \"Moonlight Descent Ceremony\" would continue without");
		config.advLore.add("opposition. Despite surviving one hit to center of mass and nearly defeating Reisen 2,");
		config.advLore.add("Yukari ultimately was successfully killed. However, on her mission to eliminate Hakurei");
		config.advLore.add("Reimu, she was ambushed and incapacitated by Reisen (Prime), cutting that instance of");
		config.advLore.add("the rifle's service life short.");
		
		config.advFuncLore.add("While not the most destructive or advanced weapon developed by the Lunarians, its");
		config.advFuncLore.add("function and capability in its role is without question. It's ability is determined");
		config.advFuncLore.add("by 3 factors: the ammunition type, the scope, and the delivery method. Upon inspection,");
		config.advFuncLore.add("there appear to be little to no moving parts beyond the trigger, this is due to the");
		config.advFuncLore.add("ammunition and delivery method. Instead of firing conventional bullets, it fires");
		config.advFuncLore.add("micro-singularities, which erase everything in their path out of existence and");
		config.advFuncLore.add("giving the target immense damage. Next is delivery, as with the quote: \"You can't");
		config.advFuncLore.add("dodge a bullet that's already hit\", the TWR sends a micro-singularity across the");
		config.advFuncLore.add("5th dimension to the target, meaning it arrives the moment the trigger is pulled.");
		config.advFuncLore.add("Finally is the \"Heisenberg Uncertainty Scope\" (nick. Schrödinger's Looking Glass).");
		config.advFuncLore.add("In layman's terms, it is like a portable Schrödinger's box and it is used to determine");
		config.advFuncLore.add("the fate of the target to ensure termination or at the very least, delivery.");
		if (MainRegistry.isPolaroid11)
		{
			config.advFuncLore.add("...");
			config.advFuncLore.add(EnumChatFormatting.STRIKETHROUGH + "Jk, we have no idea how it works");
		}
		return config;
	}
	/** Main fire gun mode **/
	public static GunConfigurationEnergy getHLRMainConfig()
	{
		GunConfigurationEnergy config = new GunConfigurationEnergy();
		int randLore = rand.nextInt(4);
		config.rateOfFire = 1;
		config.roundsPerCycle = 1;
		config.ammoCap = 100000000000L;
		config.ammoRate = 10000000;
		config.chargeRate = 100000000;
		config.gunMode = GunConfiguration.MODE_NORMAL;
		config.firingMode = GunConfiguration.FIRE_AUTO;
		config.durability = 100000000;
		config.reloadType = GunConfiguration.RELOAD_FULL;
		config.allowsInfinity = false;
		config.hasSights = true;
		config.damage = "Main: 15 - 18; Alt: 20 - 25";
		config.crosshair = Crosshair.L_CROSS;
		config.firingSound = "hbm:weapon.osiprShoot";
		config.config = new ArrayList<Integer>();
		config.config.add(BulletConfigSyncingUtil.HLR_NORMAL);
		config.name = "1974 Chang'e type LMG \"Heavy Lunatic Rifle\"";
		config.manufacturer = "Lunar Defense Corp";
		String[] lore = I18nUtil.resolveKeyArray("item.gun_hlr.desc." + randLore);
		for (String s : lore)
			config.comment.add(EnumChatFormatting.ITALIC + s);

		//config.comment.add(String.format("\"%sDodge%s%s Graze this\"", EnumChatFormatting.STRIKETHROUGH, EnumChatFormatting.RESET, EnumChatFormatting.GRAY));

		return config;
	}
	/** Alt fire gun mode **/
	public static GunConfigurationEnergy getHLRSecondConfig()
	{
		GunConfigurationEnergy config = new GunEnergyFactory().getHLRMainConfig();
		config.ammoRate = 250000000;
		config.rateOfFire = 3;
		//config.firingSound = "hbm:weapon.zomgShoot";
		config.config.clear();
		config.config.add(BulletConfigSyncingUtil.HLR_ALT);
		return config;
	}
	
	public static GunConfigurationEnergy getLunaticConfig()
	{
		GunConfigurationEnergy config = new GunConfigurationEnergy();
		config.rateOfFire = 5;
		config.roundsPerCycle = 1;
		config.ammoCap = 100000000L;
		config.ammoRate = 1000000;
		config.chargeRate = 1000000;
		config.gunMode = GunConfiguration.MODE_NORMAL;
		config.firingMode = 0;
		config.durability = 100000;
		config.allowsInfinity = false;
		config.hasSights = false;
		
		config.damage = "15 - 18";
		config.crosshair = Crosshair.CLASSIC;
		config.firingSound = "hbm:weapon.osiprShoot";
		config.name = "1958 Lunatic Gun (Revised)";
		config.manufacturer = "Lunar Defense Corp";
		config.config = new ArrayList<Integer>();
		config.config.add(BulletConfigSyncingUtil.HLR_NORMAL);
		return config;
	}
	
	public static GunConfigurationEnergy getTesterConfig()
	{
		GunConfigurationEnergy config = new GunConfigurationEnergy();
		config.rateOfFire = 1;
		config.roundsPerCycle = 1;
		config.ammoCap = 10000000000L;
		config.ammoRate = 100000;
		config.chargeRate = 100000000;
		config.gunMode = GunConfiguration.MODE_NORMAL;
		config.firingMode = GunConfiguration.FIRE_AUTO;
		config.durability = Integer.MAX_VALUE;//h
		config.reloadType = GunConfiguration.RELOAD_FULL;
		config.allowsInfinity = false;
		config.damage = "Yes";
		config.crosshair = Crosshair.L_CROSS;
		config.firingSound = "hbm:weapon.osiprShoot";
		config.config = new ArrayList<Integer>();
		config.config.add(BulletConfigSyncingUtil.HLR_NORMAL);
		config.name = "the pooper shooter";//I am very mature (and creative)
		config.manufacturer = "Fisher Price";
		config.comment.add("Let the pain end");
		
		return config;
	}
	
	public static BulletConfiguration getOrbusConfig() {
		
		BulletConfiguration bullet = new BulletConfiguration();
		
		bullet.ammo = ModItems.gun_emp_ammo;
		
		bullet.velocity = 1F;
		bullet.spread = 0.0F;
		bullet.wear = 10;
		bullet.bulletsMin = 1;
		bullet.bulletsMax = 1;
		bullet.dmgMin = 10;
		bullet.dmgMax = 12;
		bullet.gravity = 0D;
		bullet.maxAge = 100;
		bullet.doesRicochet = false;
		bullet.doesPenetrate = true;
		bullet.doesBreakGlass = false;
		bullet.style = BulletConfiguration.STYLE_ORB;
		bullet.plink = BulletConfiguration.PLINK_NONE;
		bullet.emp = 10;
		
		bullet.effects = new ArrayList();
		bullet.effects.add(new PotionEffect(Potion.moveSlowdown.id, 10 * 20, 1));
		bullet.effects.add(new PotionEffect(Potion.weakness.id, 10 * 20, 4));
		
		return bullet;
	}
	
	public static BulletConfiguration getFlameConfig() {
		
		BulletConfiguration bullet = new BulletConfiguration();
		
		bullet.ammo = ModItems.ammo_fuel;
		bullet.ammoCount = 100;
		
		bullet.velocity = 0.75F;
		bullet.spread = 0.025F;
		bullet.wear = 1;
		bullet.bulletsMin = 3;
		bullet.bulletsMax = 5;
		bullet.dmgMin = 2;
		bullet.dmgMax = 4;
		bullet.gravity = 0.01D;
		bullet.maxAge = 60;
		bullet.doesRicochet = false;
		bullet.doesPenetrate = true;
		bullet.doesBreakGlass = false;
		bullet.style = BulletConfiguration.STYLE_NONE;
		bullet.plink = BulletConfiguration.PLINK_NONE;
		bullet.vPFX = "flame";
		bullet.incendiary = 10;
		
		bullet.bImpact = new IBulletImpactBehavior() {

			@Override
			public void behaveBlockHit(EntityBulletBase bullet, int x, int y, int z) {
				
				if(!bullet.worldObj.isRemote) {
					NBTTagCompound data = new NBTTagCompound();
					data.setString("type", "vanillaburst");
					data.setString("mode", "flame");
					data.setInteger("count", 15);
					data.setDouble("motion", 0.1D);
					
					PacketDispatcher.wrapper.sendToAllAround(new AuxParticlePacketNT(data, bullet.posX, bullet.posY, bullet.posZ), new TargetPoint(bullet.dimension, bullet.posX, bullet.posY, bullet.posZ, 50));
				}
			}
		};
		
		return bullet;
	}
	
	public static BulletConfiguration getNapalmConfig() {
		
		BulletConfiguration bullet = getFlameConfig();
		
		bullet.ammo = ModItems.ammo_fuel_napalm;
		bullet.wear = 2;
		bullet.dmgMin = 4;
		bullet.dmgMax = 6;
		bullet.maxAge = 200;
		
		return bullet;
	}
	
	public static BulletConfiguration getPhosphorusConfig() {
		
		BulletConfiguration bullet = getFlameConfig();
		
		bullet.ammo = ModItems.ammo_fuel_phosphorus;
		bullet.wear = 2;
		bullet.spread = 0.0F;
		bullet.bulletsMin = 1;
		bullet.bulletsMax = 1;
		bullet.dmgMin = 4;
		bullet.dmgMax = 6;
		bullet.maxAge = 200;
		bullet.vPFX = "smoke";
		
		bullet.bImpact = BulletConfigFactory.getPhosphorousEffect(5, 60 * 20, 25, 0.25);
		
		return bullet;
	}
	
	public static BulletConfiguration getVaporizerConfig() {
		
		BulletConfiguration bullet = getFlameConfig();
		
		bullet.ammo = ModItems.ammo_fuel_vaporizer;
		bullet.wear = 4;
		bullet.spread = 0.25F;
		bullet.bulletsMin = 8;
		bullet.bulletsMax = 10;
		bullet.dmgMin = 6;
		bullet.dmgMax = 10;
		bullet.maxAge = 15;
		bullet.vPFX = "flame";
		bullet.incendiary = 0;
		
		PotionEffect eff = new PotionEffect(HbmPotion.phosphorus.id, 20 * 20, 0, true);
		eff.getCurativeItems().clear();
		bullet.effects = new ArrayList();
		bullet.effects.add(new PotionEffect(eff));
		
		return bullet;
	}
	
	public static BulletConfiguration getGasConfig() {
		
		BulletConfiguration bullet = getFlameConfig();
		
		bullet.ammo = ModItems.ammo_fuel_gas;
		bullet.wear = 1;
		bullet.spread = 0.05F;
		bullet.gravity = 0D;
		bullet.bulletsMin = 5;
		bullet.bulletsMax = 7;
		bullet.dmgMin = 0;
		bullet.dmgMax = 0;
		bullet.vPFX = "cloud";
		bullet.incendiary = 0;
		
		bullet.bImpact = BulletConfigFactory.getGasEffect(5, 60 * 20);
		
		return bullet;
	}
	
	public static BulletConfiguration getZOMGBoltConfig() {
		
		BulletConfiguration bullet = new BulletConfiguration();
		
		bullet.ammo = ModItems.nugget_euphemium;
		bullet.ammoCount = 1000;
		bullet.wear = 1;
		bullet.velocity = 1F;
		bullet.spread = 0.125F;
		bullet.maxAge = 100;
		bullet.gravity = 0D;
		bullet.bulletsMin = 5;
		bullet.bulletsMax = 5;
		bullet.dmgMin = 10000;
		bullet.dmgMax = 25000;

		bullet.style = bullet.STYLE_BOLT;
		bullet.trail = bullet.BOLT_ZOMG;
		
		bullet.effects = new ArrayList();
		bullet.effects.add(new PotionEffect(HbmPotion.bang.id, 10 * 20, 0));
		
		bullet.bImpact = new IBulletImpactBehavior() {

			@Override
			public void behaveBlockHit(EntityBulletBase bullet, int x, int y, int z) {
				
				if(!bullet.worldObj.isRemote) {
					ExplosionChaos.explodeZOMG(bullet.worldObj, (int)bullet.posX, (int)bullet.posY, (int)bullet.posZ, 5);
					bullet.worldObj.playSoundEffect(bullet.posX, bullet.posY, bullet.posZ, "hbm:entity.bombDet", 5.0F, 1.0F);
    				ExplosionLarge.spawnParticles(bullet.worldObj, bullet.posX, bullet.posY, bullet.posZ, 5);
				}
			}
		};
		
		return bullet;
	}

	public static BulletConfiguration getTurbineConfig() {
		
		BulletConfiguration bullet = new BulletConfiguration();
		
		bullet.ammo = ModItems.nothing;
		bullet.dmgMin = 100;
		bullet.dmgMax = 150;
		bullet.velocity = 1F;
		bullet.gravity = 0.0;
		bullet.maxAge = 200;
		bullet.style = bullet.STYLE_BLADE;
		bullet.destroysBlocks = true;
		bullet.doesRicochet = false;
		
		return bullet;
	}
	
	// TODO Finish, not technically a bullet, it's supposed to be a ray, not that it works in its current state anyway
	public static BulletConfiguration getSingConfig()
	{
		BulletConfiguration bullet = new BulletConfigFactory().standardBulletConfig();
		
		bullet.ammo = ModItems.singularity_micro;
		bullet.velocity = 10.0F;
		bullet.spread = 0.0F;
		bullet.wear = 1000;
		
		bullet.dmgMax = 20000;
		bullet.dmgMin = 10000;
		
		bullet.gravity = 0D;
		bullet.maxAge = 400;
		
		bullet.caustic = 100;
		bullet.doesRicochet = false;
		bullet.doesPenetrate = true;
		//bullet.isSpectral = true;
		bullet.doesBreakGlass = true;
		bullet.destroysBlocks = true;
		
		bullet.effects = new ArrayList<PotionEffect>();
		bullet.effects.add(new PotionEffect(HbmPotion.fragile.id, 60 * 20, 4));
		bullet.effects.add(new PotionEffect(HbmPotion.perforated.id, 60 * 20, 4));
		
		//bullet.instakill = true;
		// TODO Placeholder
		//bullet.style = BulletConfiguration.STYLE_ORB;
		bullet.trail = 1;

		return bullet;
	}
	public static BulletConfiguration getRegSingConfig()
	{
		BulletConfiguration bullet = GunEnergyFactory.getSingConfig();
		
		bullet.ammo = ModItems.singularity;
		bullet.ammoCount = 100;
		
		return bullet;
	}
	public static BulletConfiguration getSuperheatedSingConfig()
	{
		BulletConfiguration bullet = GunEnergyFactory.getRegSingConfig();
		
		bullet.ammo = ModItems.singularity_super_heated;
		bullet.dmgMax += 40000;
		bullet.dmgMin += 40000;
		bullet.incendiary = 100;
		bullet.effects.add(new PotionEffect(HbmPotion.phosphorus.id, 60 * 20, 4));
		
		return bullet;
	}
	public static BulletConfiguration getCounterResonantSingConfig()
	{
		BulletConfiguration bullet = GunEnergyFactory.getRegSingConfig();
		
		bullet.ammo = ModItems.singularity_counter_resonant;
		bullet.ammoCount = 500;
		bullet.velocity *= 2;
		bullet.isSpectral = true;
		bullet.doesBreakGlass = false;
		bullet.destroysBlocks = false;
		bullet.incendiary = 0;
		bullet.instakill = true;
		
		return bullet;
	}
	/** Main fire bullet **/
	public static BulletConfiguration getHLRDefaultConfig()
	{
		BulletConfiguration bullet = new BulletConfigFactory().standardBulletConfig();
		
		bullet.ammo = ModItems.nothing;
		bullet.velocity = 10.0F;
		bullet.spread = 0.01F;
		bullet.maxAge = 350;
		bullet.wear = 10;
		
		bullet.dmgMax = 18F;
		bullet.dmgMin = 15F;
		
		bullet.gravity = 0D;
		bullet.doesRicochet = false;
		bullet.doesBreakGlass = true;
		bullet.doesPenetrate = true;
		
		bullet.effects = new ArrayList<PotionEffect>();
		bullet.effects.add(new PotionEffect(HbmPotion.hollow.id, 2 * 20));
		bullet.effects.add(new PotionEffect(HbmPotion.fragile.id, 5 * 20));
		
		bullet.style = BulletConfiguration.STYLE_BOLT;
		bullet.trail = BulletConfiguration.BOLT_LASER;
		
		return bullet;
	}
	/** Alt fire bullet **/
	public static BulletConfiguration getHLRAltConfig()
	{
		BulletConfiguration bullet = new BulletConfigFactory().standardBuckshotConfig();
		
		bullet.ammo = ModItems.nothing;
		bullet.velocity = 10.0F;
		bullet.spread = 0.125F;
		bullet.maxAge = 350;
		bullet.wear = 25;
		
		bullet.dmgMax = 25F;
		bullet.dmgMin = 20F;
		bullet.bulletsMax = 18;
		bullet.bulletsMin = 12;
		
		bullet.gravity = 0D;
		bullet.doesRicochet = false;
		bullet.doesBreakGlass = true;
		bullet.doesPenetrate = true;
		
		bullet.effects = new ArrayList<PotionEffect>();
		bullet.effects.add(new PotionEffect(HbmPotion.hollow.id, 5 * 20, 1));
		bullet.effects.add(new PotionEffect(HbmPotion.fragile.id, 7 * 20, 1));
		bullet.effects.add(new PotionEffect(HbmPotion.perforated.id, 3 * 20));
		
		bullet.style = BulletConfiguration.STYLE_BOLT;
		bullet.trail = BulletConfiguration.BOLT_LASER;
		
		return bullet;
	}
}
