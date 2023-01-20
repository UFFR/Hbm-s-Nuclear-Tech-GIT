package com.hbm.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hbm.handler.BulletConfiguration.BulletBolt;
import com.hbm.handler.BulletConfiguration.BulletPlink;
import com.hbm.handler.BulletConfiguration.BulletStyle;
import com.hbm.interfaces.IBulletHitBehavior;
import com.hbm.interfaces.IBulletHurtBehavior;
import com.hbm.interfaces.IBulletImpactBehavior;
import com.hbm.interfaces.IBulletRicochetBehavior;
import com.hbm.interfaces.IBulletUpdateBehavior;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.lib.ModDamageSource;
import com.hbm.main.MainRegistry;

import api.hbm.entity.IPenetratingProjectile;
import api.hbm.entity.PenetrationModifierFunction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import scala.actors.threadpool.Arrays;

public class BulletConfigurationBuilder implements Cloneable
{
	private String registryName, vPFX, modeName, damageType = ModDamageSource.s_bullet;
	private ComparableStack ammo;
	private int ammoCount = 1, wear, bulletsMin, bulletsMax, maxAge, LBRC, HBRC, penetration = 15, incendiary, emp,
				rainbow, nuke, shrapnel, chlorine, leadChance, caustic, dischargePerShot, firingRate;
	private float velocity, spread, dmgMin, dmgMax, headshotMult = 1.0F, explosive;
	private double gravity, ricochetAngle, bounceMod, penetrationModifier = 0.95, jolt;
	private boolean doesRicochet, doesPenetrate, isSpectral, doesBreakGlass, blackPowder, blockDamage = true, destroysBlocks,
					instakill, liveAfterImpact, dmgProj = true, dmgFire, dmgExplosion, dmgBypass;
	@Nonnull
	private Optional<PenetrationModifierFunction> modFunction = Optional.of(IPenetratingProjectile.DEFAULT_FUNCTION);
	private List<PotionEffect> effects = new ArrayList<PotionEffect>();
	private Set<Block> spectralBlocks = new HashSet<Block>();
	private Set<Material> spectralMaterials = new HashSet<Material>();
	private Optional<IBulletHurtBehavior> bHurt = Optional.empty();
	private Optional<IBulletHitBehavior> bHit = Optional.empty();
	private Optional<IBulletRicochetBehavior> bRicochet = Optional.empty();
	private Optional<IBulletImpactBehavior> bImpact = Optional.empty();
	private Optional<IBulletUpdateBehavior> bUpdate = Optional.empty();
	private EnumChatFormatting chatColour = EnumChatFormatting.WHITE;
	private BulletStyle style = BulletStyle.NONE;
	private BulletPlink plink = BulletPlink.NONE;
	private BulletBolt trail = BulletBolt.LACUNAE;
	
	public BulletConfigurationBuilder(String registryName, ComparableStack ammo)
	{
		this.registryName = registryName;
		this.ammo = ammo;
	}
	
	public String getRegistryName()
	{
		return registryName;
	}

	public BulletConfigurationBuilder setRegistryName(String registryName)
	{
		this.registryName = registryName;
		return this;
	}

	public String getvPFX()
	{
		return vPFX;
	}

	public BulletConfigurationBuilder setvPFX(String vPFX)
	{
		this.vPFX = vPFX;
		return this;
	}

	public String getModeName()
	{
		return modeName;
	}

	public BulletConfigurationBuilder setModeName(String modeName)
	{
		this.modeName = modeName;
		return this;
	}

	public String getDamageType()
	{
		return damageType;
	}

	public BulletConfigurationBuilder setDamageType(String damageType)
	{
		this.damageType = damageType;
		return this;
	}

	public ComparableStack getAmmo()
	{
		return ammo;
	}

	public BulletConfigurationBuilder setAmmo(ComparableStack ammo)
	{
		this.ammo = ammo;
		return this;
	}

	public int getAmmoCount()
	{
		return ammoCount;
	}

