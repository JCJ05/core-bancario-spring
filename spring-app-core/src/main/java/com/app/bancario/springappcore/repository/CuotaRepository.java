package com.app.bancario.springappcore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bancario.springappcore.model.Cuota;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {
    
    @Query( value = "SELECT * FROM cuotas WHERE prestamo_id = ?1 ORDER BY fecha_pago ASC" , nativeQuery = true )
    List<Cuota> findByIdPrestamo(Long idPrestamo);
}
