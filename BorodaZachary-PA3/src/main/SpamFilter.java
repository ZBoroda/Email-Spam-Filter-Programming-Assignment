package main;

/**
* This class is a Spam Filter using a hash table of words and a priority queue of emails. 
* Known Bugs: "None”
*
* @author Zachary Boroda
* zacharyboroda@brandeis.edu
* May 9, 2020
* COSI 21A PA1
*/
public class SpamFilter {

	/**
	 * The hash table holding all of the words being spamified
	 */
	HashTable<int[]> words;
	/**
	 * The queue holding the email's being sorted for spam
	 */
	PriorityQueue emailsQ;
	/**
	 * The spam threshold tolerated
	 */
	int threshold;
	/**
	 * the total number of spam emails in the test data
	 */
	int totalSpam;
	/**
	 * the total number of non spam emails in the test data
	 */
	int totalSafe;
	
	/**
	 * Creates a new SpamFilter object with threshold threshold
	 * Runs at O(1)
	 * @param threshold this spam filter's threshold
	 */
	public SpamFilter(int threshold) {
		words = new HashTable<int[]>();
		emailsQ = new PriorityQueue();
		this.threshold = threshold;
		totalSpam = 0;
		totalSafe = 0;
	}
	
	/**
	 * Changes this filter's threshold
	 * @param threshold the filter's new threshold
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * Calculates a word's spamicity score based off of training data
	 * Runs at O(1)
	 * @param word the word who's score is being calculated
	 * @return the word's score
	 */
	public double getSpamicity(String word) {
		if (words.contains(word)) {
			int[] spam_not = words.get(word);
			double probSpam = (double)spam_not[0]/totalSpam;
			double probNot = (double)spam_not[1]/totalSafe;
			return probSpam/(probSpam + probNot);
		}
		return 0;
	}
	
	/**
	 * Calculates an email's spam score based off of its words
	 * Runs at O(n) where n is the number of words in the email
	 * @param words the words in this email 
	 * @return this email's spam score
	 */
	public int calculateSpamScore(String[] words) {
		int spamScore = 0;
		for(String word: words) {
			double spamicity = getSpamicity(word);
			if (spamicity >= 0.9) {
				spamScore += 4;
			}else if (spamicity >= 0.75) {
				spamScore += 2;
			} else if (spamicity >= 0.5) {
				spamScore += 1;
			}
		}
		return spamScore;
	}
	
	/**
	 * Adds a batch of emails to this spam filter to be filtered
	 * Runs at O(m*n) where m is the number of emails and n is the number of words per email 
	 * @param emails the emails being added
	 */
	public void receive(Email[] emails) {
		for(Email email: emails) {
			email.setSpamScore(calculateSpamScore(email.getWords()));
			emailsQ.insert(email);
		}
	}
	
	/**
	 * Filters out all of the emails with spam above the threshold
	 * Runs at O(mlogn) where m is the number of emails removed 
	 * and n is the number of emails in the filter
	 * @return the email's id's that were removed
	 */
	public String filter() {
		String result = "";
		while(emailsQ.getMaximumSpamScore() > threshold) {
			result+=emailsQ.extractMaximum().getID() +"\n";
		}
		return result;
	}
	
	/**
	 * Trains the spam filter for certain words then modifies the emails in the filter 
	 * so that their spam counts are up to data 
	 * Runs at O(m*n) where m is the number of emails and n is the number of words per email 
	 * @param emails the emails being used to train
	 * @param isSpam whether or not an email is spam 
	 */
	public void train(Email[] emails, boolean[] isSpam) {
		for (int cnt = 0; cnt < emails.length; cnt++) {
			if (isSpam[cnt]) {
				totalSpam ++;
				for(String word: emails[cnt].getWords()) {
					int[] data = null;
					if(!words.contains(word)) {
						int[] temp = {0,0,0};
						data = temp;
					}else {
						data = words.get(word);
					}
					if (data[2] == 0) {
						data[0] ++;
						data[2] = 1;
						words.put(word, data);
					}
				}
			} else {
				totalSafe ++;
				for(String word: emails[cnt].getWords()) {
					int[] data = null;
					if(!words.contains(word)) {
						int[] temp = {0,0,0};
						data = temp;
					}else {
						data = words.get(word);
					}
					if (data[2] == 0) {
						data[1] ++;
						data[2] = 1;
						words.put(word, data);
					}
				}
			}
			for(String word: emails[cnt].getWords()) {
				int[] data = words.get(word);
				data[2] = 0;
				words.put(word, data);
			}
		}
		updateEmails();
	}
	
	/**
	 * Updates the emails stored in this filter to the most up to data spam info
	 * Runs at O(m*n) where m is the number of emails and n is the number of words per email 
	 */
	private void updateEmails() {
		String[] ids = emailsQ.getIDs();
		for(String id: ids) {
			emailsQ.updateSpamScore(id, calculateSpamScore(emailsQ.getWords(id)));
		}
	}
	
	/**
	 * Gets a ranking of the emails by their spam count
	 * Runs at O(n) where n is the number of emails
	 * @return a String, ranking of the emails by their spam count
	 */
	public String getEmailRanking() {
		String result = "";
		String[] ranked = emailsQ.rankEmails();
		for(int cnt = 0; cnt < ranked.length; cnt++) {
			result += ranked[cnt] +"\n";
		}
		return result;
	}
}
