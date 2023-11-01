package com.acrdev.acrcatalog.dto;

import com.acrdev.acrcatalog.entities.User;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    //não vamos usar aqui - somente no DTO para inserir novo usuário
    //UserInsertDTO extends UserDTO
    // private String password;

    Set<RoleDTO> roles = new HashSet<>();
    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(User entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        //em User colocamos o fetch para trazer os perfis
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
