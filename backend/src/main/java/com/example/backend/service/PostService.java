package com.example.backend.service;

import com.example.backend.dto.PostRequest;
import com.example.backend.dto.PostResponse;
import com.example.backend.entity.Post;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse createPost(PostRequest request, String username)
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

    Post post = new Post();
    post.setContent(request.getContent());
    post.setAuthor(author);

    Post savedPost = postRepository.save(post);
    return PostResponse.from(savedPost);

    @Transactional(readOnly = true)
    public Page<PostResponse> getPosts(Pageable pageable) {
        return postRepository.findAllWithAuthor(pageable).map(PostResponse::from);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findByIdWithAuthorAndLikes(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다"));
        return PostResponse.from(post);
    }

    public PostResponse updatePost(Long postId, PostRequest request, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다"));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("게시물을 수정할 권한이 없습니다");
        }

        post.setContent(request.getContent());
        Post updatedPost = postRepository.save(post);
        return PostResponse.from(updatedPost);
    }

    public void deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> ResourceNotFoundException("게시물을 찾을 수 없습니다"));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedException("게시물을 삭제할 권한이 없습니다");
        }

        postRepository.delete(post);
    }
}
