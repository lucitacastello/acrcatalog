package com.acrdev.acrcatalog.repositories;

import com.acrdev.acrcatalog.entities.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecover, Long> {

    //Consulta para encontrar o token não expirado

    @Query("SELECT obj FROM PasswordRecover obj WHERE obj.token = :token AND obj.expiration > :now")
    List<PasswordRecover> searchValidTokens(String token, Instant now);
}
