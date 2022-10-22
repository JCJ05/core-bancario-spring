package com.app.bancario.springappcore.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "solicitud_cuenta")
public class SolicitudCuenta implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;   

    @NotBlank(message = "Tiene que indicar el tipo de moneda a utilizar en la cuenta de ahorro")
    private String tipo_moneda;
    
    @NotBlank(message = "Tiene que indicar el tipo de tarjeta que va a utilizar en la cuenta de ahorro")
    private String tipo_tarjeta;
   
    @NotBlank(message = "Tiene que indicar como quiere que se visualice su nombre en su tarjeta")
    private String nombreTarjeta;

    private String equifax;

    private String listas_negras;

    private String estado;

    @Temporal(TemporalType.DATE)
    private Date fecha_solicitud;

    @PrePersist
    public void prePersist() {
        fecha_solicitud = new Date();
    }

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo_moneda() {
        return tipo_moneda;
    }

    public void setTipo_moneda(String tipo_moneda) {
        this.tipo_moneda = tipo_moneda;
    }

    public String getTipo_tarjeta() {
        return tipo_tarjeta;
    }

    public void setTipo_tarjeta(String tipo_tarjeta) {
        this.tipo_tarjeta = tipo_tarjeta;
    }

    public String getNombreTarjeta() {
        return nombreTarjeta;
    }

    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getEquifax() {
        return equifax;
    }

    public void setEquifax(String equifax) {
        this.equifax = equifax;
    }

    public String getListas_negras() {
        return listas_negras;
    }

    public void setListas_negras(String listas_negras) {
        this.listas_negras = listas_negras;
    }
   
    public Date getFecha_solicitud() {
        return fecha_solicitud;
    }

    public void setFecha_solicitud(Date fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

   

   
    
}
