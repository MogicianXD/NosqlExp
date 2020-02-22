package tools;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonBuilder {
    public static final String KEY_DATA = "data";
    public static final String KEY_CACHE = "cache";
    public static final String KEY_RESULT = "res";
    public static final String KEY_RET = "ret";
    public static final String KEY_UPDATEINFO = "updateinfo";
    public static final String KEY_GENETIME = "genetime";
    public static final String KEY_STUDENT = "student";
    public static final String KEY_TEACHER = "teacher";
    public static final String KEY_COURSE = "course";
    public static final String KEY_SC = "elect";
    public static final String KEY_TC = "teach";
    public static final String KEY_ANALYSE = "analysis";


    JSONObject jsonObject = new JSONObject();
    JSONObject data = new JSONObject();

    public static JsonBuilder create() {
        return new JsonBuilder();
    }

    public JsonBuilder setRet(int value) {
        jsonObject.put(KEY_RET, value);
        return this;
    }

    public JsonBuilder setMsg(String value) {
        jsonObject.put("msg", value);
        return this;
    }

    public JsonBuilder setCache(long cache) {
        data.put(KEY_CACHE, cache);
        return this;
    }


    public JsonBuilder setResult(boolean result) {
        data.put(KEY_RESULT, result);
        return this;
    }


    public String buildString() {
        return buildJson().toJSONString();
    }

    public JSONObject buildJson() {
        jsonObject.put(KEY_DATA, data);
        return jsonObject;
    }

    public static JsonBuilder geneErrJson(int err, String msg) {
        return create().setRet(err).setMsg(msg).setResult(false);
    }

    public static JsonBuilder geneSuccessJson() {
        return create().setRet(200);
    }

    public JsonBuilder setGeneTime(int time) {
        data.put(KEY_GENETIME, time);
        return this;
    }

    public JsonBuilder setUpdateInfo(String info) {
        // TODO Auto-generated method stub
        data.put(KEY_UPDATEINFO, info);
        return this;
    }

    public JsonBuilder setStudent(List list) {
        data.put(KEY_STUDENT, list);
        return this;
    }

    public JsonBuilder setTeacher(List list) {
        data.put(KEY_TEACHER, list);
        return this;
    }

    public JsonBuilder setCourse(List list) {
        data.put(KEY_COURSE, list);
        return this;
    }

    public JsonBuilder setTeach(List list) {
        data.put(KEY_TC, list);
        return this;
    }

    public JsonBuilder setElect(List list) {
        data.put(KEY_SC, list);
        return this;
    }

    public JsonBuilder setAnalysis(List list) {
        data.put(KEY_ANALYSE, list);
        return this;
    }
}
