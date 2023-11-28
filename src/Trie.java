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
    /*
    // Function to suggest corrections for a misspelled word.
    public List<String> suggestCorrections(String misspelledWord) {
        List<String> suggestions = new ArrayList<>();
        suggestCorrectionsDFS(misspelledWord, root, "", suggestions, 2);
        return suggestions;
    }

    // A helper method to traverse the Trie and give possible suggestions for misspelled word
    private void suggestCorrectionsDFS(String misspelledWord, TrieNode node, String currentStr,
                                       List<String> suggestions, int maxDistance)
    {
        if (suggestions.size() >= 10) {
            return; //terminate when list is large to avoid too many calls, so it won't be slow
        }

        if (node.isEndOfWord()) {
            int distance = getLevenshteinDistance(currentStr, misspelledWord);
            // if the distance is too large, it is unlikely to be the correct suggestion. so don't include it
            // maxDistance decides the accuracy and size of suggestions. small->better accuracy, large->more suggestion
            if (distance <= maxDistance) {
                suggestions.add(currentStr);
            }
        }

        // Terminate early if curr path is unlikely to produce a suggestion,
        if (currentStr.length() >= misspelledWord.length() + maxDistance) {
            return;
        }

        // Prioritize traversal with letter order in misspelled word since that order is close to correct spelled word
        if (currentStr.length() < misspelledWord.length() && node.hasChild(misspelledWord.charAt(currentStr.length()))) {
            char nextChar = misspelledWord.charAt(currentStr.length());
            suggestCorrectionsDFS(misspelledWord, node.getChild(nextChar), currentStr + nextChar, suggestions, maxDistance);
        }

        // Continue the non-prioritized brute force DFS traverse but skip the prioritized path we went above
        for (char c = 'a'; c <= 'z'; c++) {
            if (currentStr.length() < misspelledWord.length() && c == misspelledWord.charAt(currentStr.length())) {
                continue; // skip the prioritized order traverse we did above to avoid repeated traverse calls
            }
            if (node.hasChild(c)) { // Brute force DFS following alphabetic order
                TrieNode child = node.getChild(c);
                suggestCorrectionsDFS(misspelledWord, child, currentStr + c, suggestions, maxDistance);
            }
        }
    }

    // LevenshteinDistance is minimum edits needed to change 1 word into another
    private int getLevenshteinDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        // Base case: If either of the words reduce to empty string, the distance is the length of the other word.
        if (len1 == 0) return len2;

        if (len2 == 0) return len1;


        // If the last characters are the same, exclude char to reduce both words since last char has no effect on result
        if (word1.charAt(len1 - 1) == word2.charAt(len2 - 1)) {
            return getLevenshteinDistance(word1.substring(0, len1 - 1), word2.substring(0, len2 - 1));
        }

        // Calculate the minimum of three operations: insertion, deletion, and substitution.
        int insertion = getLevenshteinDistance(word1, word2.substring(0, len2 - 1));
        int deletion = getLevenshteinDistance(word1.substring(0, len1 - 1), word2);
        int substitution = getLevenshteinDistance(word1.substring(0, len1 - 1), word2.substring(0, len2 - 1));

        return 1 + Math.min(insertion, Math.min(deletion, substitution));
    }
     */
}
