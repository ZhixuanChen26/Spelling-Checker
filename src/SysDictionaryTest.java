import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Used to test SysDictionary.java
 * @author Zhixuan Chen
 * @version 1.0
 * @since 2023-11-22
 */

class SysDictionaryTest {
  
    /**
     * Tests the addWord and hasWord methods of SysDictionary.
     * It adds a list of words to the dictionary and then checks if each word has been successfully added.
     */
    @Test
    void addWordAndHasWord() {
        SysDictionary sysDict = new SysDictionary("words_alpha.txt"); // Assume this is a valid file path

        String[] wordsToAdd = {"example", "test", "dictionary", "apple", "banana", "orange", "java", "code", "programming", "junit"};
        for (String word : wordsToAdd) {
            sysDict.addWord(word);
        }

        for (String word : wordsToAdd) {
            assertTrue(sysDict.hasWord(word), "Dictionary should contain '" + word + "' after addition");
        }
    }
  
    /**
     * Tests the suggestCorrections method of SysDictionary.
     * It adds some words and then checks if the suggested corrections for a misspelled word are accurate.
     */
    @Test
    void suggestCorrections() {
        SysDictionary sysDict = new SysDictionary("words_alpha.txt"); // Assume this is a valid file path
        sysDict.addWord("apple");
        sysDict.addWord("banana");

        List<String> corrections = sysDict.suggestCorrections("appel"); // Common misspelling
        assertTrue(corrections.contains("apple"), "Corrections should include 'apple' for 'appel'");
        assertFalse(corrections.contains("banana"), "'banana' should not be a suggestion for 'appel'");
    }
}
