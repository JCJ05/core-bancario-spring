package com.app.bancario.springappcore.integration.pasarelas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.bancario.springappcore.integration.pasarelas.dto.ActivarTarjeta;
import com.app.bancario.springappcore.integration.pasarelas.dto.ModelBuscarTarjeta;
import com.app.bancario.springappcore.integration.pasarelas.dto.ModelPagoAbono;
import com.app.bancario.springappcore.integration.pasarelas.dto.ModelRespuestaPagoAbono;
import com.app.bancario.springappcore.integration.pasarelas.dto.ModelRespuestaSaldo;
import com.app.bancario.springappcore.integration.pasarelas.dto.RespuestaPasarela;
import com.app.bancario.springappcore.integration.pasarelas.dto.UsuarioPasarela;

@Service
public class PasarelasApi {

   @Autowired
   private RestTemplate restTemplate;

   public RespuestaPasarela crearTarjetaUsuario(UsuarioPasarela usuarioPasarela){

      String apiKey = System.getenv("API_KEY_PASARELAS");
      String url = "https://fuxia-pass.herokuapp.com/api/tarjeta/crearTarjeta";

      HttpHeaders headers = new HttpHeaders();
      headers.set("apiKey", apiKey);

      HttpEntity<UsuarioPasarela> request = new HttpEntity<>(usuarioPasarela, headers);
      ResponseEntity<RespuestaPasarela> responseEntity;

      RespuestaPasarela pasarela = new RespuestaPasarela();

      try {

        responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, RespuestaPasarela.class);

        pasarela = responseEntity.getBody();
        pasarela = pasarela != null? pasarela.getStatus().equals("success")? pasarela : null : null;
        
      } catch (Exception e) {

        pasarela = null;

      }
      
     
     return pasarela;

   }

   public String activarTarjeta(ActivarTarjeta activarTarjeta){

      String apiKey = System.getenv("API_KEY_PASARELAS");
      String url = "https://fuxia-pass.herokuapp.com/api/tarjeta/activar";

      HttpHeaders headers = new HttpHeaders();
      headers.set("apiKey", apiKey);

      HttpEntity<ActivarTarjeta> request = new HttpEntity<>(activarTarjeta ,headers);
      ResponseEntity<String> responseEntity;

      String pasarela = "";

      try {

        responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        pasarela = responseEntity.getBody();
        
      } catch (Exception e) {

        pasarela = null;

      }
      
     
     return pasarela;

   }

   public ModelRespuestaSaldo getSaldoTarjeta(ModelBuscarTarjeta buscarTarjeta){

      String apiKey = System.getenv("API_KEY_PASARELAS");
      String url = "https://fuxia-pass.herokuapp.com/api/tarjeta/saldo";

      HttpHeaders headers = new HttpHeaders();
      headers.set("apiKey", apiKey);

      HttpEntity<ModelBuscarTarjeta> request = new HttpEntity<>(buscarTarjeta ,headers);
      ResponseEntity<ModelRespuestaSaldo> responseEntity;

      ModelRespuestaSaldo pasarela = new ModelRespuestaSaldo();

      try {

        responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, ModelRespuestaSaldo.class);
        pasarela = responseEntity.getBody();
        pasarela = pasarela != null? pasarela.getStatus().equals("success")? pasarela : null : null;
        
      } catch (Exception e) {

        pasarela = null;

      }
      
     
     return pasarela;



   }

   public ModelRespuestaPagoAbono abonarTarjeta(ModelPagoAbono pagoAbono){
   
      String apiKey = System.getenv("API_KEY_PASARELAS");
      String url = "https://fuxia-pass.herokuapp.com/api/tarjeta/abonar";

      HttpHeaders headers = new HttpHeaders();
      headers.set("apiKey", apiKey);

      HttpEntity<ModelPagoAbono> request = new HttpEntity<>(pagoAbono ,headers);
      ResponseEntity<ModelRespuestaPagoAbono> responseEntity;

      ModelRespuestaPagoAbono pasarela = new ModelRespuestaPagoAbono();

      try {

        responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, ModelRespuestaPagoAbono.class);
        pasarela = responseEntity.getBody();
        pasarela = pasarela != null? pasarela.getStatus().equals("success")? pasarela : null : null;
        
      } catch (Exception e) {

        pasarela = null;

      }
      
     
     return pasarela;


   }
    
}
