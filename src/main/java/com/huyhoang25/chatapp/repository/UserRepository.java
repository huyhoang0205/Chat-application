package com.huyhoang25.chatapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.huyhoang25.chatapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email); 

    Optional<User> findByEmail(String email);
    
} 
