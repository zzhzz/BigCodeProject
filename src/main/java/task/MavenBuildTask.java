package task;
import repo.IProjectRepo;
import util.Main;

import java.io.File;
import java.io.IOException;

public class MavenBuildTask implements ITask {
    @Override
    public void solve(IProjectRepo repo, String[] args) {
        if(args.length < 4) {
            throw new RuntimeException("BuildTask: please provide project path and JAVA_HOME environment");
        }
        String project_path = args[2], java_home = args[3];
        String[] cmds = {"/bin/bash", "-c", repo.getBuildCmd()};
        ProcessBuilder builder = new ProcessBuilder();
        try {
            builder.environment().put("JAVA_HOME", java_home);
            Process process = builder.command(cmds)
                                .directory(new File(project_path))
                                .inheritIO().start();
            int value = process.waitFor();
            if(value != 0){
                throw new RuntimeException("Some Error Happens when building.");
            } else {
                System.out.println("Build Task Done");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
