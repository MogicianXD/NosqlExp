package api.course;

import dao.CourseDao;
import entity.Course;
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


@WebServlet("/course")
public class CourseQuery extends HttpServlet {

    public CourseQuery() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        Map<String, String> args = LoadArgHelper.loadCols(req, CourseDao.keys);
        System.out.println("CourseQuery...");
        ArrayList<Course> list = (ArrayList<Course>) CourseDao.getInstance().Get(args);
        JsonBuilder json = JsonBuilder.geneSuccessJson();
        if (list != null && list.size() > 0)
            json.setResult(true).setCourse(list);
        else
            json.setResult(false);
        resp.getWriter().append(json.buildString());
        MLog.i("CourseQuery", json.buildString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        JsonBuilder json = JsonBuilder.geneSuccessJson();
        String id = req.getParameter(CourseDao.rowkey);
        String isUpdate = req.getParameter("update");
        if (id == null) {
            json.setResult(false);
        } else if(isUpdate != null) {
            String oldId = req.getParameter("old_" + CourseDao.rowkey);
            Map<String, String> args = LoadArgHelper.loadArgs(req, CourseDao.cols, CourseDao.cmps);
            boolean res = CourseDao.getInstance().Update(id, args);
            if(res && oldId != null)
                res = CourseDao.getInstance().UpdateRowKey(oldId, id);
            json.setResult(res);

        } else {
            Map<String, String> args = LoadArgHelper.loadArgs(req, CourseDao.cols, CourseDao.cmps);
            json.setResult(CourseDao.getInstance().Insert(id, args));
        }
        resp.getWriter().append(json.buildString());
        MLog.i("CoursePost", json.buildString());

    }
}
