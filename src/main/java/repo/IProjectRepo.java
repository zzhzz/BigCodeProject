package repo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.util.List;

public interface IProjectRepo {
    boolean isFix(String msg);
    void buildRepo();
    void checkout_to(File directory, String brach_name);
    Repository getRepository();
    String getBuildCmd();
}
