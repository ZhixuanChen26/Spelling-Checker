import static org.junit.jupiter.api.Assertions.*;

/**
 * Used to test TrieNode.java
 *
 * @authorJingwen Liu
 * @version 1.0
 * @since 2023-11-20
 */

class TrieNodeTest {
   /**
     * Tests whether the hasChild method correctly identifies the presence of a child node.
     * First, it verifies that a newly created TrieNode does not have a child for a given character.
     * After setting a child, it checks if the TrieNode correctly identifies the presence of this child.
     */
    @org.junit.jupiter.api.Test
    void hasChild() {
        TrieNode node = new TrieNode();
        char c = 'a';
        assertFalse(node.hasChild(c), "Node should not have child before setting");

        node.setChild(c, new TrieNode());
        assertTrue(node.hasChild(c), "Node should have child after setting");
    }
  
    /**
     * Tests the getChild method for retrieving a child node.
     * Firstly, ensures that the TrieNode returns null for a child that has not been set.
     * After setting a child for a character, it checks if the TrieNode correctly returns the set child node.
     */
    @org.junit.jupiter.api.Test
    void getChild() {
        TrieNode node = new TrieNode();
        char c = 'b';
        assertNull(node.getChild(c), "Child should be null initially");

        TrieNode childNode = new TrieNode();
        node.setChild(c, childNode);
        assertEquals(childNode, node.getChild(c), "Retrieved child should be the same as the one set");
    }
  
    /**
     * Tests the setChild method.
     * This method checks if a child node can be successfully set and then retrieved for a given character, 
     * ensuring that the setChild method works as expected.
     */
    @org.junit.jupiter.api.Test
    void setChild() {
        TrieNode node = new TrieNode();
        char c = 'c';
        TrieNode childNode = new TrieNode();

        node.setChild(c, childNode);
        assertTrue(node.hasChild(c), "Node should have child after setChild");
        assertEquals(childNode, node.getChild(c), "getChild should return the node set by setChild");
    }
  
    /**
     * Tests the isEndOfWord method to check if a TrieNode correctly identifies itself as the end of a word.
     * Initially, it ensures that a new TrieNode is not marked as the end of a word.
     * After setting the end of word flag, it checks if the TrieNode correctly reflects this change.
     */
    @org.junit.jupiter.api.Test
    void isEndOfWord() {
        TrieNode node = new TrieNode();
        assertFalse(node.isEndOfWord(), "Node should not be end of word by default");

        node.setEndOfWord(true);
        assertTrue(node.isEndOfWord(), "Node should be marked as end of word after setting");
    }
  
    /**
     * Tests the setEndOfWord method.
     * This method first marks a TrieNode as the end of a word and checks if this is correctly reflected.
     * It then resets this flag and checks if the TrieNode is correctly updated to not being the end of a word.
     */
    @org.junit.jupiter.api.Test
    void setEndOfWord() {
        TrieNode node = new TrieNode();
        node.setEndOfWord(true);
        assertTrue(node.isEndOfWord(), "Node should be marked as end of word after setEndOfWord");

        node.setEndOfWord(false);
        assertFalse(node.isEndOfWord(), "Node should not be marked as end of word after resetting");
    }
}
