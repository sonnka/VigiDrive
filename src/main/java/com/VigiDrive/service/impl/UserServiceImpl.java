package com.VigiDrive.service.impl;

import com.VigiDrive.config.CustomOAuth2User;
import com.VigiDrive.model.entity.User;
import com.VigiDrive.model.enums.Provider;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.repository.UserRepository;
import com.VigiDrive.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public void processGoogleOAuthPostLogin(CustomOAuth2User oAuth2User) {
        User existUser = userRepository.findByEmailIgnoreCase(oAuth2User.getEmail()).orElse(null);

        var name = oAuth2User.getName().split(" ");

        if (existUser == null) {
            User newUser = new User();
            newUser.setFirstName(name[0]);
            newUser.setLastName(name[1]);
            newUser.setEmail(oAuth2User.getEmail());
            newUser.setProvider(Provider.GOOGLE);
            newUser.setRole(Role.DRIVER);
            userRepository.save(newUser);
        }
    }
}
