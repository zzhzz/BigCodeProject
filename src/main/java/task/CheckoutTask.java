package task;

import repo.IProjectRepo;

import java.io.File;

public class CheckoutTask implements ITask {
    @Override
    public void solve(IProjectRepo repo, String[] args) {
        if(args.length < 4) {
            throw new RuntimeException("CheckoutTask: should provide branch_name and checkout_path");
        }
        String branch_name = args[2], save_path = args[3];
        repo.checkout_to(new File(save_path), branch_name);
    }

}
