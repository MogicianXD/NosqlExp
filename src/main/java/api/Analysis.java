package api;

import dao.AnalysisDao;
import dao.SCDao;
import dao.StudentDao;
import db.HBaseHelper;
import entity.Student;
import entity.StudentCourse;
import org.jcodings.util.Hash;
import tools.JsonBuilder;
import tools.MLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import joinery.DataFrame;


@WebServlet("/analysis")
public class Analysis extends HttpServlet {

    public Analysis() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        System.out.println("Analysis...");

        JsonBuilder json = JsonBuilder.geneSuccessJson();

        String tag = req.getParameter("tag");
        ArrayList<HashMap<String, Object>> result = null;

        if(tag == null)
            json.setResult(false);
        else {
            switch (Integer.parseInt(tag)) {
                case 0:
                    result = AnalysisDao.getInstance().electedCourse();
                    break;
                case 1:
                    result = AnalysisDao.getInstance().scoreStuTop(10);
                    break;
                case 2:
                    result = AnalysisDao.getInstance().electStuTop(10);
                    break;
                case 3:
                    result = AnalysisDao.getInstance().scoreStuMax();
                    break;
                case 4:
                    result = AnalysisDao.getInstance().scoreDistribution();
                    break;
                case 5:
                    result = AnalysisDao.getInstance().courseCountAndAvg();
                    break;
                case 6:
                    result = AnalysisDao.getInstance().scoreCourseMax();
                    break;
                case 7:
                    result = AnalysisDao.getInstance().scoreCourseTop(10);
                    break;
                case 8:
                    result = AnalysisDao.getInstance().electCourseTop(10);
                    break;
            }
        }
        if (result != null)
            json.setResult(true).setAnalysis(result);
        else
            json.setResult(false);
        resp.getWriter().append(json.buildString());
        MLog.i("Analysis", json.buildString());
    }
}
