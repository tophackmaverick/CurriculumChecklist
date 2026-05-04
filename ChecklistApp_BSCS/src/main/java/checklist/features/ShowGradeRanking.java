package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.Comparator;
import java.util.List;

public class ShowGradeRanking {

    private final List<Course> courses;

    public ShowGradeRanking(List<Course> courses) {
        this.courses = courses;
    }

    public void execute() {
        PrintUtil.appHeader("COURSES RANKED BY GRADE");

        List<Course> gradedCourses = courses.stream()
                .filter(this::hasValidGrade)
                .sorted(Comparator.comparingInt(this::getGrade).reversed())
                .toList();

        if (gradedCourses.isEmpty()) {
            System.out.println("\n  No graded courses to display yet.");
            InputUtil.pressEnterToContinue();
            return;
        }

        printHeader();

        int rank = 1;
        int displayedRank = 1;
        int previousGrade = -1;

        for (Course c : gradedCourses) {
            int grade = getGrade(c);

            if (grade != previousGrade) {
                displayedRank = rank;
            }

            printCourse(displayedRank, c, grade);

            previousGrade = grade;
            rank++;
        }

        System.out.println("  " + "-".repeat(80));
        System.out.printf("  Total graded courses listed: %d%n", gradedCourses.size());

        InputUtil.pressEnterToContinue();
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

    private int getGrade(Course c) {
        return Integer.parseInt(c.getGrade());
    }

    private void printHeader() {
        System.out.println();
        System.out.printf("  %-4s  %-14s  %-42s  %6s  %7s%n",
                "Rank", "Course No.", "Title", "Units", "Grade");
        System.out.println("  " + "-".repeat(80));
    }

    private void printCourse(int rank, Course c, int grade) {
        String note = grade < 75 ? "  [FAILED]" : "";

        System.out.printf("  %-4d  %-14s  %-42s  %6.1f  %7d%s%n",
                rank,
                c.getDisplayCourseNo(),
                PrintUtil.trunc(c.getDisplayTitle(), 42),
                c.getUnits(),
                grade,
                note);
    }
}