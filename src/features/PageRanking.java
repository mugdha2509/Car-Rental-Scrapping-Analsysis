package features;

import webcrawling.AvisCanadaCrawl;
import webcrawling.BudgetCanadaCrawl;
import webcrawling.CarRentalWebCrawl;

import java.util.*;

public class PageRanking {
    private Map<String, Integer> pageScores;
    private PriorityQueue<Map.Entry<String, Integer>> priorityQueue;

    public PageRanking() {
        pageScores = new HashMap<>();
        // Initialize priority queue with a comparator for sorting
        priorityQueue = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
    }

    public void calculatePageRank(Map<String, Integer> documentFrequencies) {
        if (documentFrequencies != null) {
            // Simple ranking based on frequency (replace with more advanced ranking)
            pageScores.putAll(documentFrequencies);

            // Populate the priority queue for ranking
            priorityQueue.addAll(pageScores.entrySet());
        } else {
            System.out.println("Error: Document frequencies are null");
        }
    }

    public List<Map.Entry<String, Integer>> getRankedPages() {
        // Get and return the ranked pages
        List<Map.Entry<String, Integer>> rankedPages = new ArrayList<>();

        while (!priorityQueue.isEmpty()) {
            rankedPages.add(priorityQueue.poll());
        }

        return rankedPages;
    }

    public static void main(String[] args) {
        BTree bTree = InvertedIndexing.indexDocumentsInFolder(new String[]{"AvisFiles", "BudgetFiles", "OrbitzFiles"});

        Map<String, Integer> documentFrequencies = bTree.search("kia");

        // Create a PageRank object
        PageRanking pageRank = new PageRanking();

        // Calculate PageRank based on the document frequencies
        pageRank.calculatePageRank(documentFrequencies);

        // Get and display ranked pages
        List<Map.Entry<String, Integer>> rankedPages = pageRank.getRankedPages();
        for (Map.Entry<String, Integer> entry : rankedPages) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void showRanking(String keyword) {
        BTree bTree = InvertedIndexing.indexDocumentsInFolder(new String[]{"AvisFiles/", "BudgetFiles/", "CarRentalFiles/"});

        Map<String, Integer> documentFrequencies = bTree.search(keyword);

        ensureWebsiteExistence(documentFrequencies, "avis_deals.html");
        ensureWebsiteExistence(documentFrequencies, "budget_deals.html");
        ensureWebsiteExistence(documentFrequencies, "orbitz_deals.html");

        // Create a PageRank object
        PageRanking pageRank = new PageRanking();

        // Calculate PageRank based on the document frequencies
        pageRank.calculatePageRank(documentFrequencies);

        System.out.println("Ranking of website for the selected Car Model:\n");
        // Get and display ranked pages
        List<Map.Entry<String, Integer>> rankedPages = pageRank.getRankedPages();
        int count = 1;
        for (Map.Entry<String, Integer> entry : rankedPages) {
            String website = getWebsiteForEntry(entry.getKey());
            System.out.println(count + ". " + website);
            count++;
        }
    }

    private static void ensureWebsiteExistence(Map<String, Integer> documentFrequencies, String website) {
        if (!documentFrequencies.containsKey(website)) {
            documentFrequencies.put(website, 0);
        }
    }

    private static String getWebsiteForEntry(String entryKey) {
        if (entryKey.contains("avis")) {
            return AvisCanadaCrawl.avisUrl;
        } else if (entryKey.contains("budget")) {
            return BudgetCanadaCrawl.budgetUrl;
        } else if (entryKey.contains("orbitz")) {
            return CarRentalWebCrawl.orbitzUrl;
        } else {
            return entryKey;
        }
    }
}
