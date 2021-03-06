package util;

import repo.CamelRepo;
import repo.HadoopRepo;
import repo.IProjectRepo;
import repo.MySQLRepo;
import task.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Main {


    static Map<String, Class<? extends IProjectRepo>> classMap = new HashMap<>();
    static Map<String, Class<? extends ITask>> taskMap = new HashMap<>();

    static {
        classMap.put("hadoop", HadoopRepo.class);
        classMap.put("camel", CamelRepo.class);
        classMap.put("mysql", MySQLRepo.class);

        taskMap.put("fl", FaultLocalizeTask.class);
        taskMap.put("checkout", CheckoutTask.class);
        taskMap.put("maven-build", MavenBuildTask.class);
        taskMap.put("ast", ASTgenTask.class);
    }


    public static void registerRepo(String name, Class<? extends IProjectRepo> claz) {
        classMap.put(name, claz);
    }

    public static void registerTask(String name, Class<? extends ITask> claz) {
        taskMap.put(name, claz);
    }

    public static void main(String[] args) {
        if(args.length < 2)  {
            throw new RuntimeException("Usage: java -jar BigCode.jar [project_name] [task_name] [task_args]");
        }
        String repo_name = args[0], task_name = args[1], base_path = args[2];
        String repo_path = base_path + "/" + repo_name + "/";
        IProjectRepo repo = null;
        ITask task = null;
        try {
            repo = classMap.get(repo_name)
                        .getConstructor(String.class, String.class)
                        .newInstance(repo_name, repo_path);
            repo.buildRepo();
            task = taskMap.get(task_name).getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        task.solve(repo, args);
    }
}
