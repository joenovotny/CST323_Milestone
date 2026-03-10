package com.gcu.inventoryapp.repository;

import com.gcu.inventoryapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}