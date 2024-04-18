package htmlparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.CarInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AvisBudgetParser {

    public static void main(String[] args) {

        List<CarInfo> combinedCarInfoList = new ArrayList<>();

        String[] folderPaths = {"AvisFiles/", "BudgetFiles/", "CarRentalFiles/"};

        for (String folderPath : folderPaths) {
            // Create a File object for the folder
            File folder = new File(folderPath);

            // Check if the path is a directory
            if (folder.isDirectory()) {
                // List all files in the directory
                File[] files = folder.listFiles();

                if (files != null) {
                    for (File file : files) {
//                        System.out.println(file.getName());
                        combinedCarInfoList.addAll(parseCarRentalWebsite(file.getAbsolutePath()));
                    }
                } else {
                    System.out.println("The folder is empty.");
                }
            } else {
                System.out.println("The specified path is not a directory.");
            }
        }
        saveCarInfoToJson(combinedCarInfoList, "filtered_car_deals");

    }

    public static List<CarInfo> parseFiles() {
        List<CarInfo> combinedCarInfoList = new ArrayList<>();

        String[] folderPaths = {"AvisFiles/", "BudgetFiles/"};

        for (String folderPath : folderPaths) {
            // Create a File object for the folder
            File folder = new File(folderPath);

            // Check if the path is a directory
            if (folder.isDirectory()) {
                // List all files in the directory
                File[] files = folder.listFiles();

                if (files != null) {
                    for (File file : files) {
//                        System.out.println(file.getName());
                        combinedCarInfoList.addAll(parseCarRentalWebsite(file.getAbsolutePath()));
//                        combinedCarInfoList.addAll(CarRentalParser.fetchAllCarRentalDeals());
                    }
                } else {
                    System.out.println("The folder is empty.");
                }
            } else {
                System.out.println("The specified path is not a directory.");
            }
        }
//        saveCarInfoToJson(combinedCarInfoList, "filtered_car_deals");
        return combinedCarInfoList;
    }

    private static List<CarInfo> parseCarRentalWebsite(String filePath) {
        List<CarInfo> carInfoList = new ArrayList<>();

        String rentalCompany = "";
        // Parse local HTML file
        if (filePath.toLowerCase().contains("avis")) {
            rentalCompany = "avis";
        } else if (filePath.toLowerCase().contains("budget")) {
            rentalCompany = "budget";
        }
        try {
            // Parse local HTML file
            File input = new File(filePath);
            Document document = Jsoup.parse(input, "UTF-8");

            // Example: Extract car name, price, passenger capacity, etc.
            Elements carElements = document.select(".step2dtl-avilablecar-section");


            for (Element carElement : carElements) {
//                Element aElement = carElement.select("a.vehicleinfo");

                String carName = carElement.select("p.featurecartxt.similar-car").text().split(" or")[0];
                if (carName.contains("Mystery Car")) {
                    continue;
                }
//                System.out.println(carName);
                double carPrice = Double.parseDouble(carElement.select("p.payamntp").text().replaceAll("[^0-9.]", ""));
//                System.out.println(carElement.select("span.four-seats-feat").text());
                int passengerCapacity = Integer.parseInt(fetchInt(carElement.select("span.four-seats-feat").first().text()));
                String carGroup = carElement.select("h3[ng-bind='car.carGroup']").text();
                String transmissionType = carElement.select("span.four-automatic-feat").text();
                int largeBag = Integer.parseInt(fetchInt(carElement.select("span.four-bags-feat").first().text()));
                int smallBag = Integer.parseInt(fetchInt(carElement.select("span.four-bags-feat-small").first().text()));


                // Create a CarInfo object and add it to the list
                CarInfo carInfo = new CarInfo(carName, carPrice, passengerCapacity, carGroup, transmissionType, largeBag, smallBag, rentalCompany);
                carInfoList.add(carInfo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return carInfoList;
    }

    public static String fetchInt(String string) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return string;
    }


    private static void saveCarInfoToJson(List<CarInfo> carInfoList, String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        String directoryPath = "JsonData/";

        try {
            File directory = new File(directoryPath);

            // Create the directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create the file in the specified directory with the provided filename
            File file = new File(directory, filename + ".json");

            System.out.println(carInfoList);
            // Write carInfoList to JSON file
            try {
                objectMapper.writeValue(file, carInfoList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//            System.out.println("Filtered car deals saved to: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}