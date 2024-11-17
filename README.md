# Spell Checker Project

### Overview
This project is a standalone desktop application designed to help users identify and correct spelling errors in text documents. The application features a user-friendly interface, allowing users to manually or automatically correct mistakes with ease.

### Features
- **Text Scanning and Correction**: Scans documents for spelling errors and offers manual and automated correction suggestions.
- **User-Friendly Interface**: Provides an easy-to-use GUI for selecting files, reviewing mistakes, and saving corrections.
- **Dynamic User Dictionary**: Users can add new words to a custom dictionary, making future spell checks more efficient.
- **Multiple Save Options**: Offers flexibility to save changes by overwriting the original file or saving as a new document.

### How to Use
1. Open the Application**: Launch the Spell Checker and select the file you want to check.
2. Review Mistakes**: The system will highlight errors, allowing you to manually edit or apply automatic corrections.
3. Save Changes**: Save the corrected file either by overwriting the original or as a new file.
4. User Dictionary**: Add words to your custom dictionary to ignore them in future checks.
5. Javadoc Documentation**: For the Javadoc comments created for the project, see them in each class's code (test classes don't have Javadoc comments), or browse on a website by clicking index.html in the folder.

## Notes
- The test case in TrieTest for `suggestCorrections` will fail because we cannot construct the tree in Trie.java; it must be constructed in the upper layer (SysDictionary.java). Thus, the test fails because an empty tree cannot provide suggestions.
- If you change the location of words_alpha.txt and userDictionary.txt, you need to re-enter the path; otherwise, the system cannot find them.
- Use sample.txt to test the spell checker. Select it through the "File selector" button in the GUI.

## Main Components
- Word Class**: Handles word properties and identifies error types.
- Trie and TrieNode Classes**: Efficiently stores words for fast search and correction suggestions.
- SystemDictionary**: The main dictionary used for spell checking.
- UserDictionary**: A customizable dictionary allowing users to add their own words.
- User Interface**: Guides users through the process of file selection, spell checking, and saving.
