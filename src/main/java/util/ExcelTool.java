package util;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.util.*;

public class ExcelTool {

    public static List<List<String>> readExcel(String filepath) throws IOException, BiffException {
        return readExcel(new File(filepath));
    }

    public static List<List<String>> readExcel(File file) throws IOException, BiffException {
        // 创建输入流，读取Excel
        InputStream is = new FileInputStream(file.toURI().getPath());
        // jxl提供的Workbook类
        Workbook wb = Workbook.getWorkbook(is);
        // Excel的页签数量
        int index = 0;
        List<List<String>> outerList = new ArrayList<List<String>>();
        // 每个页签创建一个Sheet对象
        Sheet sheet = wb.getSheet(index);
//            log.info("该文件有"+sheet.getRows()+"行");
        // sheet.getRows()返回该页的总行数
        for (int i = 0; i < sheet.getRows(); i++) {
            List<String> innerList = new ArrayList<String>();
            // sheet.getColumns()返回该页的总列数
            for (int j = 0; j < sheet.getColumns(); j++) {
                String cellinfo = sheet.getCell(j, i).getContents();
//                    if(cellinfo.isEmpty()){//一定要删除，不然空数据不被添加会对应不起来，gan
//                        continue;
//                    }
                innerList.add(cellinfo);
            }
            outerList.add(i, innerList);
        }
        return outerList;
    }
}
