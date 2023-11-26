import javax.swing.text.Position;
import java.util.Objects;

public class IgnoredWord {
    String word;
    Position position;

    public IgnoredWord(String word, Position position){
        this.word = word;
        this.position = position;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IgnoredWord that)) return false;
        return Objects.equals(word, that.word) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, position);
    }
}
