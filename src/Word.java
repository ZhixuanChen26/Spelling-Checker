/**
 * Represents a word with attributes to mark it as misspelled, miscapitalized, or a double word.
 * Note: This class was not applied in the final project but is kept here for potential future use or reference.
 *
 * @author Yi Ran
 * @version 1.0
 * @deprecated
 * @since 2023-10-30
 */

public class Word {

    private String spelling;
    private boolean Misspelled;
    private boolean Miscapitalized;
    private boolean DoubleWord;

    /**
     * Constructor for the Word class.
     * Initializes a new Word with default attribute values.
     *
     * @param spelling The actual spelling of the word.
     */
    public Word(String spelling) {
        this.spelling = spelling;
        this.Misspelled = false; // Default values. You can adjust as necessary.
        this.Miscapitalized = false;
        this.DoubleWord = false;
    }

    /**
     * Gets the spelling of the word.
     *
     * @return String The spelling of the word.
     */
    public String getSpelling() {
        return spelling;
    }

    /**
     * Sets the spelling of the word.
     *
     * @param spelling The new spelling of the word.
     */
    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    /**
     * Checks if the word is marked as misspelled.
     *
     * @return boolean True if the word is marked as misspelled, false otherwise.
     */
    public boolean isMisspelled() {
        return Misspelled;
    }

    /**
     * Sets the misspelled status of the word.
     *
     * @param isMisspelled The misspelled status to set.
     */
    public void setMisspelled(boolean isMisspelled) {
        this.Misspelled = isMisspelled;
    }

    /**
     * Checks if the word is marked as miscapitalized.
     *
     * @return boolean True if the word is marked as miscapitalized, false otherwise.
     */
    public boolean isMiscapitalized() {
        return Miscapitalized;
    }

    /**
     * Sets the miscapitalized status of the word.
     *
     * @param isMiscapitalized The miscapitalized status to set.
     */
    public void setMiscapitalized(boolean isMiscapitalized) {
        this.Miscapitalized = isMiscapitalized;
    }

    /**
     * Checks if the word is marked as a double word.
     *
     * @return boolean True if the word is marked as a double word, false otherwise.
     */
    public boolean isDoubleWord() {
        return DoubleWord;
    }

    /**
     * Sets the double word status of the word.
     *
     * @param isDoubleWord The double word status to set.
     */
    public void setDoubleWord(boolean isDoubleWord) {
        this.DoubleWord = isDoubleWord;
    }

    /**
     * Returns a string representation of the Word object. Indicating its spellings and flags.
     *
     * @return String The string representation of the Word.
     */
    @Override
    public String toString() {
        return "Word{" +
                "spelling='" + spelling + '\'' +
                ", isMisspelled=" + Misspelled +
                ", isMiscapitalized=" + Miscapitalized +
                ", isDoubleWord=" + DoubleWord +
                '}';
    }

}

