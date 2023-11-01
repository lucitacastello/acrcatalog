package com.acrdev.acrcatalog.repositories;

import com.acrdev.acrcatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
