package com.app.bancario.springappcore.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "cuotas")
public class Cuota implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date fecha_pago;

    private String estado;
    
    @Column(scale = 2)
    private Double saldo_capital;
    
    @Column(scale = 2)
    private Double amortizacion;
   
    @Column(scale = 2)
    private Double interes;

    @Column(scale = 2)
    private Double cuota;

    @Column(scale = 2)
    private Double seguro;

    @Column(scale = 2)
    private Double mora;

    @Column(scale = 2)
    private Double monto_total;

    private Integer numero_dias;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Date fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getSaldo_capital() {
        return saldo_capital;
    }

    public void setSaldo_capital(Double saldo_capital) {
        this.saldo_capital = saldo_capital;
    }

    public Double getAmortizacion() {
        return amortizacion;
    }

    public void setAmortizacion(Double amortizacion) {
        this.amortizacion = amortizacion;
    }

    public Double getInteres() {
        return interes;
    }

    public void setInteres(Double interes) {
        this.interes = interes;
    }

    public Double getCuota() {
        return cuota;
    }

    public void setCuota(Double cuota) {
        this.cuota = cuota;
    }

    public Double getSeguro() {
        return seguro;
    }

    public void setSeguro(Double seguro) {
        this.seguro = seguro;
    }

    public Double getMora() {
        return mora;
    }

    public void setMora(Double mora) {
        this.mora = mora;
    }

    public Double getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(Double monto_total) {
        this.monto_total = monto_total;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getNumero_dias() {
        return numero_dias;
    }

    public void setNumero_dias(Integer numero_dias) {
        this.numero_dias = numero_dias;
    }

    
    
}
