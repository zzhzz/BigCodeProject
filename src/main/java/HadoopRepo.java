import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class HadoopRepo implements IProjectRepo {

    String name;
    String path;
    Repository repository = null;
    List<BugFixInfo> bugFixInfos = new ArrayList<>();
    Git git = null;
    final static String bug_branch_prefix = "BugId-";
    final static String hadoop_regex = "(HADOOP|YARN|HDFS" +
            "|MAPREDUCE|MR|HDS|REDUCE|HADOP|MAPREUDUCE" +
            "|ADOOP|ARN|HBASE|HDDS|DFS|SUBMARINE|HDD|)(-| |_|=|.)*\\d";
    static Pattern hadoop_pattern = Pattern.compile(hadoop_regex, Pattern.CASE_INSENSITIVE);

    HadoopRepo(String name, String path){
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
            git = new Git(repository);
        } catch (IOException e) {
            System.err.println("Couldn't found repository " + this.path + "!");
            e.printStackTrace();
        }
    }

    @Override
    public void checkout_to(File directory, String branch_name) {
        System.out.println("Start Cloneing");
        try {
            Git.cloneRepository().setURI(this.path).setDirectory(directory)
                    .setBranchesToClone(Collections.singletonList("refs/heads/" + branch_name))
                    .setBranch("refs/heads/" + branch_name)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public Git getGit(){
        return git;
    }

    @Override
    public void init() {
        Iterable<RevCommit> commits;
        try {
            commits = git.log().call();
            RevCommit previous_commit = null;
            int bug_id = 0;
            for(RevCommit commit : commits) {
                if (previous_commit != null) {
                    String msg = previous_commit.getFullMessage();
                    if (isFix(msg)) {
                        BugFixInfo info = UtilsForRepo.getFixInfo(git, commit, previous_commit);
                        if (info.size() == 0) {
                            previous_commit = commit;
                            continue;
                        }
                        String branch_name = bug_branch_prefix + bug_id;
                        info.setBug_id(bug_id);
                        bugFixInfos.add(info);
                        git.branchCreate().setStartPoint(commit).setName(branch_name).setForce(true).call();
                        bug_id++;
                    }
                }
                previous_commit = commit;
            }
        } catch (GitAPIException e){
            e.printStackTrace();
        }
    }
    @Override
    public List<BugFixInfo> getBugFixInfos(){
        return bugFixInfos;
    }
}
