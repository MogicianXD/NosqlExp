package dao;

import db.HBaseHelper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import util.CompareDict;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class EntityDao {

    protected EntityDao() {
    }

    protected List Get(Map<String, String> args, String tablename, String rowkey,
                       String colFamily, String[] cols, String[][] cmps) {
        ArrayList result = new ArrayList<>();
        try {
            String id = args.get(rowkey);
            if (id != null) {
                Result res = HBaseHelper.GetInstance().get(tablename, id, colFamily, null);
                if (res == null || res.getNoVersionMap() == null)
                    return null;
                NavigableMap<byte[], byte[]> map = res.getNoVersionMap().get(Bytes.toBytes(colFamily));
                if (map != null && eqAll(args, map, cols) && cmpSuccess(args, map, cmps))
                    result.add(pack(map, id));
            } else {
                FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                String opt = null;
                String value = null;
                for (String[] cmp : cmps)
                    if ((opt = args.get(cmp[0])) != null && (value = args.get(cmp[1])) != null) {
                        filterList.addFilter(new SingleColumnValueFilter(
                                Bytes.toBytes(colFamily),
                                Bytes.toBytes(cmp[1]),
                                CompareDict.trans2Opt(opt),
                                Bytes.toBytes(value)));
                        args.remove(cmp[0]);
                        args.remove(cmp[1]);
                    }
                for (Map.Entry<String, String> entry : args.entrySet()) {
                    filterList.addFilter(
                            new SingleColumnValueFilter(
                                    Bytes.toBytes(colFamily),
                                    Bytes.toBytes(entry.getKey()),
                                    CompareFilter.CompareOp.EQUAL,
                                    Bytes.toBytes(entry.getValue())));
                }
                ResultScanner scanner = HBaseHelper.GetInstance().scan(tablename, colFamily, filterList);
                for (Result res : scanner) {
                    NavigableMap<byte[], byte[]> map = res.getNoVersionMap().get(Bytes.toBytes(colFamily));
                    if (map != null)
                        result.add(pack(map, new String(res.getRow())));
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Object GetRow(String id, String tablename, String rowkey,
                       String colFamily) {
        try {
            if (id != null) {
                Result res = HBaseHelper.GetInstance().get(tablename, id, colFamily, null);
                if (res == null || res.getNoVersionMap() == null)
                    return null;
                NavigableMap<byte[], byte[]> map = res.getNoVersionMap().get(Bytes.toBytes(colFamily));
                if (map != null)
                    return pack(map, id);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected boolean Insert(String id, Map<String, String> col2val, String tablename,
                             String colFamily) {
        try {
            if (col2val == null || col2val.size() == 0)
                return false;
            HBaseHelper.GetInstance().insertRow(tablename, id, colFamily, col2val);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean Update(String id, Map<String, String> col2val, String tablename,
                             String colFamily) {
        try {
            if (col2val == null || col2val.size() == 0)
                return false;
            ArrayList<String> cols4delete = new ArrayList<>();
            for(Map.Entry<String, String> entry: col2val.entrySet()) {
                if(entry.getValue().equals("")){
                    cols4delete.add(entry.getKey());
                    col2val.remove(entry.getKey());
                }
            }
            HBaseHelper.GetInstance().insertRow(tablename, id, colFamily, col2val);
            if(cols4delete.size() > 0)
                HBaseHelper.GetInstance().deleteRow(tablename, id, colFamily, cols4delete);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean UpdateRowKey(String oldId, String newId,
                                String tablename, String colFamily, String otherFamily) {

        try {
            return HBaseHelper.GetInstance()
                    .updateRowkey(oldId, newId, tablename,
                            new String[]{colFamily, otherFamily});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean eqAll(Map<String, String> args, NavigableMap<byte[], byte[]> map,
                            String[] cols) {

        for (String key : cols)
            if (args.containsKey(key) &&
                    !args.get(key).equals(getString(map, key)))
                return false;
        return true;
    }

    protected boolean cmpSuccess(Map<String, String> args, NavigableMap<byte[], byte[]> map,
                                 String[][] cmps) throws Exception {
        String opt = null;
        String value = null;
        for (String[] cmp : cmps)
            if ((opt = args.get(cmp[0])) != null
                    && (value = args.get(cmp[1])) != null
                    && !CompareDict.compare(opt,
                    Integer.parseInt(getString(map, cmp[1])),
                    Integer.parseInt(value)))
                return false;
        return true;
    }

    public abstract Object pack(NavigableMap<byte[], byte[]> map, String id);

    protected String getString(NavigableMap<byte[], byte[]> map, String key) {
        byte[] bs = map.get(Bytes.toBytes(key));
        if (bs == null)
            return null;
        return new String(bs, StandardCharsets.UTF_8);
    }

}
