package api.student;

import dao.CourseDao;
import dao.SCDao;
import dao.StudentDao;
import db.HBaseHelper;
import entity.Student;
import entity.StudentCourse;
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


@WebServlet("/elect")
public class ElectQuery extends HttpServlet {

    public ElectQuery() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        System.out.println("SCQuery...");

        String id = req.getParameter(StudentDao.rowkey);
        ArrayList<StudentCourse> list =
                (ArrayList<StudentCourse>) SCDao.getInstance().Get(id);
        JsonBuilder json = JsonBuilder.geneSuccessJson();
        if (list != null && list.size() > 0)
            json.setResult(true).setElect(list);
        else
            json.setResult(false);
        resp.getWriter().append(json.buildString());
        MLog.i("SCQuery", json.buildString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        JsonBuilder json = JsonBuilder.geneSuccessJson();
        String id = req.getParameter(StudentDao.rowkey);
        String cid = req.getParameter(CourseDao.rowkey);
        String toElect = req.getParameter("elect");
        if (id == null) {
            json.setResult(false);
        } else if(toElect == null) {
            json.setResult(SCDao.getInstance().Delete(id, cid));
        } else {
            json.setResult(SCDao.getInstance().Insert(id, cid));
        }
        resp.getWriter().append(json.buildString());
        MLog.i("SCPost", json.buildString());
    }

}
