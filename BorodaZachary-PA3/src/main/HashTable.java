package main;

/**
* This class is a hash table with a String key being hashed polynomially
* and a generic type value being stored with each key 
* Operates on a array. 
* Known Bugs: "None”
*
* @author Zachary Boroda
* zacharyboroda@brandeis.edu
* May 9, 2020
* COSI 21A PA1
 * @param <V> the data being stored in this entry 
*/
public class HashTable<V> {
	

	// KEEP THIS PUBLIC 
	/**
	 * The array that will hold the data for this hash table
	 */
	public Entry<V>[] entries;
	
	/**
	 * The entry that will represent a entry that has been deleted 
	 */
	public final Entry<V> voidEntry;
	
	// KEEP THIS PUBLIC AND NOT FINAL 
	// However, feel free to change the load factor 
	/**
	 * This hash table's load factor
	 */
	public static double LOAD_FACTOR = 0.5;
	
	/**
	 * The constant used in my polynomial hashing
	 */
	private static final int HORNERS_METHOD_POLYNOMIAL_CONSTANT = 31;
	
	/**
	 * The total number of elements in my table including void elements
	 */
	private int totalElements;
	
	/**
	 * The total number of not void elements in my hash table
	 */
	private int realTotal;
	
	/**
	 * The max size of this hash table pre rehashing
	 */
	private int maxSize;
	
	/**
	 * Creates a new hash table 
	 * Runs at O(1)
	 */
	@SuppressWarnings("unchecked")
	public HashTable() {
		voidEntry = new Entry<V>("", null);
		// Use this to create generic arrays of type "Entry<V>" 
		// feel free to change the size of this array from 11 
		entries = (Entry<V>[]) new Entry[11];
		maxSize = entries.length;
		totalElements = 0;
	}
	
	/**
	 * Returns the value held at the given key's entry
	 * Runs at O(1) average but can run as slow as O(n) where n is the number of elements
	 *  if there are many collisions and this element is far from the place the hash function said it was 
	 * @param key the key of the entry containing the value
	 * @return the value held at the entry with the given key or null if entry with key is not in the table
	 */
	public V get(String key) {
		if (!contains(key)) {
			return null;
		}
		int hash = getHash(key);
		return entries[hash].getValue();
	}
	
	/**
	 * Inserts a new entry with key key and value value or 
	 * edits an entry with the given key so that the values match
	 * Runs at O(1) average time but can Run(n) if the tree is getting full 
	 * @param key the key of the entry being added\modified
	 * @param value the entry's new value
	 */
	public void put(String key, V value) {
		Entry<V> entry = new Entry<V>(key, value);
		Entry<V> result = search(entry);
		while(result == null) {
			rehash();
			result = search(entry);
		}
		if (entry != result) {
			result.setValue(value);
		} else {
			totalElements++;
			realTotal++;
			if((double)totalElements/maxSize >= LOAD_FACTOR) {
				rehash();
			}
		}
	}
	
	/**
	 * Deletes the entry with the given key from the table
	 * or does nothing if the key can't be found
	 * Runs at O(1) average time, but can run at O(n) 
	 * where n is the number of elements in this table if the table is getting full 
	 * @param key the key for the entry being deleted
	 * @return the entry deleted's value or null if no entry exists
	 */
	public V delete(String key) {
		if (!contains(key)) {
			return null;
		}
		realTotal--;
		int hash = getHash(key);
		V value = entries[hash].getValue();
		entries[hash] = voidEntry;
		return value;
	}
	
	/**
	 * Gets the keys of the entries stored in this hash table
	 * Runs at O(n) where n is the number of spots in this table
	 * @return the keys of all of the values
	 */
	public String[] getKeys() {
		String[] keys = new String[realTotal];
		int cntKeys = 0;
		for(int cnt = 0; cnt < entries.length; cnt++) {
			if (entries[cnt] != null && entries[cnt] != voidEntry) {
				keys[cntKeys] = entries[cnt].getKey();
				cntKeys ++;
			}
		}
		return keys;
	}
	
	/**
	 * Returns the number of elements stored in this table
	 * Runs at O(1)
	 * @return this table's size
	 */
	public int size() {
		return realTotal;
	}
	
	/**
	 * Returns whether or not an entry with this key is in this table
	 * Runs at O(1) average time but can run at O(n) if the tree is getting full
	 * @param key the key being searched for
	 * @return wether an entry with this key is in this table
	 */
	public boolean contains(String key) {
		int hash = getHash(key);
		return !(hash == -1 || entries[hash] == null);
	}
	
	/**
	 * Searches for an entry in this table and adds it if it isn't in the table
	 * Runs at O(1) average, but can run at O(n) 
	 * where n is the number of elements in this table if the table is getting full 
	 * @param entry the entry being searched for
	 * @return the entry that was either found or was just added
	 */
	public Entry<V> search(Entry<V> entry){
		String key = entry.getKey();
		int hash = getHash(key);
		if (hash == -1) {
			return null;
		}
		if (entries[hash] == null) {
			entries[hash] = entry;
		}
		return entries[hash];
	}
	
	/**
	 * Takes in a key and returns the hash value for that key based off of the 
	 * hash function and quadratic probing
	 * Runs at O(n) worst case where n is the number of elements in the table
	 * @param key the key being searched for
	 * @return the position in the table where an element with that key will be stored 
	 */
	public int getHash(String key) {
		int hash = hashFunction(key);
		int i = 0;
		while(i < maxSize && entries[(int) ((hash + Math.pow(i, 2)) % maxSize)] != null 
			&& !(entries[(int) ((hash + Math.pow(i, 2)) % maxSize)].getKey().equals(key))) {
			i++;
		}
		if((entries[(int) ((hash + Math.pow(i, 2)) % maxSize)] == null) || (entries[(int) ((hash + Math.pow(i, 2)) % maxSize)].getKey().equals(key))){
			return (int) ((hash + Math.pow(i, 2)) % maxSize);
		}
		return -1;
	}
	
	/**
	 * Remaps every element in this hash table with one with more capacity
	 * Runs at O(n) where n is the number of elements in the hash table
	 */
	@SuppressWarnings("unchecked")
	public void rehash() {
		Entry<V>[] oldTable = entries;
		maxSize = (maxSize * 2) + 1;
		entries = (Entry<V>[]) new Entry[maxSize];
		totalElements = 0;
		for(int cnt = 0; cnt < oldTable.length; cnt++) {
			if (oldTable[cnt] != null && oldTable[cnt] != voidEntry) {
				totalElements ++;
				search(oldTable[cnt]);
			}
		}
	}
	
	/**
	 * A polynomial hashing function that takes a String key 
	 * and returns an index for that key in a hash table of size num 
	 * Runs at O(l) where l is the number of letters in the key
	 * @param key the value being hashed 
	 * @return the hash value for the key 
	 */
	public int hashFunction(String key) {
		long hash = 0;
		for(int cnt = 0; cnt < key.length(); cnt++) {
			hash += key.charAt(cnt) * (long)(Math.pow(HORNERS_METHOD_POLYNOMIAL_CONSTANT,key.length()-cnt -1) % Long.MAX_VALUE);
		}
		return Math.abs((int)hash % maxSize);
	}
}
