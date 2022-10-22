package com.app.bancario.springappcore.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cuenta_ahorro")
public class CuentaAhorro implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id")
    private SolicitudCuenta solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarjeta_id")
    private TarjetaAhorro tarjeta;
   
    private String numero_cuenta;

    private String cuenta_interbancaria;

    private String cuenta_internacional;

    private final static long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SolicitudCuenta getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudCuenta solicitud) {
        this.solicitud = solicitud;
    }

    public TarjetaAhorro getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(TarjetaAhorro tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public String getCuenta_interbancaria() {
        return cuenta_interbancaria;
    }

    public void setCuenta_interbancaria(String cuenta_interbancaria) {
        this.cuenta_interbancaria = cuenta_interbancaria;
    }

    public String getCuenta_internacional() {
        return cuenta_internacional;
    }

    public void setCuenta_internacional(String cuenta_internacional) {
        this.cuenta_internacional = cuenta_internacional;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
