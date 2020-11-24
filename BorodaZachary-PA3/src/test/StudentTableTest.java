package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import main.Entry;
import main.HashTable;

class StudentTableTest {
	
	HashTable<Integer> hash = null;
	
	@Test
	void testHashFunction() {
		hash = new HashTable<Integer>();
		String a = "z";
		assertEquals(a.hashCode() % 11,hash.hashFunction(a));
		a = "ab";
		assertEquals(a.hashCode() % 11,hash.hashFunction(a));
		a = "abc";
		assertEquals(a.hashCode() % 11,hash.hashFunction(a));
		a = "abcdef";
		assertEquals(Math.abs(a.hashCode() % 11),hash.hashFunction(a));
		a = "abcdefffff";
		assertEquals(Math.abs(a.hashCode() % 11),hash.hashFunction(a));
		a = "zzzzzzzzzz";
		assertEquals(Math.abs(a.hashCode() % 11),hash.hashFunction(a));
		a = "zzzzzzzzzzz";
		String A = "zzzzzzzzzzz";
		assertEquals(hash.hashFunction(A),hash.hashFunction(a));
	}
	
	@Test
	void testGetHash() {
		hash = new HashTable<Integer>();
		String a = "z";
		hash.entries[hash.hashFunction(a)] = new Entry<Integer>(a, 1);
		assertEquals(hash.hashFunction(a),hash.getHash(a));
		
		hash = new HashTable<Integer>();
		//Now Lets test one collision
		a = "zz";
		hash.entries[hash.getHash(a)] = new Entry<Integer>("testFill1", 2);
		assertEquals((hash.hashFunction(a)+1) % 11,hash.getHash(a));
		
		//Now Lets test two collisions
		hash.entries[hash.getHash(a)] = new Entry<Integer>("testFill2", 2);
		assertEquals((hash.hashFunction(a)+4) % 11,hash.getHash(a));
		
		//Now Lets test three collisions
		hash.entries[hash.getHash(a)] = new Entry<Integer>("testFill3", 2);
		assertEquals((hash.hashFunction(a)+9) % 11,hash.getHash(a));
		
		//Now Lets test four collisions
		hash.entries[hash.getHash(a)] = new Entry<Integer>("testFill4", 2);
		assertEquals((hash.hashFunction(a)+16) % 11,hash.getHash(a));
		
		//Now Lets test five collisions
		hash.entries[hash.getHash(a)] = new Entry<Integer>("testFill5", 2);
		assertEquals((hash.hashFunction(a)+25) % 11,hash.getHash(a));
		
		//Now Lets test the maximum capacity reached
		hash.entries[hash.getHash(a)] = new Entry<Integer>("testFill6", 2);
		assertEquals(-1,hash.getHash(a));
	}
	
	@Test
	void testSearch() {
		
		hash = new HashTable<Integer>();
		String a = "testFill";
		Entry<Integer> aEntry1 = new Entry<Integer>(a, 1);
		Entry<Integer> aEntry2 = new Entry<Integer>(a, 2);
		Entry<Integer> aEntry3 = new Entry<Integer>(a, 3);
		Entry<Integer> aEntry4 = new Entry<Integer>(a, 4);
		Entry<Integer> aEntry5 = new Entry<Integer>(a, 5);
		//First test searching
		hash.entries[hash.getHash(a)] = aEntry1;
		assertEquals(aEntry1, hash.search(aEntry1));
		//Now a little more tricky 
		assertEquals(aEntry1, hash.search(aEntry2));
		
		//Now after one collision 
		hash = new HashTable<Integer>();
		String insertString = "z";
		Entry<Integer> aEntryActual = new Entry<Integer>(insertString, 2);
		
		hash.entries[hash.getHash(insertString)] = aEntry1;
		hash.entries[hash.getHash(insertString)] = aEntry2;
		hash.entries[hash.getHash(insertString)] = aEntryActual;
		assertEquals(aEntryActual, hash.search(aEntryActual));
		
		//Now after a ton of collisions
		hash = new HashTable<Integer>();
		hash.entries[hash.getHash(insertString)] = aEntry1;
		hash.entries[hash.getHash(insertString)] = aEntry2;
		hash.entries[hash.getHash(insertString)] = aEntry3;
		hash.entries[hash.getHash(insertString)] = aEntry4;
		hash.entries[hash.getHash(insertString)] = aEntry5;
		hash.entries[hash.getHash(insertString)] = aEntryActual;
		assertEquals(aEntryActual, hash.search(aEntryActual));
		
		//Now lets test inserting capabilities
		
		//With no collisions
		hash = new HashTable<Integer>();
		hash.search(aEntryActual);
		assertEquals(aEntryActual, hash.entries[hash.getHash(insertString)]);
		
		//With a few collisions
		hash.entries[hash.getHash(insertString)] = aEntry1;
		hash.entries[hash.getHash(insertString)] = aEntry2;
		hash.search(aEntryActual);
		assertEquals(aEntryActual, hash.entries[hash.getHash(insertString)]);
		
		//With a bunch of collisions
		hash = new HashTable<Integer>();
		hash.entries[hash.getHash(insertString)] = aEntry1;
		hash.entries[hash.getHash(insertString)] = aEntry2;
		hash.entries[hash.getHash(insertString)] = aEntry3;
		hash.entries[hash.getHash(insertString)] = aEntry4;
		hash.entries[hash.getHash(insertString)] = aEntry5;
		hash.search(aEntryActual);
		assertEquals(aEntryActual, hash.entries[hash.getHash(insertString)]);
	}
	
