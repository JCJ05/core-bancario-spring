package com.app.bancario.springappcore.integration.pasarelas.dto;

public class ModelRespuestaPagoAbono {
    
    private String status;
    private ModelPagoAbono tarjeta;
    private String mensaje;
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public ModelPagoAbono getTarjeta() {
        return tarjeta;
    }
    public void setTarjeta(ModelPagoAbono tarjeta) {
        this.tarjeta = tarjeta;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    
}
