package checklist.util;

public class PrintUtil {

    public static final String THICK_LINE =
            "================================================================================";

    public static final String THIN_LINE =
            "--------------------------------------------------------------------------------";

    private PrintUtil() {}

    public static void appHeader(String title) {
        System.out.println();
        System.out.println(THICK_LINE);
        System.out.println("  " + title);
        System.out.println(THICK_LINE);
    }

    public static void termHeader(int year, String term) {
        System.out.println();
        System.out.println(THIN_LINE);
        System.out.printf("  Year = %-15s   Term = %s%n", yearLabel(year), term);
        System.out.println(THIN_LINE);
    }

    public static void printColumnHeaderNoGrade() {
        System.out.printf("  %-14s  %-48s  %s%n",
                "Course No.", "Descriptive Title", "Units");
        System.out.println("  " + "-".repeat(70));
    }

    public static void printColumnHeaderWithGrade() {
        System.out.printf("  %-14s  %-44s  %6s  %s%n",
                "Course No.", "Descriptive Title", "Units", "Grade");
        System.out.println("  " + "-".repeat(76));
    }

    public static String yearLabel(int year) {
        return switch (year) {
            case 1 -> "First Year";
            case 2 -> "Second Year";
            case 3 -> "Third Year";
            case 4 -> "Fourth Year";
            default -> year + "th Year";
        };
    }

    public static String trunc(String s, int maxLen) {
        if (s == null || s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 3) + "...";
    }

    public static void blank() {
        System.out.println();
    }
}