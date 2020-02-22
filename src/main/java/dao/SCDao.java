package dao;

import db.HBaseHelper;
import entity.Course;
import entity.Student;
import entity.StudentCourse;
import entity.Teacher;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SCDao {

    private static SCDao instance;
    public static final String tablename = "student";
    public static final String rowkey = "sid";
    public static final String colFamily = "sc";
    public static final String[] cols = new String[]{"cid", "tid"};
    private static final char prefix = 't';

    private SCDao() {
        super();
    }

    public static SCDao getInstance() {
        if (instance == null) {
            instance = new SCDao();
        }
        return instance;
    }

    public List Get(String id) {
        ArrayList<StudentCourse> result = new ArrayList<>();
        try {
            if (id != null) {
                Result res = null;
                res = HBaseHelper.GetInstance().get(tablename, id, colFamily, null);
                if (res == null || res.getNoVersionMap() == null)
                    return null;
                NavigableMap<byte[], byte[]> map = res.getNoVersionMap().get(Bytes.toBytes(colFamily));
                if (map != null) {
                    return getSC(map, id, true, null);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<Student, List<StudentCourse>> GetAll() {
        HashMap<Student, List<StudentCourse>> result = new HashMap<>();
        try {
            ResultScanner courseScan = HBaseHelper.GetInstance().scan("course", "info", null);
            HashMap<String, Course> courses = new HashMap<>();
            for (Result res : courseScan) {
                NavigableMap<byte[], byte[]> map = res.getNoVersionMap().get(Bytes.toBytes("info"));
                if (map != null) {
                    String id = new String(res.getRow());
                    NavigableMap<byte[], byte[]> map2 = res.getNoVersionMap().get(Bytes.toBytes(StudentDao.colFamily));
                    if(map2 == null)
                        continue;
                    Course course = CourseDao.getInstance().pack(map2, id);
                    courses.put(id, course);
                }
            }
            ResultScanner scanner = HBaseHelper.GetInstance().scan(tablename, null, null);
            for (Result res : scanner) {
                NavigableMap<byte[], byte[]> map = res.getNoVersionMap().get(Bytes.toBytes(colFamily));
                if (map != null) {
                    String id = new String(res.getRow());
                    NavigableMap<byte[], byte[]> map2 = res.getNoVersionMap().get(Bytes.toBytes(StudentDao.colFamily));
                    if(map2 == null)
                        continue;
                    Student student = StudentDao.getInstance().pack(map2, id);
                    result.put(student, getSC(map, id, false, courses));
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<StudentCourse> getSC(NavigableMap<byte[], byte[]> map, String id,
                                           boolean otherTables, HashMap<String, Course> courses) {
        ArrayList<StudentCourse> result = new ArrayList<>();
        Map<String, String[]> tmp = new HashMap<>();
        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
            String key = new String(entry.getKey(), StandardCharsets.UTF_8);
            String val = new String(entry.getValue(), StandardCharsets.UTF_8);
            String cid = null;
            String tid = null;
            String score = null;
            if (key.charAt(0) == prefix) {
                cid = key.substring(1);
                tid = val;
            } else {
                cid = key;
                score = val;
            }
            String[] oldVal = tmp.get(cid);
            if (oldVal == null)
                tmp.put(cid, new String[]{score, tid});
            else {
                if (oldVal[0] == null)
                    oldVal[0] = score;
                else
                    oldVal[1] = tid;
                tmp.put(cid, oldVal);
            }
        }
        for (Map.Entry<String, String[]> entry : tmp.entrySet()) {
            String[] val = entry.getValue();
            String cid = entry.getKey();
            Course course = null;
            Teacher teacher = null;
            if(otherTables) {
                course = CourseDao.getInstance().GetRow(cid);
                teacher = TeacherDao.getInstance().GetRow(val[1]);
            } else if (courses != null) {
                course = courses.get(cid);
            }
            result.add(new StudentCourse(id, course, teacher, val[0]));
        }
        return result;
    }


    public boolean Delete(String id, String cid) {
        List<String> list = new ArrayList<>();
        list.add(cid);
        list.add(prefix+cid);
        try {
            HBaseHelper.GetInstance().deleteRow(tablename, id, colFamily, list);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Insert(String id, String cid) {
        try {
            Map<String, String> col2val = new HashMap<>();
            col2val.put(cid, "");
            String tid = null;
            Result res = HBaseHelper.GetInstance().get("course", id, "info", "tid");
            if (res != null && res.getNoVersionMap() != null) {
                tid = new String(res.getNoVersionMap().get(Bytes.toBytes("info")).get(Bytes.toBytes("tid")));
            }
            if(tid != null)
                col2val.put(prefix + cid, tid);
            HBaseHelper.GetInstance().insertRow(tablename, id, colFamily, col2val);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