	public BulletConfigurationBuilder setAmmoCount(int ammoCount)
	{
		this.ammoCount = ammoCount;
		return this;
	}

	public int getWear()
	{
		return wear;
	}

	public BulletConfigurationBuilder setWear(int wear)
	{
		this.wear = wear;
		return this;
	}

	public int getBulletsMin()
	{
		return bulletsMin;
	}

	public BulletConfigurationBuilder setBulletsMin(int bulletsMin)
	{
		this.bulletsMin = bulletsMin;
		return this;
	}

	public int getBulletsMax()
	{
		return bulletsMax;
	}

	public BulletConfigurationBuilder setBulletsMax(int bulletsMax)
	{
		this.bulletsMax = bulletsMax;
		return this;
	}

	public int getMaxAge()
	{
		return maxAge;
	}

	public BulletConfigurationBuilder setMaxAge(int maxAge)
	{
		this.maxAge = maxAge;
		return this;
	}

	public int getLBRC()
	{
		return LBRC;
	}

	public BulletConfigurationBuilder setLBRC(int lBRC)
	{
		LBRC = lBRC;
		return this;
	}

	public int getHBRC()
	{
		return HBRC;
	}

	public BulletConfigurationBuilder setHBRC(int hBRC)
	{
		HBRC = hBRC;
		return this;
	}

	public int getPenetration()
	{
		return penetration;
	}

	public BulletConfigurationBuilder setPenetration(int penetration)
	{
		this.penetration = penetration;
		return this;
	}

	public int getIncendiary()
	{
		return incendiary;
	}

	public BulletConfigurationBuilder setIncendiary(int incendiary)
	{
		this.incendiary = incendiary;
		return this;
	}

	public int getEmp()
	{
		return emp;
	}

	public BulletConfigurationBuilder setEmp(int emp)
	{
		this.emp = emp;
		return this;
	}

	public int getRainbow()
	{
		return rainbow;
	}

	public BulletConfigurationBuilder setRainbow(int rainbow)
	{
		this.rainbow = rainbow;
		return this;
	}

	public int getNuke()
	{
		return nuke;
	}

	public BulletConfigurationBuilder setNuke(int nuke)
	{
		this.nuke = nuke;
		return this;
	}

	public int getShrapnel()
	{
		return shrapnel;
	}

	public BulletConfigurationBuilder setShrapnel(int shrapnel)
	{
		this.shrapnel = shrapnel;
		return this;
	}

	public int getChlorine()
	{
		return chlorine;
	}

	public BulletConfigurationBuilder setChlorine(int chlorine)
	{
		this.chlorine = chlorine;
		return this;
	}

	public int getLeadChance()
	{
		return leadChance;
	}

	public BulletConfigurationBuilder setLeadChance(int leadChance)
	{
		this.leadChance = leadChance;
		return this;
	}

	public int getCaustic()
	{
		return caustic;
	}

	public BulletConfigurationBuilder setCaustic(int caustic)
	{
		this.caustic = caustic;
		return this;
	}

	public BulletStyle getStyle()
	{
		return style;
	}

	public BulletConfigurationBuilder setStyle(BulletStyle style)
	{
		this.style = style;
		return this;
	}

	public BulletBolt getTrail()
	{
		return trail;
	}

	public BulletConfigurationBuilder setTrail(BulletBolt trail)
	{
		this.trail = trail;
		style = BulletStyle.BOLT;
		return this;
	}

	public BulletPlink getPlink()
	{
		return plink;
	}

	public BulletConfigurationBuilder setPlink(BulletPlink plink)
	{
		this.plink = plink;
		return this;
	}

	public int getDischargePerShot()
	{
		return dischargePerShot;
	}

	public BulletConfigurationBuilder setDischargePerShot(int dischargePerShot)
	{
		this.dischargePerShot = dischargePerShot;
		return this;
	}

	public int getFiringRate()
	{
		return firingRate;
	}

