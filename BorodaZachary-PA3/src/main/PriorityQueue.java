package main;

import java.util.NoSuchElementException;

/**
* This class is a priority queue of Emails 
* storing them in  a max heap operating on a array.
* For easier access the id's of the emails are also used in a hash table 
* to give linear time access to their indexes 
* Known Bugs: "None”
*
* @author Zachary Boroda
* zacharyboroda@brandeis.edu
* May 9, 2020
* COSI 21A PA1
*/
public class PriorityQueue {

	/**
	 * The max heap that is storing the data for this priority queue
	 */
	public Email[] heap; // KEEP THIS PUBLIC
	
	/**
	 * The default size that this heap will be constructed to 
	 */
	public static final int DEFAULT_SIZE = 11;
	
	/**
	 * The current size of this heap also the index where the next element will be added
	 *  to preserve the heap property 
	 */
	private int cursize;
	
	/**
	 * A hash table linking the Email's id's to their indexes
	 */
	private HashTable<Integer> indexes;
	
	/**
	 * Constructs a new priority queue 
	 * Runs at O(1)
	 */
	public PriorityQueue() {
		heap = new Email[11];
		cursize = 0;
		indexes = new HashTable<Integer>();
	}
	
	// RECOMMENDED method: you can delete it
	/**
	 * Swaps the elements at those two indexes and updates their locations in the hashtable
	 * Runs at O(1)
	 * @param i the first index
	 * @param j the second index
	 */
	private void swap(int i, int j) {
		Email temp = heap[i];
		heap[i] = heap[j];
		heap[j] = temp;
		indexes.put(heap[j].getID(), j);
		indexes.put(heap[i].getID(), i);
	}

	// RECOMMENDED method: you can delete it
	/**
	 * Heapifies the node at the index index down to its proper location in the heap
	 * Runs at O(logn) where n is the number of Emails in this queue
	 * @param index the index being heapified
	 */
	private void heapifyDown(int index) {
		int[] children = getChildren(index);
		int largest = index;
		if (children[0] > 0 && heap[children[0]].getSpamScore() > heap[largest].getSpamScore()) {
			largest = children[0];
		}
		if (children[1] > 0 && heap[children[1]].getSpamScore() > heap[largest].getSpamScore()) {
			largest = children[1];
		}
		if(largest != index) {
			swap(index, largest);
			heapifyDown(largest);
		}
	}
	
	
	
	// RECOMMENDED method: you can delete it
	/**
	 * Heapifies the node at the index index up to its proper location in the heap
	 * Runs at O(logn) where n is the number of Emails in this queue
	 * @param index the index being heapified
	 */
	private void heapifyUp(int index) {
		int parent = getParent(index);
		if (parent >= 0 && heap[parent].getSpamScore() < heap[index].getSpamScore()) {
			swap(index, parent);
			heapifyUp(parent);
		}
	}
	
	/**
	 * Returns the indexes of a node in the heap's children 
	 * Runs at O(1)
	 * @param index the index of the node
	 * @return the index of its children in the form of an array with a left then right
	 */
	private int[] getChildren(int index) {
		int[] children = new int[2];
		if (cursize < (index*2 +1)) {
			children[0] = -1;
		}else {
			children[0] = index*2 +1;
		}
		if (cursize < (index*2 +2)) {
			children[1] = -1;
		}else {
			children[1] = index*2 +2;
		}
		return children;
	}
	
	/**
	 * Returns the index of a node's parent
	 * Runs at O(1)
	 * @param index the index of the node
	 * @return the index of the node's parent or -1 if the node is the head
	 */
	private int getParent(int index) {
		if(index - 1 >= 0) {
			return (int)(index - 1)/2;
		} else {
			return -1;
		}
	}
	
	/**
	 * Inserts Email e into this queue 
	 * Runs at O(logn) where n is the number of elements in the queue
	 * However when resizing is needed runs at O(n)
	 * @param e the element being inserted
	 */
	public void insert(Email e) {
		if(!indexes.contains(e.getID())) {
			if(cursize == heap.length) {
				resize();
			}
			heap[cursize] = e;
			indexes.put(e.getID(), cursize);
			heapifyUp(cursize);
			cursize++;
		}
	}
	
	/**
	 * Resizes this queue so that more elements can fit
	 * Runs at O(n) where n is the number of elements in this queue
	 */
	private void resize() {
		Email[] oldheap = heap;
		heap = new Email[oldheap.length*2 +1];
		for(int cnt = 0; cnt < oldheap.length; cnt++) {
			heap[cnt] = oldheap[cnt];
		}
	}
	
