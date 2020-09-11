package task;

import dataset.FaultDataset;
import dataset.IDataset;
import dataset.item.BugFixInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import repo.IProjectRepo;
import util.UtilsForRepo;

import java.io.File;

public class FaultLocalizeTask implements ITask{

    final static String bug_branch_prefix = "BugId-";
    FaultDataset dataset = new FaultDataset();
    @Override
    public void solve(IProjectRepo repo, String[] args) {
        if(args.length < 3) {
            System.err.println("Should give save_path for fault information.");
            throw new RuntimeException("Wrong arguments.");
        }
        String save_path = args[2];
        System.out.println("Task FL start.");
        Git git = new Git(repo.getRepository());
        Iterable<RevCommit> commits;
        try {
            commits = git.log().call();
            RevCommit previous_commit = null;
            int bug_id = 0;
            for(RevCommit commit : commits) {
                if (previous_commit != null) {
                    String msg = previous_commit.getFullMessage();
                    if (repo.isFix(msg)) {
                        BugFixInfo info = UtilsForRepo.getFixInfo(git, commit, previous_commit);
                        if (info.size() == 0) {
                            previous_commit = commit;
                            continue;
                        }
                        String branch_name = bug_branch_prefix + bug_id;
                        info.setBug_id(bug_id);
                        dataset.add(info);
                        git.branchCreate().setStartPoint(commit).setName(branch_name).setForce(true).call();
                        bug_id++;
                    }
                }
                previous_commit = commit;
            }
            System.out.println("Data size " + dataset.size() + " FL data export.");
            dataset.export(new File(save_path));
        } catch (GitAPIException e){
            e.printStackTrace();
        }
    }

}
