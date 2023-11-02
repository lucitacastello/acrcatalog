package com.acrdev.acrcatalog.services.validation;

import com.acrdev.acrcatalog.controllers.exceptions.FieldMessage;
import com.acrdev.acrcatalog.dto.UserInsertDTO;
import com.acrdev.acrcatalog.dto.UserUpdateDTO;
import com.acrdev.acrcatalog.entities.User;
import com.acrdev.acrcatalog.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//nossa implementação da validação

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    //HttpServletRequest guarda as informações da requisição
    @Autowired
    private HttpServletRequest request; //pegar o Id para atualização

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserUpdateValid ann) {
        ConstraintValidator.super.initialize(ann);
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        //pega os atribuitos da URL
        @SuppressWarnings("unchecked")
        var uriVars =(Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        //id passado na requisição
        long userId = Long.parseLong(uriVars.get("id"));



        List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista

        //verifica se id do usuário não é o da atualização,
        //tentando atualizar o mesmo email de um usário que já existe
        User user = repository.findByEmail(dto.getEmail());
        if (user != null && userId != user.getId()) {
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
