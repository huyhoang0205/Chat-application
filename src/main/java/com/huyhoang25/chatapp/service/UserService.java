package com.huyhoang25.chatapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.huyhoang25.chatapp.dto.request.CreateUserRequest;
import com.huyhoang25.chatapp.dto.response.CreateUserResponse;
import com.huyhoang25.chatapp.dto.response.PageResponse;
import com.huyhoang25.chatapp.dto.response.UserDetailResponse;
import com.huyhoang25.chatapp.entity.Role;
import com.huyhoang25.chatapp.entity.User;
import com.huyhoang25.chatapp.exception.AppException;
import com.huyhoang25.chatapp.exception.ErrorCode;
import com.huyhoang25.chatapp.repository.RoleRepository;
import com.huyhoang25.chatapp.repository.UserRepository;

import static com.huyhoang25.chatapp.constant.AppConstant.USER_ROLE;

import java.util.List;

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

    public UserDetailResponse myInfo(String userId) {

        return userRepository.findById(userId)
            .map(user -> UserDetailResponse.builder()
                            .userId(user.getId())
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .build())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public PageResponse<UserDetailResponse> searchUsers(String keyword, int page, int size) {
        // 1. Lấy thông tin current user từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String userId = authentication.getName();

        // 2. Tạo Pageable object
        Pageable pageable = PageRequest.of(page -1, size);
        String searchKey = keyword == null ? "" : keyword;
        // 3. Search users từ database
        Page<User> userpage = userRepository.searchUser(searchKey, pageable);

        // 4. Filter để loại bỏ current user và map sang DTO
        List<UserDetailResponse> content = userpage.getContent()
            .stream()
            .filter(user -> !user.getId().equals(userId))
            .map(user -> UserDetailResponse.builder()
                            .userId(user.getId())
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .build())
            .toList();

        // 5. Build PageResponse
        return PageResponse.<UserDetailResponse>builder()
        .currentPage(page)
        .pageSize(pageable.getPageSize())
        .totalPage(userpage.getTotalPages())
        .totalElement(userpage.getTotalElements())
        .content(content)
        .build();
    }
}
