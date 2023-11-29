import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDictionaryTest {

    @Test
    void addAndHasWord() {
        UserDictionary userDict = new UserDictionary(); // Make sure the file exists or handle the case where it doesn't

        String testWord = "example";
        assertFalse(userDict.hasWord(testWord), "Dictionary should not contain the word before adding");

        userDict.add(testWord);
        assertTrue(userDict.hasWord(testWord), "Dictionary should contain the word after adding");
    }

    @Test
    void removeWord() {
        UserDictionary userDict = new UserDictionary(); // Make sure the file exists or handle the case where it doesn't

        String testWord = "removable";
        userDict.add(testWord);
        assertTrue(userDict.hasWord(testWord), "Dictionary should contain the word before removal");

        userDict.remove(testWord);
        assertFalse(userDict.hasWord(testWord), "Dictionary should not contain the word after removal");
    }

    @Test
    void hasWord() {
        UserDictionary userDict = new UserDictionary(); // Make sure the file exists or handle the case where it doesn't

        String existingWord = "existing";
        userDict.add(existingWord);

        assertTrue(userDict.hasWord(existingWord), "Dictionary should confirm the existence of the word");
        assertFalse(userDict.hasWord("nonexistent"), "Dictionary should confirm the non-existence of the word");
    }
}