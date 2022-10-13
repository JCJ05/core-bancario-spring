package com.app.bancario.springappcore.model.dto;

public class RespuestaPago {
    
    private String status;
    private FormPago tarjeta;
    private String mensaje;
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public FormPago getTarjeta() {
        return tarjeta;
    }
    public void setTarjeta(FormPago tarjeta) {
        this.tarjeta = tarjeta;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
