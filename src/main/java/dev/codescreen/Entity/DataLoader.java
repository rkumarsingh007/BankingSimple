package dev.codescreen.Entity;

import dev.codescreen.Repository.UserBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {

    @Autowired
    private UserBalanceRepository userRepository;

    public DataLoader(UserBalanceRepository userRepository) {
        this.userRepository = userRepository;
        LoadUsers();
    }

    private void LoadUsers() {

        List<UserBalance> a = new ArrayList<>();
        a.add(new UserBalance(100.00, "user-1", "USD"));
        a.add(new UserBalance(1202.00, "user-2", "USD"));
        a.add(new UserBalance(190.00, "user-3", "USD"));
        a.add(new UserBalance(1500.00, "user-4", "INR"));
        System.out.println("\n\n\nFollowing data is been added to the database: \n"+a+"\n\n\n");
        userRepository.saveAll(a);
    }
}