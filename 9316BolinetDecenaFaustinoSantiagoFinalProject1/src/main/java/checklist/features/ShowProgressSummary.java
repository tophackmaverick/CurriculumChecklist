package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.List;

public class ShowProgressSummary {

    private final List<Course> courses;
    private static final int BAR_WIDTH = 40;

    public ShowProgressSummary(List<Course> courses) {
        this.courses = courses;
    }

    public void execute() {
        PrintUtil.appHeader("CURRICULUM PROGRESS SUMMARY");

        double totalUnits = 0;
        double passedUnits = 0;
        double failedUnits = 0;
        int passedCount = 0;
        int failedCount = 0;
        int untakenCount = 0;

        for (Course c : courses) {
            if (isExtra(c)) continue;

            totalUnits += c.getUnits();

            if (!c.isTaken()) {
                untakenCount++;
            } else if (c.isPassed()) {
                passedUnits += c.getUnits();
                passedCount++;
            } else {
                failedUnits += c.getUnits();
                failedCount++;
            }
        }

        double remainingUnits = totalUnits - passedUnits;
        double percent = totalUnits == 0 ? 0 : (passedUnits / totalUnits) * 100;

        printStats(totalUnits, passedUnits, failedUnits, remainingUnits,
                passedCount, failedCount, untakenCount);

        printProgressBar(percent);
        showFailedCourses();
        showRemainingCourses();

        InputUtil.pressEnterToContinue();
    }

    private boolean isExtra(Course c) {
        return c.getTerm().equalsIgnoreCase("Extra");
    }

    private void printStats(double total, double passed, double failed, double remaining,
                            int passedCount, int failedCount, int untakenCount) {

        System.out.println();
        System.out.printf("  %-36s  %8.1f units%n", "Total curriculum units:", total);
        System.out.printf("  %-36s  %8.1f units%n", "Units earned (passed):", passed);
        System.out.printf("  %-36s  %8.1f units%n", "Units failed:", failed);
        System.out.printf("  %-36s  %8.1f units%n", "Units remaining:", remaining);
        System.out.println();
        System.out.printf("  %-36s  %8d%n", "Courses passed:", passedCount);
        System.out.printf("  %-36s  %8d%n", "Courses failed:", failedCount);
        System.out.printf("  %-36s  %8d%n", "Courses not yet taken:", untakenCount);
    }

    private void printProgressBar(double percent) {
        int filled = (int) Math.round((percent / 100.0) * BAR_WIDTH);
        String bar = "#".repeat(filled) + "-".repeat(BAR_WIDTH - filled);

        System.out.printf("%n  Progress: [%s] %.1f%%%n", bar, percent);
    }

    private void showFailedCourses() {
        List<Course> failed = courses.stream()
                .filter(c -> c.isTaken() && !c.isPassed() && !isExtra(c))
                .toList();

        if (failed.isEmpty()) return;

        System.out.println();
        System.out.println("  [!] COURSES WITH FAILING GRADES:");
        System.out.println("  " + "-".repeat(68));

        for (Course c : failed) {
            System.out.printf("      %-14s  %-42s  Grade: %s%n",
                    c.getDisplayCourseNo(),
                    PrintUtil.trunc(c.getDisplayTitle(), 42),
                    c.getGrade());
        }
    }

    private void showRemainingCourses() {
        List<Course> remaining = courses.stream()
                .filter(c -> !c.isTaken() && !isExtra(c))
                .toList();

        if (remaining.isEmpty()) return;

        System.out.println();
        System.out.printf("  Remaining courses (%d):%n", remaining.size());
        System.out.println("  " + "-".repeat(68));

        for (Course c : remaining) {
            System.out.printf("      %-14s  %s  (%.1f units)%n",
                    c.getCourseNo(),
                    PrintUtil.trunc(c.getDescriptiveTitle(), 42),
                    c.getUnits());
        }
    }
}