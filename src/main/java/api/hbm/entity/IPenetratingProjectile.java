package api.hbm.entity;

import java.util.Optional;

import com.hbm.interfaces.ILocationProvider;

public interface IPenetratingProjectile
{
	public int getPenetration();
	public double getModifier();
	public ILocationProvider getOrigin();
	public Optional<PenetrationModifierFunction> getModifierFunction();
	
	/**
	 * {@code m^(d / 50) * p}<br>
	 * Penetration value = p<br>
	 * Distance = d<br>
	 * Modifier = m (default 0.95)<br>
	 * Meaning, assuming the modifier is 0.95, the penetration will drop ~5% over 50 meters or be reduced to ~95% of the original value
	 * **/
	public static final PenetrationModifierFunction DEFAULT_FUNCTION = (pen, mod, dist) -> pen * Math.pow(mod, dist / 50);
	
	public static double getPenetrationValue(IPenetratingProjectile proj, ILocationProvider currentLoc)
	{
		return proj.getModifierFunction().orElse(DEFAULT_FUNCTION).mod(proj.getPenetration(), proj.getModifier(), ILocationProvider.distance(currentLoc, proj.getOrigin()));
	}
}
