/**
 * Represents a word that is ignored in the spell checking process, along with its position in the document.
 * This class is used to track words that are to be ignored once during spell checking.
 *
 * @author Zirui Si
 * @version 1.0
 * @since 2023-11-25
 */

import javax.swing.text.Position;
import java.util.Objects;

public class IgnoredWord {
    String word;
    Position position;

    /**
     * Constructs an IgnoredWord object with the specified word and its position.
     *
     * @param word The word to be ignored.
     * @param position The position of the word in the document.
     */
    public IgnoredWord(String word, Position position){
        this.word = word;
        this.position = position;
    }

    /**
     * Gets the word.
     *
     * @return String The ignored word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets the word.
     *
     * @param word The new word to be set.
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Gets the position of the word in the document.
     *
     * @return Position The position of the word.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of the word in the document.
     *
     * @param position The new position to be set.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o The reference object with which to compare.
     * @return boolean True if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IgnoredWord that)) return false;
        return Objects.equals(word, that.word) && Objects.equals(position, that.position);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return int A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(word, position);
    }
}
