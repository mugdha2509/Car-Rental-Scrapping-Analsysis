package features;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FrequencyCount {

    public static void main(String[] args) {
        String filePath = "JsonData/filtered_car_deals.json";

        try {
            // Read the JSON file and get the frequency count
            Map<String, Integer> frequencyMap = getFrequencyCount(filePath);

            // Print the frequency count
            for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
                System.out.println("Name: " + entry.getKey() + ", Frequency: " + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> getFrequencyCount(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);

        // Read the JSON file into a JsonNode
        JsonNode jsonNode = objectMapper.readTree(file);

        Map<String, Integer> frequencyMap = new HashMap<>();

        // Iterate through the array in the JSON file
        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            JsonNode element = elements.next();
            String nameOfCar = element.get("name").asText();

            // Update the frequency based on the occurrence of the car's appearance on the site
            frequencyMap.put(nameOfCar, frequencyMap.getOrDefault(nameOfCar, 0) + 1);
        }

        return frequencyMap;
    }
}
