package com.example.backend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponse {

    private boolean isFollowing;

    private Long followersCount;

    private Long followingCount;
}
