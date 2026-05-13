package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;
import checklist.util.TermGrouper;

import java.util.List;
import java.util.Map;

public class ShowSubjectsByTerm {

    private final List<Course> courses;

    public ShowSubjectsByTerm(List<Course> courses) {
        this.courses = courses;
    }

    public void execute() {
        PrintUtil.appHeader("SHOW SUBJECTS FOR EACH SCHOOL TERM");

        List<Course> curriculumCourses = courses.stream()
                .filter(c -> !c.getTerm().equalsIgnoreCase("Extra"))
                .toList();

        List<Map.Entry<String, List<Course>>> terms =
                TermGrouper.group(curriculumCourses).entrySet().stream().toList();

        for (int i = 0; i < terms.size(); i++) {
            Map.Entry<String, List<Course>> entry = terms.get(i);

            String[] parts = entry.getKey().split("\\|");
            int year = Integer.parseInt(parts[0]);
            String term = parts[1];

            PrintUtil.termHeader(year, term);
            PrintUtil.printColumnHeaderNoGrade();

            double totalUnits = 0;

            for (Course c : entry.getValue()) {
                printCourse(c);
                totalUnits += c.getUnits();
            }

            System.out.printf("%n  %-64s  %.1f%n", "TOTAL UNITS:", totalUnits);

            if (i < terms.size() - 1) {
                InputUtil.pressEnterToContinue();
            }
        }
    }

    private void printCourse(Course c) {
        String note = c.isCredited()
                ? "  [CREDITED from " + c.getCreditedFrom() + "]"
                : "";

        System.out.printf("  %-14s  %-48s  %.1f%s%n",
                c.getDisplayCourseNo(),
                PrintUtil.trunc(c.getDisplayTitle(), 48),
                c.getUnits(),
                note);
    }
}