	public BulletConfigurationBuilder setFiringRate(int firingRate)
	{
		this.firingRate = firingRate;
		return this;
	}

	public float getVelocity()
	{
		return velocity;
	}

	public BulletConfigurationBuilder setVelocity(float velocity)
	{
		this.velocity = velocity;
		return this;
	}

	public float getSpread()
	{
		return spread;
	}

	public BulletConfigurationBuilder setSpread(float spread)
	{
		this.spread = spread;
		return this;
	}

	public float getDmgMin()
	{
		return dmgMin;
	}

	public BulletConfigurationBuilder setDmgMin(float dmgMin)
	{
		this.dmgMin = dmgMin;
		return this;
	}

	public float getDmgMax()
	{
		return dmgMax;
	}

	public BulletConfigurationBuilder setDmgMax(float dmgMax)
	{
		this.dmgMax = dmgMax;
		return this;
	}

	public float getHeadshotMult()
	{
		return headshotMult;
	}

	public BulletConfigurationBuilder setHeadshotMult(float headshotMult)
	{
		this.headshotMult = headshotMult;
		return this;
	}

	public float getExplosive()
	{
		return explosive;
	}

	public BulletConfigurationBuilder setExplosive(float explosive)
	{
		this.explosive = explosive;
		return this;
	}

	public double getGravity()
	{
		return gravity;
	}

	public BulletConfigurationBuilder setGravity(double gravity)
	{
		this.gravity = gravity;
		return this;
	}

	public double getRicochetAngle()
	{
		return ricochetAngle;
	}

	public BulletConfigurationBuilder setRicochetAngle(double ricochetAngle)
	{
		this.ricochetAngle = ricochetAngle;
		return this;
	}

	public double getBounceMod()
	{
		return bounceMod;
	}

	public BulletConfigurationBuilder setBounceMod(double bounceMod)
	{
		this.bounceMod = bounceMod;
		return this;
	}

	public double getPenetrationModifier()
	{
		return penetrationModifier;
	}

	public BulletConfigurationBuilder setPenetrationModifier(double penetrationModifier)
	{
		this.penetrationModifier = penetrationModifier;
		return this;
	}

	public double getJolt()
	{
		return jolt;
	}

	public BulletConfigurationBuilder setJolt(double jolt)
	{
		this.jolt = jolt;
		return this;
	}

	public boolean isDoesRicochet()
	{
		return doesRicochet;
	}

	public BulletConfigurationBuilder setDoesRicochet(boolean doesRicochet)
	{
		this.doesRicochet = doesRicochet;
		return this;
	}

	public boolean isDoesPenetrate()
	{
		return doesPenetrate;
	}

	public BulletConfigurationBuilder setDoesPenetrate(boolean doesPenetrate)
	{
		this.doesPenetrate = doesPenetrate;
		return this;
	}

	public boolean isSpectral()
	{
		return isSpectral;
	}

	public BulletConfigurationBuilder setSpectral(boolean isSpectral)
	{
		this.isSpectral = isSpectral;
		return this;
	}

	public boolean isDoesBreakGlass()
	{
		return doesBreakGlass;
	}

	public BulletConfigurationBuilder setDoesBreakGlass(boolean doesBreakGlass)
	{
		this.doesBreakGlass = doesBreakGlass;
		return this;
	}

	public boolean isBlackPowder()
	{
		return blackPowder;
	}

	public BulletConfigurationBuilder setBlackPowder(boolean blackPowder)
	{
		this.blackPowder = blackPowder;
		return this;
	}

	public boolean isBlockDamage()
	{
		return blockDamage;
	}

	public BulletConfigurationBuilder setBlockDamage(boolean blockDamage)
	{
		this.blockDamage = blockDamage;
		return this;
	}

	public boolean isDestroysBlocks()
	{
		return destroysBlocks;
	}

	public BulletConfigurationBuilder setDestroysBlocks(boolean destroysBlocks)
	{
		this.destroysBlocks = destroysBlocks;
		return this;
	}

