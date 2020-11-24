package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Email;
import main.SpamFilter;

import static test.EmailFileReader.*;

/**
 * This file contains some simple unit tests for the SpamFilter class. The
 * purpose of each test and how you should use it to evaluate your work are
 * outlined below.
 * 
 * NUMBER TESTS: 4
 * 
 * @author Chami
 */
class ExampleFilterTest {

	// Tolerance of floating point comparisons
	static final double TOLERANCE = 0.00001;

	// SpamFilter that will be used in testing
	SpamFilter f;

	// Resets the SpamFilter before each test which load/receive different data
	@BeforeEach
	void setup() {
		f = new SpamFilter(5);
	}

	/**
	 * Tests that getSpamicity() is implemented properly using a small training data
	 * file.
	 * 
	 * Here is an explanation of the calculations:
	 * 
	 * getSpamicity("free"): 
	 * 	"free" occurs in 2/2 spam emails 
	 * 	"free" occurs in 0/4 safe emails 
	 * 	Therefore, spamicity of "free" is 1.0/(1.0 + 0.0) = 1.0
	 * 
	 * getSpamicity("I"): 
	 * 	"I" occurs in 0/2 spam emails 
	 * 	"I" occurs in 3/4 safe emails 
	 * 	Therefore, spamicity of "I" is 0.0/(0.0 + 0.75) = 0.0
	 * 
	 * getSpamicity("cookies"): 
	 * 	"cookies" occurs in 1/2 spam emails 
	 * 	"cookies" occurs in 4/4 safe emails 
	 * 	Therefore, spamicity of "cookies" is 0.5/(0.5 + 1.0) = 0.33333...
	 * 
	 * getSpamicity("you"): 
	 * 	"you" occurs in 1/2 spam emails 
	 * 	"you" occurs in 2/4 safe emails 
	 * 	Therefore, spamicity of "you" is 0.5/(0.5 + 0.5) = 0.5
	 */
	@Test
	void testGetSpamicity() {

		// Prepare the training data by reading from the input data file
		Email[] t = new Email[10];
		boolean[] s = new boolean[10];

		int l = readTrainingFile("student_training_small.txt", t, s);
		t = downsize(t, l);
		s = downsize(s, l);

		// train the filter with the prepared training data
		f.train(t, s);

		// Compare the filter's spamicity calculations with the expected results
		// TOLERANCE is to account for floating point value comparison issues
		assertEquals(1.0, f.getSpamicity("free"), TOLERANCE);
		assertEquals(0.0, f.getSpamicity("I"), TOLERANCE);
		assertEquals(0.33333, f.getSpamicity("cookies"), TOLERANCE);
		assertEquals(0.5, f.getSpamicity("you"), TOLERANCE);
	}

	/**
	 * Tests that calculateSpamScore() is implemented properly using a larger
	 * training data file.
	 * 
	 * The method is tested on 5 String arrays. Here is an explanation of the
	 * calculations:
	 * 
	 * Array built from: "Are you free tomorrow? I am free in the afternoon" 
	 * 	The word "free" appears in all the spam emails and in no safe emails. 
	 * 	It has spamicity = 1.0 
	 * 	It appears 2x so spam score of this array = 2*4 = 8
	 * 
	 * Array built from: "Reply for a free guitar" 
	 * 	The word "free" appears in all the spam emails and in no safe emails 
	 * 	It has spamicity = 1.0 
	 * 	The word "for" appears in 3/6 of the spam emails and in no safe emails 
	 * 	It has spamicity = 0.5 
	 * 	The word "a" appears in 3/6 of the spam emails and in no safe emails 
	 * 	It has spamicity = 0.5 
	 * 	Each of these words appear only once so spam score of this array = 4 + 1 + 1 = 6
	 * 
	 * Array built from: "Respond by tomorrow please" 
	 * 	The word "Respond" appears in 5/6 spam emails and in 1/4 safe emails 
	 * 	It has spamicity ~ (0.83/(0.83 + 0.25)) > 0.75 
	 * 	It appears only once so spam score of this array = 2
	 * 
	 * Array built from: "Would you like to eat lunch on Sunday?" 
	 * 	The word "to" appears in 3/6 of the spam emails and in no safe emails 
	 * 	It has spamicity = 0.5 
	 * 	It appears only once so spam score of this array = 1
	 * 
	 * Array built from: "I lost my cookies" 
	 * 	None of these words appear in any of the spam emails 
	 * 	Therefore, the spam score of this array = 0
	 */
	@Test
	void testCalculateSpamScore() {

		// Prepare the training data by reading from the input data file
		Email[] t = new Email[10];
		boolean[] s = new boolean[10];

		int l = readTrainingFile("student_training_big.txt", t, s);
		t = downsize(t, l);
		s = downsize(s, l);

		// train the filter with the prepared training data
		f.train(t, s);

		// Build the arrays from the 5 strings discussed in the above comment
		String[] score8 = "Are you free tomorrow? I am free in the afternoon".split(" ");
		String[] score6 = "Reply for a free guitar".split(" ");
		String[] score2 = "Respond by tomorrow please".split(" ");
		String[] score1 = "Would you like to eat lunch on Sunday?".split(" ");
		String[] score0 = "I lost my cookies".split(" ");

		// Compare the calculate spam scores for the built arrays with the expected
		// results
		assertEquals(8, f.calculateSpamScore(score8));
		assertEquals(6, f.calculateSpamScore(score6));
		assertEquals(2, f.calculateSpamScore(score2));
		assertEquals(1, f.calculateSpamScore(score1));
		assertEquals(0, f.calculateSpamScore(score0));
	}

