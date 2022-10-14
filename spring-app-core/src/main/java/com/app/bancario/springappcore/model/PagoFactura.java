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
@Table(name = "pago_factura")
public class PagoFactura implements Serializable{
    
     @Id
     @GeneratedValue( strategy =GenerationType.IDENTITY )
     Long id;

     private Long numeroFactura;
     
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "cuota_id")
     private Cuota cuota;

     private static final long serialVersionUID = 1L;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getNumeroFactura() {
            return numeroFactura;
        }

        public void setNumeroFactura(Long numeroFactura) {
            this.numeroFactura = numeroFactura;
        }

        public Cuota getCuota() {
            return cuota;
        }

        public void setCuota(Cuota cuota) {
            this.cuota = cuota;
        }

        public static long getSerialversionuid() {
            return serialVersionUID;
        }

     

}
