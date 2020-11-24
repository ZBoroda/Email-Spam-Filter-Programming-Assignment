package main;

/**
* This class is an entry for a hash table storing data for a key 
* Known Bugs: "None”
*
* @author Zachary Boroda
* zacharyboroda@brandeis.edu
* May 9, 2020
* COSI 21A PA1
 * @param <V> the data being stored in this entry 
*/
public class Entry<V> {
	
	/**
	 * Holds this entry node's identifying key
	 */
	String key;
	
	/**
	 * Holds this entry's data
	 */
	V value;

	/**
	 * Creates a new entry object with key key and value value
	 * Runs at O(1)
	 * @param key this entry's key
	 * @param value the value stored in this entry
	 */
	public Entry(String key, V value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Gets this node's key
	 * Runs at O(1)
	 * @return this entry's key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Gets the value stored in this entry
	 * Runs at O(1)
	 * @return the value stored in this entry
	 */
	public V getValue() {
		return value;
	}
	
	/**
	 * Sets this entry's value to Value
	 * Runs at O(1)
	 * @param value the new value of this entry
	 */
	public void setValue(V value) {
		this.value = value;
	}
	
	/**
	 * Provides a String representation of this object
	 * Runs at O(1)
	 */
	public String toString() {
		if (value != null) {
			return "(" +key +"," +value.toString()+")";
		}
		return "(Null Entry)";
	}
}
