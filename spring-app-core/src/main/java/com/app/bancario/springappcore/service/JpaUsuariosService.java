package com.app.bancario.springappcore.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.bancario.springappcore.model.Rol;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.repository.UsuarioRepository;

@Service
public class JpaUsuariosService implements UserDetailsService{

    @Autowired
    private UsuarioRepository repository;

    private Logger logger = LoggerFactory.getLogger(JpaUsuariosService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      
            Usuario usuario = repository.findByUsername(username);

            if(usuario == null){
                logger.error("Error usuario no existe: " + username);
                throw new UsernameNotFoundException("Username: " + username + " no existe en el sistema");
            }

            List<GrantedAuthority> authorities = new ArrayList<>();

            for (Rol rol : usuario.getRoles()) {
                
                authorities.add(new SimpleGrantedAuthority(rol.getAuthoritie()));
            }

            
            if(authorities.isEmpty()){
                logger.error("Error usuario : " + username + " no tiene roles asignados");
                throw new UsernameNotFoundException("Username: " + username + " no tiene roles asignados");
            }

            return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled() , true , true , true , authorities);

    }
    
}
