package com.app.bancario.springappcore.model;

import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "t_tipo_cambio")

public class TipoCambio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    
    @Column(columnDefinition = "numeric(22,6)")
    @NotNull
    private Double compra;
    
    @Column(columnDefinition = "numeric(22,6)")
    @NotNull
    private Double venta;
    
    @NotNull
    @CreationTimestamp
    private Date fechaHora;

    public TipoCambio(){
        
    }

    public TipoCambio(Integer id, Double compra, Double venta, Date fechaHora){
        this.id = id;
        this.compra = compra;
        this.venta = venta;
        this.fechaHora = fechaHora;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCompra() {
        return compra;
    }

    public void setCompra(Double compra) {
        this.compra = compra;
    }

    public Double getVenta() {
        return venta;
    }

    public void setVenta(Double venta) {
        this.venta = venta;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    


}
