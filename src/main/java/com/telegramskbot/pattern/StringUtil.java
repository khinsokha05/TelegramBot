package com.telegramskbot.pattern;
import java.util.Random;

public class StringUtil {

    // Generate a random 6-digit number as a String
    public static String generateRandomSixDigitNumber() {
        Random random = new Random();
        // Generate a random 6-digit number between 100000 and 999999
        int number = random.nextInt(900000) + 100000;
        return String.valueOf(number); // Convert to String
    }

    // Reverse the input string
    public static String reverseString(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder reversed = new StringBuilder(input);
        return reversed.reverse().toString();
    }

    // Check if a string is empty or null
    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }

    // Capitalize the first letter of the string
    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    // Generate a random string of a specified length with alphanumeric characters
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }
        return randomString.toString();
    }

    // Main method to test the utility functions
    public static void main(String[] args) {
        // Testing generateRandomSixDigitNumber
        System.out.println("Random 6-digit number: " + generateRandomSixDigitNumber());

        // Testing reverseString
        String input = "hello";
        System.out.println("Reversed: " + reverseString(input));

        // Testing isEmpty
        System.out.println("Is empty: " + isEmpty(""));

        // Testing capitalize
        System.out.println("Capitalized: " + capitalize("hello"));

        // Testing generateRandomString
        System.out.println("Random String: " + generateRandomString(8)); // Generates an 8-character random string
    }
}
