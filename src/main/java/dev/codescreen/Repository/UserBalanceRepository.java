package dev.codescreen.Repository;
import dev.codescreen.Entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
    UserBalance findByUserId(String userId) ;
}

