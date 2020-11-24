package main;

/**
* This class is an email object it stores the email's spam info 
* Known Bugs: "None”
*
* @author Zachary Boroda
* zacharyboroda@brandeis.edu
* May 9, 2020
* COSI 21A PA1
*/
public class Email {
	
	/**
	 * The sequence that will be splitting our words
	 */
	private final static String WORD_REGEX = " ";
	
	/**
	 * The unique identifying code for this Email
	 */
	private final String id;
	
	/**
	 * The contents of this email
	 */
	private String contents;
	
	/**
	 * The spam score that this email has
	 */
	private int spamScore;
	
	/**
	 * The words in this email's contents
	 */
	private String[] words;

	/**
	 * Creates a new email with id id and contents contents
	 * Runs at O(n) where n is the number of characters in this email's contents due to the split method
	 * @param id the unique identifying code for this email 
	 * @param contents the string containing this email's actual message
	 */
	public Email(String id, String contents) {
		this.id = id;
		this.contents = contents;
		this.spamScore = -1;
		this.words = contents.trim().split(WORD_REGEX);
	}
	
	/**
	 * Returns this email's unique id code
	 * Runs at O(1)
	 * @return the id code
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Returns the words held in this email 
	 * Run's at O(1)
	 * @return the words in this email
	 */
	public String[] getWords() {
		return words;
	}
	
	/**
	 * Returns this email's spam score
	 * Runs at O(1)
	 * @return the spam score
	 */
	public int getSpamScore() {
		return spamScore;
	}
	
	/**
	 * Sets this email's spam score
	 * Runs O(1)
	 * @param score this emails new score
	 */
	public void setSpamScore(int score) {
		spamScore = score;
	}
	
	/**
	 * Returns a string representation of this email 
	 * Runs at O(1)
	 */
	public String toString() {
		return id +" -- "+spamScore;
	}
}
