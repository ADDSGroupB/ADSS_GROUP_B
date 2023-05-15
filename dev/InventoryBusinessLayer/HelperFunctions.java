package InventoryBusinessLayer;

import java.util.Scanner;

public class HelperFunctions {
    public static int positiveItegerInsertionWithCancel() {
        Scanner scanner = new Scanner(System.in);
        int number = -2;
        while (number <= 0) {
            try {
                number = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer");
                scanner.nextLine(); // clear input buffer
                continue;
            }
            if (number == -1){
                break;
            }
            if (number <= 0) {
                System.out.println("Invalid input (input <= 0), please try again");
            }
        }
        return number;
    }
    public static int positiveItegerInsertion() {
        Scanner scanner = new Scanner(System.in);
        int number = -2;
        while (number <= 0) {
            try {
                number = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter an integer");
                scanner.nextLine(); // clear input buffer
                continue;
            }
            if (number <= 0) {
                System.out.println("Invalid input (input <= 0), please try again");
            }
        }
        return number;
    }
    public static double positiveDoubleInsertion() {
        Scanner scanner = new Scanner(System.in);
        double number = -2.0;
        while (number <=  0.0) {
            try {
                number = scanner.nextDouble();
            } catch (Exception e) {
                System.out.println("Please enter a valid decimal number");
                scanner.nextLine(); // clear input buffer
                continue;
            }
            if (number <= 0.0) {
                System.out.println("Invalid input (input <= 0), please try again");
            }
        }
        return number;
    }
}
