package checklist.data;

import checklist.model.Course;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRecordManager {

    private final String recordFilePath;

    public StudentRecordManager(String recordFilePath) {
        this.recordFilePath = recordFilePath;
    }

    public void save(List<Course> courses) throws IOException {
        File file = new File(recordFilePath);

        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("# COURSE_NO|TITLE|UNITS|PREREQ|YEAR|TERM|GRADE|CREDITED|CREDITED_FROM|ACTUAL_NO|ACTUAL_TITLE");

            for (Course c : courses) {
                writer.println(c);
            }
        }
    }

    public List<Course> load() throws IOException {
        List<Course> courses = new ArrayList<>();
        File file = new File(recordFilePath);

        if (!file.exists()) {
            return courses;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                Course c = parseCourse(line);

                if (c != null) {
                    courses.add(c);
                }
            }
        }

        return courses;
    }

    public boolean recordExists() {
        return new File(recordFilePath).exists();
    }

    private Course parseCourse(String line) {
        try {
            String[] p = line.split("\\|", 11);

            if (p.length < 6) {
                return null;
            }

            Course c = new Course(
                    p[0].trim(),
                    p[1].trim(),
                    Double.parseDouble(p[2].trim()),
                    p[3].trim(),
                    Integer.parseInt(p[4].trim()),
                    p[5].trim()
            );

            if (p.length > 6 && !p[6].trim().isEmpty()) c.setGrade(p[6].trim());
            if (p.length > 7 && !p[7].trim().isEmpty()) c.setCredited(Boolean.parseBoolean(p[7].trim()));
            if (p.length > 8 && !p[8].trim().isEmpty()) c.setCreditedFrom(p[8].trim());
            if (p.length > 9 && !p[9].trim().isEmpty()) c.setActualCourseNo(p[9].trim());
            if (p.length > 10 && !p[10].trim().isEmpty()) c.setActualTitle(p[10].trim());

            return c;

        } catch (Exception e) {
            return null;
        }
    }
}