package com.app.bancario.springappcore.integration.pasarelas.dto;

public class ModelBuscarTarjeta {

    private String nroTarjeta;
    private String dueMonth;
    private String dueYear;
    private String cvv;
    private String nombre;
    
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

    
    
}
