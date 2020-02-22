package dao;

import entity.Student;
import entity.StudentCourse;
import joinery.DataFrame;
import tools.JsonBuilder;
import tools.MLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class AnalysisDao {

    private static AnalysisDao instance;


    public  static AnalysisDao getInstance() {
        if (instance == null) {
            instance = new AnalysisDao();
        }
        return instance;
    }

    public ArrayList<HashMap<String, Object>> electedCourse(){
        DataFrame<Object> df = new DataFrame<>("name");
        for(HashMap.Entry<Student, List<StudentCourse>> entry: SCDao.getInstance().GetAll().entrySet()){
            for(StudentCourse sc: entry.getValue())
                df.append(Arrays.asList(sc.getCourse().getName()));
        }
        return transform(df.unique("name").col("name"), "name");
    }

    public ArrayList<HashMap<String, Object>> scoreStuTop(int topk){
        DataFrame<Object> df = new DataFrame<>("sid", "name", "score");
        for(HashMap.Entry<Student, List<StudentCourse>> entry: SCDao.getInstance().GetAll().entrySet()){
            Student student = entry.getKey();
            for(StudentCourse sc: entry.getValue()) {
                if(sc.getScore() != null && !sc.getScore().equals(""))
                    df.append(Arrays.asList(student.getSid(),
                            student.getName(), Integer.parseInt(sc.getScore())));
            }
        }
        DataFrame<Object> t2 = df.drop("name")
                .groupBy("sid")
                .mean()
                .sortBy("-score")
                .head(topk);
        return transform(df.drop("score").unique("sid")
                .joinOn(t2, DataFrame.JoinType.RIGHT, "sid")
                .drop("sid_right","sid_left"), "sid");
    }

    public ArrayList<HashMap<String, Object>> scoreCourseTop(int topk){
        DataFrame<Object> df = new DataFrame<>("cid", "name", "score");
        for(HashMap.Entry<Student, List<StudentCourse>> entry: SCDao.getInstance().GetAll().entrySet()){
            for(StudentCourse sc: entry.getValue()) {
                if(sc.getScore() != null && !sc.getScore().equals(""))
                    df.append(Arrays.asList(sc.getCourse().getCid(),
                            sc.getCourse().getName(), Integer.parseInt(sc.getScore())));
            }
        }
        DataFrame<Object> t2 = df.drop("name")
                .groupBy("cid")
                .mean()
                .sortBy("-score")
                .head(topk);
        return transform(df.drop("score").unique("cid")
                .joinOn(t2, DataFrame.JoinType.RIGHT, "cid")
                .drop("cid_right","cid_left"));
    }

    public ArrayList<HashMap<String, Object>> electStuTop(int topk){
        DataFrame<Object> df = new DataFrame<>("sid", "name", "count");
        for(HashMap.Entry<Student, List<StudentCourse>> entry: SCDao.getInstance().GetAll().entrySet()){
            Student student = entry.getKey();
            for(StudentCourse sc: entry.getValue()) {
                    df.append(Arrays.asList(student.getSid(),
                            student.getName(), 1));
            }
        }
        DataFrame<Object> t2 = df.drop("name")
                .groupBy("sid")
                .count()
                .sortBy("-count")
                .head(topk);
        return transform(df.drop("count").unique("sid")
                .joinOn(t2, DataFrame.JoinType.RIGHT, "sid")
                .drop("sid_right","sid_left"), "sid");
    }

    public ArrayList<HashMap<String, Object>> electCourseTop(int topk){
        DataFrame<Object> df = new DataFrame<>("cid", "name", "count");
        for(HashMap.Entry<Student, List<StudentCourse>> entry: SCDao.getInstance().GetAll().entrySet()){
            for(StudentCourse sc: entry.getValue()) {
                df.append(Arrays.asList(sc.getCourse().getCid(),
                        sc.getCourse().getName(), 1));
            }
        }
        DataFrame<Object> t2 = df.drop("name")
                .groupBy("cid")
                .count()
                .sortBy("-count")
                .head(topk);
        return transform(df.drop("count").unique("cid")
                .joinOn(t2, DataFrame.JoinType.RIGHT, "cid")
                .drop("cid_right","cid_left"), "cid");
    }

    public ArrayList<HashMap<String, Object>> courseCountAndAvg(){
        DataFrame<Object> df = new DataFrame<>("cid", "name", "count", "score");
        for(HashMap.Entry<Student, List<StudentCourse>> entry: SCDao.getInstance().GetAll().entrySet()){
            for(StudentCourse sc: entry.getValue()) {
                String score = sc.getScore();
                df.append(Arrays.asList(sc.getCourse().getCid(),
                        sc.getCourse().getName(), 1,
                        score == null || score.equals("") ? null : Integer.parseInt(score)));
            }
        }
        DataFrame<Object> t1 = df.drop("name", "count")
                .groupBy("cid")
                .mean().sortBy("cid");
        DataFrame<Object> t2 = df.drop("name", "score")
                .groupBy("cid")
                .count().sortBy("cid");
        return transform(df.drop("count", "score").sortBy("cid")
                .add("count", new ArrayList<>(t2.col("count")))
                .add("score", new ArrayList<>(t1.col("score"))));
    }

    public ArrayList<HashMap<String, Object>> scoreStuMax(){
        DataFrame<Object> df = new DataFrame<>("sid", "name", "score", "cname");
        for(HashMap.Entry<Student, List<StudentCourse>> entry: SCDao.getInstance().GetAll().entrySet()){
            Student student = entry.getKey();
            for(StudentCourse sc: entry.getValue()) {
                if(sc.getScore() != null && !sc.getScore().equals(""))
                    df.append(Arrays.asList(student.getSid(),
                            student.getName(), Integer.parseInt(sc.getScore()),
                            sc.getCourse().getName()));
            }
        }
        DataFrame<Object> t2 = df.drop("name", "cname")
                .groupBy("sid")
                .max()
                .sortBy("sid");
        return transform(df.drop("score").unique("sid").merge(t2), "sid");
    }

    public ArrayList<HashMap<String, Object>> scoreCourseMax(){
        DataFrame<Object> df = new DataFrame<>(
                "cid", "cname", "score", "sid", "name");
        for(HashMap.Entry<Student, List<StudentCourse>> entry: SCDao.getInstance().GetAll().entrySet()){
            Student student = entry.getKey();
            for(StudentCourse sc: entry.getValue()) {
                if(sc.getScore() != null && !sc.getScore().equals(""))
                    df.append(Arrays.asList(sc.getCourse().getCid(),
                            sc.getCourse().getName(), Integer.parseInt(sc.getScore()),
                            student.getSid(), student.getName()));
            }
        }
        return transform(df.sortBy("-score").unique("cid").sortBy("cid"));
    }

    public ArrayList<HashMap<String, Object>> scoreDistribution(){
        DataFrame<Object> df = new DataFrame<>("type", "count");
        for(HashMap.Entry<Student, List<StudentCourse>> entry: SCDao.getInstance().GetAll().entrySet()){
            Student student = entry.getKey();
            for(StudentCourse sc: entry.getValue()) {
                if(sc.getScore() == null || sc.getScore().length() == 0)
                    continue;
                int score = Integer.parseInt(sc.getScore());
                String type = score < 60 ? "fail" : score < 80 ? "pass" : score < 90 ? "good" : "excellent";
                df.append(Arrays.asList(type, 1));
            }
        }
        return bag(df.groupBy("type").count().reindex("type"));
    }

    private ArrayList<HashMap<String, Object>> transform(DataFrame<Object> df) {
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        Object[] columns = df.columns().toArray();
        for(List<Object> list: df.map().values()) {
            HashMap<String, Object> map = new HashMap<>();
            for(int i = 0; i < list.size(); i++)
                map.put((String)columns[i], list.get(i)+"");
            result.add(map);
        }
        return result;
    }

    private ArrayList<HashMap<String, Object>> transform(DataFrame<Object> df, String indexName) {
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        String[] columns = df.columns().toArray(new String[0]);
        for(Map.Entry<Object, List<Object>> entry: df.map().entrySet()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(indexName, entry.getKey());
            List<Object> list = entry.getValue();
            for(int i = 0; i < list.size(); i++)
                map.put(columns[i], list.get(i));
            result.add(map);
        }
        return result;
    }

    private ArrayList<HashMap<String, Object>> transform(List<Object> colVals, String col) {
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        for(Object val: colVals) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(col, val);
            result.add(map);
        }
        return result;
    }

    private ArrayList<HashMap<String, Object>> bag(DataFrame<Object> df) {
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        for(Object type: df.index()) {
            map.put(type+"", df.row(type).get(0));
        }
        result.add(map);
        return result;
    }
}
