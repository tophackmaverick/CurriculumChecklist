package checklist.model;

public class Course {

    private String courseNo;
    private String descriptiveTitle;
    private double units;
    private String prereq;
    private int year;
    private String term;

    private String grade;
    private boolean credited;
    private String creditedFrom;
    private String actualCourseNo;
    private String actualTitle;

    // -------------------------------------------------------------------------

    public Course(String courseNo, String descriptiveTitle, double units,
                  String prereq, int year, String term) {

        this.courseNo = courseNo;
        this.descriptiveTitle = descriptiveTitle;
        this.units = units;
        this.prereq = prereq;
        this.year = year;
        this.term = term;

        this.grade = null;
        this.credited = false;
        this.creditedFrom = "";
        this.actualCourseNo = "";
        this.actualTitle = "";
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getCourseNo() { return courseNo; }
    public String getDescriptiveTitle() { return descriptiveTitle; }
    public double getUnits() { return units; }
    public String getPrereq() { return prereq; }
    public int getYear() { return year; }
    public String getTerm() { return term; }
    public String getGrade() { return grade; }
    public boolean isCredited() { return credited; }
    public String getCreditedFrom() { return creditedFrom; }
    public String getActualCourseNo() { return actualCourseNo; }
    public String getActualTitle() { return actualTitle; }

    // -------------------------------------------------------------------------
    // Setters (IMPORTANT — includes missing ones)
    // -------------------------------------------------------------------------

    public void setDescriptiveTitle(String title) { this.descriptiveTitle = title; }
    public void setUnits(double units) { this.units = units; }

    public void setGrade(String grade) { this.grade = grade; }
    public void setCredited(boolean credited) { this.credited = credited; }
    public void setCreditedFrom(String from) { this.creditedFrom = from; }
    public void setActualCourseNo(String no) { this.actualCourseNo = no; }
    public void setActualTitle(String title) { this.actualTitle = title; }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean isTaken() {
        return grade != null && !grade.isEmpty();
    }

    public boolean isPassed() {
        if (!isTaken()) return false;

        try {
            return Integer.parseInt(grade) >= 75;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isElective() {
        String no = courseNo.toUpperCase();
        return no.contains("ELEC");
    }

    public String getDisplayCourseNo() {
        return actualCourseNo.isEmpty() ? courseNo : actualCourseNo;
    }

    public String getDisplayTitle() {
        return actualTitle.isEmpty() ? descriptiveTitle : actualTitle;
    }

    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return String.join("|",
                courseNo,
                descriptiveTitle,
                String.valueOf(units),
                prereq,
                String.valueOf(year),
                term,
                grade == null ? "" : grade,
                String.valueOf(credited),
                creditedFrom,
                actualCourseNo,
                actualTitle
        );
    }
}