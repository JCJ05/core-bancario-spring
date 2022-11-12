package com.app.bancario.springappcore.integration.pasarelas.dto;

public class RespuestaTransferencia {
    
    private String status;

    private String mensaje;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    
}
