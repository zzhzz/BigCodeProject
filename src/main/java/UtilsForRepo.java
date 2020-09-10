import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class UtilsForRepo {

    static CanonicalTreeParser getTreeParser(Repository repository, RevCommit commit) {
        ObjectId treeId = commit.getTree().getId();
        try(ObjectReader reader = repository.newObjectReader()) {
            return new CanonicalTreeParser(null, reader, treeId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static BugFixInfo getFixInfo(Git git, RevCommit bug_commit, RevCommit fix_commit){
        Repository repository = git.getRepository();
        String msg = fix_commit.getFullMessage();
        BugFixInfo info = new BugFixInfo();
        info.setFixTime(fix_commit.getCommitTime());
        info.setMessage(msg);
        String commit_id = bug_commit.getId().name();
        CanonicalTreeParser fix_v_iter = getTreeParser(repository, fix_commit);
        CanonicalTreeParser bug_v_iter = getTreeParser(repository, bug_commit);
        OutputStream stream = DisabledOutputStream.INSTANCE;
        try (DiffFormatter formatter = new DiffFormatter(stream)) {
            formatter.setRepository(repository);
            List<DiffEntry> entries = formatter.scan(bug_v_iter, fix_v_iter);
            for (DiffEntry entry : entries) {
                FileHeader header = formatter.toFileHeader(entry);
                String fix_path = entry.getNewPath();
                String buggy_path = entry.getOldPath();
                EditList editList = header.toEditList();
                for (Edit edit : editList) {
                    int lineno_start = edit.getBeginA(), lineno_end = edit.getEndA();
                    if (edit.getType() == Edit.Type.REPLACE) {
                        if (!fix_path.equals(buggy_path)) {
                            System.out.println(fix_commit.getFooterLines());
                            throw new RuntimeException();
                        } else if(fix_path.endsWith(".java")){
                            info.addFaultyLine(buggy_path, lineno_start, lineno_end);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }
}
