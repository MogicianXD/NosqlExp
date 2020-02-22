package dao;

import entity.Student;
import util.DateTools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentDao extends EntityDao {

    private static StudentDao instance;
    private static final String tablename = "student";
    public static final String rowkey = "sid";
    public static final String colFamily = "info";
    public static final String[] cols = new String[]{"name", "sex", "birthday", "dname", "class"};
    public static final String[][] cmps = new String[][]{{"cmp", "age"}};
    public static final String[] keys = new String[]{"sid", "name", "sex", "age", "cmp", "birthday", "dname", "class"};

    private StudentDao() {
        super();
    }

    public static StudentDao getInstance() {
        if (instance == null) {
            instance = new StudentDao();
        }
        return instance;
    }

    public List Get(Map<String, String> args) {
        return super.Get(args, tablename, rowkey, colFamily, cols, cmps);
    }

    public Student GetRow(String id) {
        return (Student)super.GetRow(id, tablename, rowkey, colFamily);
    }


    public boolean Insert(String id, Map<String, String> args) {
        return super.Insert(id, args, tablename, colFamily);
    }

    public boolean Update(String id, Map<String, String> args) {
        return super.Update(id, args, tablename, colFamily);
    }

    public boolean UpdateRowKey(String oldId, String newId) {
        return super.UpdateRowKey(oldId, newId, tablename, colFamily, "sc");
    }

    private DateFormat format0 = DateFormat.getDateInstance(DateFormat.SHORT);
    private DateFormat format1 = DateFormat.getDateInstance(DateFormat.FULL);


    @Override
    public Student pack(NavigableMap<byte[], byte[]> map, String id) {
        return new Student(id,
                getString(map, "name"),
                getString(map, "sex"),
                getString(map, "age"),
                dateFormat(getString(map, "birthday")),
                getString(map, "dname"),
                getString(map, "class"));
    }

    private String dateFormat(String old) {
        if (old != null && old.length() > 0)
            try {
                return DateTools.getSimpleDateFormat()
                        .format(DateTools.getDateFormat().parse(old));
            } catch (Exception e) {
                System.out.println(old);
                e.printStackTrace();
            }
        return null;
    }
}
