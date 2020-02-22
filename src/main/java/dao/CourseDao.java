package dao;

import entity.Course;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

public class CourseDao extends EntityDao {

    private static CourseDao instance;
    private static final String tablename = "course";
    public static final String rowkey = "cid";
    public static final String colFamily = "info";
    public static final String[] cols = new String[]{"name", "fcid", "credit"};
    public static final String[][] cmps = new String[][]{{"cmp", "credit"}};
    public static final String[] keys = new String[]{"cid", "name", "fcid", "credit", "cmp"};

    private CourseDao() {
        super();
    }

    public static CourseDao getInstance() {
        if (instance == null) {
            instance = new CourseDao();
        }
        return instance;
    }

    public List Get(Map<String, String> args) {
        List<Course> result = Get(args, tablename, rowkey, colFamily, cols, cmps);
        if(result!=null)
            for(Course course : result){
                Course fc;
                if(course.getFcid()!=null)
                    if((fc = GetRow(course.getFcid())) != null)
                        course.setFcname(fc.getName());
            }
        return result;
    }

    public Course GetRow(String id) {
        Course course = (Course)super.GetRow(id, tablename, rowkey, colFamily);
        if(course != null){
            Course fc;
            if(course.getFcid() != null && course.getFcid().length() > 0)
                if((fc = (Course)super.GetRow(course.getFcid(), tablename, rowkey, colFamily)) != null)
                    course.setFcname(fc.getName());
        }
        return course;
    }

    public boolean Insert(String id, Map<String, String> args) {
        return super.Insert(id, args, tablename, colFamily);
    }

    public boolean Update(String id, Map<String, String> args) {
        return super.Update(id, args, tablename, colFamily);
    }

    public boolean UpdateRowKey(String oldId, String newId) {
        return super.UpdateRowKey(oldId, newId, tablename, colFamily, "tc");
    }

    @Override
    public Course pack(NavigableMap<byte[], byte[]> map, String id) {
        return new Course(id,
                getString(map, "name"),
                getString(map, "fcid"),
                getString(map, "credit"),
                null);
    }
}
