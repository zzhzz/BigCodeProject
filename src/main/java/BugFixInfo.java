import com.google.gson.annotations.Expose;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BugFixInfo {
    @Expose
    private String message;

    @Expose
    private int time;

    @Expose
    private int bug_id;

    @Expose
    private List<Map<String, String>> buggyLines = new ArrayList<>();

    void setBug_id(int id) {
        this.bug_id = id;
    }

    void setFixTime(int time) {
        this.time = time;
    }

    void setMessage(String msg) {
        message = msg;
    }
    void addFaultyLine(String path, int lineno_start, int lineno_end) {
        Map<String, String> info = new HashMap<>();
        info.put("filepath", path);
        info.put("start", String.valueOf(lineno_start));
        info.put("end", String.valueOf(lineno_end));
        buggyLines.add(info);
    }
    int size() {
        return buggyLines.size();
    }


}
