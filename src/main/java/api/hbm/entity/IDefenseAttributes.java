package api.hbm.entity;

import java.util.Arrays;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.google.common.annotations.Beta;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;

import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

@Beta
public interface IDefenseAttributes
{
	/**
	 * Armor value in vanilla Minecraft terms. 20 is diamond.
	 * @return A positive integer representing the armor value the entity has.
	 */
	@Nonnegative
	public int getArmorValue();
	/**
	 * 4 values for the specific armor values on each body part's front. Should sum to the same as {@link #getArmorValue()}.
	 * @return A 4 element array, each value representing the armor value on each body part of an entity, according to vanilla.
	 */
	@Nonnull
	public int[] getFrontArmorValues();
	/**
	 * Minimum amount of damage to actually cause harm (below this number is cancelled). 0 to have no threshold.
	 * @return A float representing the threshold.
	 */
	@Nonnegative
	public float getDamageThreshold();
	/**
	 * Maximum damage the entity will take, values above will be clamped to this. <0 to have no cap.
	 * @return Float representing the damage cap.
	 */
	@Nonnegative
	public float getDamageCap();
	/**
	 * General damage modifier value.
	 * @return 0 to make immune to all damage, 1 to have no modifier, 1< to make weak to all damage.
	 */
	@Nonnegative
	public float getDamageModifier();
	/**
	 * Projectile based damage modifier value.
	 * @return 0 to make immune to all projectile based damage, 1 to have no modifier, 1< to make weak to projectiles.
	 */
	@Nonnegative
	public float getProjectileResist();
	/**
	 * Modifier for explosion-type damage.
	 * @return 0 to make immune to explosions, 1 to have no modifier, 1< to make weak to explosions.
	 */
	@Nonnegative
	public float getBlastResist();
	/**
	 * Modifier for fire-type damage.
	 * @return 0 to make immune to fire, 1 to have no modifier, 1< to make weak to fire.
	 */
	@Nonnegative
	public float getFireResist();
	/**
	 * Modifier for radiation absorption.
	 * @return 0 to make immune to radiation, 1 to have no modifier, 1< to make weak to radiation.
	 */
	@Nonnegative
	public float getRadResist();
	
	/**
	 * 4 values for the specific armor values on each body part's back. By default, same value as {@link #getFrontArmorValues()}.
	 * @return A 4 element array, each value representing the armor value on each body part of an entity, according to vanilla.
	 */
	@Nonnull
	public default int[] getBackArmorValues()
	{
		return getFrontArmorValues();
	}
	
	public default String listValues()
	{
		final StringBuilder builder = new StringBuilder();
		
		builder.append("Vanilla armor level: ").append(getArmorValue()).append('\n')
			   .append("Front armor levels: ").append(Arrays.toString(getFrontArmorValues())).append('\n')
			   .append("Back armor levels: ").append(Arrays.toString(getBackArmorValues())).append('\n')
			   .append("Overall damage threshold: ").append(getDamageThreshold()).append('\n')
			   .append("Overall damage cap: ").append(getDamageCap()).append('\n')
			   .append("Overall damage modifier: ").append(getDamageModifier()).append('\n')
			   .append("Projectile resistance: ").append(getProjectileResist()).append('\n')
			   .append("Explosion resistance: ").append(getBlastResist()).append('\n')
			   .append("Fire resistance: ").append(getFireResist()).append('\n')
			   .append("Radiation resistance: ").append(getRadResist());
		
		return builder.toString();
	}
	
	public static float calculateDefenses(IDefenseAttributes defense, DamageSource source, float amount)
	{
		float newAmount = amount;
		newAmount *= defense.getDamageModifier();
		
		if (defense.getDamageCap() > 0)
			newAmount = MathHelper.clamp_float(newAmount, 0, defense.getDamageCap());
		
		if (source.isProjectile())
			newAmount *= defense.getProjectileResist();
		
		if ("radiation".equals(source.getDamageType()))
			newAmount *= defense.getRadResist();
		
		if (source.isExplosion())
			newAmount *= defense.getRadResist();
		
		if (source.isFireDamage())
			newAmount *= defense.getFireResist();
		
		if (defense.getDamageThreshold() > newAmount || newAmount == 0)
		{
			if (defense instanceof EntitySmartNPCBase)
				((EntitySmartNPCBase) defense).onDamageBlocked(source, amount);
		}
		
		return newAmount;
	}
}
