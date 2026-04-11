package nl.fontys.newswebapplication.repositories;

import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity getAccountEntityById(int id);
    AccountEntity getAccountEntityByUsername(String username);
    boolean existsByUsername(String username);
}
