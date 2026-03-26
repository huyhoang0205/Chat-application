package com.huyhoang25.chatapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.huyhoang25.chatapp.dto.request.CreateUserRequest;
import com.huyhoang25.chatapp.dto.response.CreateUserResponse;
import com.huyhoang25.chatapp.entity.Role;
import com.huyhoang25.chatapp.entity.User;
import com.huyhoang25.chatapp.exception.AppException;
import com.huyhoang25.chatapp.exception.ErrorCode;
import com.huyhoang25.chatapp.repository.RoleRepository;
import com.huyhoang25.chatapp.repository.UserRepository;

import static com.huyhoang25.chatapp.constant.AppConstant.USER_ROLE;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserResponse createUser (CreateUserRequest request) {
        //df: rq {email,name,pw} -> check exsit{email} -> save user {email,name,pw} 
        // -> create role User if not exist -> add Role for user -> save user
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = User.builder()
                        .email(request.getEmail())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build();
        
        Role role = roleRepository.findByName(USER_ROLE)
                    .orElseGet(() -> roleRepository.save(Role.builder()
                                                                .name(USER_ROLE)
                                                                .build()));
        user.addRole(role);
        
        userRepository.save(user);

        return CreateUserResponse.builder()
        .username(user.getUsername())
        .email(user.getEmail())
        .build();
    }
}
