package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;
import checklist.util.TermGrouper;

import java.util.List;
import java.util.Map;

public class ShowSubjectsWithGrades {

    private final List<Course> courses;

    public ShowSubjectsWithGrades(List<Course> courses) {
        this.courses = courses;
    }

    public void execute() {
        PrintUtil.appHeader("SHOW SUBJECTS WITH GRADES FOR EACH TERM");

        List<Course> curriculumCourses = courses.stream()
                .filter(c -> !c.getTerm().equalsIgnoreCase("Extra"))
                .toList();

        for (Map.Entry<String, List<Course>> entry : TermGrouper.group(curriculumCourses).entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            int year = Integer.parseInt(parts[0]);
            String term = parts[1];

            PrintUtil.termHeader(year, term);
            PrintUtil.printColumnHeaderWithGrade();

            double totalUnits = 0;
            double passedUnits = 0;

            for (Course c : entry.getValue()) {
                printCourse(c);

                totalUnits += c.getUnits();
                if (c.isPassed()) {
                    passedUnits += c.getUnits();
                }
            }

            System.out.printf("%n  %-62s  %6.1f%n", "TOTAL UNITS THIS TERM:", totalUnits);
            System.out.printf("  %-62s  %6.1f%n", "UNITS PASSED:", passedUnits);

            InputUtil.pressEnterToContinue();
        }

        showExtraCourses();
    }

    private void showExtraCourses() {
        List<Course> extraCourses = courses.stream()
                .filter(c -> c.getTerm().equalsIgnoreCase("Extra"))
                .toList();

        if (extraCourses.isEmpty()) {
            return;
        }

        PrintUtil.appHeader("EXTRA / NON-CURRICULUM COURSES");
        PrintUtil.printColumnHeaderWithGrade();

        for (Course c : extraCourses) {
            printCourse(c);
        }

        InputUtil.pressEnterToContinue();
    }

    private void printCourse(Course c) {
        String grade = getGradeDisplay(c);
        String note = getNote(c);

        System.out.printf("  %-14s  %-44s  %6.1f  %-13s%s%n",
                c.getDisplayCourseNo(),
                PrintUtil.trunc(c.getDisplayTitle(), 44),
                c.getUnits(),
                grade,
                note);
    }

    private String getGradeDisplay(Course c) {
        if (!c.isTaken()) {
            return "Not yet taken";
        }

        return c.getGrade();
    }

    private String getNote(Course c) {
        if (c.isTaken() && !c.isPassed()) {
            return "  [FAILED]";
        }

        if (c.isCredited()) {
            return "  [CREDITED from " + c.getCreditedFrom() + "]";
        }

        return "";
    }
}