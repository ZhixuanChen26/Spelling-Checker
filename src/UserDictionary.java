import java.util.HashSet;


public class UserDictionary {
    private HashSet<String> userWords;

    public UserDictionary() {
        userWords = new HashSet<>();
    }

    public void add(String str) {
        userWords.add(str);
    }

    public void remove(String word) {
        userWords.remove(word);
    }

    public boolean hasWord(String word) {
        return userWords.contains(word);
    }
}
