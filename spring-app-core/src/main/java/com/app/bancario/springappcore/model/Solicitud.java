package com.app.bancario.springappcore.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "solciitud_prestamo")
public class Solicitud implements Serializable{
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarifa_id")
    private Tarifa tarifa;
    
    private String estado;
    
    @NotNull(message = "Tiene que indicar la cantidad de cuotas a pagar")
    @Min(value = 1, message = "La cantidad de cuotas debe ser mayor a 0")
    @Max(value = 36, message = "La cantidad de cuotas maxima es 36")
    private Integer cuotas;
    
    @NotBlank(message = "Tiene que indicar el motivo del prestamo")
    private String razon_prestamo;
    
    @NotBlank(message = "Tiene que indicar una pequeña descripcion de su trabajo")
    private String des_trabajo;
    
    @NotNull(message = "Tiene que indicar el sueldo que percibe en su trabajo")
    @Column(scale = 2)
    private Double sueldo_trabajo;
    
    @NotNull(message = "Tiene que indicar el monto del prestamo")
    @Min(value = 500, message = "El monto minimo del prestamo es de 500 soles o dolares")
    @Max(value = 200000, message = "El monto maximo del prestamo es de 200000 soles o dolares")
    @Column(scale = 2)
    private Double monto;
    
    @NotBlank(message = "Tiene que indicar el tipo de moneda del prestamo")
    private String tipo_moneda;
    
    @NotNull(message = "Tiene que indicar la cantidad de hijos que tiene")
    private Integer num_hijos;
    
    @NotNull(message = "Tiene que indicar la cantidad de hijos mayores que tiene")
    private Integer num_hijos_mayores;

    private String lista_negra;
    
    private String equifax;
    
    @NotNull(message = "Tiene que indicar la fecha de desembolso de su prestamo")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fecha_desembolso;

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

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCuotas() {
        return cuotas;
    }

    public void setCuotas(Integer cuotas) {
        this.cuotas = cuotas;
    }

    public String getRazon_prestamo() {
        return razon_prestamo;
    }

    public void setRazon_prestamo(String razon_prestamo) {
        this.razon_prestamo = razon_prestamo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getTipo_moneda() {
        return tipo_moneda;
    }

    public void setTipo_moneda(String tipo_moneda) {
        this.tipo_moneda = tipo_moneda;
    }

    public Integer getNum_hijos() {
        return num_hijos;
    }

    public void setNum_hijos(Integer num_hijos) {
        this.num_hijos = num_hijos;
    }

    public Integer getNum_hijos_mayores() {
        return num_hijos_mayores;
    }

    public void setNum_hijos_mayores(Integer num_hijos_mayores) {
        this.num_hijos_mayores = num_hijos_mayores;
    }

    public String getLista_negra() {
        return lista_negra;
    }

    public void setLista_negra(String lista_negra) {
        this.lista_negra = lista_negra;
    }

    public String getEquifax() {
        return equifax;
    }

    public void setEquifax(String equifax) {
        this.equifax = equifax;
    }

    public Date getFecha_desembolso() {
        return fecha_desembolso;
    }

    public void setFecha_desembolso(Date fecha_desembolso) {
        this.fecha_desembolso = fecha_desembolso;
    }
  
    public String getDes_trabajo() {
        return des_trabajo;
    }

    public void setDes_trabajo(String des_trabajo) {
        this.des_trabajo = des_trabajo;
    }

    public Double getSueldo_trabajo() {
        return sueldo_trabajo;
    }

    public void setSueldo_trabajo(Double sueldo_trabajo) {
        this.sueldo_trabajo = sueldo_trabajo;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
