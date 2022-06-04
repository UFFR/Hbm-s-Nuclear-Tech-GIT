package api.hbm.entity;

import com.google.common.annotations.Beta;

@Beta
public interface IEntityDefenseAttributes
{
	/**Armor value in vanilla Minecraft terms**/
	public int getArmorValue();
	/**Minimum amount of damage to actually cause harm (below this number is cancelled). 0 to have no threshold.**/
	public float getDamageThreshold();
	/**Maximum damage the entity will take, values above will be clamped to this. <0 to have no cap.**/
	public float getDamageCap();
	/**General damage modifier. 0 to make immune to all damage, 1 to do nothing, 1< to make weak to all damage.**/
	public float getDamageModifier();
	/**Modifier for explosion-type damage. 0 to make immune to explosions, 1 to do nothing, 1< to make weak to explosions.**/
	public float getBlastResist();
	/**If the entity is immune to fire, no middle ground, I guess**/
	public boolean isFireproof();
	/**Modifier for radiation absorption. 0 to make immune to radiation, 1 to do nothing, 1< to make weak to radiation.**/
	public float getRadResist();
}
