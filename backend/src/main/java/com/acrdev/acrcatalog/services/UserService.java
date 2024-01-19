package com.acrdev.acrcatalog.services;

import com.acrdev.acrcatalog.dto.RoleDTO;
import com.acrdev.acrcatalog.dto.UserDTO;
import com.acrdev.acrcatalog.dto.UserInsertDTO;
import com.acrdev.acrcatalog.dto.UserUpdateDTO;
import com.acrdev.acrcatalog.entities.Role;
import com.acrdev.acrcatalog.entities.User;
import com.acrdev.acrcatalog.projections.UserDetailsProjection;
import com.acrdev.acrcatalog.repositories.RoleRepository;
import com.acrdev.acrcatalog.repositories.UserRepository;
import com.acrdev.acrcatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder; //na classe AppConfig

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> page = repository.findAll(pageable);
        return page.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Entity not found");
        }
        User user = repository.getReferenceById(id);
        return new UserDTO(user);
    }

    //usuário logado - token que chegou na requisição
    @Transactional(readOnly = true)
    public UserDTO findMe() {
        User user = authService.authenticated() ;
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {

        User entity = new User();
        copyDtoToEntity(dto, entity);

        //ignorar qualquer role que venha do json
        entity.getRoles().clear();
        //somente role operator para novos usuário - signup
        Role role = roleRepository.findByAuthority("ROLE_OPERATOR"); //igual ao seed

        entity.getRoles().add(role);

        //precisamos usar BCryptPasswordEncoder
        entity.setPassword(passwordEncoder.encode(dto.getPassword())); //cript a senha
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            User entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException ex) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    @Transactional
    public void detele(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }

        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Id not found");
        }
    }


    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        //roles do usuário
        entity.getRoles().clear();
        for (RoleDTO roleDTO : dto.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> list = repository.searchUserAndRolesByEmail(username);

        if (list.size() == 0) {
            throw new UsernameNotFoundException("Email not found");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(list.get(0).getPassword());
        for (UserDetailsProjection projection : list){
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }
}
