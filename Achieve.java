import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

// For logging purposes
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;

/**
 * A class for managing stats and achievements. Also provides rules which is used in defining constraints.
 * Uses the singleton pattern. 
 */
public class Achieve {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());		
	// activation rules
	public static final String GREATER_THAN = ">";
	public static final String LESS_THAN = "<";
	public static final String EQUALS_TO = "==";

	private HashMap<String, Stat> stats; // dictionary of properties
	private HashMap<String, Achievement> achievements; // dictionary of achievements

	private static Achieve achieve = null;

	private Achieve() {
		this.stats = new HashMap<>();
		this.achievements = new HashMap<>();
	}

	public static Achieve getInstance() {
		if (achieve == null){
			achieve = new Achieve();
			LOGGER.info("Instatiated singleton object 'Achieve' ");
		}
		return achieve;
	}

	public static void destroy() {
		if (achieve != null) {
			LOGGER.info("Removed reference to singleton object 'Achieve' ");
			achieve = null;
		}
	}

	public HashMap<String, Achievement> getAchievements() {
		return new HashMap<>(this.achievements); // returns a copy I think?
	}

	public void defineStat(String name, int f){
		LOGGER.info("defining stat {}", name);
		this.stats.put(name, new Stat(name, f));
	}

	public void defineAchievement(String name, ArrayList<Constraint> constraints) {
		LOGGER.info("defining achievement {}", name);
		this.achievements.put(name, new Achievement(name, constraints));
	}

	public Stat getStat(String statName) {
		return this.stats.get(statName);
	}

	public int getStatValue(String name) {
		return this.stats.get(name).getValue();
	}

	public void setStatValue(String name, int value) {
		this.stats.get(name).setValue(value);
	}

	public Achievement getAchievement(String aName) {
		return this.achievements.get(aName);
	}

	public ArrayList<Achievement> getUnlockedAchievements() {
		return this.achievements.values().stream().filter(a -> a.isUnlocked())
				.collect(Collectors.toCollection(ArrayList::new));
	}
}





