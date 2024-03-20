package com.VigiDrive.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateManagerRequest {
    
    private String avatar;

    @Size(min = 2, max = 24)
    @NotEmpty
    @NotNull
    private String firstName;

    @Size(min = 2, max = 24)
    @NotEmpty
    @NotNull
    private String lastName;

    @NotEmpty
    @NotNull
    @Size(min = 5, max = 24)
    private String password;
}
