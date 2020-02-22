package db;

import jxl.read.biff.BiffException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.BasicConfigurator;
import util.ExcelTool;
import util.LogHelper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HBaseHelper {

    private static HBaseHelper instance;

    private HBaseHelper() {
    }

    public static HBaseHelper GetInstance() {
        if (instance == null) {
            instance = new HBaseHelper();
            instance.init();
        }
        return instance;
    }

    public static Configuration configuration;
    public static Connection connection;
    public static Admin admin;
    public static final String namespace = "user201700301037";
    public static String namespace_prefix = "";

    /**
     * 初始化连接
     */
    public void init() {
        configuration = HBaseConfiguration.create();
//        configuration.set("hbase.zookeeper.property.clientPort", "2181");
//        configuration.set("hbase.zookeeper.quorum", "10.0.75.1");
//        configuration.set("hbase.master", "10.0.75.1:60000");
        BasicConfigurator.configure();
        if (!namespace.equals(""))
            namespace_prefix = namespace + ":";
        File workaround = new File(".");
        System.getProperties().put("hadoop.home.dir",
                workaround.getAbsolutePath());
        new File("./bin").mkdirs();
        try {
            new File("./bin/winutils.exe").createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (null != admin)
                admin.close();
            if (null != connection)
                connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化namespce
     */
    public void initNamespace() throws IOException {
        try {
            System.out.println("命名空间已存在");
            admin.getNamespaceDescriptor(namespace);
        } catch (NamespaceNotFoundException e) {
            newNamespace(namespace);
        }
    }

    /**
     * 新建namespce
     */
    public void newNamespace(String name) throws IOException {
        admin.createNamespace(NamespaceDescriptor.create(name).build());
    }

    /**
     * 检查table是否存在
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public boolean tableExists(String tableName) throws IOException {

        TableName Name = TableName.valueOf(namespace_prefix + tableName);
        return admin.tableExists(Name);
    }

    /**
     * 创建表
     *
     * @param tName
     * @param cols
     * @throws IOException
     */
    public void createTable(String tName, String[] cols) throws IOException {

        TableName tableName = TableName.valueOf(namespace_prefix + tName);

        if (admin.tableExists(tableName)) {
            System.out.println("table " + tName + " is exists!");
        } else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            for (String col : cols) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(col);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            admin.createTable(hTableDescriptor);
        }
    }

    /**
     * 删除表
     *
     * @param tableName
     * @throws IOException
     */
    public void deleteTable(String tableName) throws IOException {
        TableName tn = TableName.valueOf(namespace_prefix + tableName);
        if (admin.tableExists(tn)) {
            admin.disableTable(tn);
            admin.deleteTable(tn);
        }
    }

    /**
     * list
     *
     * @throws IOException
     */
    public void listTables() throws IOException {
        HTableDescriptor hTableDescriptors[] = admin.listTables();
        for (HTableDescriptor hTableDescriptor : hTableDescriptors) {
            System.out.println(hTableDescriptor.getNameAsString());
        }
    }

    /**
     * 相当于 put
     *
     * @param tableName
     * @param rowkey
     * @param colFamily
     * @param col
     * @param val
     * @throws IOException
     */
    public void insertRow(String tableName, String rowkey, String colFamily, String col, String val) throws IOException {
        Table table = connection.getTable(TableName.valueOf(namespace_prefix + tableName));
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(col), Bytes.toBytes(val));
        table.put(put);
        table.close();
    }

    public void insertRow(String tableName, String rowkey, String colFamily, Map<String, String> col2val) throws IOException {
        Table table = connection.getTable(TableName.valueOf(namespace_prefix + tableName));
        Put put = new Put(Bytes.toBytes(rowkey));
        for (Map.Entry<String, String> entry : col2val.entrySet())
            put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
        table.put(put);
        table.close();
    }

    public void insertRow(String tableName, String rowkey, String colFamily, NavigableMap<byte[], byte[]> col2val) throws IOException {
        Table table = connection.getTable(TableName.valueOf(namespace_prefix + tableName));
        Put put = new Put(Bytes.toBytes(rowkey));
        for (Map.Entry<byte[], byte[]> entry : col2val.entrySet())
            put.addColumn(Bytes.toBytes(colFamily), entry.getKey(), entry.getValue());
        table.put(put);
        table.close();
    }

    public void insertRows(String tableName, List<Put> puts) throws IOException {
        Table table = connection.getTable(TableName.valueOf(namespace_prefix + tableName));
        table.put(puts);
        table.close();
    }

    public void insertRowsFromExcel(String filepath, String tablename, String colFamily,
                                    boolean hasHeader, String prefix, boolean sc) throws IOException, BiffException {
        List<List<String>> data = ExcelTool.readExcel(filepath);
        List<Put> puts = new ArrayList<>();
        for (List<String> row : data) {
            if (hasHeader) {
                hasHeader = false;
                continue;
            }
            Put put = new Put(Bytes.toBytes(row.get(0)));
            if(sc)
                put.addColumn(Bytes.toBytes(colFamily),
                        Bytes.toBytes(row.get(1)), Bytes.toBytes(row.get(2)))
                    .addColumn(Bytes.toBytes(colFamily),
                            Bytes.toBytes(prefix+row.get(1)), Bytes.toBytes(row.get(3)));
            else
                put.addColumn(Bytes.toBytes(colFamily),
                        Bytes.toBytes("tid"), Bytes.toBytes(prefix+row.get(1)));
            puts.add(put);
        }
        insertRows(tablename, puts);
    }

    public void insertRowsFromExcel(String filepath, String tablename, String colFamily, String[] header, boolean rename) throws IOException, BiffException {
        List<List<String>> data = ExcelTool.readExcel(filepath);
        List<Put> puts = new ArrayList<>();
        ArrayList<String> head = new ArrayList<>();
        for (List<String> row : data) {
            if (head.isEmpty()) {
                if (rename)
                    head = new ArrayList<>(Arrays.asList(header));
                else
                    head = new ArrayList<>(row);
                continue;
            }
            Put put = new Put(Bytes.toBytes(row.get(0)));
            for (int i = 1; i < head.size(); i++) {
                put.addColumn(Bytes.toBytes(colFamily),
                        Bytes.toBytes(head.get(i)), Bytes.toBytes(row.get(i)));
            }
            puts.add(put);
        }
        insertRows(tablename, puts);
    }

    /**
     * 删除行
     *
     * @param tableName
     * @param rowkey
     * @param colFamily
     * @param cols
     * @throws IOException
     */
    public void deleteRow(String tableName, String rowkey, String colFamily, List<String> cols) throws IOException {
        Table table = connection.getTable(TableName.valueOf(namespace_prefix + tableName));
        Delete delete = new Delete(Bytes.toBytes(rowkey));
        // 删除指定列族
        if(colFamily != null) {
            delete.addFamily(Bytes.toBytes(colFamily));
            // 删除指定列
            if(cols != null)
                for(String col : cols)
                    delete.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(col));
        }
        table.delete(delete);
        table.close();
    }

    public void deleteRows(String tableName, List<Delete> deletes) throws IOException {
        Table table = connection.getTable(TableName.valueOf(namespace_prefix + tableName));
        table.delete(deletes);
        table.close();
    }

    public boolean deleteRowKey(String oldId, String tablename, String colFamily) {
        try {
            deleteRow(tablename, oldId, colFamily, null);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据rowkey查找数据
     *
     * @param tableName
     * @param rowkey
     * @param colFamily
     * @param col
     * @throws IOException
     */
    public Result get(String tableName, String rowkey, String colFamily, String col) throws IOException {
        final Table table = connection.getTable(TableName.valueOf(namespace_prefix + tableName));
        Get get = new Get(Bytes.toBytes(rowkey));
        // 获取指定列族数据
        if (colFamily != null) {
            get.addFamily(Bytes.toBytes(colFamily));
            if (col != null) {
                get.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(col));
            }
        }
        return table.get(get);
    }


    public ResultScanner scan(String tableName, String colFamily, Map<String, String> col, String startRow, String stopRow, Filter filter) throws IOException {
        final Table table = connection.getTable(TableName.valueOf(namespace_prefix + tableName));
        Scan scan = new Scan();
        scan.setCaching(1000);
        if (colFamily != null)
            scan.addFamily(Bytes.toBytes(colFamily));
        if (col != null)
            for (Map.Entry<String, String> entry : col.entrySet())
                scan.addColumn(Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
        if (startRow != null)
            scan.setStartRow(Bytes.toBytes(startRow));
        if (stopRow != null)
            scan.setStopRow(Bytes.toBytes(stopRow));
        if (filter != null && filter.hasFilterRow())
            scan.setFilter(filter);
        ResultScanner rss = table.getScanner(scan);
        table.close();
        return rss;
    }

    public ResultScanner scan(String tableName, String colFamliy, Filter filter) throws IOException {
        return scan(tableName, colFamliy, null, null, null, filter);
    }

    public void transfer(String oldId, String newId,
                         String tablename, String[] colFamilies) throws IOException {
        for(String colFamily : colFamilies) {
            Result res = get(tablename, oldId, colFamily, null);
            NavigableMap<byte[], NavigableMap<byte[], byte[]>> map;
            if (res != null && (map = res.getNoVersionMap()) != null)
                insertRow(tablename, newId, colFamily, map.get(Bytes.toBytes(colFamily)));
        }
    }

    public boolean updateRowkey(String oldId, String newId,
                                String tablename, String[] colFamilies) throws IOException {
        transfer(oldId, newId, tablename, colFamilies);
        return HBaseHelper.GetInstance().deleteRowKey(oldId, tablename, null);
    }
}