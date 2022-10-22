package com.app.bancario.springappcore.integration.pasarelas.dto;

import java.util.Date;

public class TarjetaPasarela {

    private Integer id;

    private String credenciales;

    private Date dueDate;

    private String tipo;

    private boolean active;

    private UsuarioRespuesta usuario;

    private String moneda;

    private Double saldo;

    private Double limDiario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(String credenciales) {
        this.credenciales = credenciales;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UsuarioRespuesta getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioRespuesta usuario) {
        this.usuario = usuario;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getLimDiario() {
        return limDiario;
    }

    public void setLimDiario(Double limDiario) {
        this.limDiario = limDiario;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    private String dni;
    
}
