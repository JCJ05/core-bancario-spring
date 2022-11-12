package com.app.bancario.springappcore.integration.pasarelas.dto;

import java.util.List;

public class RespuestaMovimientos {

    private String status;
    private String mensaje;
    private List<Movimiento> movimientos;
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
    public List<Movimiento> getMovimientos() {
        return movimientos;
    }
    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    
    
}
