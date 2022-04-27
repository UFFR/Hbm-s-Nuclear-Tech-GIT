package com.hbm.entity.mob;

import java.util.Collection;
import java.util.HashSet;

import com.hbm.lib.Library;
import com.hbm.util.I18nUtil;

public class NameUtil
{
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
		PRIVATE("Pvt");
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
	}
	private static final String[][] firstLast = 
		{
				{"David", "William", "Robert", "Thomas", "Michael", "Charles", "John", "James", "Richard", "Donald", "Paul", "Daniel",
					"Kenneth", "Mark", "Joseph", "Stephen", "Gregory", "Carl", "George", "Anthony", "Raymond", "Brian", "Peter", "Timothy",
					"Lawrence", "Kevin", "Bruce", "Jerry", "Edward", "Scott", "Jeffrey", "Dennis", "Ricky", "Barry", "Ronnie", "Arthur", "Harold",
					"Andrew", "Chistopher", "Henry", "Albert", "Martin", "Jack", "Phillip", "Eric", "Louis", "Harry", "Frederick", "Leonard", "Eugene",
					"Fred", "Francis", "Vincent", "Larry", "Steven", "Gary", "Ronald", "Wayne", "Patrick", "Roger", "Keith", "Alan", "Randall",
					"Douglas", "Danny", "Dale", "Matthew", "Earl", "Liam", "Billy", "Glenn", "Willie", "Allen", "Samuel", "Roy", "Russel",
					"Walter", "Ralph", "Joeseph", "Craig", "Terry", "Rodney", "Curtis", "Stanley", "Victor", "Michael", "Howard", "Ernest",
					"Darrell", "Topias", "Gordon", "Scott", "Clark"},
				{"Wilson", "Johnson", "Brown", "Martin", "Thompson", "Jackson", "Anderson", "Green", "Miller", "Wilson", "Garcia", "Davis",
						"Williams", "Smith", "Jones", "Thomas", "Moore", "Taylor", "White", "Martinez", "Carter", "Robinson", "Clark", "Turner",
						"Walker", "Young", "Hill", "Adams", "Phillips", "Campbell", "Lewis", "Lopez", "Scott", "Perez", "Wright", "Hall",
						"King", "Harris", "Lee", "Allen", "Mitchell", "Baker", "Torres", "Evans", "Sanchez", "Nelson", "Roberts", "Myers",
						"Stuart", "Cooper", "Gomez", "Hughes", "Bailey", "Collins", "Russel", "Edwards", "Bell", "Kelley", "Sanders", "Cook",
						"Powell", "Jenkins", "Brooks", "Fisher", "Howard", "Peterson", "Morgan", "Morris", "Ross", "Price", "Richardson", "Gray",
						"Murphy", "Sullivan", "Reed", "Ward", "Barnes", "Wood", "Foster", "Bennet", "James", "Parker", "Perry", "Rogers",
						"Watson", "Butler", "Cox", "Rodriguez", "Hernandez", "Ramirez", "Gonzalez", "Cunningham", "Freeman", "West", "Stevens", "Reyes",
						"Dixon", "Kennedy", "Bradley", "Reynolds", "Burns"}
		};
	private static final HashSet<Character> middleInitials = new HashSet<Character>();
	static
	{
		for (String s : firstLast[1])
			middleInitials.add(s.charAt(0));
	}
	
	public static String generateName(Collection<String> defined)
	{
		String name = "";
		do
		{
			name = generateName();
		} while (defined.contains(name));
		return name;
	}
	public static String generateName()
	{
		return String.format("%s %s. %s", Library.randomFromArray(firstLast[0]), Library.randomFromCollection(middleInitials), Library.randomFromArray(firstLast[1]));
	}
}
