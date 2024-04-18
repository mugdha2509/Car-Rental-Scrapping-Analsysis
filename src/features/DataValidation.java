package features;

import java.util.Scanner;
import java.util.regex.Pattern;

// DataValidation class to perform input validation for car rental information
public class DataValidation {

    private static final Pattern SINGLE_LETTER_PATTERN = Pattern.compile("^[AM]$");

    // Regular expressions for validating car name, date, time, and city name
    static Pattern PTRN_FOR_CAR_NAME = Pattern.compile("^[a-zA-Z0-9\\s]+$");
    private static final Pattern RANGE_PATTERN = Pattern.compile("^\\d+-\\d+$");
    static Pattern PTRN_FOR_DATE = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$"); // date format update
    static Pattern PTRN_FOR_TIME = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9] (AM|PM)$");
    static Pattern PTRN_NAME_FOR_CITY = Pattern.compile("^[a-zA-Z\\s]+$");
    static Pattern PTRN_FOR_INTEGER = Pattern.compile("^-?\\d+$");

    // Method to validate car name
    public static boolean validateCarName(String nameOfCar) {
        boolean isValid = PTRN_FOR_CAR_NAME.matcher(nameOfCar).matches();
        if (!isValid) {
            System.out.println("Invalid car name! Please enter a proper car name. Please try again.\n");
        }
        return isValid;
    }

    public static boolean validateInteger(int userResponse) {
        String responseString = String.valueOf(userResponse);
        boolean isValid = PTRN_FOR_INTEGER.matcher(responseString).matches();

        if (!isValid) {
            System.out.println("Not a valid response. Please try again.");
        }

        return isValid;
    }

    // Method to validate date
    public static boolean validateDate(String date) {
        boolean isValid = PTRN_FOR_DATE.matcher(date).matches();
        if (!isValid) {
            System.out.println("Invalid date format. Use the format DD/MM/YYYY and try again.");
        }
        return isValid;
    }

    // Method to validate time
    public static boolean validateTime(String time) {
        boolean isValid = PTRN_FOR_TIME.matcher(time).matches();
        if (!isValid) {
            System.out.println("Invalid time format. Please use the format HH:MM. Please try again.");
        }
        return isValid;
    }

    // Method to validate city name
    public static boolean validateCityName(String cityName) {
        boolean isValid = PTRN_NAME_FOR_CITY.matcher(cityName).matches();
        if (!isValid) {
            System.out.println("Invalid input. Please use letters only. Please try again.");
        }
        return isValid;
    }

    // Method to validate user response (used in potential future interactions)
    public static boolean validateUserResponse(String input) {
        if (input.length() == 1 && (input.charAt(0) == 'y' || input.charAt(0) == 'n')) {
            return true;
        } else {
            System.out.print("Invalid input. Please enter 'y' or 'n'\n");
            return false;
        }
    }

    public static boolean validateRangeInput(String input) {
        boolean isValid = RANGE_PATTERN.matcher(input).matches();

        if (!isValid) {
            System.out.println("Invalid range. Please try again.");
        }
        return isValid;
    }

    public static boolean validateTransmissionTypes(String preferredTransmission) {
        boolean isValid = SINGLE_LETTER_PATTERN.matcher(preferredTransmission).matches();
        if (!isValid) {
            System.out.println("Invalid selected type. Please try again.");
        }
        return isValid;
    }
}
