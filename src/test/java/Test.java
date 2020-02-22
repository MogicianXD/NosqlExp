import com.alibaba.fastjson.JSONObject;
import dao.StudentDao;
import db.HBaseHelper;
import joinery.DataFrame;
import org.apache.commons.logging.Log;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import tools.JsonBuilder;
import util.LogHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {

//        Log log = LogHelper.getLog();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("sid", "200900130604");
//        StudentDao.getInstance().Get(map);
//        ResultScanner rs = HBaseHelper.GetInstance().scan("student");
//        for(Result r : rs)
//        {
//            for(Cell cell : r.listCells())
//                cell.getRowArray();
//        }

//        List list = new ArrayList();
//        JsonBuilder json = JsonBuilder.geneSuccessJson();
//        json.setResult(true).setStudent(list);
//        System.out.println(json.buildString());
//        Result res = HBaseHelper.GetInstance().get("course", "300004", "info", "tid");
//        System.out.println(res.toString());

        DataFrame<Object> df = new DataFrame<>("cid", "cname", "sid", "name", "score");
        df.append(Arrays.asList("c1", "cn1", "s2", "sn2", 3));
        df.append(Arrays.asList("c2", "cn2", "s3", "sn3", 4));
        df.append(Arrays.asList("c3", "cn2", "s4", "sn4", 5));
        DataFrame<Object> t2 = df.drop("name", "cname").groupBy("cid").max().head(2);
        System.out.println(t2);
        DataFrame<Object> t1 = df.sortBy("-score").unique("cid").sortBy("cid");

        System.out.println(df.drop("score").joinOn(t2, DataFrame.JoinType.RIGHT, "cid")
                .drop("cid_right").rename("cid_left", "cid"));
    }
}
