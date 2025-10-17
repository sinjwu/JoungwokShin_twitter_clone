package com.example.backend.service;

import com.example.backend.entity.Like;
import com.example.backend.entity.Post;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.LikeRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public boolean toggleLike(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다"));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false;
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다"));

        return likeRepository.existsByUserAndPost(user, post);
    }
}
