package entity;

public class Course {
    private String cid;
    private String name;
    private String fcid;
    private String fcname;

    public Course(String cid, String name, String fcid, float credit, String fcname) {
        this.cid = cid;
        this.name = name;
        this.fcid = fcid;
        this.credit = credit;
        this.fcname = fcname;
    }

    public Course(String cid, String name, String fcid, String credit, String fcname) {
        this.cid = cid;
        this.name = name;
        this.fcid = fcid;
        if (credit != null)
            this.credit = Float.parseFloat(credit);
        this.fcname = fcname;
    }

    public String getFcname() {
        return fcname;
    }

    public void setFcname(String fcname) {
        this.fcname = fcname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFcid() {
        return fcid;
    }

    public void setFcid(String fcid) {
        this.fcid = fcid;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    private float credit;

}
