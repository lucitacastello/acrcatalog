package com.acrdev.acrcatalog.dto;

import com.acrdev.acrcatalog.services.validation.UserInsertValid;

@UserInsertValid //nossa validation, verifica se o email já existe no DB
public class UserInsertDTO extends UserDTO {

    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
