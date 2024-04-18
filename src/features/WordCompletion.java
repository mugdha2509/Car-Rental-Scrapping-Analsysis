package features;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class TrieNode {
    TrieNode[] children;
    boolean wordEnd;

    public TrieNode() {
        this.children = new TrieNode[128];
        this.wordEnd = false;
    }
}

class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insertNode(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            int index = ch;
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.wordEnd = true;
    }

    public List<String> getSuggestions(String prefix) {
        TrieNode node = findNode(prefix);
        List<String> suggestions = new ArrayList<>();
        if (node != null) {
            getAllWords(node, prefix, suggestions);
        }

        suggestions.sort(Comparator.comparingInt(suggestion -> calculateEditDistance(prefix, suggestion)));

        return suggestions;
    }

    private TrieNode findNode(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            int index = ch;
            if (node.children[index] == null) {
                return null;
            }
            node = node.children[index];
        }
        return node;
    }

    private void getAllWords(TrieNode node, String currentPrefix, List<String> suggestions) {
        if (node.wordEnd) {
            String suggestionWithoutOrSimilar = removeOrSimilar(currentPrefix);
            suggestions.add(suggestionWithoutOrSimilar);
        }

        for (int i = 0; i < node.children.length; i++) {
            if (node.children[i] != null) {
                char ch = (char) i;
                getAllWords(node.children[i], currentPrefix + ch, suggestions);
            }
        }
    }

    private String removeOrSimilar(String suggestion) {
        return suggestion.replace(" or similar", "");
    }

    private int calculateEditDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) {
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(word1.charAt(i - 1), word2.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[word1.length()][word2.length()];
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private int min(int... numbers) {
        return java.util.Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }
}

public class WordCompletion {

    public static Trie trie;

    public static void main(String[] args) {
        trie = new Trie();
        Scanner scanner = new Scanner(System.in);

        initializeDictionaryFromJsonFile("JsonData/filtered_car_deals.json");

        while (true) {
            System.out.print("Enter a prefix (type 'exit' to quit): ");
            String prefix = scanner.nextLine();

            if (prefix.equals("exit")) {
                break;
            }

            List<String> suggestions = getSuggestions(prefix);

            if (!suggestions.isEmpty()) {
                System.out.println("Suggestions:");
                suggestions.forEach(System.out::println);
            } else {
                System.out.println("No suggestions found.");
            }
        }

        scanner.close();
    }

    public static void initializeDictionaryFromJsonFile(String filename) {
        trie = new Trie();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode jsonArray = (ArrayNode) objectMapper.readTree(new File(filename));
            for (int i = 0; i < jsonArray.size(); i++) {
                trie.insertNode(jsonArray.get(i).get("name").asText().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getSuggestions(String prefix) {
        return trie.getSuggestions(prefix);
    }
}
