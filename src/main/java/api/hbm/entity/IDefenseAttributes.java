package api.hbm.entity;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.google.common.annotations.Beta;

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
}
