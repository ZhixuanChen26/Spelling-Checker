import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SysDictionary {

    private Trie trie;

    public SysDictionary(String filePath) {
        trie = new Trie();
        constructDict(filePath);
    }

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

    public void addWord(String word) {
        trie.insert(word);
    }

    public boolean hasWord(String word) {
        return trie.contains(word);
    }

    public List<String> suggestCorrections(String misspelled) {
        return trie.suggestCorrections(misspelled);
    }

}
