import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;

import deco2800.ragnarok.stats.Achieve;
import deco2800.ragnarok.stats.Constraint;


/**
 * Represents unlockable achievements for a user. Achievments have related constraints 
 * that must be satisfied in order to unlock it. Based on the constraints given 
 * the object will identify the appropriate stats and assign itself as a 
 * subscriber (Observer).
 */
public class Achievement implements Observer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private String name;
	private ArrayList<Constraint> constraints;
	private HashMap<Constraint, Constraint> constraintsToBeSatisfied = new HashMap<>();

	/**
	 * Instanciates an achievement
	 * @param identifier : The unique name/description of this achievement
	 * @param constraints : A list of Constraint objects, specifiyng the constraints 
	 * that must be satisfied in order to unlock it.
	 */
	public Achievement(String identifier, ArrayList<Constraint> constraints) {
		this.name = identifier;
		this.constraints = constraints;
		this.constraints.stream().filter(c -> !this.constraintSatisfied(c)).forEach(c -> this.constraintsToBeSatisfied.put(c, c));
		this.subscribe();
		LOGGER.info("Created '{}' achievement", name);
	}

	/**
	 * The object puts itself as an observer of the related stats in order to track updates.
	 */
	private void subscribe() {
		constraintsToBeSatisfied.values().stream().forEach(rule -> rule.getStat().addObserver(this));
	}

	private Boolean constraintSatisfied(Constraint c) {
		switch (c.getRule()) {
			case Achieve.GREATER_THAN:
				return c.getStat().getValue() > c.getValue();
			case Achieve.EQUALS_TO:
				return c.getStat().getValue() == c.getValue();
			case Achieve.LESS_THAN:
				return c.getStat().getValue() < c.getValue();
			default:
				return false;
		}
	}

	/**
	 * @param constraints : You wish to consider
	 * @param statName    : Stat you wish to find constraints on
	 * @return A list of constraints that has been set on the given stat
	 */
	private ArrayList<Constraint> getConstraintsOnStat(Collection<Constraint> constraints, String statName) {
		return constraints.stream().filter(rule -> rule.getStat().getName().equals(statName))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Checks to see if constraints have been satisfied from the list of remaing
	 * constraints and updates (removes) them accordingly
	 */
	private void updateConstraints(ArrayList<Constraint> constraints) {
		constraints.stream().filter(this::constraintSatisfied).forEach(this.constraintsToBeSatisfied::remove);
	}

	/**
	 * @return All the constraints that are related to the given stat
	 */
	public ArrayList<Constraint> getAllConstraintsOnstat(Stat stat){
		return getConstraintsOnStat(this.constraints, stat.getName());
	}

	/**
	 * @return The remaining constraints that are to be satisfied in order to unlock the achievement
	 */
	public ArrayList<Constraint> getRemainingConstraintsOnStat(Stat stat) {
		return getConstraintsOnStat(this.constraintsToBeSatisfied.values(), stat.getName());
	}

	/**
	 * @return true if all the constrains are satisfied, i.e. achievement is unlocked.
	 */
	public Boolean isUnlocked() {
		return this.constraintsToBeSatisfied.isEmpty();
	}

	/**
	 * A method for updating this achievement object or related data structures 
	 * when the achievemnt is unlocked.
	 */
	private void updateAchievement() {
		if (this.isUnlocked()) {
			LOGGER.info("'{}' achievement unlocked!", this.name);
			// This is where you make stuff happen when this achievment is unlocked!
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		Stat stat = (Stat) o;
		LOGGER.info("stat '{}' notified achievement '{}'' about value: {}", stat.getName(), this.name, stat.getValue());
		ArrayList<Constraint> remConstraints = this.getRemainingConstraintsOnStat(stat);
		if (remConstraints.isEmpty()) { // All constraints on this stat has been satisfied -> Unsubscribe.
			o.deleteObserver(this);
			return;
		}
		this.updateConstraints(remConstraints);
		this.updateAchievement();
	}

	public String getName() {
		return this.name;
	}
}