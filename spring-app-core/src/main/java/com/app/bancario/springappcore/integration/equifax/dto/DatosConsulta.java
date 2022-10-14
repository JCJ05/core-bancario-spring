package com.app.bancario.springappcore.integration.equifax.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class DatosConsulta {
    
    private Integer id;
    private BigDecimal deuda;
    private Date fecha; 
    private Integer diasVencidas;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public BigDecimal getDeuda() {
        return deuda;
    }
    public void setDeuda(BigDecimal deuda) {
        this.deuda = deuda;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Integer getDiasVencidas() {
        return diasVencidas;
    }
    public void setDiasVencidas(Integer diasVencidas) {
        this.diasVencidas = diasVencidas;
    }

    
}