	public boolean isInstakill()
	{
		return instakill;
	}

	public BulletConfigurationBuilder setInstakill(boolean instakill)
	{
		this.instakill = instakill;
		return this;
	}

	public boolean isLiveAfterImpact()
	{
		return liveAfterImpact;
	}

	public BulletConfigurationBuilder setLiveAfterImpact(boolean liveAfterImpact)
	{
		this.liveAfterImpact = liveAfterImpact;
		return this;
	}

	public boolean isDmgProj()
	{
		return dmgProj;
	}

	public BulletConfigurationBuilder setDmgProj(boolean dmgProj)
	{
		this.dmgProj = dmgProj;
		return this;
	}

	public boolean isDmgFire()
	{
		return dmgFire;
	}

	public BulletConfigurationBuilder setDmgFire(boolean dmgFire)
	{
		this.dmgFire = dmgFire;
		return this;
	}

	public boolean isDmgExplosion()
	{
		return dmgExplosion;
	}

	public BulletConfigurationBuilder setDmgExplosion(boolean dmgExplosion)
	{
		this.dmgExplosion = dmgExplosion;
		return this;
	}

	public boolean isDmgBypass()
	{
		return dmgBypass;
	}

	public BulletConfigurationBuilder setDmgBypass(boolean dmgBypass)
	{
		this.dmgBypass = dmgBypass;
		return this;
	}

	public Optional<PenetrationModifierFunction> getModFunction()
	{
		return modFunction;
	}

	public BulletConfigurationBuilder setModFunction(@Nullable PenetrationModifierFunction modFunction)
	{
		this.modFunction = Optional.ofNullable(modFunction);
		return this;
	}

	public List<PotionEffect> getEffects()
	{
		return effects;
	}

	public BulletConfigurationBuilder setEffects(List<PotionEffect> effects)
	{
		this.effects = effects;
		return this;
	}
	
	public BulletConfigurationBuilder addEffects(Collection<PotionEffect> effects)
	{
		this.effects.addAll(effects);
		return this;
	}
	
	public BulletConfigurationBuilder addEffects(PotionEffect... effects)
	{
		return addEffects(Arrays.asList(effects));
	}

	public Set<Block> getSpectralBlocks()
	{
		return spectralBlocks;
	}

	public BulletConfigurationBuilder setSpectralBlocks(Set<Block> spectralBlocks)
	{
		this.spectralBlocks = spectralBlocks;
		return this;
	}
	
	public BulletConfigurationBuilder addSpectralBlocks(Collection<Block> blocks)
	{
		spectralBlocks.addAll(blocks);
		return this;
	}
	
	public BulletConfigurationBuilder addSpectralBlocks(Block... blocks)
	{
		return addSpectralBlocks(Arrays.asList(blocks));
	}

	public Set<Material> getSpectralMaterials()
	{
		return spectralMaterials;
	}

	public BulletConfigurationBuilder setSpectralMaterials(Set<Material> spectralMaterials)
	{
		this.spectralMaterials = spectralMaterials;
		return this;
	}
	
	public BulletConfigurationBuilder addSpectralMaterials(Collection<Material> materials)
	{
		spectralMaterials.addAll(materials);
		return this;
	}
	
	public BulletConfigurationBuilder addSpectralMaterials(Material... materials)
	{
		return addSpectralMaterials(Arrays.asList(materials));
	}

	public Optional<IBulletHurtBehavior> getbHurt()
	{
		return bHurt;
	}

	public BulletConfigurationBuilder setbHurt(@Nullable IBulletHurtBehavior bHurt)
	{
		this.bHurt = Optional.ofNullable(bHurt);
		return this;
	}

	public Optional<IBulletHitBehavior> getbHit()
	{
		return bHit;
	}

	public BulletConfigurationBuilder setbHit(@Nullable IBulletHitBehavior bHit)
	{
		this.bHit = Optional.ofNullable(bHit);
		return this;
	}

