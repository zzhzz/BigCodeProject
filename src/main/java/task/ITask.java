package task;

import repo.IProjectRepo;

public interface ITask {
    void solve(IProjectRepo repo, String[] args);
}
