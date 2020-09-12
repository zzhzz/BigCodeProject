package repo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import repo.IProjectRepo;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CamelRepo implements IProjectRepo {

    final static String camel_regex = "(CAMEL)(-|.|=| )*\\d";
    final static String LANGUAGE = "JAVA";
    final static String BUILD_CMD = "mvn install -Dfastinstall";
    final static Pattern pattern = Pattern.compile(camel_regex, Pattern.CASE_INSENSITIVE);

    private Repository repository = null;
    private String path;
    private String name;

    public CamelRepo(String name, String path) {
        this.path = path;
        this.name = name;
    }

    @Override
    public boolean isFix(String msg) {
        Matcher matcher = pattern.matcher(msg);
        return matcher.find();
    }

    @Override
    public void buildRepo() {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        try{
            repository = repositoryBuilder.setGitDir(new File(this.path))
                    .readEnvironment()
                    .setGitDir(new File(this.path + "/.git/"))
                    .build();
        } catch (IOException e) {
            System.err.println("Couldn't found repository " + this.path + "!");
            e.printStackTrace();
        }

    }

    @Override
    public void checkout_to(File directory, String branch_name) {
        System.out.println("Start Cloning " + this.name);
        try {
            Git.cloneRepository().setURI(this.path).setDirectory(directory)
                    .setBranchesToClone(Collections.singletonList("refs/heads/" + branch_name))
                    .setBranch("refs/heads/" + branch_name)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Repository getRepository() {
        return repository;
    }

    @Override
    public String getBuildCmd() {
        return BUILD_CMD;
    }
}
