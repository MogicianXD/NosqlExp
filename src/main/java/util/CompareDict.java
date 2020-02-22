package util;

import org.apache.hadoop.hbase.filter.CompareFilter;

public class CompareDict {
    public static boolean compare(String cmp, double v1, double v2) throws Exception {
        switch (cmp) {
            case "lt":
                return v1 < v2;
            case "le":
                return v1 <= v2;
            case "eq":
                return v1 == v2;
            case "ne":
                return v1 != v2;
            case "ge":
                return v1 >= v2;
            case "gt":
                return v1 > v2;
            default:
                throw new Exception("Invalid compare operator : " + cmp);
        }
    }

    public static CompareFilter.CompareOp trans2Opt(String cmp) throws Exception {
        switch (cmp) {
            case "lt":
                return CompareFilter.CompareOp.LESS;
            case "le":
                return CompareFilter.CompareOp.LESS_OR_EQUAL;
            case "eq":
                return CompareFilter.CompareOp.EQUAL;
            case "ne":
                return CompareFilter.CompareOp.NOT_EQUAL;
            case "ge":
                return CompareFilter.CompareOp.GREATER_OR_EQUAL;
            case "gt":
                return CompareFilter.CompareOp.GREATER;
            default:
                throw new Exception("Invalid compare operator : " + cmp);
        }
    }

}
