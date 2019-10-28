package deco2800.ragnarok.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;


/**
 * A class which instanciates data oriented objects defining activation constraints for stats.
 * Used by Achievement objects in order to determine if the appropriate stat constraints have been
 * fulfilled.
 */
public class Constraint {
	private final String rule;
	private final int value;
	private final Stat stat;

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Creates a new constraint. The following parameters define the rule:
	 * @param rule : E.g. "GREATER_THAN". Use the static Strings provided in the Achieve.java class.
	 * @param f : The treshold value which is used together with the rule.
	 * @param string : The stat wich this constraint has been set upon.
	 */
	public Constraint(String rule, int f, Stat stat) {
		this.rule = rule;
		this.value = f;
		this.stat = stat;
		LOGGER.info("A new constraint was put on stat: {} with rule: {} and value {}", stat.getName(), rule, f);
	}

	public Stat getStat() {
		return this.stat;
	}

	public String getRule() {
		return this.rule;
	}

	public int getValue() {
		return this.value;
	}
}