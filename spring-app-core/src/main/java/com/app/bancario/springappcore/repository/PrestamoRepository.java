package com.app.bancario.springappcore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bancario.springappcore.model.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    @Query("select p from Prestamo p join fetch p.solicitud s join fetch p.cuotas c where p.estado = 'Activo' and p.solicitud.id = ?1")
    Optional<Prestamo> findByIdSolicitud(Long idSolicitud);
    
}
