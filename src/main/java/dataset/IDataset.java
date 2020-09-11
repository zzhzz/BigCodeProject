package dataset;

import java.io.File;

public interface IDataset {
    void export(File export_file);
    int size();
}
