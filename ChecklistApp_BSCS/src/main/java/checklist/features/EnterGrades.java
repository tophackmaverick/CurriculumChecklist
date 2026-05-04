package checklist.features;

import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.util.List;

public class EnterGrades {

    private final List<Course> courses;

    public EnterGrades(List<Course> courses) {
        this.courses = courses;
    }

    public void execute() {
        PrintUtil.appHeader("ENTER GRADES");

        System.out.println("  [1] Curriculum");
        System.out.println("  [2] Credited");
        System.out.println("  [3] Extra");
        System.out.println("  [0] Back");

        switch (InputUtil.readIntInRange("\n  Choice: ", 0, 3)) {
            case 1 -> curriculum();
            case 2 -> credited();
            case 3 -> extra();
        }
    }

    // -------------------------------------------------------------------------

    private void curriculum() {
        List<Course> list = courses.stream()
                .filter(c -> !c.isTaken() && !isExtra(c))
                .toList();

        if (list.isEmpty()) {
            System.out.println("\n  All courses already graded.");
            InputUtil.pressEnterToContinue();
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            Course c = list.get(i);
            System.out.printf("  [%d] %s (%s)%n",
                    i + 1,
                    c.getDisplayCourseNo(),
                    PrintUtil.trunc(c.getDisplayTitle(), 40));
        }

        int idx = InputUtil.readIntInRange("\n  Select (0 cancel): ", 0, list.size());
        if (idx == 0) return;

        Course c = list.get(idx - 1);

        if (c.isElective() && c.getActualCourseNo().isBlank()) {
            c.setActualCourseNo(InputUtil.readNonBlank("  Actual no: "));
            c.setActualTitle(InputUtil.readNonBlank("  Actual title: "));
        }

        saveGrade(c);
    }

    private void credited() {
        String no = InputUtil.readNonBlank("\n  Course no: ");
        Course c = find(no);

        String from = InputUtil.readNonBlank("  Credited from: ");
        String grade = InputUtil.readGrade(no);

        if (c != null) {
            c.setGrade(grade);
            c.setCredited(true);
            c.setCreditedFrom(from);
        } else {
            Course extra = new Course(
                    no,
                    InputUtil.readNonBlank("  Title: "),
                    parseUnits(InputUtil.readNonBlank("  Units: ")),
                    "",
                    0,
                    "Extra"
            );
            extra.setGrade(grade);
            extra.setCredited(true);
            extra.setCreditedFrom(from);
            courses.add(extra);
        }

        System.out.println("\n  Credited course saved.");
        InputUtil.pressEnterToContinue();
    }

    private void extra() {
        String no = InputUtil.readNonBlank("\n  Course no: ");
        String title = InputUtil.readNonBlank("  Title: ");
        double units = parseUnits(InputUtil.readNonBlank("  Units: "));
        String grade = InputUtil.readGrade(no);

        Course c = new Course(no, title, units, "", 0, "Extra");
        c.setGrade(grade);
        courses.add(c);

        System.out.println("\n  Extra course added.");
        InputUtil.pressEnterToContinue();
    }

    // -------------------------------------------------------------------------

    private void saveGrade(Course c) {
        String grade = InputUtil.readGrade(c.getDisplayCourseNo());
        c.setGrade(grade);

        int g = Integer.parseInt(grade);
        System.out.println(g < 75 ? "\n  [WARNING] Failing grade." : "\n  Grade saved.");

        InputUtil.pressEnterToContinue();
    }

    private boolean isExtra(Course c) {
        return c.getTerm().equalsIgnoreCase("Extra");
    }

    private Course find(String no) {
        return courses.stream()
                .filter(c -> c.getCourseNo().equalsIgnoreCase(no))
                .findFirst()
                .orElse(null);
    }

    private double parseUnits(String raw) {
        try {
            return Double.parseDouble(raw);
        } catch (Exception e) {
            return 3.0;
        }
    }
}