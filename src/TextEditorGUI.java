import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.*;
import java.util.List;
public class TextEditorGUI {
    private JFrame frame;
    private JTextPane textPane;
    private JButton OpenButton, CheckButton, ExitButton;
    private JScrollPane scrollPane;
    private JFileChooser fileChooser;
    private SysDictionary legalDic;

    public TextEditorGUI() {
        legalDic = new SysDictionary("words_alpha.txt");

        frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        textPane = new JTextPane();
        scrollPane = new JScrollPane(textPane);

        OpenButton = new JButton("File Selector");
        CheckButton = new JButton("Check");
        ExitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(OpenButton);
        buttonPanel.add(CheckButton);
        buttonPanel.add(ExitButton);

        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        fileChooser = new JFileChooser();

        OpenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    Path selectedFile = fileChooser.getSelectedFile().toPath();
                    try {
                        String content = new String(Files.readAllBytes(selectedFile));
                        textPane.setText(content);
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                }
            }
        });

        CheckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightMisspelledWords();
            }
        });

        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

    }

    private void highlightMisspelledWords() {
        // Retrieve the document from the text pane for styling
        StyledDocument doc = textPane.getStyledDocument();

        // Define a red underline style for spelling errors
        Style redStyle = doc.addStyle("RedUnderline", null);
        StyleConstants.setUnderline(redStyle, true);
        StyleConstants.setForeground(redStyle, Color.RED);

        // Define a blue underline style for capitalization errors
        Style blueStyle = doc.addStyle("BlueUnderline", null);
        StyleConstants.setUnderline(blueStyle, true);
        StyleConstants.setForeground(blueStyle, Color.BLUE);

        // Define a green style for double words
        Style greenStyle = doc.addStyle("GreenUnderline", null);
        StyleConstants.setUnderline(greenStyle, false);
        StyleConstants.setForeground(greenStyle, Color.GREEN);

        // Get the entire text from the text pane
        String text = textPane.getText();
        // Index to keep track of the current location in the text
        int idx = 0;
        // Flags to track sentence boundaries
        boolean isNewSentence = true;
        boolean isDiffSentence = true;
        // Variable to remember the previous word for double word detection
        String prev = "";

        // Split the text into words based on non-word characters
        for (String word : text.split("\\W+")) {
            // Check if the word contains only alphabetic characters
            if (word.matches("^[a-zA-Z]+$")) {
                // Determine if the word is mixed case or improperly capitalized
                boolean isMixedCase = !word.equals(word.toLowerCase())
                        && !word.equals(word.toUpperCase())
                        && !(Character.isUpperCase(word.charAt(0))
                        && word.substring(1).equals(word.substring(1).toLowerCase()));

                // Check if the word should be capitalized (start of a new sentence)
                boolean isStartOfSentenceError = isNewSentence && Character.isLowerCase(word.charAt(0));

                // Apply blue underline style for capitalization errors
                if (isMixedCase || isStartOfSentenceError) {
                    int length = word.length();
                    doc.setCharacterAttributes(idx, length, blueStyle, false);
                }

                // Check if the word is spelled correctly
                if (!legalDic.hasWord(word.toLowerCase())) {
                    int length = word.length();
                    doc.setCharacterAttributes(idx, length, redStyle, false);
                }

                // Highlight double words in green
                if (word.equalsIgnoreCase(prev) && !isDiffSentence) {
                    int length = word.length();
                    doc.setCharacterAttributes(idx, length, greenStyle, false);
                }
            }

            // Calculate the end index of the current word
            int wordEnd = idx + word.length();
            // Update idx to the position of the next word
            idx += word.length();

            // Skip non-letter characters to find the start of the next word
            while (idx < text.length() && !Character.isLetter(text.charAt(idx))) {
                idx++;
            }

            // Update isNewSentence based on punctuation
            isNewSentence = (wordEnd < text.length() &&
                    (text.charAt(wordEnd) == '.' ||
                            text.charAt(wordEnd) == '!' ||
                            text.charAt(wordEnd) == '?')) ||
                    (wordEnd + 1 < text.length() &&
                            text.substring(wordEnd, wordEnd + 2).matches("\\s+[.!?]"));

            // Update isDiffSentence based on non-letter and non-digit characters
            isDiffSentence = (wordEnd < text.length() &&
                    !Character.isLetter(text.charAt(wordEnd)) &&
                    !Character.isSpaceChar(text.charAt(wordEnd)));

            // Reset previous word if the current character marks a different sentence
            prev = isDiffSentence ? "" : word;
        }

    }

    public void display() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TextEditorGUI().display();
        });
    }
}