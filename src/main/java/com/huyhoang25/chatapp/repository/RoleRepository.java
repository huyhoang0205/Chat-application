package com.huyhoang25.chatapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.huyhoang25.chatapp.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,String>{
    Optional<Role> findByName(String name);
}
