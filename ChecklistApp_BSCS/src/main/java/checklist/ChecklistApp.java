package checklist;

import checklist.data.CurriculumLoader;
import checklist.data.StudentRecordManager;
import checklist.features.*;
import checklist.model.Course;
import checklist.util.InputUtil;
import checklist.util.PrintUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ChecklistApp {

    private static final String DATA_DIR = resolveDataDir();
    private static final String CURRICULUM_FILE = DATA_DIR + "bscs_curriculum.txt";
    private static final String RECORD_FILE = DATA_DIR + "student_record.txt";

    private List<Course> courses;
    private final StudentRecordManager recordManager;

    public ChecklistApp() {
        recordManager = new StudentRecordManager(RECORD_FILE);
    }

    private static String resolveDataDir() {
        String[] possiblePaths = {
                "data",
                "ChecklistApp/data",
                "src/main/java/checklist/data"
        };

        for (String path : possiblePaths) {
            File folder = new File(path);
            if (folder.isDirectory()) {
                return folder.getPath() + File.separator;
            }
        }

        return "data" + File.separator;
    }

    private void init() throws IOException {
        if (recordManager.recordExists()) {
            courses = recordManager.load();
            System.out.println("  [INFO] Student record loaded from: " + RECORD_FILE);
        } else {
            CurriculumLoader loader = new CurriculumLoader(CURRICULUM_FILE);
            courses = loader.load();
            System.out.println("  [INFO] Curriculum loaded from: " + CURRICULUM_FILE);
            System.out.println("  [INFO] No saved record found. Starting fresh.");
        }

        System.out.printf("  [INFO] %d courses ready.%n", courses.size());
    }

    private void run() {
        boolean running = true;

        while (running) {
            printMenu();

            int choice = InputUtil.readIntInRange(
                    "  Enter a number corresponding to your choice: ", 1, 8);

            switch (choice) {
                case 1 -> new ShowSubjectsByTerm(courses).execute();
                case 2 -> new ShowSubjectsWithGrades(courses).execute();
                case 3 -> {
                    new EnterGrades(courses).execute();
                    autosave();
                }
                case 4 -> {
                    new EditCourse(courses).execute();
                    autosave();
                }
                case 5 -> new ShowGPA(courses).execute();
                case 6 -> new ShowGradeRanking(courses).execute();
                case 7 -> new ShowProgressSummary(courses).execute();
                case 8 -> {
                    autosave();
                    System.out.println("\n  ..Thank you. Goodbye!\n");
                    running = false;
                }
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println(PrintUtil.THICK_LINE);
        System.out.println("  My Checklist Monitoring Application");
        System.out.println("  BSCS — Saint Louis University  A.Y. 2018-2019");
        System.out.println(PrintUtil.THICK_LINE);
        System.out.println("  <1>  Show subjects for each school term");
        System.out.println("  <2>  Show subjects with grades for each term");
        System.out.println("  <3>  Enter grades for subjects recently finished");
        System.out.println("  <4>  Edit a course");
        System.out.println("  <5>  Show General Weighted Average (GWA)");
        System.out.println("  <6>  Show courses ranked by grade");
        System.out.println("  <7>  Show curriculum progress summary");
        System.out.println("  <8>  Quit");
        System.out.println(PrintUtil.THICK_LINE);
    }

    private void autosave() {
        try {
            recordManager.save(courses);
            System.out.println("  [INFO] Record saved to: " + RECORD_FILE);
        } catch (IOException e) {
            System.out.println("  [WARNING] Could not save record: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println(PrintUtil.THICK_LINE);
        System.out.println("  My Checklist Monitoring Application");
        System.out.println("  Initializing...");
        System.out.println(PrintUtil.THICK_LINE);

        ChecklistApp app = new ChecklistApp();

        try {
            app.init();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load data: " + e.getMessage());
            System.err.println("  Looked for: " + CURRICULUM_FILE);
            System.err.println("  Working directory: " + new File(".").getAbsolutePath());
            System.err.println("  Please ensure bscs_curriculum.txt is inside a data folder.");
            System.exit(1);
        }

        printAssumptionsAndLimitations();
        app.run();
    }

    private static void printAssumptionsAndLimitations() {
        System.out.println();
        System.out.println("  " + PrintUtil.THIN_LINE);
        System.out.println("  ASSUMPTIONS AND LIMITATIONS");
        System.out.println("  " + PrintUtil.THIN_LINE);
        System.out.println("  1. The passing grade is 75 and above.");
        System.out.println("  2. Grades are entered as whole numbers from 0 to 100.");
        System.out.println("  3. A failing grade remains part of the student record.");
        System.out.println("  4. Credited courses can be traced to their source.");
        System.out.println("  5. Extra courses outside the curriculum can be recorded.");
        System.out.println("  6. The record is auto-saved after every entry or edit.");
        System.out.println("  " + PrintUtil.THIN_LINE);
    }
}