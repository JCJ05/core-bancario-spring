package com.app.bancario.springappcore.integration.crm.dto;

public class UserCrm {
    
    private String apellidos;
    private String banco;
    private String celular;
    private Integer dni;
    private String fecha;
    private Integer idCliente;
    private String nombres;
    private String rubro_empresa;
    
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    public String getCelular() {
        return celular;
    }
    public void setCelular(String celular) {
        this.celular = celular;
    }
    public Integer getDni() {
        return dni;
    }
    public void setDni(Integer dni) {
        this.dni = dni;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public Integer getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getRubro_empresa() {
        return rubro_empresa;
    }
    public void setRubro_empresa(String rubro_empresa) {
        this.rubro_empresa = rubro_empresa;
    }

    

}
