package com.app.bancario.springappcore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bancario.springappcore.model.Solicitud;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    
    @Query("select s from Solicitud s where s.usuario.id = ?1")
    List<Solicitud> findSolicitudPendienteByUsuarioId(Long id);
    
    @Query("select s from Solicitud s join fetch s.usuario u join fetch s.tarifa t where s.estado = 'Pendiente' order by s.fecha_desembolso asc")
    List<Solicitud> findByEstado();
    
    @Query("select s from Solicitud s join fetch s.usuario u join fetch s.tarifa t where s.estado = 'Pendiente' and s.id = ?1")
    Optional<Solicitud> findById(Long id);
    
    @Query("select s from Solicitud s join fetch s.usuario u join fetch s.tarifa t where s.estado = 'Aprobado' and s.usuario.id = ?1")
    Optional<Solicitud> findByIdUsuarioAndStatus(Long id);
    
    @Query("select s from Solicitud s join fetch s.usuario u join fetch s.tarifa t where s.estado = 'Pendiente' and s.usuario.id = ?1")
    Optional<Solicitud> findByIdUsuarioAndStatusForNotAcceptDuplicates(Long id);
}
    

