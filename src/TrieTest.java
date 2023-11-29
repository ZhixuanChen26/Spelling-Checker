import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    @Test
    void insertAndContains() {
        Trie trie = new Trie();
        assertFalse(trie.contains("apple"), "Trie should not contain 'apple' before insertion");

        trie.insert("apple");
        assertTrue(trie.contains("apple"), "Trie should contain 'apple' after insertion");
    }

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
