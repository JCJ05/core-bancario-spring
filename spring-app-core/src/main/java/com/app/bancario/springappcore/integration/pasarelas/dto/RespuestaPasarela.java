package com.app.bancario.springappcore.integration.pasarelas.dto;

public class RespuestaPasarela {
    
    private String status;

    private TarjetaPasarela tarjeta;
    
    private String mensaje;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public TarjetaPasarela getTarjeta() {
        return tarjeta;
    }
    public void setTarjeta(TarjetaPasarela tarjeta) {
        this.tarjeta = tarjeta;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    
}