	/**
	 * Tests that getEmailRanking() is implemented properly. It should be
	 * returning a String formatted in the proper manner described on the PDF.
	 * 
	 * The test firsts trains the filter given the big training data file and then
	 * receives 5 emails from a receive data file. The emails are identical to the
	 * ones used in @see #testCalculateSpamScore() and the spam score calculations
	 * are explained there.
	 * 
	 * Email identified by "ba9eacd63cda49488370c208cd037d02" has spam score 8. 
	 * Email identified by "3a97ec65a18044e5b3e1a5a93b7a5f86" has spam score 6. 
	 * Email identified by "8899bd9640bd488ba7b24b3eb495e95e" has spam score 2. 
	 * Email identified by "00707afb5ab64b249af42868504e1bdf" has spam score 1. 
	 * Email identified by "3fbc3ba9c0c44020b615aefe3f663dc5" has spam score 0.
	 * 
	 * In the resulting ranking, these IDs should be separated from their spam score
	 * by 2 spaces and dashes " -- ". Then, each of these emails should be on
	 * separate lines.
	 */
	@Test
	void testRankEmails() {

		// Prepare the training data by reading from the input data file
		Email[] t = new Email[10];
		boolean[] s = new boolean[10];

		int l = readTrainingFile("student_training_big.txt", t, s);
		t = downsize(t, l);
		s = downsize(s, l);

		// Prepare the received email data from the input data file
		Email[] r = new Email[10];

		int lr = readReceivedEmails("student_receive.txt", r);
		r = downsize(r, lr);

		// train the filter with the prepared training data, and
		// then receive the emails from
		// the data file
		f.train(t, s);
		f.receive(r);

		// Get the ranking from the filter of the received emails
		String ranking = f.getEmailRanking().trim();

		assertEquals(
				"ba9eacd63cda49488370c208cd037d02 -- 8\n3a97ec65a18044e5b3e1a5a93b7a5f86 -- 6\n8899bd9640bd488ba7b24b3eb495e95e -- 2\n00707afb5ab64b249af42868504e1bdf -- 1\n3fbc3ba9c0c44020b615aefe3f663dc5 -- 0",
				ranking);

	}

	/**
	 * Tests that filter() is implemented properly. It should be returning a 
	 * String formatted in the proper manner described on the PDF. 
	 * 
	 * The test firsts trains the filter given the big training data file and then
	 * receives 5 emails from a receive data file. The emails are identical to the
	 * ones used in @see #testCalculateSpamScore(), @see #testRankEmails() and the 
	 * spam score calculations are explained there.
	 * 
	 * 2 of these Emails are above the threshold of 5 @see #testRankEmails(), 
	 * therefore those 2 emails are removed and their IDs are returned as a 
	 * String on separate lines.
	 */
	@Test
	void testFilter() {
		// Prepare the training data by reading from the input data file
		Email[] t = new Email[10];
		boolean[] s = new boolean[10];

		int l = readTrainingFile("student_training_big.txt", t, s);
		t = downsize(t, l);
		s = downsize(s, l);

		// Prepare the received email data from the input data file
		Email[] r = new Email[10];

		int lr = readReceivedEmails("student_receive.txt", r);
		r = downsize(r, lr);

		// Construct a SpamFilter, train the filter with the prepared training data, and
		// then receive the emails from
		// the data file
		f.train(t, s);
		f.receive(r);

		// Filters out emails greater than or equal to the threshold (5)
		String filtered = f.filter().trim();
		
		// Tests if the correct emails are filtered 
		assertEquals("ba9eacd63cda49488370c208cd037d02\n3a97ec65a18044e5b3e1a5a93b7a5f86", filtered);

	}
}
