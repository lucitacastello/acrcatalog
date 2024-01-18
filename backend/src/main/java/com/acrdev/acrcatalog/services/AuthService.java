package com.acrdev.acrcatalog.services;

import com.acrdev.acrcatalog.dto.EmailDTO;
import com.acrdev.acrcatalog.entities.PasswordRecover;
import com.acrdev.acrcatalog.entities.User;
import com.acrdev.acrcatalog.repositories.PasswordRecoveryRepository;
import com.acrdev.acrcatalog.repositories.UserRepository;
import com.acrdev.acrcatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    // configuração no application.properties
    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    // link para recupara a senha - config. application.properties
    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoveryRepository passwordRecoveryRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void createRecoverToken(EmailDTO body) {

        User user = userRepository.findByEmail(body.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("E-mail não encontrado");
        }

        String token = UUID.randomUUID().toString();
        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        entity.setToken(token); //gera um token aleatório
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L)); // * 60 para segundos

        entity = passwordRecoveryRepository.save(entity);

        String bodyMessage = "Acesse o link para definir uma nova senha\n\n"
                + recoverUri + token ;

        // envia email para recuperação com o token
        emailService.sendEmail(body.getEmail(),"Recuperação de senha", bodyMessage  );
    }
}
