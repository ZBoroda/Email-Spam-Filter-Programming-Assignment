package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import main.Email;
import main.PriorityQueue;

class StudentQueueTest {
	
	PriorityQueue queue;

	@Test
	final void testInsert() {
		//Test inserting and a ressize
		queue = new PriorityQueue();
		Email a = new Email("a", "a a a");
		Email b = new Email("b", "b b b");
		Email c = new Email("c", "c c c");
		Email d = new Email("d", "d d d");
		Email e = new Email("e", "e e e");
		Email f = new Email("f", "f f f");
		Email g = new Email("g", "g g g");
		Email h = new Email("h", "h h h");
		Email i = new Email("i", "i i i");
		Email j = new Email("j", "j j j");
		Email k = new Email("k", "k k k");
		Email l = new Email("l", "l l l");
		Email m = new Email("m", "m m m");
		a.setSpamScore(10);
		b.setSpamScore(11);
		c.setSpamScore(12);
		d.setSpamScore(13);
		e.setSpamScore(14);
		f.setSpamScore(15);
		g.setSpamScore(16);
		h.setSpamScore(17);
		i.setSpamScore(18);
		j.setSpamScore(19);
		k.setSpamScore(20);
		l.setSpamScore(21);
		m.setSpamScore(22);
		
		queue.insert(a);
		queue.insert(b);
		queue.insert(c);
		queue.insert(d);
		queue.insert(e);
		queue.insert(f);
		queue.insert(g);
		queue.insert(h);
		queue.insert(i);
		assertEquals(18, queue.getMaximumSpamScore());
		
		//Test to see what happens if i try to add two of the same node 
		queue.insert(a);

		//Test now resizing
		queue.insert(j);
		queue.insert(k);
		queue.insert(l);
		assertEquals("|l -- 21|j -- 19|k -- 20|g -- 16|i -- 18|f -- 15|e -- 14|a -- 10|d -- 13|c -- 12|h -- 17|b -- 11|",queue.toString());
	}

	@Test
	final void testUpdateSpamScore() {
		queue = new PriorityQueue();
		Email a = new Email("a", "a a a");
		Email b = new Email("b", "b b b");
		Email c = new Email("c", "c c c");
		Email d = new Email("d", "d d d");
		Email e = new Email("e", "e e e");
		Email f = new Email("f", "f f f");
		Email g = new Email("g", "g g g");
		Email h = new Email("h", "h h h");
		Email i = new Email("i", "i i i");
		Email j = new Email("j", "j j j");
		Email k = new Email("k", "k k k");
		Email l = new Email("l", "l l l");
		Email m = new Email("m", "m m m");
		a.setSpamScore(10);
		b.setSpamScore(11);
		c.setSpamScore(12);
		d.setSpamScore(13);
		e.setSpamScore(14);
		f.setSpamScore(15);
		g.setSpamScore(16);
		h.setSpamScore(17);
		i.setSpamScore(18);
		j.setSpamScore(19);
		k.setSpamScore(20);
		l.setSpamScore(21);
		m.setSpamScore(22);
		
		queue.insert(a);
		queue.insert(b);
		queue.insert(c);
		queue.insert(d);
		queue.insert(e);
		queue.insert(f);
		queue.insert(g);
		queue.insert(h);
		queue.insert(i);
		queue.insert(j);
		queue.insert(k);
		queue.insert(l);
		
		queue.updateSpamScore("a", 28);
		assertEquals("|a -- 28|l -- 21|k -- 20|j -- 19|i -- 18|f -- 15|e -- 14|g -- 16|d -- 13|c -- 12|h -- 17|b -- 11|",queue.toString());
		queue.updateSpamScore("a", 17);
		assertEquals("|l -- 21|j -- 19|k -- 20|a -- 17|i -- 18|f -- 15|e -- 14|g -- 16|d -- 13|c -- 12|h -- 17|b -- 11|",queue.toString());
	}

	@Test
	final void testExtractMaximum() {
		queue = new PriorityQueue();
		Email a = new Email("a", "a a a");
		Email b = new Email("b", "b b b");
		Email c = new Email("c", "c c c");
		Email d = new Email("d", "d d d");
		Email e = new Email("e", "e e e");
		Email f = new Email("f", "f f f");
		Email g = new Email("g", "g g g");
		Email h = new Email("h", "h h h");
		Email i = new Email("i", "i i i");
		Email j = new Email("j", "j j j");
		Email k = new Email("k", "k k k");
		Email l = new Email("l", "l l l");
		Email m = new Email("m", "m m m");
		a.setSpamScore(10);
		b.setSpamScore(11);
		c.setSpamScore(12);
		d.setSpamScore(13);
		e.setSpamScore(14);
		f.setSpamScore(15);
		g.setSpamScore(16);
		h.setSpamScore(17);
		i.setSpamScore(18);
		j.setSpamScore(19);
		k.setSpamScore(20);
		l.setSpamScore(21);
		m.setSpamScore(22);
		
		queue.insert(a);
		queue.insert(b);
		queue.insert(c);
		queue.insert(d);
		queue.insert(e);
		queue.insert(f);
		queue.insert(g);
		queue.insert(h);
		queue.insert(i);
		queue.insert(j);
		queue.insert(k);
		queue.insert(l);
		
		queue.extractMaximum();
		assertEquals("|k -- 20|j -- 19|f -- 15|g -- 16|i -- 18|b -- 11|e -- 14|a -- 10|d -- 13|c -- 12|h -- 17|",queue.toString());
		queue.extractMaximum();
		queue.extractMaximum();
		queue.extractMaximum();
		queue.extractMaximum();
		queue.extractMaximum();
		queue.extractMaximum();
		queue.extractMaximum();
		queue.extractMaximum();
		queue.extractMaximum();
		assertEquals("|b -- 11|a -- 10|", queue.toString());
		queue.extractMaximum();
		queue.extractMaximum();
		assertEquals("|", queue.toString());
		assertThrows(NoSuchElementException.class, () -> {  
			queue.extractMaximum();
		});
	}

