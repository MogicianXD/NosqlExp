package entity;

public class Teacher {
    private String tid;
    private String name;
    private String sex;
    private int age;
    private String dname;

    public Teacher(String tid, String name, String sex, int age, String dname) {
        this.tid = tid;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.dname = dname;
    }

    public Teacher(String tid, String name, String sex, String age, String dname) {
        this.tid = tid;
        this.name = name;
        this.sex = sex;
        if (age != null)
            this.age = Integer.parseInt(age);
        this.dname = dname;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

}
