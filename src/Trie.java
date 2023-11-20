import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Function to insert a word into the trie. this is the function that will construct the tree
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.hasChild(c)) {
                node.setChild(c, new TrieNode());
            }
            node = node.getChild(c);
        }
        node.setEndOfWord(true);
    }

    // Function to check if a word exists in the trie.
    public boolean contains(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.hasChild(c)) {
                return false;
            }
            node = node.getChild(c);
        }
        return node.isEndOfWord();
    }

    // Function to suggest corrections for a misspelled word.
    public List<String> suggestCorrections(String misspelledWord) {
        Set<String> suggestions = new HashSet<>(); // Use HashSet to avoid duplicates in the suggestions

        // Remove each letter from the word for possible suggestions
        for (int i = 0; i < misspelledWord.length(); i++) {
            StringBuilder sb = new StringBuilder(misspelledWord);
            sb.deleteCharAt(i);
            if (contains(sb.toString())) {
                suggestions.add(sb.toString());
            }
        }

        // Change each letter from the word into other letters for possible suggestions
        for (int i = 0; i < misspelledWord.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                StringBuilder sb = new StringBuilder(misspelledWord);
                sb.setCharAt(i, c);
                if (this.contains(sb.toString())) {
                    suggestions.add(sb.toString());
                }
            }
        }

        // Insert all 26 letters into each possible location of the word for possible suggestions
        for (int i = 0; i <= misspelledWord.length(); i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                StringBuilder sb = new StringBuilder(misspelledWord);
                sb.insert(i, c);
                if (this.contains(sb.toString())) {
                    suggestions.add(sb.toString());
                }
            }
        }

        // Swap all possible pairs of consecutive letters in the word for possible suggestions
        for (int i = 0; i < misspelledWord.length() - 1; i++) {
            StringBuilder sb = new StringBuilder(misspelledWord);
            char current = sb.charAt(i);
            char next = sb.charAt(i + 1);
            sb.setCharAt(i, next);
            sb.setCharAt(i + 1, current);
            if (this.contains(sb.toString())) {
                suggestions.add(sb.toString());
            }
        }

        return new ArrayList<>(suggestions); // return it as a List
    }
}
