package api.hbm;

/**
 * Notify an object that the time system has been updated, either by
 * command or if the object was unloaded for a while. For entities and
 * tile entities.
 * @author UFFR
 *
 */
public interface IHasAge
{
	public static final int acceptableDelay = 12000;
	public void incrementAge();
	public void notifyTimeUpdated(Date newTime);
	public Date getInternalDate();
}
