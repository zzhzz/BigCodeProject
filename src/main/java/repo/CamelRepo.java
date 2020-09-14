package repo;

import com.google.re2j.Pattern;

public class CamelRepo extends DefaultRepo implements IProjectRepo {

    final static String regex = "((CAMEL|CAME|CAMWL|)(-|.|=| )*\\d)|fix";
    final public static String LANGUAGE = "JAVA";
    final static String BUILD_CMD = "mvn package -Dfastinstall";
    final static String URL = "https://github.com/apache/camel.git";
    final static Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    public CamelRepo(String name, String path) {
        super(name, path, LANGUAGE, URL, BUILD_CMD, pattern);
        cloneRepo();
    }
}
