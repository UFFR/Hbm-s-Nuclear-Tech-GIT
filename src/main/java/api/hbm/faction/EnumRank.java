package api.hbm.faction;

import java.util.Arrays;

import com.hbm.util.I18nUtil;

public enum EnumRank
{
	/**General of the Army / Supreme Commander of the Armed Forces**/
	GENERAL_ARMY("GA"),
	/**General**/
	GENERAL("Gen"),
	/**Lieutenant General**/
	GENERAL_LT("LtG"),
	/**Major General**/
	GENERAL_MAJ("MG"),
	/**Brigadier General**/
	GENERAL_BRIG("BG"),
	/**Colonel**/
	COLONEL("Col"),
	/**Lieutenant Colonel**/
	COLONEL_LT("LtC"),
	/**Major**/
	MAJOR("Maj"),
	/**Captain**/
	CAPTAIN("Cpt"),
	/**First Lieutenant**/
	LIEUTENANT_FIRST("1Lt"),
	/**Second Lieutenant**/
	LIEUTENANT_SECOND("2Lt"),
	/**Chief Warrant Officer 5**/
	CWO5("CW5"),
	/**Chief Warrant Officer 4**/
	CWO4("CW4"),
	/**Chief Warrant Officer 3**/
	CWO3("CW3"),
	/**Chief Warrant Officer 2**/
	CWO2("CW2"),
	/**Warrant Officer**/
	WO("WO1"),
	/**Sergeant Major of the Army**/
	SERGEANT_MAJOR_ARMY("SMA"),
	/**Command Sergeant Major**/
	SERGEANT_CMD("CSM"),
	/**Sergeant Major**/
	SERGEANT_MAJOR("SgM"),
	/**First Sergeant**/
	SERGEANT_FIRST("1Sg"),
	/**Master Sergeant**/
	SERGEANT_MASTER("MSg"),
	/**Sergeant First Class**/
	SERGEANT_FCLASS("SFC"),
	/**Staff Sergeant**/
	SERGEANT_STAFF("SSG"),
	/**Sergeant**/
	SERGEANT("Sgt"),
	/**Corporal**/
	CORPORAL("Cpl"),
	/**Specialist**/
	SPECIALIST("Spc"),
	/**Private First Class**/
	PRIVATE_FCLASS("PFC"),
	/**Private 2**/
	PRIVATE_2("2Pv"),
	/**Private**/
	PRIVATE("Pvt"),
	/**No rank**/
	NONE("NONE");
	public final String abbreviation;
	private EnumRank(String abbr)
	{
		abbreviation = abbr + '.';
	}
	
	public String getUnlocalized()
	{
		return "rank." + toString().toLowerCase();
	}
	
	public String getLocalized()
	{
		return I18nUtil.resolveKey(getUnlocalized());
	}
	
	public int numericForm()
	{
		return values().length - ordinal() - 1;
	}
	
	public String toBinaryForm()
	{
		final StringBuilder builder = new StringBuilder();
		final String binary = Integer.toBinaryString(numericForm());
		if (builder.length() < 5)
		{
			final char[] padding = new char[5 - builder.length()];
			Arrays.fill(padding, '0');
			builder.append(padding);
		}
		builder.append(binary);
		return builder.toString();
	}
	
	@Override
	public String toString()
	{
		return abbreviation;
	}
}