	public Optional<IBulletRicochetBehavior> getbRicochet()
	{
		return bRicochet;
	}

	public BulletConfigurationBuilder setbRicochet(@Nullable IBulletRicochetBehavior bRicochet)
	{
		this.bRicochet = Optional.ofNullable(bRicochet);
		return this;
	}

	public Optional<IBulletImpactBehavior> getbImpact()
	{
		return bImpact;
	}

	public BulletConfigurationBuilder setbImpact(@Nullable IBulletImpactBehavior bImpact)
	{
		this.bImpact = Optional.ofNullable(bImpact);
		return this;
	}

	public Optional<IBulletUpdateBehavior> getbUpdate()
	{
		return bUpdate;
	}

	public BulletConfigurationBuilder setbUpdate(@Nullable IBulletUpdateBehavior bUpdate)
	{
		this.bUpdate = Optional.ofNullable(bUpdate);
		return this;
	}

	public EnumChatFormatting getChatColour()
	{
		return chatColour;
	}

	public BulletConfigurationBuilder setChatColour(EnumChatFormatting chatColour)
	{
		this.chatColour = chatColour;
		return this;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(HBRC, LBRC, ammo, ammoCount, blackPowder, blockDamage, bounceMod, bulletsMax, bulletsMin,
				caustic, chatColour, chlorine, damageType, destroysBlocks, dischargePerShot, dmgBypass, dmgExplosion,
				dmgFire, dmgMax, dmgMin, dmgProj, doesBreakGlass, doesPenetrate, doesRicochet, effects, emp, explosive,
				firingRate, gravity, headshotMult, incendiary, instakill, isSpectral, jolt, leadChance, liveAfterImpact,
				maxAge, modFunction, modeName, nuke, penetration, penetrationModifier, plink, rainbow, registryName,
				ricochetAngle, shrapnel, spectralBlocks, spectralMaterials, spread, style, trail, vPFX, velocity, wear);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof BulletConfigurationBuilder))
			return false;
		final BulletConfigurationBuilder other = (BulletConfigurationBuilder) obj;
		return HBRC == other.HBRC && LBRC == other.LBRC && Objects.equals(ammo, other.ammo)
				&& ammoCount == other.ammoCount && blackPowder == other.blackPowder && blockDamage == other.blockDamage
				&& Double.doubleToLongBits(bounceMod) == Double.doubleToLongBits(other.bounceMod)
				&& bulletsMax == other.bulletsMax && bulletsMin == other.bulletsMin && caustic == other.caustic
				&& chatColour == other.chatColour && chlorine == other.chlorine
				&& Objects.equals(damageType, other.damageType) && destroysBlocks == other.destroysBlocks
				&& dischargePerShot == other.dischargePerShot && dmgBypass == other.dmgBypass
				&& dmgExplosion == other.dmgExplosion && dmgFire == other.dmgFire
				&& Float.floatToIntBits(dmgMax) == Float.floatToIntBits(other.dmgMax)
				&& Float.floatToIntBits(dmgMin) == Float.floatToIntBits(other.dmgMin) && dmgProj == other.dmgProj
				&& doesBreakGlass == other.doesBreakGlass && doesPenetrate == other.doesPenetrate
				&& doesRicochet == other.doesRicochet && Objects.equals(effects, other.effects) && emp == other.emp
				&& Float.floatToIntBits(explosive) == Float.floatToIntBits(other.explosive)
				&& firingRate == other.firingRate
				&& Double.doubleToLongBits(gravity) == Double.doubleToLongBits(other.gravity)
				&& Float.floatToIntBits(headshotMult) == Float.floatToIntBits(other.headshotMult)
				&& incendiary == other.incendiary && instakill == other.instakill && isSpectral == other.isSpectral
				&& Double.doubleToLongBits(jolt) == Double.doubleToLongBits(other.jolt)
				&& leadChance == other.leadChance && liveAfterImpact == other.liveAfterImpact && maxAge == other.maxAge
				&& Objects.equals(modFunction, other.modFunction) && Objects.equals(modeName, other.modeName)
				&& nuke == other.nuke && penetration == other.penetration
				&& Double.doubleToLongBits(penetrationModifier) == Double.doubleToLongBits(other.penetrationModifier)
				&& plink == other.plink && rainbow == other.rainbow && Objects.equals(registryName, other.registryName)
				&& Double.doubleToLongBits(ricochetAngle) == Double.doubleToLongBits(other.ricochetAngle)
				&& shrapnel == other.shrapnel && Objects.equals(spectralBlocks, other.spectralBlocks)
				&& Objects.equals(spectralMaterials, other.spectralMaterials)
				&& Float.floatToIntBits(spread) == Float.floatToIntBits(other.spread) && style == other.style
				&& trail == other.trail && Objects.equals(vPFX, other.vPFX)
				&& Float.floatToIntBits(velocity) == Float.floatToIntBits(other.velocity) && wear == other.wear;
	}
	
	@Override
	public BulletConfigurationBuilder clone()
	{
		try
		{
			return (BulletConfigurationBuilder) super.clone();
		} catch (CloneNotSupportedException e)
		{
			MainRegistry.logger.catching(e);
			return new BulletConfigurationBuilder(registryName, ammo);
		}
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("BulletConfigurationBuilder [registryName=").append(registryName).append(", ammo=").append(ammo)
				.append(", ammoCount=").append(ammoCount).append(", velocity=").append(velocity).append(", spread=")
				.append(spread).append(", wear=").append(wear).append(", bulletsMin=").append(bulletsMin)
				.append(", bulletsMax=").append(bulletsMax).append(", dmgMin=").append(dmgMin).append(", dmgMax=")
				.append(dmgMax).append(", headshotMult=").append(headshotMult).append(", gravity=").append(gravity)
				.append(", maxAge=").append(maxAge).append(", doesRicochet=").append(doesRicochet)
				.append(", ricochetAngle=").append(ricochetAngle).append(", LBRC=").append(LBRC).append(", HBRC=")
				.append(HBRC).append(", bounceMod=").append(bounceMod).append(", penetration=").append(penetration)
				.append(", penetrationModifier=").append(penetrationModifier).append(", modFunction=")
				.append(modFunction).append(", doesPenetrate=").append(doesPenetrate).append(", isSpectral=")
				.append(isSpectral).append(", doesBreakGlass=").append(doesBreakGlass).append(", liveAfterImpact=")
				.append(liveAfterImpact).append(", blackPowder=").append(blackPowder).append(", effects=")
				.append(effects).append(", spectralBlocks=").append(spectralBlocks).append(", spectralMaterials=")
				.append(spectralMaterials).append(", incendiary=").append(incendiary).append(", emp=").append(emp)
				.append(", blockDamage=").append(blockDamage).append(", explosive=").append(explosive).append(", jolt=")
				.append(jolt).append(", rainbow=").append(rainbow).append(", nuke=").append(nuke).append(", shrapnel=")
				.append(shrapnel).append(", chlorine=").append(chlorine).append(", leadChance=").append(leadChance)
				.append(", caustic=").append(caustic).append(", destroysBlocks=").append(destroysBlocks)
				.append(", instakill=").append(instakill).append(", style=").append(style).append(", trail=")
				.append(trail).append(", plink=").append(plink).append(", vPFX=").append(vPFX)
				.append(", dischargePerShot=").append(dischargePerShot).append(", modeName=").append(modeName)
				.append(", chatColour=").append(chatColour).append(", firingRate=").append(firingRate)
				.append(", damageType=").append(damageType).append(", dmgProj=").append(dmgProj).append(", dmgFire=")
				.append(dmgFire).append(", dmgExplosion=").append(dmgExplosion).append(", dmgBypass=").append(dmgBypass)
				.append(']');
		return builder.toString();
	}
}
