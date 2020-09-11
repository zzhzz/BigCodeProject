package task;

import dataset.IDataset;
import repo.IProjectRepo;

public interface ITask {
    void solve(IProjectRepo repo, String[] args);
}
