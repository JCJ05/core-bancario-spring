package com.app.bancario.springappcore.integration.sunat;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.bancario.springappcore.integration.sunat.dto.UserSunat;
import com.app.bancario.springappcore.model.Cuota;
import com.app.bancario.springappcore.model.PagoFactura;
import com.app.bancario.springappcore.repository.PagoFacturaRepository;

@Service
public class SunatApi {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PagoFacturaRepository facturaRepository;
    
   public void saveFacturaInSunat(int dni , Cuota cuota , double monto){
     
    Integer numFactura = (int)(Math.random()*(99999999-11111111+1)+11111111);
    Integer idFactura  = (int)(Math.random()*(99999999-11111111+1)+11111111);
    Long numRucEmisor= 23456172720L;

     UserSunat userSunat = new UserSunat();

        userSunat.setDniReceptor(dni);
        userSunat.setFechaEmision(new Date());
        userSunat.setIdFactura(idFactura);
        userSunat.setMontoTotal(monto);
        

        userSunat.setNumeroFactura(numFactura.longValue());
        userSunat.setNumRUCEmisor(numRucEmisor);

        HttpEntity<UserSunat> sunat = new HttpEntity<UserSunat>(userSunat);
        restTemplate.exchange("https://sunatapi.herokuapp.com/api/factura/", HttpMethod.POST, sunat, String.class);

        PagoFactura factura = new PagoFactura();
        factura.setNumeroFactura(numFactura.longValue());
        factura.setCuota(cuota);

        facturaRepository.save(factura);
   }
   

}
