package com.app.bancario.springappcore.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.bancario.springappcore.integration.reniec.ReniecApi;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.repository.UsuarioRepository;

@Component
public class UserValidador implements Validator {
    
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private ReniecApi reniecApi;

    @Override
    public boolean supports(Class<?> clazz) {
       
        return Usuario.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
      
        Usuario usuario = (Usuario) target;

        if(usuario.getDni().length() > 0){

            if(reniecApi.findExitsUserByDni(usuario.getDni()) == null){

                errors.rejectValue("dni", "invalidate.identificador");

            }
        }

        if(repository.findByUsername(usuario.getUsername()) != null){
            
            errors.rejectValue("username", "repetido.correo");

        }

        if(repository.findByNumeroDni(usuario.getDni()) != null){

            errors.rejectValue("dni", "identificacion.repetido");
        }
        
    }
    
}
