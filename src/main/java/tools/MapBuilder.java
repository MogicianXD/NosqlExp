package tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapBuilder {
    private static final String TAG = "MapBuilder";
    Map<String, Object> map = new HashMap<>();

    public static MapBuilder create() {
        return new MapBuilder();
    }

    public static Map<String, Object> empty() {
        return MapBuilder.create().build();
    }


    public MapBuilder setResult(boolean bool) {
        map.put("result", bool);
        return this;
    }

    public MapBuilder setData(List list) {
        map.put("data", list);
        return this;
    }

    public Map<String, Object> build() {
//        map.put("timestamp", System.currentTimeMillis());
        return map;
    }


}
