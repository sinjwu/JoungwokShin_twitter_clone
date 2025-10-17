package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class RegisterRequest {
    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 30, message = "사용자명은 3~30자 사이여야 합니다")
    private String username;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일을 입력해야 합니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, message = "비밀번호는 최소 6자리 이상이어야 합니다")
    private String password;

    @NotBlank(message = "풀네임은 필수입니다")
    private String fullName;
}
