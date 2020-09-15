package task;

import repo.IProjectRepo;

import java.io.File;

public class CheckoutTask implements ITask {
    @Override
    public void solve(IProjectRepo repo, String[] args) {
        if(args.length < 5) {
            throw new RuntimeException("CheckoutTask: should provide branch_name and checkout_path");
        }
        String branch_name = args[3], save_path = args[4];
        repo.checkout_to(new File(save_path), branch_name);
    }

}
