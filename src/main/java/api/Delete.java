package api;

import dao.StudentDao;
import db.HBaseHelper;
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


@WebServlet("/delete")
public class Delete extends HttpServlet {

    public Delete() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        JsonBuilder json = JsonBuilder.geneSuccessJson();
        String id = req.getParameter("id");
        String tablename = req.getParameter("tablename");
        String colFamily = req.getParameter("colFamily");

        if (id == null || tablename == null) {
            json.setResult(false);
        } else {
            json.setResult(HBaseHelper.GetInstance().deleteRowKey(id, tablename, colFamily));
        }
        resp.getWriter().append(json.buildString());
        MLog.i("Delete", json.buildString());
    }

}
