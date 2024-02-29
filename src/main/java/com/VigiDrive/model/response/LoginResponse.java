package com.VigiDrive.model.response;

import lombok.Builder;

@Builder
public record LoginResponse(

        Long id,

        String token,

        String name,

        String surname,

        String role,

        String avatar
) {
}
