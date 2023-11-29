import static org.junit.jupiter.api.Assertions.*;

class TrieNodeTest {

    @org.junit.jupiter.api.Test
    void hasChild() {
        TrieNode node = new TrieNode();
        char c = 'a';
        assertFalse(node.hasChild(c), "Node should not have child before setting");

        node.setChild(c, new TrieNode());
        assertTrue(node.hasChild(c), "Node should have child after setting");
    }

    @org.junit.jupiter.api.Test
    void getChild() {
        TrieNode node = new TrieNode();
        char c = 'b';
        assertNull(node.getChild(c), "Child should be null initially");

        TrieNode childNode = new TrieNode();
        node.setChild(c, childNode);
        assertEquals(childNode, node.getChild(c), "Retrieved child should be the same as the one set");
    }

    @org.junit.jupiter.api.Test
    void setChild() {
        TrieNode node = new TrieNode();
        char c = 'c';
        TrieNode childNode = new TrieNode();

        node.setChild(c, childNode);
        assertTrue(node.hasChild(c), "Node should have child after setChild");
        assertEquals(childNode, node.getChild(c), "getChild should return the node set by setChild");
    }

    @org.junit.jupiter.api.Test
    void isEndOfWord() {
        TrieNode node = new TrieNode();
        assertFalse(node.isEndOfWord(), "Node should not be end of word by default");

        node.setEndOfWord(true);
        assertTrue(node.isEndOfWord(), "Node should be marked as end of word after setting");
    }

    @org.junit.jupiter.api.Test
    void setEndOfWord() {
        TrieNode node = new TrieNode();
        node.setEndOfWord(true);
        assertTrue(node.isEndOfWord(), "Node should be marked as end of word after setEndOfWord");

        node.setEndOfWord(false);
        assertFalse(node.isEndOfWord(), "Node should not be marked as end of word after resetting");
    }
}
