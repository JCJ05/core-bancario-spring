package com.app.bancario.springappcore.integration.equifax.dto;

import java.util.List;

public class UserEquifax {
    
    private Integer id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String nacionalidad;
    private String ocupacion;
    private List<DatosConsulta> datosConsultas;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getNacionalidad() {
        return nacionalidad;
    }
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
    public String getOcupacion() {
        return ocupacion;
    }
    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }
    public List<DatosConsulta> getDatosConsultas() {
        return datosConsultas;
    }
    public void setDatosConsultas(List<DatosConsulta> datosConsultas) {
        this.datosConsultas = datosConsultas;
    }

    

}
