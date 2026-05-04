package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.List;

public class EditCourse {

    private final List<Course> courses;

    public EditCourse(List<Course> courses) {
        this.courses = courses;
    }

    public void execute() {
        PrintUtil.appHeader("EDIT A COURSE");

        String input = InputUtil.readNonBlank("  Course No. (0 cancel): ");
        if (input.equals("0")) return;

        Course c = find(input);

        if (c == null) {
            System.out.println("\n  Course not found.");
            InputUtil.pressEnterToContinue();
            return;
        }

        showDetails(c);

        System.out.println("\n  [1] Grade  [2] Title  [3] Units  [4] Credited");
        System.out.println("  [5] Actual No.  [6] Actual Title  [0] Cancel");

        switch (InputUtil.readIntInRange("  Choice: ", 0, 6)) {
            case 1 -> editGrade(c);
            case 2 -> c.setDescriptiveTitle(InputUtil.readNonBlank("  New title: "));
            case 3 -> editUnits(c);
            case 4 -> editCredited(c);
            case 5 -> setOrClear(v -> c.setActualCourseNo(v), () -> c.setActualCourseNo(""), "actual no");
            case 6 -> setOrClear(v -> c.setActualTitle(v), () -> c.setActualTitle(""), "actual title");
        }

        InputUtil.pressEnterToContinue();
    }

    private void editGrade(Course c) {
        String raw = InputUtil.readNonBlank("  Grade (0-100 or clear): ");

        if (raw.equalsIgnoreCase("clear")) {
            c.setGrade(null);
            System.out.println("  Grade cleared.");
            return;
        }

        try {
            int g = Integer.parseInt(raw);
            if (g < 0 || g > 100) {
                System.out.println("  Invalid range.");
                return;
            }

            c.setGrade(String.valueOf(g));
            System.out.println(g < 75 ? "  [WARNING] Failing grade." : "  Grade updated.");

        } catch (Exception e) {
            System.out.println("  Invalid input.");
        }
    }

    private void editUnits(Course c) {
        try {
            c.setUnits(Double.parseDouble(InputUtil.readNonBlank("  New units: ")));
            System.out.println("  Units updated.");
        } catch (Exception e) {
            System.out.println("  Invalid number.");
        }
    }

    private void editCredited(Course c) {
        if (c.isCredited()) {
            System.out.println("  [1] Change  [2] Remove  [0] Cancel");
            int sub = InputUtil.readIntInRange("  Choice: ", 0, 2);

            if (sub == 1) {
                c.setCreditedFrom(InputUtil.readNonBlank("  New source: "));
            } else if (sub == 2) {
                c.setCredited(false);
                c.setCreditedFrom("");
            }
        } else {
            String from = InputUtil.readNonBlank("  Credited from (0 cancel): ");
            if (!from.equals("0")) {
                c.setCredited(true);
                c.setCreditedFrom(from);
            }
        }
    }

    // reusable helper
    private void setOrClear(java.util.function.Consumer<String> set,
                            Runnable clear, String label) {
        String val = InputUtil.readNonBlank("  New " + label + " (or clear): ");
        if (val.equalsIgnoreCase("clear")) clear.run();
        else set.accept(val);
    }

    private void showDetails(Course c) {
        System.out.println("\n  " + PrintUtil.THIN_LINE);
        System.out.println("  Course: " + c.getCourseNo());
        System.out.println("  Title : " + c.getDescriptiveTitle());
        System.out.println("  Units : " + c.getUnits());
        System.out.println("  Term  : " + c.getTerm());
        System.out.println("  Grade : " + (c.getGrade() == null ? "Not yet taken" : c.getGrade()));
        System.out.println("  " + PrintUtil.THIN_LINE);
    }

    private Course find(String no) {
        return courses.stream()
                .filter(c -> c.getCourseNo().equalsIgnoreCase(no)
                        || c.getActualCourseNo().equalsIgnoreCase(no))
                .findFirst()
                .orElse(null);
    }
}