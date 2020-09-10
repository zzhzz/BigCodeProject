import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FaultDataset {

    @Expose
    List<BugFixInfo> dataList = new ArrayList<>();

    FaultDataset(List<BugFixInfo> list) {
        dataList.addAll(list);
    }

    void add(BugFixInfo info){
        dataList.add(info);
    }

    void export(File export_path) {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json_str = gson.toJson(dataList);
        try {
            FileWriter writer = new FileWriter(export_path);
            writer.write(json_str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
