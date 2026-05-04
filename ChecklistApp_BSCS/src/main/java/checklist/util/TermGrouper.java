package checklist.util;

import checklist.model.Course;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TermGrouper {

    private TermGrouper() {}

    public static Map<String, List<Course>> group(List<Course> courses) {
        Map<String, List<Course>> map = new LinkedHashMap<>();

        for (Course c : courses) {
            String key = c.getYear() + "|" + c.getTerm();
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(c);
        }

        return map;
    }
}