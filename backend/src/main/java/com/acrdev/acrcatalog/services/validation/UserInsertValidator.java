package com.acrdev.acrcatalog.services.validation;

import com.acrdev.acrcatalog.controllers.exceptions.FieldMessage;
import com.acrdev.acrcatalog.dto.UserInsertDTO;
import com.acrdev.acrcatalog.entities.User;
import com.acrdev.acrcatalog.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

//nossa implementação da validação

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {


    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
        ConstraintValidator.super.initialize(ann);
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista

        //validando o email
        User user = repository.findByEmail(dto.getEmail());
        if(user != null){
            //add erro
            list.add(new FieldMessage("email", "E-mail já existe"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
