package com.gmail.apachdima.asfosis.user.repository;

import com.gmail.apachdima.asfosis.user.model.Role;
import com.gmail.apachdima.asfosis.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByRole(UserRole user);
}
