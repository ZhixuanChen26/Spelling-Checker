import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Used to test Trie.java
 *
 * @authorJingwen Liu
 * @version 1.0
 * @since 2023-11-20
 */

class TrieTest {
    /**
     * Test the insert and contains methods of the Trie.
     * This test first ensures that the Trie does not contain a specific word ('apple') before it's inserted. 
     * After insertion, it checks if the Trie correctly reports that it now contains the word.
     */
    @Test
    void insertAndContains() {
        Trie trie = new Trie();
        assertFalse(trie.contains("apple"), "Trie should not contain 'apple' before insertion");

        trie.insert("apple");
        assertTrue(trie.contains("apple"), "Trie should contain 'apple' after insertion");
    }
    /**
     * Test the suggestCorrections method of the Trie.
     * This test inserts a couple of words ('apple' and 'app') into the Trie and then asks for corrections for a typo ('aple'). 
     * It checks if the returned suggestionscorrectly include the words 'apple' and 'app'.
     */
    @Test
    void suggestCorrections() {
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("app");

        List<String> corrections = trie.suggestCorrections("aple");
        assertTrue(corrections.contains("apple"), "Corrections should include 'apple' for 'aple'");
        assertTrue(corrections.contains("app"), "Corrections should include 'app' for 'aple'");
    }
}
