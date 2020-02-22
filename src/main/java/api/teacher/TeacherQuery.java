package api.teacher;

import dao.TeacherDao;
import entity.Teacher;
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


@WebServlet("/teacher")
public class TeacherQuery extends HttpServlet {

    public TeacherQuery() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        Map<String, String> args = LoadArgHelper.loadCols(req, TeacherDao.keys);
        System.out.println("TeacherQuery...");
        ArrayList<Teacher> list = (ArrayList<Teacher>) TeacherDao.getInstance().Get(args);
        JsonBuilder json = JsonBuilder.geneSuccessJson();
        if (list != null && list.size() > 0)
            json.setResult(true).setTeacher(list);
        else
            json.setResult(false);
        resp.getWriter().append(json.buildString());
        MLog.i("TeacherQuery", json.buildString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        JsonBuilder json = JsonBuilder.geneSuccessJson();
        String id = req.getParameter(TeacherDao.rowkey);
        String isUpdate = req.getParameter("update");
        if (id == null) {
            json.setResult(false);
        } else if(isUpdate != null) {
            String oldId = req.getParameter("old_" + TeacherDao.rowkey);
            Map<String, String> args = LoadArgHelper.loadArgs(req, TeacherDao.cols, TeacherDao.cmps);
            boolean res = TeacherDao.getInstance().Update(id, args);
            if(res && oldId != null)
                res = TeacherDao.getInstance().UpdateRowKey(oldId, id);
            json.setResult(res);

        } else {
            Map<String, String> args = LoadArgHelper.loadArgs(req, TeacherDao.cols, TeacherDao.cmps);
            json.setResult(TeacherDao.getInstance().Insert(id, args));
        }
        resp.getWriter().append(json.buildString());
        MLog.i("TeacherPost", json.buildString());
    }
}
