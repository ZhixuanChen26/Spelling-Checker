import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Represents a system dictionary utilizing a Trie data structure for efficient word storage and retrieval.
 * This class provides functionalities to construct a dictionary from a file, add words to the dictionary,
 * check the existence of words, and suggest corrections for misspelled words.
 *
 * @author Yi Ran, Zirui Si
 * @version 1.1
 * @since 2023-10-30
 */
public class SysDictionary {

    private Trie trie;

    /**
     * Constructs a SysDictionary object and initializes the dictionary by reading words from a given file path.
     *
     * @param filePath The file path to read the dictionary words from.
     */
    public SysDictionary(String filePath) {
        trie = new Trie();
        constructDict(filePath);
    }

    /**
     * Reads words from the specified file and adds them to the dictionary.
     *
     * @param filePath The file path to read the dictionary words from.
     */
    private void constructDict(String filePath) {
        try {
            List<String> words = Files.readAllLines(Paths.get(filePath));
            for (String word : words) {
                addWord(word);
            }
        } catch (IOException e) {
            System.err.println("Error reading from path: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Adds a word to the dictionary.
     *
     * @param word The word to be added to the dictionary.
     */
    public void addWord(String word) {
        trie.insert(word);
    }

    /**
     * Checks if a word exists in the dictionary.
     *
     * @param word The word to check in the dictionary.
     * @return boolean True if the word exists in the dictionary, false otherwise.
     */
    public boolean hasWord(String word) {
        return trie.contains(word.toLowerCase());
    }

    /**
     * Suggests possible corrections for a misspelled word by querying the Trie.
     *
     * @param misspelled The misspelled word for which to suggest corrections.
     * @return A list of possible corrections.
     */
    public List<String> suggestCorrections(String misspelled) {
        return trie.suggestCorrections(misspelled);
    }

}
