package com.example.backend.dto;

import com.example.backend.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private UserDto user;
    private LocalDateTime createdAt;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(UserDto.fromEntity(comment.getUser()))
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
