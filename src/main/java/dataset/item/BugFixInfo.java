package dataset.item;

import com.google.gson.annotations.Expose;

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
    private String fix_commit_id;

    @Expose
    private String bug_commit_id;

    @Expose
    private List<Map<String, String>> buggyLines = new ArrayList<>();

    public void setBug_id(int id) {
        this.bug_id = id;
    }

    public void setBugCommitId(String id) {
        this.bug_commit_id = id;
    }

    public void setFixCommitId(String id) {
        this.fix_commit_id = id;
    }

    public void setFixTime(int time) {
        this.time = time;
    }

    public void setMessage(String msg) {
        message = msg;
    }
    public void addFaultyLine(String path, int lineno_start, int lineno_end) {
        Map<String, String> info = new HashMap<>();
        info.put("filepath", path);
        info.put("start", String.valueOf(lineno_start));
        info.put("end", String.valueOf(lineno_end));
        buggyLines.add(info);
    }
    public int size() {
        return buggyLines.size();
    }


}
