package entity;

public class StudentCourse {
    private String sid;
    private Teacher teacher;
    private Course course;
    private String score;

    public StudentCourse(String sid, Course course, Teacher teacher, String score) {
        this.sid = sid;
        this.teacher = teacher;
        this.course = course;
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }



}
