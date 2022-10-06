package com.app.bancario.springappcore.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    @Email(message = "El correo tiene un formato incorrecto")
    @NotBlank(message = "El campo correo es obligatorio")
    private String username;

    @Column(length = 60)
    @Size(min = 8 , message = "El campo password tiene que tener mas de 8 caracteres")
    @NotBlank(message = "El campo password es obligatorio")
    private String password;

    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{8}$" ,  message = "El dni tiene que ser solo numerico y solo contiene 8 digitos numericos")
    @NotBlank(message = "El campo dni es obligatorio")
    private String dni;
    
    @OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private List<Rol> roles;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

   
      
}
