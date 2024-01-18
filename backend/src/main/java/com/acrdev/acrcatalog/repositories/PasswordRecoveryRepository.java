package com.acrdev.acrcatalog.repositories;

import com.acrdev.acrcatalog.entities.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecover, Long> {

}
