package features;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

// SpellChecking class to perform spell checking using a trie data structure
public class SpellChecking {

    private static final int ALPHABET_SIZE = 26;

    // TrieNode class representing a node in the trie
    private static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        boolean isWordEnding = false;
    }

    private static TrieNode root = new TrieNode();

    // Inserting word into trie
    public static void insertWord(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c >= 'a' && c <= 'z') {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
        }
        node.isWordEnding = true;
    }

    // Method to search for a word in the trie
    public static boolean search(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            if (node.children[index] == null) {
                return false;
            }
            node = node.children[index];
        }
        return node != null && node.isWordEnding;
    }

    // Method to initialize the dictionary from a file
    public static void initializeDictionary(String filePath) throws IOException {
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : line.split("\\W+")) {
                    if (!word.isEmpty()) {
                        insertWord(word.toLowerCase());
                    }
                }
            }
        }
    }

    // Method to check the spelling of a word
    public static boolean checkSpelling(String word) {
        return search(word.toLowerCase());
    }

    // Main method to execute spell checking
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Initialize the dictionary from the specified file
            initializeDictionary("JsonData/filtered_car_deals.json");

            // Take input for the word to check for spelling
            System.out.println("Enter a word to check for spelling:");
            String spelling = scanner.nextLine();

            // Check spelling and print the result
            boolean isSpelledCorrectly = checkSpelling(spelling);
            System.out.println("Is the word spelled correctly? " + isSpelledCorrectly);
        } catch (IOException e) {
            // Handle IO exception
            throw new RuntimeException(e);
        } finally {
            // Close the scanner
            scanner.close();
        }
    }
}
