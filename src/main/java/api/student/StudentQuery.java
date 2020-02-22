package api.student;

import dao.StudentDao;
import entity.Student;
import tools.JsonBuilder;
import tools.MLog;
import util.LoadArgHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


@WebServlet("/student")
public class StudentQuery extends HttpServlet {

    public StudentQuery() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        System.out.println("StudentQuery...");

        Map<String, String> args = LoadArgHelper.loadCols(req, StudentDao.keys);
        ArrayList<Student> list = (ArrayList<Student>) StudentDao.getInstance().Get(args);
        JsonBuilder json = JsonBuilder.geneSuccessJson();
        if (list != null && list.size() > 0)
            json.setResult(true).setStudent(list);
        else
            json.setResult(false);
        resp.getWriter().append(json.buildString());
        MLog.i("StudentQuery", json.buildString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        JsonBuilder json = JsonBuilder.geneSuccessJson();
        String id = req.getParameter(StudentDao.rowkey);
        String isUpdate = req.getParameter("update");
        if (id == null) {
            json.setResult(false);
        } else if(isUpdate != null) {
            String oldId = req.getParameter("old_" + StudentDao.rowkey);
            Map<String, String> args = LoadArgHelper.loadArgs(req, StudentDao.cols, StudentDao.cmps);
            boolean res = StudentDao.getInstance().Update(id, args);
            if(res && oldId != null)
                res = StudentDao.getInstance().UpdateRowKey(oldId, id);
            json.setResult(res);

        } else {
            Map<String, String> args = LoadArgHelper.loadArgs(req, StudentDao.cols, StudentDao.cmps);
            json.setResult(StudentDao.getInstance().Insert(id, args));
        }
        resp.getWriter().append(json.buildString());
        MLog.i("StudentPost", json.buildString());
    }

}
