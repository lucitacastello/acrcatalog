package com.acrdev.acrcatalog.dto;

import com.acrdev.acrcatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid //nossa validation, verifica se o email já existe no DB
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Campo obrigatório")
    @Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
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
