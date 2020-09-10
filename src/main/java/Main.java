import java.io.File;

public class Main {
    public static void main(String[] args) {
        String repo_name = args[0], cmd = args[1];
        String repo_path = "/home/zzhzz/Documents/BigCodeProject/" + repo_name + "/";
        IProjectRepo repo = new HadoopRepo("Hadoop", repo_path);
        repo.buildRepo();
        if(cmd.equals("init")) {
            repo.init();
            FaultDataset dataset = new FaultDataset(repo.getBugFixInfos());
            dataset.export(new File("./buggy_lines/" + repo_name + ".json"));
        }
    }
}
