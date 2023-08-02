package com.ABC.Bank.Service;

import com.ABC.Bank.Entity.User;
import com.ABC.Bank.Repository.LoanApplicationRepository;
import com.ABC.Bank.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

// Other methods using UserRepository as needed
}

