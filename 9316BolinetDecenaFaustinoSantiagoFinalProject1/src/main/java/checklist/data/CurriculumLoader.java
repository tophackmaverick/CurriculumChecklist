package checklist.data;

import checklist.model.Course;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CurriculumLoader {

    private final String filePath;

    public CurriculumLoader(String filePath) {
        this.filePath = filePath;
    }

    public List<Course> load() throws IOException {
        List<Course> courses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                Course course = parseCourse(line);

                if (course != null) {
                    courses.add(course);
                }
            }
        }

        return courses;
    }

    private Course parseCourse(String line) {
        line = line.trim();

        if (line.isEmpty() || line.startsWith("#")) {
            return null;
        }

        try {
            String[] p = line.split("\\|", 6);

            if (p.length < 5) {
                return null;
            }

            int year = Integer.parseInt(p[0].trim());
            String term = p[1].trim();
            String no = p[2].trim();
            String title = p[3].trim();
            double units = Double.parseDouble(p[4].trim());
            String prereq = p.length == 6 ? p[5].trim() : "";

            return new Course(no, title, units, prereq, year, term);

        } catch (Exception e) {
            return null;
        }
    }
}