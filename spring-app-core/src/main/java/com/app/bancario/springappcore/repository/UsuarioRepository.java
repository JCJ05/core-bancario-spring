package com.app.bancario.springappcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bancario.springappcore.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario , Long> {
    
    public Usuario findByUsername(String username);
    
    @Query(value = "SELECT * FROM usuarios u WHERE u.verification_code = ?1" , nativeQuery = true)
    public Usuario findByVerificationCode(String code);
    
    @Query(value = "SELECT u.dni FROM usuarios u WHERE u.dni = ?1" , nativeQuery = true)
    public String findByNumeroDni(String dni);
}
