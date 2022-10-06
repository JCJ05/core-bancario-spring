package com.app.bancario.springappcore.integration.exchangerates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.bancario.springappcore.integration.exchangerates.dto.ExchangeRatesResponse;

@Service
public class ExchangeRatesApi {
        
    @Autowired
    private RestTemplate restTemplate;

    public ExchangeRatesResponse getTipoCambioActual(){

        ResponseEntity<ExchangeRatesResponse> respuesta = null;

        try {
            
            String apiJket = System.getenv("API_KEY_CAMBIO");
            HttpHeaders headers = new HttpHeaders();
            headers.add("apikey", apiJket);
            HttpEntity<Object> httpEntity=new HttpEntity<Object>(headers);

            respuesta = restTemplate.exchange("https://api.apilayer.com/exchangerates_data/latest?symbols=PEN&base=USD", HttpMethod.GET, httpEntity, ExchangeRatesResponse.class);

         } catch (Exception e) {
             
             System.out.println(e.getStackTrace());
         }
       
         if(respuesta == null){
            return null;
         }

         return respuesta.getBody();

    } 

}
