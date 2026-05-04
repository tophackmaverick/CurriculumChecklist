package checklist.util;

import java.util.Scanner;

public class InputUtil {

    private static final Scanner SCANNER = new Scanner(System.in);

    private InputUtil() {}

    // -------------------------------------------------------------------------

    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();

            try {
                int value = Integer.parseInt(input);

                if (value < min || value > max) {
                    System.out.printf("  The number must be from %d to %d.%n", min, max);
                } else {
                    return value;
                }

            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter an integer.");
            }
        }
    }

    // -------------------------------------------------------------------------

    public static String readNonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("  Input cannot be blank.");
        }
    }

    // -------------------------------------------------------------------------

    public static String readGrade(String courseNo) {
        while (true) {
            System.out.printf("  Enter grade for %-14s (0 - 100): ", courseNo);
            String input = SCANNER.nextLine().trim();

            try {
                int grade = Integer.parseInt(input);

                if (grade < 0 || grade > 100) {
                    System.out.println("  Grade must be between 0 and 100.");
                } else {
                    return String.valueOf(grade);
                }

            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a number.");
            }
        }
    }

    // -------------------------------------------------------------------------

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    // -------------------------------------------------------------------------

    public static void pressEnterToContinue() {
        System.out.print("\n  Press ENTER to continue...");

        while (true) {
            String input = SCANNER.nextLine();
            if (input.isEmpty()) return;
        }
    }
}