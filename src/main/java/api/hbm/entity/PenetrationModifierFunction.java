package api.hbm.entity;

@FunctionalInterface
public interface PenetrationModifierFunction
{
	public double mod(int penetration, double modifier, double distance);

}