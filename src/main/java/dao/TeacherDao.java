package dao;

import entity.Teacher;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

public class TeacherDao extends EntityDao {

    private static TeacherDao instance;
    private static final String tablename = "teacher";
    public static final String rowkey = "tid";
    public static final String colFamily = "info";
    public static final String[] cols = new String[]{"name", "sex", "dname"};
    public static final String[][] cmps = new String[][]{{"cmp", "age"}};
    public static final String[] keys = new String[]{"tid", "name", "sex", "age", "cmp", "dname"};

    private TeacherDao() {
        super();
    }

    public static TeacherDao getInstance() {
        if (instance == null) {
            instance = new TeacherDao();
        }
        return instance;
    }

    public List Get(Map<String, String> args) {
        return super.Get(args, tablename, rowkey, colFamily, cols, cmps);
    }

    public Teacher GetRow(String id) {
        return (Teacher)super.GetRow(id, tablename, rowkey, colFamily);
    }

    public boolean Insert(String id, Map<String, String> args) {
        return super.Insert(id, args, tablename, colFamily);
    }

    public boolean Update(String id, Map<String, String> args) {
        return super.Update(id, args, tablename, colFamily);
    }

    public boolean UpdateRowKey(String oldId, String newId) {
        return super.UpdateRowKey(oldId, newId, tablename, colFamily, null);
    }

    @Override
    public Teacher pack(NavigableMap<byte[], byte[]> map, String id) {
        return new Teacher(id,
                getString(map, "name"),
                getString(map, "sex"),
                getString(map, "age"),
                getString(map, "dname"));
    }
}
