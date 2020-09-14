package repo;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class DefaultRepo implements IProjectRepo {

    protected String name;
    protected String path;
    protected String URL;
    protected String BUILD_CMD;
    protected String LANGUAGE;
    protected Repository repository;
    protected Pattern pattern;

    DefaultRepo(String name, String path, String language, String URL, String BUILD_CMD, Pattern pattern) {
        this.URL = URL;
        this.BUILD_CMD = BUILD_CMD;
        this.pattern = pattern;
        this.name = name;
        this.path = path;
        this.LANGUAGE = language;
    }

    @Override
    public void cloneRepo() {
        if(URL == null){
            throw new NoSuchFieldError("URL");
        }
        File pathF = new File(path);
        if(!pathF.exists()) {
            System.out.println("Repo " + path + " not exists.");
            try {
                Git.cloneRepository().setURI(URL).setDirectory(pathF).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Repo " + path + " exists.");
        }
    }

    @Override
    public boolean isFix(String msg) {
        if(pattern == null) {
            throw new NoSuchFieldError("pattern");
        }
        Matcher matcher = pattern.matcher(msg.trim());
        return matcher.find();
    }

    @Override
    public void buildRepo() {
        if(this.path == null || this.name == null){
            throw new NoSuchFieldError("path and name");
        }
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        try{
            repository = repositoryBuilder.setGitDir(new File(this.path))
                    .readEnvironment()
                    .setGitDir(new File(this.path + "/.git/"))
                    .build();
        } catch (IOException e) {
            System.err.println("Couldn't found repository " + this.path + "!");
            e.printStackTrace();
        }
    }

    @Override
    public void checkout_to(File directory, String branch_name) {
        if(this.path == null || this.name == null){
            if(URL == null){
                throw new NoSuchFieldError("URL");
            }
        }
        System.out.println("Start Cloning " + this.name);
        try {
            Git.cloneRepository().setURI(this.path).setDirectory(directory)
                    .setBranchesToClone(Collections.singletonList("refs/heads/" + branch_name))
                    .setBranch("refs/heads/" + branch_name)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Repository getRepository() {
        if(repository == null) {
            throw new NoSuchFieldError("repository, should invoke build_repo() first");
        }
        return repository;
    }


    @Override
    public String getBuildCmd() {
        if(BUILD_CMD == null){
            throw new NoSuchFieldError("BUILD_CMD, should specify in your class");
        }
        return BUILD_CMD;
    }

    @Override
    public String getLanguage() {
        return LANGUAGE;
    }
}
