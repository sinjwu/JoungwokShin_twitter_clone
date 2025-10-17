package com.example.backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String profileImageUrl;

    private String accessToken;
    private String refreshToken;
}
