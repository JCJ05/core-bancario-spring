package com.app.bancario.springappcore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bancario.springappcore.model.Codigo;

@Repository
public interface CodigoRepository extends JpaRepository<Codigo , Long> {
    
    @Query("select c from Codigo c join fetch c.usuario u where c.codigo = ?1 and c.usuario.id = ?2")
    Optional<Codigo> findByCodigoAndIdUsuario(String code , Long id);
   
    @Query("select c from Codigo c join fetch c.usuario u where c.activo = true and c.usuario.id = ?1")
    List<Codigo> findByUsuarioId(Long id);
    
}
