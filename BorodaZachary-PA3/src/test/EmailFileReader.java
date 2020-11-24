package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.Email;

/**
 * This class provides 4 utility methods to be used when preparing training and
 * receive data in unit tests.
 * 
 * @author Chami
 */
public class EmailFileReader {

	/**
	 * This method reads a training data file into an array of Emails and an array
	 * of booleans.
	 * 
	 * Training data files are formatted as 3 line entries in the following manner:
	 * 
	 * <email id> <email contents> <0 or 1 to represent spam or not>
	 * 
	 * @param filename the file path to the training data file
	 * @param emails   an empty array of Emails to load from the training data
	 * @param isSpam   an empty array of booleans to load from the training data
	 * 
	 *                 PRE-CONDITION: emails and isSpam have enough space to store
	 *                 all the entries in the data file
	 * 
	 *                 Reminder: isSpam[i] = true/false means emails[i] is a
	 *                 spam/safe email
	 * 
	 * @return the number of entries that were actually read
	 * 
	 *         This will be used by the downsize() methods to create Email and
	 *         boolean arrays of the proper sizes to be used by SpamFilter.
	 */
	public static int readTrainingFile(String filename, Email[] emails, boolean[] isSpam) {
		// current counter of entries that have been read
		int curr = 0;

		// scanner for file reading
		Scanner s = null;
		// stores the id of the next Email to be created
		String id;
		// stores the contents of the next Email to be created
		String contents;
		// stores 0 or 1 to fill next isSpam entry
		long spam;

		try {
			s = new Scanner(new File(filename));

			// Until you have reached EOF
			while (s.hasNextLine()) {

				// Read the next 3 lines into file reading variables
				id = s.nextLine().trim();
				contents = s.nextLine().trim();
				spam = s.nextLong();
				// Clear the scanner buffer unless at EOF and \n to read
				if (s.hasNextLine()) {
					s.nextLine();
				}

				// create the new email and boolean spam array entries
				emails[curr] = new Email(id, contents);
				if (spam == 0) {
					isSpam[curr] = false;
				} else {
					isSpam[curr] = true;
				}
				// increase counter
				curr++;
			}

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} finally {
			if (s != null)
				s.close();
		}

		// return how many elements were actually read (curr ends 1 beyond last added
		// value)
		return curr;
	}

	/**
	 * This method reads a receive data file into an array of Emails.
	 * 
	 * Receive data files are formatted as 2 line entries in the following manner:
	 * 
	 * <email id> <email contents>
	 * 
	 * @param filename the file path to the training data file
	 * @param emails   an empty array of Emails to load from the training data
	 * 
	 *                 PRE-CONDITION: emails has enough space to store all the
	 *                 entries in the data file
	 * 
	 * @return the number of entries that were actually read
	 * 
	 *         This will be used by the downsize() method to create Email arrays of
	 *         the proper size to be used by SpamFilter.
	 */
	public static int readReceivedEmails(String filename, Email[] emails) {
		// current counter of entries that have been read
		int curr = 0;

		// scanner for file reading
		Scanner s = null;
		// stores the id of the next Email to be created
		String id;
		// stores the contents of the next Email to be created
		String contents;

		try {
			s = new Scanner(new File(filename));

			// Until you have reached EOF
			while (s.hasNextLine()) {

				// Read the next 2 lines into file reading variables
				id = s.nextLine().trim();
				contents = s.nextLine().trim();
				// create the new email array entries
				emails[curr] = new Email(id, contents);
				// increase counter
				curr++;
			}
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} finally {
			s.close();
		}

		// return how many elements were actually read (curr ends 1 beyond last added
		// value)
		return curr;
	}

	/**
	 * Resizes an Email array down to a provided size. This is used to resize the
	 * results of the file-reading methods above into a proper Email array to be
	 * used by the SpamFilter train(), receive() methods.
	 * 
	 * @param a    an array of Email objects
	 * @param size a size to resize down to
	 * 
	 *             PRE-CONDITION: size is <= a.length
	 * 
	 * @return the downsized array of length size
	 */
	public static Email[] downsize(Email[] a, int size) {
		Email[] out = new Email[size];
		for (int i = 0; i < size; i++) {
			out[i] = a[i];
		}
		return out;
	}

	/**
	 * Resizes a boolean array down to a provided size. This is used to resize the
	 * results of the file-reading methods above into a proper boolean array to be
	 * used by the SpamFilter train() method
	 * 
	 * @param a    an array of booleans
	 * @param size a size to resize down to
	 * 
	 *             PRE-CONDITION: size is <= a.length
	 * 
	 * @return the downsized array of length size
	 */
	public static boolean[] downsize(boolean[] a, int size) {
		boolean[] out = new boolean[size];
		for (int i = 0; i < size; i++) {
			out[i] = a[i];
		}
		return out;
	}
}
