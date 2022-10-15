package com.app.bancario.springappcore.integration.sunat.dto;



import java.util.Date;

public class UserSunat {
     
    private int idFactura;
    private Long numeroFactura;
    private Long numRUCEmisor;
    private int dniReceptor;
    private Date fechaEmision;
    private Double montoTotal;

    public int getIdFactura() {
        return idFactura;
    }
    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }
    public Long getNumeroFactura() {
        return numeroFactura;
    }
    public void setNumeroFactura(Long numeroFactura) {
        this.numeroFactura = numeroFactura;
    }
    public Long getNumRUCEmisor() {
        return numRUCEmisor;
    }
    public void setNumRUCEmisor(Long numRUCEmisor) {
        this.numRUCEmisor = numRUCEmisor;
    }
    public int getDniReceptor() {
        return dniReceptor;
    }
    public void setDniReceptor(int dniReceptor) {
        this.dniReceptor = dniReceptor;
    }
    public Date getFechaEmision() {
        return fechaEmision;
    }
    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    public Double getMontoTotal() {
        return montoTotal;
    }
    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

    


}
