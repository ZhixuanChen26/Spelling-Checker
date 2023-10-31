public class Word {

    private String spelling;
    private boolean Misspelled;
    private boolean Miscapitalized;
    private boolean DoubleWord;

    // Constructor
    public Word(String spelling) {
        this.spelling = spelling;
        this.Misspelled = false; // Default values. You can adjust as necessary.
        this.Miscapitalized = false;
        this.DoubleWord = false;
    }

    // Getters and setters

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    public boolean isMisspelled() {
        return Misspelled;
    }

    public void setMisspelled(boolean isMisspelled) {
        this.Misspelled = isMisspelled;
    }

    public boolean isMiscapitalized() {
        return Miscapitalized;
    }

    public void setMiscapitalized(boolean isMiscapitalized) {
        this.Miscapitalized = isMiscapitalized;
    }

    public boolean isDoubleWord() {
        return DoubleWord;
    }

    public void setDoubleWord(boolean isDoubleWord) {
        this.DoubleWord = isDoubleWord;
    }

    @Override
    public String toString() {
        return "Word{" +
                "spelling='" + spelling + '\'' +
                ", isMisspelled=" + Misspelled +
                ", isMiscapitalized=" + Miscapitalized +
                ", isDoubleWord=" + DoubleWord +
                '}';
    }

    // You can also add more methods as necessary for checking or updating the states,
    // or even methods for comparison or other utility purposes.
}

