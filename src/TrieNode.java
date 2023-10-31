public class TrieNode {
    // The number of children nodes can vary depending on the alphabet size.
    private final TrieNode[] children;
    private boolean isEndOfWord;

    public TrieNode() {
        children = new TrieNode[26]; // All words will be reduced to lower case letters and only alphabetic words
        isEndOfWord = false;
    }


    public Boolean hasChild(char c) {
        return children[c - 'a'] != null;
    }


    public TrieNode getChild(char c) {
        return children[c - 'a'];
    }

    public void setChild(char c, TrieNode node) {
        children[c - 'a'] = node;
    }

    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }
}
