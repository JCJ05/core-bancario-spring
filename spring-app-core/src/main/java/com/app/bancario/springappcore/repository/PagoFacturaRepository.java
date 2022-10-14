package com.app.bancario.springappcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.bancario.springappcore.model.PagoFactura;

@Repository
public interface PagoFacturaRepository  extends JpaRepository<PagoFactura, Integer> {
    

}
    

