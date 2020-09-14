package task;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import dataset.ASTDataset;
import dataset.item.ASTInfo;
import repo.IProjectRepo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ASTgenTask implements ITask {

    ASTDataset dataset = new ASTDataset();

    @Override
    public void solve(IProjectRepo repo, String[] args) {
        if(args.length < 4) {
            System.err.println("Should specify AST save path and target project path");
            throw new RuntimeException();
        }
        String save_path = args[2], project_path = args[3];
        if(repo.getLanguage().equals("JAVA")) {
            ParserConfiguration configuration = new ParserConfiguration();
            configuration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8);
            JavaParser parser = new JavaParser(configuration);
            try(Stream<Path> paths = Files.walk(Paths.get(project_path))) {
                List<Path> pathList = paths.collect(Collectors.toList());
                for(Path path : pathList) {
                    ParseResult<CompilationUnit> result = parser.parse(path);
                    if(result.isSuccessful()){
                        Optional<CompilationUnit> unit = result.getResult();
                        unit.ifPresent(compilationUnit -> dataset.add(new ASTInfo(compilationUnit)));
                    } else {
                        System.err.println("JavaParser: Parsing failed for " + path);
                        throw new RuntimeException();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(repo.getLanguage().equals("C/C++")){

        }
        dataset.export(new File(save_path));
    }
}
