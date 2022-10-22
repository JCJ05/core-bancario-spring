package com.app.bancario.springappcore.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "movimiento")
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CuentaAhorro cuentaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    private CuentaAhorro cuentaDestino;

    @Column(scale = 2)
    private Double montoOrigen;

    @Column(scale = 2)
    private Double montoDestino;
    private Date fechaOperacion;
    private String descripcion;

}
