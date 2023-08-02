package com.ABC.Bank.Repository;

import com.ABC.Bank.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom query methods here if needed
    User findByUserName(String userName);


}