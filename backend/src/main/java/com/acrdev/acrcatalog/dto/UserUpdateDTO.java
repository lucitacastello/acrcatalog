package com.acrdev.acrcatalog.dto;

import com.acrdev.acrcatalog.services.validation.UserUpdateValid;

@UserUpdateValid//nossa validation, verifica se o email já existe no DB
public class UserUpdateDTO extends UserDTO {


}
