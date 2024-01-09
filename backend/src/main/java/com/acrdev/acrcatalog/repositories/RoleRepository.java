package com.acrdev.acrcatalog.repositories;

import com.acrdev.acrcatalog.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    //para user signup - novo usuário - buscar as autorities
    Role findByAuthority(String authority);
}
