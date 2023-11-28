/**
 * Implementation of a Trie Node for use in a Trie data structure.
 * This class represents a single node in the Trie, with references to child nodes.
 *
 * @author Yi Ran
 * @version 1.0
 * @since 2023-10-30
 */
public class TrieNode {
    // The number of children nodes can vary depending on the alphabet size.
    private final TrieNode[] children;
    private boolean isEndOfWord;

    /**
     * Constructor for TrieNode. Initializes a node with an array of children nodes.
     * Assumes only lower case alphabetic characters.
     */
    public TrieNode() {
        children = new TrieNode[26]; // All words will be reduced to lower case letters and only alphabetic words
        isEndOfWord = false;
    }

    /**
     * Checks if the node has a child node corresponding to the given character.
     *
     * @param c The character to check in the children array.
     * @return Boolean indicating whether the child node exists.
     */
    public Boolean hasChild(char c) {
        return children[c - 'a'] != null;
    }

    /**
     * Retrieves the child node corresponding to the given character.
     *
     * @param c The character for which to retrieve the child node.
     * @return TrieNode representing the child node for the given character.
     */
    public TrieNode getChild(char c) {
        return children[c - 'a'];
    }

    /**
     * Sets the child node for a given character.
     * Associates the specified TrieNode as a child for the character 'c'.
     *
     * @param c The character to associate the child node with.
     * @param node The TrieNode to be set as a child for character 'c'.
     */
    public void setChild(char c, TrieNode node) {
        children[c - 'a'] = node;
    }

    /**
     * Checks whether this node marks the end of a word in the Trie.
     *
     * @return boolean indicating whether this node is the end of a word.
     */
    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    /**
     * Sets the end of word status of this node.
     * Marks whether this node represents the end of a word in the Trie.
     *
     * @param endOfWord The boolean value to set the end of word status.
     */
    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }
}
