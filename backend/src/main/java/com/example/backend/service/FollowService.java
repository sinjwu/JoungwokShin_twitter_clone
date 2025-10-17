package com.example.backend.service;

import com.example.backend.dto.UserResponse;
import com.example.backend.entity.Follow;
import com.example.backend.entity.User;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.FollowRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public boolean toggleFollow(Long targetUserId, String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("대상 사용자를 찾을 수 없습니다"));

        if (currentUser.getId().equals(targetUser.getId())) {
            throw new BadRequestException("자기 자신을 팔로우할 수 없습니다");
        }

        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(
                currentUser, targetUser
        );

        if (existingFollow.isPresent()) {
            followRepository.delete(existingFollow.get());
            return false;
        } else {
            Follow follow = new Follow();
            follow.setFollower(currentUser);
            follow.setFollowing(targetUser);
            followRepository.save(follow);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        List<Follow> follows = followRepository.findByFollowing(user);
        return follows.stream()
                .map(follow -> UserResponse.from(follow.getFollower()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        List<Follow> follows = followRepository.findByFollower(user);
        return follows.stream()
                .map(follow -> UserResponse.from(follow.getFollowing()))
                .toList();
    }
}