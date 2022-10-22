package com.app.bancario.springappcore.integration.crm;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.bancario.springappcore.integration.crm.dto.UserCrm;

@Service
public class CrmApi {
    
    @Autowired
    private RestTemplate restTemplate;

    public void saveUserInCrm() {

        
        UserCrm userCrm = new UserCrm();
        userCrm.setApellidos("Juan");
        userCrm.setCelular("123456789");
        userCrm.setBanco("'BBVA'");
        userCrm.setDni(23456700);
        userCrm.setFecha("2020-01-03");
        userCrm.setIdCliente(1);
        userCrm.setNombres("Perez");
        userCrm.setRubro_empresa("Bancario");

       
        restTemplate.postForEntity("https://crm-g-negro.herokuapp.com/api/Cliente/", userCrm, UserCrm.class);
  
        System.out.println("Usuario guardado en CRM");
        
      
        
      
    }
}
