package expt;

import org.apache.commons.logging.Log;
import db.HBaseHelper;
import util.LogHelper;

public class Exp2 {
    private static Log log;

    public static void main(String[] args) {
        log = LogHelper.getLog();
        try {
            log.info("初始化HBase命名空间" + HBaseHelper.namespace);
            HBaseHelper.GetInstance().initNamespace();

            log.info("创建表student, teacher, course");
            HBaseHelper.GetInstance().createTable("student",
                    new String[]{"info", "sc"});
            HBaseHelper.GetInstance().createTable("teacher",
                    new String[]{"info"});
            HBaseHelper.GetInstance().createTable("course",
                    new String[]{"info", "tc"});

            log.info("读取Excel");
            String root = "D:/data";

            HBaseHelper.GetInstance().insertRowsFromExcel(
                    root + "/student.xls", "student", "info",
                    new String[]{"sid", "name", "sex", "age", "birthday", "dname", "class"}, true);
            HBaseHelper.GetInstance().insertRowsFromExcel(
                    root + "/teacher.xls", "teacher", "info",
                    new String[]{"tid", "name", "sex", "age", "dname"}, true);
            HBaseHelper.GetInstance().insertRowsFromExcel(
                    root + "/course.xls", "course", "info",
                    new String[]{"cid", "name", "fcid", "credit"}, true);
            HBaseHelper.GetInstance().insertRowsFromExcel(
                    root + "/student_course.xls", "student", "sc",
                    true, "t", true);
            HBaseHelper.GetInstance().insertRowsFromExcel(
                    root + "/teacher_course.xls", "course", "info",
                    true, null, false);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
