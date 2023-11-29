import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SysDictionaryTest {

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
