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


@WebServlet("/login")
public class StudentLogin extends HttpServlet {

    public StudentLogin() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        System.out.println("StudentQuery...");

        String sid = req.getParameter(StudentDao.rowkey);
        Student result = StudentDao.getInstance().GetRow(sid);
        JsonBuilder json = JsonBuilder.geneSuccessJson();
        if (result != null)
            json.setResult(true);
        else
            json.setResult(false);
        resp.getWriter().append(json.buildString());
        MLog.i("StudentQuery", json.buildString());
    }
}
