package com.app.bancario.springappcore.integration.pasarelas.dto;

public class ModelPagoAbono {

    private String nroTarjeta;
    private String dueMonth;
    private String dueYear;
    private String cvv;
    private String nombre;
    private String moneda;
    private Double monto;
    private Double tcCompra;
    private Double tcVenta;
    private String descripcion;
    
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
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public Double getMonto() {
        return monto;
    }
    public void setMonto(Double monto) {
        this.monto = monto;
    }
    public Double getTcCompra() {
        return tcCompra;
    }
    public void setTcCompra(Double tcCompra) {
        this.tcCompra = tcCompra;
    }
    public Double getTcVenta() {
        return tcVenta;
    }
    public void setTcVenta(Double tcVenta) {
        this.tcVenta = tcVenta;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    

    
    
}
