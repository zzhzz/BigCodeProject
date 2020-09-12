package repo;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import repo.IProjectRepo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HadoopRepo implements IProjectRepo {

    String name;
    String path;
    public final static String LANGUAGE = "JAVA";
    public final static String BUILD_CMD = "mvn package -Pdist -DskipTests";
    Repository repository = null;
    final static String hadoop_regex = "(HADOOP|YARN|HDFS" +
            "|MAPREDUCE|MR|HDS|REDUCE|HADOP|MAPREUDUCE" +
            "|ADOOP|ARN|HBASE|HDDS|DFS|SUBMARINE|HDD|)(-| |_|=|.)*\\d";
    static Pattern hadoop_pattern = Pattern.compile(hadoop_regex, Pattern.CASE_INSENSITIVE);

    public HadoopRepo(String name, String path){
        this.name = name;
        this.path = path;
    }

    @Override
    public boolean isFix(String msg) {
        Matcher matcher = hadoop_pattern.matcher(msg.trim());
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