	@Test
	final void testGetIDs() {
		queue = new PriorityQueue();
		Email a = new Email("a", "a a a");
		Email b = new Email("b", "b b b");
		Email c = new Email("c", "c c c");
		Email d = new Email("d", "d d d");
		Email e = new Email("e", "e e e");
		Email f = new Email("f", "f f f");
		Email g = new Email("g", "g g g");
		Email h = new Email("h", "h h h");
		Email i = new Email("i", "i i i");
		Email j = new Email("j", "j j j");
		Email k = new Email("k", "k k k");
		Email l = new Email("l", "l l l");
		Email m = new Email("m", "m m m");
		a.setSpamScore(10);
		b.setSpamScore(11);
		c.setSpamScore(12);
		d.setSpamScore(13);
		e.setSpamScore(14);
		f.setSpamScore(15);
		g.setSpamScore(16);
		h.setSpamScore(17);
		i.setSpamScore(18);
		j.setSpamScore(19);
		k.setSpamScore(20);
		l.setSpamScore(21);
		m.setSpamScore(22);
		
		queue.insert(a);
		queue.insert(b);
		queue.insert(c);
		queue.insert(d);
		queue.insert(e);
		queue.insert(f);
		queue.extractMaximum();
		queue.extractMaximum();
		queue.insert(i);
		String[] array = {"a","b","c","d","e"};
		System.out.println(Arrays.toString(queue.getIDs()));
		assertEquals(array[0], queue.getIDs()[0]);
	}
	
	@Test
	final void testGetWords() {
		queue = new PriorityQueue();
		Email a = new Email("a", "a a a");
		Email b = new Email("b", "b b b");
		Email c = new Email("c", "c c c");
		Email d = new Email("d", "d d d");
		Email e = new Email("e", "e e e");
		Email f = new Email("f", "f f f");
		Email g = new Email("g", "g g g");
		Email h = new Email("h", "h h h");
		Email i = new Email("i", "i i i");
		Email j = new Email("j", "j j j");
		Email k = new Email("k", "k k k");
		Email l = new Email("l", "l l l");
		Email m = new Email("m", "m m m");
		a.setSpamScore(10);
		b.setSpamScore(11);
		c.setSpamScore(12);
		d.setSpamScore(13);
		e.setSpamScore(14);
		f.setSpamScore(15);
		g.setSpamScore(16);
		h.setSpamScore(17);
		i.setSpamScore(18);
		j.setSpamScore(19);
		k.setSpamScore(20);
		l.setSpamScore(21);
		m.setSpamScore(22);
		
		queue.insert(a);
		queue.insert(b);
		queue.insert(c);
		queue.insert(d);
		queue.insert(e);
		queue.insert(f);
		assertEquals("a", queue.getWords("a")[0]);
	}

	@Test
	final void testSize() {
		queue = new PriorityQueue();
		Email a = new Email("a", "a a a");
		Email b = new Email("b", "b b b");
		Email c = new Email("c", "c c c");
		Email d = new Email("d", "d d d");
		Email e = new Email("e", "e e e");
		Email f = new Email("f", "f f f");
		Email g = new Email("g", "g g g");
		Email h = new Email("h", "h h h");
		Email i = new Email("i", "i i i");
		Email j = new Email("j", "j j j");
		Email k = new Email("k", "k k k");
		Email l = new Email("l", "l l l");
		Email m = new Email("m", "m m m");
		a.setSpamScore(10);
		b.setSpamScore(11);
		c.setSpamScore(12);
		d.setSpamScore(13);
		e.setSpamScore(14);
		f.setSpamScore(15);
		g.setSpamScore(16);
		h.setSpamScore(17);
		i.setSpamScore(18);
		j.setSpamScore(19);
		k.setSpamScore(20);
		l.setSpamScore(21);
		m.setSpamScore(22);
		
		queue.insert(a);
		queue.insert(b);
		queue.insert(c);
		queue.insert(d);
		queue.insert(e);
		queue.insert(f);
		queue.extractMaximum();
		queue.extractMaximum();
		queue.insert(i);
		
		assertEquals(5, queue.size());
	}

	@Test
	final void testGet() {
		queue = new PriorityQueue();
		Email a = new Email("a", "a a a");
		Email b = new Email("b", "b b b");
		Email c = new Email("c", "c c c");
		Email d = new Email("d", "d d d");
		Email e = new Email("e", "e e e");
		Email f = new Email("f", "f f f");
		Email g = new Email("g", "g g g");
		Email h = new Email("h", "h h h");
		Email i = new Email("i", "i i i");
		Email j = new Email("j", "j j j");
		Email k = new Email("k", "k k k");
		Email l = new Email("l", "l l l");
		Email m = new Email("m", "m m m");
		a.setSpamScore(10);
		b.setSpamScore(11);
		c.setSpamScore(12);
		d.setSpamScore(13);
		e.setSpamScore(14);
		f.setSpamScore(15);
		g.setSpamScore(16);
		h.setSpamScore(17);
		i.setSpamScore(18);
		j.setSpamScore(19);
		k.setSpamScore(20);
		l.setSpamScore(21);
		m.setSpamScore(22);
		
		queue.insert(a);
		queue.insert(b);
		queue.insert(c);
		queue.insert(d);
		queue.insert(e);
		queue.insert(f);
		queue.extractMaximum();
		queue.extractMaximum();
		queue.insert(i);
		
		assertEquals(a, queue.get("a"));
	}

}