	@Test
	void testRehash() {
		hash = new HashTable<Integer>();
		Entry<Integer> aEntry1 = new Entry<Integer>("a", 1);
		Entry<Integer> aEntry2 = new Entry<Integer>("b", 2);
		Entry<Integer> aEntry3 = new Entry<Integer>("c", 3);
		Entry<Integer> aEntry4 = new Entry<Integer>("d", 4);
		Entry<Integer> aEntry5 = new Entry<Integer>("e", 5);
		Entry<Integer> aEntry6 = new Entry<Integer>("z", 5);
		Entry<Integer> aEntry7 = new Entry<Integer>("y", 5);
		Entry<Integer> aEntry8 = new Entry<Integer>("x", 5);
		hash.search(aEntry1);
		hash.search(aEntry2);
		hash.search(aEntry3);
		hash.search(aEntry4);
		hash.search(aEntry5);
		hash.search(aEntry6);
		hash.search(aEntry7);
		hash.search(aEntry8);
		hash.rehash();
		assertEquals(23, hash.entries.length);
		assertEquals(aEntry1, hash.entries[hash.getHash("a")]);
		assertEquals(aEntry2, hash.entries[hash.getHash("b")]);
		assertEquals(aEntry3, hash.entries[hash.getHash("c")]);
		assertEquals(aEntry4, hash.entries[hash.getHash("d")]);
		assertEquals(aEntry5, hash.entries[hash.getHash("e")]);
		assertEquals(aEntry6, hash.entries[hash.getHash("z")]);
	}
	
	@Test
	void testPut() {
		hash = new HashTable<Integer>();
		Entry<Integer> aEntry1 = new Entry<Integer>("a", 1);
		Entry<Integer> aEntry2 = new Entry<Integer>("b", 2);
		Entry<Integer> aEntry3 = new Entry<Integer>("c", 3);
		Entry<Integer> aEntry4 = new Entry<Integer>("d", 4);
		Entry<Integer> aEntry5 = new Entry<Integer>("e", 5);
		Entry<Integer> aEntry6 = new Entry<Integer>("z", 5);
		Entry<Integer> aEntry7 = new Entry<Integer>("y", 5);
		Entry<Integer> aEntry8 = new Entry<Integer>("x", 5);
		hash.put("a", 2);
		hash.put("b", 3);
		hash.put("c", 4);
		hash.put("z", 4);
		assertEquals(hash.entries[hash.getHash("z")], hash.search(aEntry6));
		hash.put("y", 2);
		hash.put("x", 3);
		hash.put("j", 4);
		hash.put("h", 4);
		//Test resize
		assertEquals(23, hash.entries.length);
		assertEquals(hash.entries[hash.getHash("z")], hash.search(aEntry6));
		assertEquals(hash.entries[hash.getHash("a")], hash.search(aEntry1));
	}
	
	@Test
	void testDelete() {
		hash = new HashTable<Integer>();
		hash.put("a", 2);
		hash.put("b", 3);
		hash.put("c", 4);
		hash.put("z", 4);
		System.out.println(Arrays.toString(hash.entries));
		hash.delete("z");
		assertEquals(hash.entries[1], hash.voidEntry);
		assertEquals(3, hash.size());
	}
	
	@Test
	void testGetKeys() {
		hash = new HashTable<Integer>();
		hash.put("a", 2);
		hash.put("b", 3);
		hash.put("c", 4);
		hash.put("z", 4);
		hash.delete("z");
		assertEquals("[c, a, b]", Arrays.toString(hash.getKeys()));
	}

}

