import javax.swing.*;
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
    private JButton OpenButton, CheckButton, ExitButton, SaveButton;
    private JScrollPane scrollPane;
    private JFileChooser fileChooser;
    private SysDictionary legalDic;
    private UserDictionary userDic = new UserDictionary();
    private File currentFile;
    private List<String> ignoredWords = new ArrayList<>();

    private static final String IGNORE_ONCE_ATTRIBUTE = "ignoreOnce";

    private List<IgnoredWord> ignoredWordList = new ArrayList<>();




    public TextEditorGUI() {

        /*
        Sizr
         */
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(OpenButton);
        buttonPanel.add(CheckButton);
        buttonPanel.add(ExitButton);
        buttonPanel.add(SaveButton);

        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);



        fileChooser = new JFileChooser();
        setupContextMenu();
        OpenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(frame);
                //我们在这里把arraylist重新进行更新
                ignoredWords = new ArrayList<>();
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    Path selectedFile = fileChooser.getSelectedFile().toPath();
                    currentFile = fileChooser.getSelectedFile();
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

        SaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTextToFile(currentFile);
            }
        });
    }

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
                        parentMenu.add(suggestionsMenu);

                        // Create 'Add to Dictionary' menu item
                        JMenuItem addToDicItem = new JMenuItem("Add to Dictionary");
                        addToDicItem.addActionListener(ae -> {
                            // Add the selected word to the user dictionary
                            userDic.add(selectWord);
                        });
                        parentMenu.add(addToDicItem);
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
                                        wordEnd++; // 如果后面有空格，则将结束位置向后移动一个字符
                                    } else if (wordStart > 0 && textPane.getText(wordStart - 1, 1).equals(" ")) {
                                        wordStart--; // 如果后面没有空格，但前面有空格，则将开始位置向前移动一个字符
                                    }

                                    textPane.getDocument().remove(wordStart, wordEnd - wordStart);
                                    highlightMisspelledWords(); // 重新进行拼写检查
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        parentMenu.add(deleteItem);

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
                                    highlightMisspelledWords(); // 重新进行拼写检查，忽略选中的单词
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        parentMenu.add(ignoreAllItem);

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
//                                    // 先删除选中的单词
//                                    textPane.getDocument().remove(wordStart, wordEnd - wordStart);
//
//                                    // 重新插入该单词，并应用特殊样式
//                                    Style ignoreStyle = textPane.addStyle(IGNORE_ONCE_ATTRIBUTE, null);
//                                    StyleConstants.setForeground(ignoreStyle, Color.darkGray);
//                                    StyleConstants.setUnderline(ignoreStyle, false);
//                                    textPane.getDocument().insertString(wordStart, selectedWord, ignoreStyle);


                                    // 重新进行拼写检查
                                    highlightMisspelledWords();
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        parentMenu.add(ignoreOnceItem);


/*
Sizr
 */
                        // Show the context menu
                        parentMenu.show(textPane, e.getX(), e.getY());
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void highlightMisspelledWords() {
        // Retrieve the document from the text pane for styling
        StyledDocument doc = textPane.getStyledDocument();
        String text = textPane.getText();



        // 重置整个文档的样式为 normalStyle
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
                    }

                    // Apply blue underline style for capitalization errors
                    if (isMixedCase || isStartOfSentenceError) {
                        int length = word.length();
                        doc.setCharacterAttributes(idx, length, blueStyle, false);
                    }

                    // Highlight double words in green
                    if (word.equalsIgnoreCase(prev) && !isDiffSentence) {
                        int length = word.length();
                        doc.setCharacterAttributes(idx, length, greenStyle, false);
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

    private void display() {
        frame.setVisible(true);
    }

    private void saveTextToFile(File file) {
        String content = textPane.getText(); // Get the current state of text from JTextPane
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content); // Write current state of text to the file to overwrite(save)
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Fail to save the file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isWordIgnored(String word, int startOffset) {
        for (IgnoredWord ignoredWord : ignoredWordList) {
            if (ignoredWord.getWord().equalsIgnoreCase(word) &&
                    ignoredWord.getPosition().getOffset() == startOffset) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TextEditorGUI().display();
        });
    }
}