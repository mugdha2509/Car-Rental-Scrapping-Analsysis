package htmlparser;

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

public class CarRentalParser {

    public static List<CarInfo> fetchAllCarRentalDeals(){
        String website1Path = "CarRentalFiles/orbitz_deals.html";
        File folder = new File(website1Path);
//        if (folder.exists() && folder.isDirectory()) {
//            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".html"));
//            if (files != null) {
//                for (File file : files) {
//                    if (file.isFile()) {
//                        String documentName = file.getName();
//                    }
//                }
//            }
//        }

        // Parse and extract car information from both websites
        return parseCarRentalWebsite(website1Path);
    }

    public static void main(String[] args) {
        // Replace these paths with the actual paths of your car rental HTML files
        String website1Path = "CarRentalFiles";
        File folder = new File(website1Path);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".html"));
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String documentName = file.getName();
                    }
                }
            }
        }

        // Parse and extract car information from both websites
        List<CarInfo> carInfoList1 = parseCarRentalWebsite(website1Path);
    }

    private static List<CarInfo> parseCarRentalWebsite(String filePath) {
        List<CarInfo> carInfoList = new ArrayList<>();

        try {
            // Parse local HTML file
            File input = new File(filePath);
            Document document = Jsoup.parse(input, "UTF-8");

            // Example: Extract car name, price, passenger capacity, etc.
            Elements carElements = document.select("li.offer-card-desktop");


            for (Element carElement : carElements) {
//                Element aElement = carElement.select("a.vehicleinfo");

                String carName = carElement.select("div.uitk-text.uitk-type-300.uitk-text-default-theme.uitk-spacing.uitk-spacing-margin-blockend-one").text().split(" or ")[0]
                        .trim();
                if (carName.contains("Economy Special") || carName.contains("Special")) {
                    continue;
                }
//                System.out.println(carName);
//                double carPrice = Double.parseDouble(carElement.select("p.payamntp").text().replaceAll("[^0-9.]", ""));
                double carPrice = Double.parseDouble(carElement.select("span.total-price").first().text().replaceAll("[^\\d.]", ""));
//                System.out.println(carPrice);
                int passengerCapacity = Integer.parseInt(fetchInt(carElement.select("span.uitk-spacing.text-attribute.uitk-spacing-padding-inlinestart-two.uitk-spacing-padding-inlineend-three").first().text()));
//                System.out.println(passengerCapacity);
                String carGroup = carElement.select("h3.uitk-heading.uitk-heading-5.uitk-spacing.uitk-spacing-padding-inline-three.uitk-layout-grid-item").text();
//                System.out.println(carGroup);
                String transmissionType = carElement.select("span.uitk-spacing.text-attribute.uitk-spacing-padding-inlinestart-two.uitk-spacing-padding-inlineend-three").text().split(" ")[1];


//                System.out.println(transmissionType);
                int largeBag = new Random().nextInt(6) + 1;
                int smallBag = new Random().nextInt(6) + 1;
//                System.out.println(largeBag);
//                System.out.println(smallBag);

                // Create a CarInfo object and add it to the list
                CarInfo carInfo = new CarInfo(carName, carPrice, passengerCapacity, carGroup, transmissionType, largeBag, smallBag, "CarRental");
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

}
