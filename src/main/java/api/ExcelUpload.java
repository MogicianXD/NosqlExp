package api;

import db.HBaseHelper;
import jxl.read.biff.BiffException;
import org.apache.commons.io.IOUtils;
import tools.JsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import tools.MLog;

@WebServlet("/upload")
public class ExcelUpload extends HttpServlet {
    public ExcelUpload() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        JsonBuilder json = JsonBuilder.geneSuccessJson();

        String tablename = null, colfamily = null;

        DiskFileItemFactory factory = new DiskFileItemFactory();//1.创建DiskFileItemFactory工厂类
        ServletFileUpload upload = new ServletFileUpload(factory);//2.创建解析类，用于解析resquest

        boolean res = true;
        File file;
        try {
            List<FileItem> list = upload.parseRequest(req);//3.解析内容，获取一个list，数据都存储在list中
            for (FileItem item : list) {
                if (item.isFormField()) {//判断是否是普通的表单内容
                    if (item.getFieldName().equals("tablename")) //获取的是表单中name属性的值
                        tablename = item.getString();//获取的是对应的表单的值
                    else if (item.getFieldName().equals("colfamily"))
                        colfamily = item.getString();
                } else {//为假，说明是上传项
                    //获取流，进行处理
                    InputStream ism = item.getInputStream();
                    String path = getServletContext().getRealPath("/upload/");
                    System.out.println(path);
                    File dir = new File(path);
                    if (!dir.exists())
                        dir.mkdirs();
                    String filename = item.getName();//这里getName可以获取文件名
                    System.out.println(filename);
                    file = new File(path + filename);
                    file.createNewFile();//这里不做文件存在性和名字重复判断
                    OutputStream fos = new FileOutputStream(file);
                    //这里直接借助commons.io来做io对接，不然需要做流的读取和写入
                    IOUtils.copy(ism, fos);//把输入流的数据拷贝到输出流
                    IOUtils.closeQuietly(ism);
                    IOUtils.closeQuietly(fos);

                    if (tablename != null && colfamily != null)
                        HBaseHelper.GetInstance()
                                .insertRowsFromExcel(path + filename, tablename,
                                        colfamily, null, false);
                    else {
                        res = false;
                        file.delete();
                        break;
                    }
                    file.delete();
                }
            }
            json.setResult(res).setMsg("表插入成功");
        } catch (IOException e) {
            e.printStackTrace();
            json.setResult(false).setMsg("表结构错误");
        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            json.setResult(false).setMsg("文件上传失败");
        } catch (BiffException e) {
            e.printStackTrace();
            json.setResult(false).setMsg("文件损坏");
        }
        resp.getWriter().append(json.buildString());
        MLog.i("Upload", json.buildString());

    }

}
