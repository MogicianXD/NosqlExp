package util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LoadArgHelper {
    public static Map<String, String> loadArgs(HttpServletRequest req, String[] cols, String[][] cmps) {
        Map<String, String> args = new HashMap<>();
        loadCols(args, req, cols, false);
        loadCmps(args, req, cmps);
        return args;
    }

    public static Map<String, String> loadArgs(HttpServletRequest req, String[] cols, String[][] cmps, boolean canClear) {
        Map<String, String> args = new HashMap<>();
        loadCols(args, req, cols, canClear);
        loadCmps(args, req, cmps);
        return args;
    }

    public static Map<String, String> loadCols(HttpServletRequest req, String[] cols) {
        Map<String, String> args = new HashMap<>();
        return loadCols(args, req, cols, false);
    }


    public static Map<String, String> loadCols(HttpServletRequest req, String[] cols, boolean canClear) {
        Map<String, String> args = new HashMap<>();
        return loadCols(args, req, cols, canClear);
    }

    public static Map<String, String> loadCols(Map<String, String> args, HttpServletRequest req, String[] cols, boolean canClear) {
        for (String key : cols) {
            String val = req.getParameter(key);
            if (val != null && (canClear || !val.equals("")))
                args.put(key, val);
        }
        return args;
    }

    public static Map<String, String> loadCmps(HttpServletRequest req, String[][] cmps) {
        Map<String, String> args = new HashMap<>();
        return loadCmps(args, req, cmps);
    }

    public static Map<String, String> loadCmps(Map<String, String> args, HttpServletRequest req, String[][] cmps) {
        for (String[] cmp : cmps) {
            String val = req.getParameter(cmp[1]);
            if (val != null && !val.equals(""))
                args.put(cmp[1], val);
        }
        return args;
    }

}
