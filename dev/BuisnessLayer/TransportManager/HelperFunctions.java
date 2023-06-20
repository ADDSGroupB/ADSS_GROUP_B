package BuisnessLayer.TransportManager;

public class HelperFunctions
{
    public static boolean check_int_in_range(int val, int start, int end){
        /**
         Check if an integer is within a given range.

         Args:
         val (int): The value to check.
         start (int): The start of the range.
         end (int): The end of the range.

         Returns:
         bool: True if the value is within the range, False otherwise.
         */
        return val >= start && val <= end;
    }

    public boolean check_int_in_length(int real_length, int wanted_length) {
        /**
         Check if the length of an integer is the same as a given length.

         Args:
         real_length (int): The length of the integer to check.
         wanted_length (int): The desired length.

         Returns:
         bool: True if the length is equal to the desired length, False otherwise.
         */
        return real_length==wanted_length;
    }

    public boolean check_string_contains_only_letters (String str) {
        /**
         Check if a string contains only letters.

         Args:
         str (str): The string to check.

         Returns:
         bool: True if the string contains only letters, False otherwise.
         */
        return str.matches("[a-zA-Z]+");
    }

    public boolean check_string_contains_only_numbers (String input) {
        /**
         Check if a string contains only numbers.

         Args:
         input (str): The string to check.

         Returns:
         bool: True if the string contains only numbers, False otherwise.
         */


        if (input == null || input.isEmpty()) {
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static String capitalize_first_letter(String str) {
        /**
         Capitalize the first letter of each word in a string.

         Args:
         str (str): The string to capitalize.

         Returns:
         str: The capitalized string.
         */
        StringBuilder result = new StringBuilder();
        String[] words = str.split(" ");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1));
                }
                result.append(" ");
            }
        }
        return result.toString().trim();
    }

}
