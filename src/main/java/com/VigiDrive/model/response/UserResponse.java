package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long userId;

    private String fullName;

    private String avatar;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.fullName = user.getFirstName() + " " + user.getLastName();
        this.avatar = user.getAvatar();
    }
}
