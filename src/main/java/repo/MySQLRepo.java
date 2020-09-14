package repo;

import com.google.re2j.Pattern;

public class MySQLRepo extends DefaultRepo {
    final static String regex = "(Bug)(#| |-|=|.)*\\d";
    final public static String LANGUAGE = "C/C++";
    final static String BUILD_CMD = "";
    final static String URL = "https://github.com/mysql/mysql-server";
    final static Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    MySQLRepo(String name, String path) {
        super(name, path, LANGUAGE, URL, BUILD_CMD, pattern);
    }
}
