package com.app.bancario.springappcore.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tarifa_bancaria")
public class Tarifa implements Serializable {
  
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
  
   @Column(scale = 2)
   @NotNull(message = "Tiene que indicar el porcentaje de la tarifa especial anual")
   @Max(value = 100, message = "El porcentaje maximo de la tarifa especial anual es de 100%")
   private Double tea;

   @Column(scale = 3)
   @Max(value = 1, message = "El porcentaje maximo del desgravamen es del 1%")
   @NotNull(message = "Tiene que indicar el porcentaje del desgravamen")
   private Double desgravamen;
  
   @Column(scale = 2)
   @NotNull(message = "Tiene que indicar el porcentaje de interes de la mora")
   @Max(value = 100, message = "El porcentaje maximo de interes de mora es de 100%")
   private Double interes_mora;
   
   @Temporal(TemporalType.DATE)
   private Date fecha_creacion;

    @PrePersist
    public void prePersist() {
         fecha_creacion = new Date();
    }

   private static final long serialVersionUID = 1L;

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public Double getTea() {
    return tea;
}

public void setTea(Double tea) {
    this.tea = tea;
}

public Double getDesgravamen() {
    return desgravamen;
}

public void setDesgravamen(Double desgravamen) {
    this.desgravamen = desgravamen;
}

public Double getInteres_mora() {
    return interes_mora;
}

public void setInteres_mora(Double interes_mora) {
    this.interes_mora = interes_mora;
}

public Date getFecha_creacion() {
    return fecha_creacion;
}

public void setFecha_creacion(Date fecha_creacion) {
    this.fecha_creacion = fecha_creacion;
}

public static long getSerialversionuid() {
    return serialVersionUID;
}



   
}
