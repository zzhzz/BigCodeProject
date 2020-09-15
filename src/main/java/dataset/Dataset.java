package dataset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import dataset.item.BugFixInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Dataset<T> {

    @Expose
    List<T> dataList = new ArrayList<>();

    public Dataset() {}

    public void add(T info){
        dataList.add(info);
    }

    public void addAll(Collection<T> collection){
        dataList.addAll(collection);
    }

    public void export(File export_path) {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json_str = gson.toJson(dataList);
        try {
            if(!export_path.exists()) {
                export_path.getParentFile().mkdirs();
            }
            FileWriter writer = new FileWriter(export_path);
            writer.write(json_str);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int size() {
        return dataList.size();
    }
}
