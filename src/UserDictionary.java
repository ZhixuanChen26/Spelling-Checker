import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages a user-defined dictionary, allowing for the addition and removal of words.
 * This dictionary is stored in a file and is loaded upon initialization of the class.
 *
 * @author Yi Ran, Zirui Si
 * @version 1.1
 * @since 2023-10-30
 */
public class UserDictionary {
    private HashSet<String> userWords;
    private String filePath = "userDictionary.txt"; // Path to the file where user defined legit words are stored

    /**
     * Constructor for UserDictionary. Initializes the userWords HashSet and loads words from the file.
     */
    public UserDictionary() {
        userWords = new HashSet<>();
        loadWords();
    }

    /**
     * Loads words from the user dictionary file into the HashSet.
     */
    private void loadWords() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            userWords.addAll(lines);
        } catch (IOException e) {
            // Handle the case where the file doesn't exist yet
            System.out.println("User dictionary file not found. A new one will be created upon adding words.");
        }
    }

    /**
     * Saves the current state of user words into the file.
     */
    private void saveWords() {
        try {
            Files.write(Paths.get(filePath), userWords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a word to the user dictionary and saves the updated dictionary to the file.
     *
     * @param str The word to be added.
     */
    public void add(String str) {
        userWords.add(str);
        saveWords();
    }

    /**
     * Removes a word from the user dictionary.
     *
     * @param word The word to be removed.
     */
    public void remove(String word) {
        userWords.remove(word);
    }

    /**
     * Checks if a word exists in the user dictionary.
     *
     * @param word The word to check in the user dictionary.
     * @return boolean True if the word exists in the user dictionary, false otherwise.
     */
    public boolean hasWord(String word) {
        return userWords.contains(word);
    }
}
