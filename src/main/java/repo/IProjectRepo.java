package repo;

import com.google.re2j.Matcher;
import org.eclipse.jgit.lib.Repository;

import java.io.File;

public interface IProjectRepo {
    Matcher isFix(String msg);
    void buildRepo();
    void checkout_to(File directory, String brach_name);
    void cloneRepo();
    Repository getRepository();
    String getBuildCmd();
    String getLanguage();
}