	/**
	 * Updates an email's spam score so changes it's priority 
	 * Runs at O(logn) where n is the number of elements in the queue
	 * @param eID the element's id number
	 * @param score the element's new score
	 */
	public void updateSpamScore(String eID, int score) {
		if(!indexes.contains(eID)) {
			throw new NoSuchElementException("Email with this id doesn't exist");
		}
		int index = indexes.get(eID);
		if(heap[index].getSpamScore() < score) {
			heap[index].setSpamScore(score);
			heapifyUp(index);
		}else {
			heap[index].setSpamScore(score);
			heapifyDown(index);
		}
		
	}
	
	/**
	 * Returns the maximum score in the queue 
	 * Runs at O(n)
	 * @return the maximum spam score in this queue
	 */
	public int getMaximumSpamScore() {
		if(cursize == 0) {
			throw new NoSuchElementException("No element exists with maximum score");
		}
		return heap[0].getSpamScore();
	}
	
	/**
	 * Removes the maximum from this queue and preserves the heap property 
	 * Runs at O(logn) where n is the number of elements in the queue
	 * @return the old maximum spam Email of this queue
	 */
	public Email extractMaximum() {
		if(cursize == 0) {
			throw new NoSuchElementException("No element exists with maximum score");
		}
		Email maximum = heap[0];
		heap[0] = heap[cursize -1];
		cursize --;
		indexes.delete(maximum.getID());
		indexes.put(heap[0].getID(), 0);
		heapifyDown(0);
		return maximum;
	}
	
	/**
	 * Returns all of these element's id's 
	 * Runs at O(n) where n is the number of elements in this queue
	 * @return all of these email's id's  
	 */
	public String[] getIDs() {
		return indexes.getKeys();
	}
	
	/**
	 * Sorts a copy of this queue and ranks the email's based on their spamness
	 * Runs at O(nlogn) where n is the number of elements in this queue
	 * @return the string representation of the emails of this queue ranked
	 */
	public String[] rankEmails() {
		Email[] copy = copy();
		sort(copy);
		return stringArray(copy);
	}
	
	/**
	 * Sorts the given heap array
	 * Runs at O(nlogn) where n is the number of elements in this heap 
	 * @param copy a copy of the heap that is being sorted
	 */
	private void sort(Email[] copy) { 
        for (int i = cursize - 1; i > 0; i--) { 
            // Move current root to end 
            Email temp = copy[0]; 
            copy[0] = copy[i]; 
            copy[i] = temp;
            heapifySort(copy, i, 0); 
        } 
    } 
  
	/**
	 * A heapify down method that doesn't modify our original heap or its index hash table
	 * @param copy the heap copy we are working on 
	 * @param n the length of our heap
	 * @param i the index being heapified
	 */
    private void heapifySort(Email[] copy, int n, int i) { 
        int largest = i; 
        int l = 2*i + 1;
        int r = 2*i + 2;
        if (l < n && copy[l].getSpamScore() > copy[largest].getSpamScore()) {
        	 largest = l; 
        }
        if (r < n && copy[r].getSpamScore() > copy[largest].getSpamScore()) {
        	 largest = r; 
        } 
        if (largest != i) { 
            Email swap = copy[i]; 
            copy[i] = copy[largest]; 
            copy[largest] = swap;
            heapifySort(copy, n, largest); 
        } 
    }
	
    /**
     * Creates a copy of the given heap 
     * Runs at O(n) where n is the number of elements in this heap
     * @return a copy of the given heap 
     */
	private Email[] copy() {
		Email[] copy = new Email[cursize];
		for(int cnt = 0; cnt < cursize; cnt++) {
			copy[cnt] = heap[cnt];
		}
		
		return copy;
	}
	
	/**
	 * Returns the words for an Email with identifying index i 
	 * Runs at O(1) 
	 * @param id the id for the Email
	 * @return the words for an Email or null if there is no such email
	 */
	public String[] getWords(String id) {
		if (!indexes.contains(id)) {
			return null;
		}
		int index = indexes.get(id);
		return heap[index].getWords();
	}
	
	/**
	 * Returns this queue's size
	 * Runs at O(1)
	 * @return this queue's size
	 */
	public int size() {
		return cursize;
	}
	
	/**
	 * Turns an array of emails into a String array
	 * Runs at O(n)
	 * @param array an array of emails
	 * @return a string array representing those emails
	 */
	private String[] stringArray(Email[] array) {
		String[] result = new String[array.length];
		for(int cnt = 0; cnt < array.length; cnt++) {
			result[array.length - cnt - 1] = array[cnt].toString();
		}
		return result;
	}
	
	/**
	 * Gets the email with the given id 
	 * Runs at O(1)
	 * @param id the email's id 
	 * @return the email or null if no such id is in this queue
	 */
	public Email get(String id) {
		int index = indexes.get(id);
		return heap[index];
	}
	
	/**
	 * Returns a string representation of this queue 
	 * Runs at O(n)
	 */
	public String toString() {
		String result = "|";
		for(int cnt = 0; cnt < cursize; cnt++) {
			result += heap[cnt].toString() +"|";
		}
		return result;
	}
}
