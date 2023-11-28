/**
 * A Graphical User Interface (GUI) for a text editor application.
 * It provides functionalities for opening, editing, spell checking, and saving text files.
 * Additional features include statistics about the text and highlighting of misspellings,
 * capitalization errors, and double words.
 *
 * @author Yi Ran, Zirui Si, Jingwen Liu, Zhixuan Chen, Yuyang Zhou
 * @version 1.14
 * @since 2023-11-12
 */

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
public class TextEditorGUI {
    private JFrame frame;
    private JTextPane textPane;
    private JButton OpenButton, CheckButton, ExitButton, SaveButton, HelpButton;
    private JScrollPane scrollPane;
    private JFileChooser fileChooser;
    private SysDictionary legalDic;
    private UserDictionary userDic = new UserDictionary();
    private File currentFile;
    private List<String> ignoredWords = new ArrayList<>();

    private static final String IGNORE_ONCE_ATTRIBUTE = "ignoreOnce";

    private List<IgnoredWord> ignoredWordList = new ArrayList<>();

    private String originalContent = "";

    private int countMisspellings;
    private int countCapitalizationErrors;
    private int countDoubleWords;

    private int countCharacters = 0;
    private int countLines = 0;
    private int countWords = 0;



    /**
     * Constructor for TextEditorGUI. Initializes the GUI components and dictionaries.
     */
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
        SaveButton = new JButton("Save");
        HelpButton = new JButton("Help");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(OpenButton);
        buttonPanel.add(CheckButton);
        buttonPanel.add(ExitButton);
        buttonPanel.add(SaveButton);
        buttonPanel.add(HelpButton);

        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);


        fileChooser = new JFileChooser();


        setupContextMenu();
        OpenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(frame);
                //re-update the arrayList
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    Path selectedFile = fileChooser.getSelectedFile().toPath();
                    currentFile = fileChooser.getSelectedFile();
                    try {
                        String content = new String(Files.readAllBytes(selectedFile));
                        textPane.setText(content);
                        originalContent = textPane.getText();
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
                calculateStatistics();

                JOptionPane.showMessageDialog(frame,
                        "Document Statistics:\n" +
                                "Characters: " + countCharacters + "\n" +
                                "Lines: " + countLines + "\n" +
                                "Words: " + countWords + "\n" +
                                "Misspellings: " + countMisspellings + "\n" +
                                "Capitalization Errors: " + countCapitalizationErrors + "\n" +
                                "Double Words: " + countDoubleWords,
                        "Statistics", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textPane.getText().equals(originalContent)) {
                    int result = JOptionPane.showConfirmDialog(frame,
                            "You have unsaved changes. Are you sure you want to exit?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        frame.dispose();
                    }
                } else {
                    originalContent = "";
                    frame.dispose();
                }
            }
        });

        SaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser saveChooser = new JFileChooser();
                if (currentFile != null) {
                    saveChooser.setSelectedFile(currentFile);
                }
                saveChooser.setFileFilter(new FileNameExtensionFilter("Text Files",".txt"));

                int returnValue = saveChooser.showSaveDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = saveChooser.getSelectedFile();
                    // Ensure file's extension is correct
                    if (!fileToSave.getName().endsWith(".txt")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                    }
                    saveTextToFile(fileToSave);
                    originalContent = "";
                }
            }
        });

        HelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog helpDialog = new JDialog(frame, "Help", false);
                JTextArea helpTextArea = new JTextArea(10, 30);
                helpTextArea.setText(
                        "File Selector: Open a text file.\n" +
                                "Check: Check the text for spelling and other errors.\n" +
                                "Save: Save the current text to a file.\n" +
                                "Exit: Exit the application.\n\n" +
                                "Right-click on words for more options."
                );
                helpTextArea.setEditable(false);
                helpTextArea.setWrapStyleWord(true);
                helpTextArea.setLineWrap(true);

                JScrollPane helpScrollPane = new JScrollPane(helpTextArea);
                helpDialog.add(helpScrollPane);
                helpDialog.pack();
                helpDialog.setLocationRelativeTo(frame);
                helpDialog.setVisible(true);
            }
        });

    }

    /**
     * Sets up the context menu for the text pane with options like suggestions, ignore, delete etc.
     */
    private void setupContextMenu() {
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int offset = textPane.viewToModel2D(e.getPoint());
                    try {
                        int wordStart = Utilities.getWordStart(textPane, offset);
                        int wordEnd = Utilities.getWordEnd(textPane, offset);
                        String selectWord = textPane.getDocument().getText(wordStart, wordEnd - wordStart);

                        AttributeSet attrs = textPane.getStyledDocument().getCharacterElement(wordStart).getAttributes();

                        boolean hasRedStyle = StyleConstants.getForeground(attrs).equals(Color.RED);
                        boolean hasBlueStyle = StyleConstants.getForeground(attrs).equals(Color.BLUE);
                        boolean hasGreenStyle = StyleConstants.getForeground(attrs).equals(Color.GREEN);

                        // Use the method in SysDictionary to generate suggestions for selected word
                        List<String> suggestions;
                        if (!legalDic.hasWord(selectWord.toLowerCase())) {
                            suggestions = legalDic.suggestCorrections(selectWord.toLowerCase());
                        } else {
                            suggestions = new ArrayList<>();
                            StringBuilder sb = new StringBuilder(selectWord.length());
                            sb.append(Character.toUpperCase(selectWord.charAt(0)));
                            sb.append(selectWord.substring(1).toLowerCase());
                            suggestions.add(sb.toString());
                            suggestions.add(selectWord.toUpperCase());
                            suggestions.add(selectWord.toLowerCase());
                        }

                        JMenu suggestionsMenu = new JMenu("Suggestions");

                        // Add suggestions to the submenu(child menu)
                        for (String suggestion : suggestions) {
                            JMenuItem suggestionItem = new JMenuItem(suggestion);
                            suggestionItem.addActionListener(ae -> {
                                try {
                                    // Replace the current selected word with the suggestion user choose
                                    textPane.getDocument().remove(wordStart, wordEnd - wordStart);
                                    textPane.getDocument().insertString(wordStart, suggestion, null);
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
                            });
                            suggestionsMenu.add(suggestionItem);
                        }

                        // Create the parent(main) menu and add sub menu to main menu
                        JPopupMenu parentMenu = new JPopupMenu();

                        // Create 'Add to Dictionary' menu item
                        JMenuItem addToDicItem = new JMenuItem("Add to Dictionary");
                        addToDicItem.addActionListener(ae -> {
                            // Add the selected word to the user dictionary
                            userDic.add(selectWord);
                        });
/*
Sizr
 */

                        JMenuItem deleteItem = new JMenuItem("Delete");
                        deleteItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent ae) {
                                try {
                                    int offset = textPane.viewToModel2D(e.getPoint());
                                    int wordStart = Utilities.getWordStart(textPane, offset);
                                    int wordEnd = Utilities.getWordEnd(textPane, offset);
                                    if (wordEnd < textPane.getDocument().getLength() && textPane.getText(wordEnd, 1).equals(" ")) {
                                        wordEnd++; // if it has space after, move wordEnd further by 1
                                    } else if (wordStart > 0 && textPane.getText(wordStart - 1, 1).equals(" ")) {
                                        wordStart--; // if no space after but has space before, move wordStart backward by 1
                                    }

                                    textPane.getDocument().remove(wordStart, wordEnd - wordStart);
                                    highlightMisspelledWords(); // Recheck the spelling
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        JMenuItem ignoreAllItem = new JMenuItem("Ignore All");
                        ignoreAllItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent ae) {
                                try {
                                    int offset = textPane.viewToModel2D(e.getPoint());
                                    int wordStart = Utilities.getWordStart(textPane, offset);
                                    int wordEnd = Utilities.getWordEnd(textPane, offset);
                                    String selectedWord = textPane.getDocument().getText(wordStart, wordEnd - wordStart);
                                    ignoredWords.add(selectedWord.toLowerCase());
                                    highlightMisspelledWords(); // Recheck spellings, ignore selected word
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        JMenuItem ignoreOnceItem = new JMenuItem("Ignore Once");
                        ignoreOnceItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent ae) {
                                try {
                                    int offset = textPane.viewToModel2D(e.getPoint());
                                    int wordStart = Utilities.getWordStart(textPane, offset);
                                    int wordEnd = Utilities.getWordEnd(textPane, offset);
                                    String selectedWord = textPane.getDocument().getText(wordStart, wordEnd - wordStart);

                                    Position pos = textPane.getDocument().createPosition(wordStart);
                                    ignoredWordList.add(new IgnoredWord(selectedWord, pos));
//
//
//                                    // Delete selected word first
//                                    textPane.getDocument().remove(wordStart, wordEnd - wordStart);
//
//                                    // Re-insert curr word and apply special style on it
//                                    Style ignoreStyle = textPane.addStyle(IGNORE_ONCE_ATTRIBUTE, null);
//                                    StyleConstants.setForeground(ignoreStyle, Color.darkGray);
//                                    StyleConstants.setUnderline(ignoreStyle, false);
//                                    textPane.getDocument().insertString(wordStart, selectedWord, ignoreStyle);


                                    // Recheck spellings
                                    highlightMisspelledWords();
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        if(hasBlueStyle || hasRedStyle){
                            parentMenu.add(suggestionsMenu);
                            parentMenu.add(addToDicItem);
                        }


                        parentMenu.add(deleteItem);
                        parentMenu.add(ignoreOnceItem);
                        parentMenu.add(ignoreAllItem);

                        // Show the context menu
                        parentMenu.show(textPane, e.getX(), e.getY());
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Highlights misspelled words, capitalization errors, and double words in the text pane.
     */
    private void highlightMisspelledWords() {
        countMisspellings = 0;
        countCapitalizationErrors = 0;
        countDoubleWords = 0;

        // Retrieve the document from the text pane for styling
        StyledDocument doc = textPane.getStyledDocument();
        String text = textPane.getText();



        // reset entire text to normalStyle
        Style normalStyle = textPane.addStyle("NormalStyle", null);
        StyleConstants.setForeground(normalStyle, Color.BLACK);
        StyleConstants.setUnderline(normalStyle, false);
        doc.setCharacterAttributes(0, doc.getLength(), normalStyle, true);


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
        StyleConstants.setUnderline(greenStyle, true);
        StyleConstants.setForeground(greenStyle, Color.GREEN);

//Sizr
        doc.setCharacterAttributes(0, doc.getLength(), normalStyle, true);

        // Index to keep track of the current location in the text
        int idx = 0;
        // Flags to track sentence boundaries
        boolean isNewSentence = true;
        boolean isDiffSentence = true;
        // Variable to remember the previous word for double word detection
        String prev = "";

        // Split the text into words based on non-word characters
        for (String word : text.split("\\W+")) {
            int wordStart = idx;
            int wordEnd = idx + word.length();

            // Check if the word contains only alphabetic characters




            if (word.matches("^[a-zA-Z]+$") && !ignoredWords.contains(word.toLowerCase()) && !isWordIgnored(word, wordStart)) {
                    // Determine if the word is mixed case or improperly capitalized
                    boolean isMixedCase = !word.equals(word.toLowerCase())
                            && !word.equals(word.toUpperCase())
                            && !(Character.isUpperCase(word.charAt(0))
                            && word.substring(1).equals(word.substring(1).toLowerCase()));

                    // Check if the word should be capitalized (start of a new sentence)
                    boolean isStartOfSentenceError = isNewSentence && Character.isLowerCase(word.charAt(0));

                    // Check if the word is spelled correctly. The order of how we highlight the different errors matters.
                    if (!legalDic.hasWord(word.toLowerCase()) && !userDic.hasWord(word)) {
                        int length = word.length();
                        doc.setCharacterAttributes(idx, length, redStyle, false);
                        countMisspellings ++;
                    }

                    // Apply blue underline style for capitalization errors
                    if (isMixedCase || isStartOfSentenceError) {
                        int length = word.length();
                        doc.setCharacterAttributes(idx, length, blueStyle, false);
                        countCapitalizationErrors++;
                    }

                    // Highlight double words in green
                    if (word.equalsIgnoreCase(prev) && !isDiffSentence) {
                        int length = word.length();
                        doc.setCharacterAttributes(idx, length, greenStyle, false);
                        countDoubleWords++;
                    }
                }



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

    /**
     * Makes the frame visible to display the GUI.
     */
    private void display() {
        frame.setVisible(true);
    }

    /**
     * Saves the current text from the JTextPane to a specified file.
     *
     * @param file The file to save the text to.
     */
    private void saveTextToFile(File file) {
        String content = textPane.getText(); // Get the current state of text from JTextPane
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content); // Write current state of text to the file to overwrite(save)
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Fail to save the file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks if a given word at a specific position is in the list of ignored words.
     *
     * @param word The word to check.
     * @param startOffset The starting offset of the word in the document.
     * @return boolean True if the word is ignored, false otherwise.
     */
    private boolean isWordIgnored(String word, int startOffset) {
        for (IgnoredWord ignoredWord : ignoredWordList) {
            if (ignoredWord.getWord().equalsIgnoreCase(word) &&
                    ignoredWord.getPosition().getOffset() == startOffset) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates statistics like the number of characters, lines, and words in the text pane.
     */
    private void calculateStatistics() {
        String text = textPane.getText();
        countLines = calculateVisualLines(textPane); // Count how many lines
        countWords = text.split("\\s+").length;
        String textWithoutNewLines = text.replaceAll("\r\n|\r|\n", "");
        countCharacters = textWithoutNewLines.length(); // Count chars excluding \n
    }

    /**
     * Calculates the number of visual lines in a JTextPane.
     *
     * @param textPane The JTextPane to calculate lines for.
     * @return int The number of visual lines.
     */
    private int calculateVisualLines(JTextPane textPane) {
        View rootView = textPane.getUI().getRootView(textPane).getView(0);
        int totalLines = 0;

        for (int i = 0; i < rootView.getViewCount(); i++) {
            View lineView = rootView.getView(i);
            totalLines += lineView.getViewCount();
        }

        return totalLines;
    }

    /**
     * The main method to run the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TextEditorGUI().display();
        });
    }
}