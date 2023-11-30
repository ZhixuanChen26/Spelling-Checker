import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Used to test UserDictionary.java
 * @author Zhixuan Chen
 * @version 1.0
 * @since 2023-11-22
 */

class UserDictionaryTest {
     /**
     * Tests the addAndHasWord method of UserDictionary.
     * It first checks that a new word is not already in the dictionary,
     * then adds the word and checks if the dictionary reflects this addition.
     */
    @Test
    void addAndHasWord() {
        UserDictionary userDict = new UserDictionary(); // Make sure the file exists or handle the case where it doesn't

        String testWord = "example";
        assertFalse(userDict.hasWord(testWord), "Dictionary should not contain the word before adding");

        userDict.add(testWord);
        assertTrue(userDict.hasWord(testWord), "Dictionary should contain the word after adding");
    }
  
    /**
     * Tests the removeWord method of UserDictionary.
     * It adds a word, checks its presence, then removes it and verifies its absence.
     */
    @Test
    void removeWord() {
        UserDictionary userDict = new UserDictionary(); // Make sure the file exists or handle the case where it doesn't

        String testWord = "removable";
        userDict.add(testWord);
        assertTrue(userDict.hasWord(testWord), "Dictionary should contain the word before removal");

        userDict.remove(testWord);
        assertFalse(userDict.hasWord(testWord), "Dictionary should not contain the word after removal");
    }

    /**
     * Tests the hasWord method of UserDictionary.
     * It adds a word, verifies its presence, and also checks for a non-existent word.
     */
    @Test
    void hasWord() {
        UserDictionary userDict = new UserDictionary(); // Make sure the file exists or handle the case where it doesn't

        String existingWord = "existing";
        userDict.add(existingWord);

        assertTrue(userDict.hasWord(existingWord), "Dictionary should confirm the existence of the word");
        assertFalse(userDict.hasWord("nonexistent"), "Dictionary should confirm the non-existence of the word");
    }
}
