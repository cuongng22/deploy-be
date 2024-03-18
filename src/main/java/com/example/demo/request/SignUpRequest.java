package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    private Long id;

    private String username;

    private String password;

    private String confirmPassword;

    private String name;

    private String gender;

    private String email;

    private MultipartFile avatar;

    private MultipartFile background;
}
