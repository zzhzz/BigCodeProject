package repo;

import com.google.re2j.Pattern;

public class HadoopRepo extends DefaultRepo implements IProjectRepo {
    final public static String LANGUAGE = "JAVA";
    final static String BUILD_CMD = "mvn package -Pdist -DskipTests -Dmaven.javadoc.skip=true";
    final static String URL = "https://github.com/apache/hadoop.git";
    final static String regex = "(HADOOP|YARN|HDFS" +
            "|MAPREDUCE|MR|HDS|REDUCE|HADOP|MAPREUDUCE" +
            "|ADOOP|ARN|HBASE|HDDS|DFS|SUBMARINE|HDD|)(-| |_|=|.)*\\d";
    static Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    public HadoopRepo(String name, String path)  {
        super(name, path, LANGUAGE, URL, BUILD_CMD, pattern);
        cloneRepo();
    }
}
