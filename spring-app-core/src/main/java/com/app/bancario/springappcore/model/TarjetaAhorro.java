package com.app.bancario.springappcore.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tarjeta_ahorro")
public class TarjetaAhorro implements Serializable {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    private String nroTarjeta;

    private String dueMonth;

    private String dueYear;

    private String cvv;

    private String nombre_titular;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroTarjeta() {
        return nroTarjeta;
    }

    public void setNroTarjeta(String nroTarjeta) {
        this.nroTarjeta = nroTarjeta;
    }

    public String getDueMonth() {
        return dueMonth;
    }

    public void setDueMonth(String dueMonth) {
        this.dueMonth = dueMonth;
    }

    public String getDueYear() {
        return dueYear;
    }

    public void setDueYear(String dueYear) {
        this.dueYear = dueYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getNombre_titular() {
        return nombre_titular;
    }

    public void setNombre_titular(String nombre_titular) {
        this.nombre_titular = nombre_titular;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

   
}
