package com.app.bancario.springappcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bancario.springappcore.model.Tarifa;


@Repository
public interface TarifaRepository extends JpaRepository<Tarifa , Long> {
    
    @Query("select t from Tarifa t where t.fecha_creacion = (select max(t.fecha_creacion) from Tarifa t)")
    Tarifa findByMaxFecha();
}
