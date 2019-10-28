package deco2800.ragnarok.stats;

import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;

/**
 * Represents a specific user stat. Implements the observer design pattern. Allows observers of this class
 * , e.g. an Achievement to be notified when it's values is changed. 
 */
public class Stat extends Observable{
	private String name;
	private int value;

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public Stat(String name, int f){
		this.name = name;
		this.value = f;
		LOGGER.info("Instantiated stat {} with value {}", name, f);
	}

	public String getName() {
		return this.name;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int val) {
		this.value = val;
		setChanged();
		notifyObservers();
		clearChanged();
	}
}