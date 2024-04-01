package com.VigiDrive.model.response;

import com.VigiDrive.config.CustomAuthenticationToken;
import lombok.Builder;

@Builder
public record LoginResponse(

        Long id,

        CustomAuthenticationToken token,

        String role
) {
}
