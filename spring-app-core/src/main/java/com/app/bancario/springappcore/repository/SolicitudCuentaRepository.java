package com.app.bancario.springappcore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bancario.springappcore.model.SolicitudCuenta;

@Repository
public interface SolicitudCuentaRepository extends JpaRepository<SolicitudCuenta, Long> {
    
    @Query("select s from SolicitudCuenta s where (s.estado = 'Pendiente' or s.estado = 'Aprobado') and s.usuario.id = ?1")
    Optional<SolicitudCuenta> findByUsuarioId(Long id);
    
    @Query("select s from SolicitudCuenta s join fetch s.usuario u where s.estado = 'Pendiente'")
    List<SolicitudCuenta> findByEstado();

    @Query("select s from SolicitudCuenta s join fetch s.usuario u where s.estado = 'Pendiente' and s.id = ?1")
    Optional<SolicitudCuenta> findByIdUsuario(Long id);
}
    

