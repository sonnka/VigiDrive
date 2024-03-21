package com.VigiDrive.config;

import com.VigiDrive.model.entity.User;
import com.VigiDrive.model.enums.Provider;
import com.VigiDrive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public void processOAuthPostLogin(String username) {
        User existUser = repo.findByEmailIgnoreCase(username).orElse(null);

        if (existUser == null) {
            User newUser = new User();
            newUser.setEmail(username);
            newUser.setProvider(Provider.GOOGLE);

            repo.save(newUser);
        }

    }

}
