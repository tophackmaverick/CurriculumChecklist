package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.List;

public class ShowGPA {

    private final List<Course> courses;

    public ShowGPA(List<Course> courses) {
        this.courses = courses;
    }

    public void execute() {
        PrintUtil.appHeader("GENERAL WEIGHTED AVERAGE (GWA)");

        double weightedTotal = 0;
        double gradedUnits = 0;
        int passed = 0;
        int failed = 0;
        int untaken = 0;

        printHeader();

        for (Course c : courses) {
            if (isExtra(c)) continue;

            if (!hasValidGrade(c)) {
                untaken++;
                continue;
            }

            int grade = Integer.parseInt(c.getGrade());

            printCourse(c, grade);

            weightedTotal += grade * c.getUnits();
            gradedUnits += c.getUnits();

            if (grade >= 75) passed++;
            else failed++;
        }

        System.out.println("  " + "-".repeat(76));

        if (gradedUnits == 0) {
            System.out.println("\n  No graded courses yet. GWA cannot be computed.");
            InputUtil.pressEnterToContinue();
            return;
        }

        double gwa = weightedTotal / gradedUnits;

        printSummary(passed, failed, untaken, gradedUnits, gwa);

        InputUtil.pressEnterToContinue();
    }

    private boolean isExtra(Course c) {
        return c.getTerm().equalsIgnoreCase("Extra");
    }

    private boolean hasValidGrade(Course c) {
        try {
            if (!c.isTaken()) return false;
            int grade = Integer.parseInt(c.getGrade());
            return grade >= 0 && grade <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void printHeader() {
        System.out.println();
        System.out.printf("  %-14s  %-42s  %6s  %7s%n",
                "Course No.", "Title", "Units", "Grade");
        System.out.println("  " + "-".repeat(76));
    }

    private void printCourse(Course c, int grade) {
        String note = grade < 75 ? "  [FAILED]" : "";

        System.out.printf("  %-14s  %-42s  %6.1f  %7d%s%n",
                c.getDisplayCourseNo(),
                PrintUtil.trunc(c.getDisplayTitle(), 42),
                c.getUnits(),
                grade,
                note);
    }

    private void printSummary(int passed, int failed, int untaken,
                              double gradedUnits, double gwa) {
        System.out.printf("%n  Courses passed       : %d%n", passed);
        System.out.printf("  Courses failed       : %d%n", failed);
        System.out.printf("  Not yet taken        : %d%n", untaken);
        System.out.printf("  Total units graded   : %.1f%n", gradedUnits);

        System.out.println();
        System.out.println("  +--------------------------------+");
        System.out.printf("  |  GWA  =  %-6.2f              |%n", gwa);
        System.out.println("  +--------------------------------+");

        String distinction = getDistinction(gwa);
        if (!distinction.isEmpty()) {
            System.out.println("  Distinction: " + distinction);
        }
    }

    private String getDistinction(double gwa) {
        if (gwa >= 95) return "SUMMA CUM LAUDE candidate";
        if (gwa >= 90) return "MAGNA CUM LAUDE candidate";
        if (gwa >= 85) return "CUM LAUDE candidate";
        return "";
    }
}