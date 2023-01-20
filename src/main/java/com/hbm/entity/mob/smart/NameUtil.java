package com.hbm.entity.mob.smart;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Supplier;

import com.hbm.lib.Library;
import com.hbm.main.MainRegistry;

/**
 * Utility class for generating names for faction NPCs.
 * @author UFFR
 *
 */
public class NameUtil
{
	public static final short MAX_COMBINE_SQUAD_NAMES = 180, MAX_COMBINE_AIR_NAMES = 130;
	public static final int MAX_HUMAN_NAMES = 172900;
	private static final String[][] FIRST_LAST = 
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
	private static final String[] COMBINE_SQUAD_NAMES = {"Flash", "Ranger", "Hunter", "Blade", "Scar", "Hammer", "Sweeper",
														"Swift", "Fist", "Sword", "Savage", "Tracker", "Slash", "Razor",
														"Stab", "Spear", "Striker", "Dagger"},
								  COMBINE_SKYSHIELD_NAMES = {"Ghost", "Reaper", "Nomad", "Hurricane", "Phantom", "Judge",
										  					"Shadow", "Slam", "Stinger", "Storm", "Vamp", "Winder", "Star"};
//								  COMBINE_SQUADS = {"Apex", "Boomer", "Helix", "Ice", "Ion", "Nova", "Slam", "Spear", "Storm"};
	private static final HashSet<Character> MIDDLE_INITIALS = new HashSet<Character>();
	private static final Random RANDOM = new Random();
	static
	{
		for (String s : FIRST_LAST[1])
			MIDDLE_INITIALS.add(s.charAt(0));
	}
	
	/**
	 * Makes a generated name, featuring a first, middle initial, and last name.
	 * @return A randomly generated human name.
	 */
	public static String generateHumanName()
	{
		return String.format("%s %s. %s", Library.randomFromArray(FIRST_LAST[0]), Library.randomFromCollection(MIDDLE_INITIALS), Library.randomFromArray(FIRST_LAST[1]));
	}
	
	/**
	 * Generates a numeric Combine unit designation.
	 * @return A string version of a random number between 0 and 2^15-2.
	 */
	public static String generateCombineName()
	{
		return String.valueOf(RANDOM.nextInt(Short.MAX_VALUE));
	}
	
	/**
	 * A squad specific name for a Combine unit (not meant for special forces squads).
	 * @return A random string name and a number 0 - 9 at the end.
	 */
	public static String generateCombineSquadName()
	{
		return Library.randomFromArray(COMBINE_SQUAD_NAMES) + '-' + RANDOM.nextInt(10);
	}
	
	/**
	 * A name specifically given to Combine air units.
	 * @return A random string name and a number 0 - 9 at the end.
	 */
	public static String generateCombineAirUnitName()
	{
		return Library.randomFromArray(COMBINE_SKYSHIELD_NAMES) + '-' + RANDOM.nextInt(10);
	}
	
	/**
	 * Generates a random Moon Rabbit numeric designation in hexadecimal.
	 * @return A hexadecimal number between -2^31 and 2^31-1.
	 */
	public static String generateLunarName()
	{
		final StringBuilder builder = new StringBuilder(8);
		final String name = Integer.toHexString(RANDOM.nextInt() & 0xffffffff).toUpperCase();
		if (name.length() < 8)
		{
			final char[] padding = new char[8 - name.length()];
			Arrays.fill(padding, '0');
			builder.append(padding);
		}
		builder.append(name);
		return builder.toString();
	}
	
	/**
	 * Attempts to generate a unique random name, given a generator method and a list of already defined names. 
	 * @param generator The method that generates the desired name type, generally a method reference.
	 * @param defined The collection of predefined names that the internal loop will check against.<br>Is not modified by this method. You must add the new name yourself if you want that.
	 * @param failSafe The maximum number of possible names, to prevent infinite loops.
	 * @return A new name that is not in {@code defined}.
	 */
	public static String generateNameFromDefined(Supplier<String> generator, Collection<String> defined, int failSafe)
	{
		String name;
		int iterations = 0;
		do
		{
			name = generator.get();
		} while (defined.contains(name) && iterations++ < failSafe);
		if (defined.contains(name))
			MainRegistry.logger.warn("Failed to generate unique NPC name because loop ran for " + iterations + " iterations, which is or exceeds the set fail safe parameter.");
//		System.out.println(name);
		return name;
	}
	
	public static String generateCombineCommandoSquadName(Collection<String> defined)
	{
		String name;
		int i = 1;
		do
		{
			name = "Combine Special Stabilization Force " + i++;
		} while (defined.contains(name));
		return name;
	}
}
