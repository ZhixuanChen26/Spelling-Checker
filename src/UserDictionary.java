import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class UserDictionary {
    private HashSet<String> userWords;
    private String filePath = "userDictionary.txt"; // Path to the file where user defined legit words are stored

    public UserDictionary() {
        userWords = new HashSet<>();
        loadWords();
    }

    private void loadWords() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            userWords.addAll(lines);
        } catch (IOException e) {
            // Handle the case where the file doesn't exist yet
            System.out.println("User dictionary file not found. A new one will be created upon adding words.");
        }
    }

    private void saveWords() {
        try {
            Files.write(Paths.get(filePath), userWords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void add(String str) {
        userWords.add(str);
        saveWords();
    }

    public void remove(String word) {
        userWords.remove(word);
    }

    public boolean hasWord(String word) {
        return userWords.contains(word);
    }
}
