package com.app.bancario.springappcore.Controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bancario.springappcore.integration.pasarelas.PasarelasApi;
import com.app.bancario.springappcore.integration.pasarelas.dto.ModelTransferencia;
import com.app.bancario.springappcore.integration.pasarelas.dto.RespuestaTransferencia;
import com.app.bancario.springappcore.model.Codigo;
import com.app.bancario.springappcore.model.CuentaAhorro;
import com.app.bancario.springappcore.model.TipoCambio;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.repository.CodigoRepository;
import com.app.bancario.springappcore.repository.CuentaAhorroRepository;
import com.app.bancario.springappcore.repository.TipoCambioRepository;
import com.app.bancario.springappcore.repository.UsuarioRepository;
import com.app.bancario.springappcore.service.MailService;

@RestController
@RequestMapping("/api/transferencia")
public class TransferenciaController {
    
    @Autowired
    private CuentaAhorroRepository ahorroRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private CodigoRepository codigoRepository;

    @Autowired
    private TipoCambioRepository _dataTipoCambio;

    @Autowired
    private PasarelasApi pasarelasApi;

    @PostMapping(path = "/consulta/cuenta", consumes = "application/json")
    public ResponseEntity<?> consultaCuenta(@RequestBody Map<String , String> body , Authentication authentication){
        
        String tipo = body.get("tipo");
        String numeroCuenta = body.get("numero_cuenta");

        Map<String , Object> response = new HashMap<>();

        CuentaAhorro ahorro = null;
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName());

        if(tipo.equals("Bancaria")){

            ahorro = ahorroRepository.findByTransferenciaFuxia(numeroCuenta , usuario.getId()).orElse(null);

        }else if(tipo.equals("Interbancaria")){
                
            ahorro = ahorroRepository.findByTransferenciaInterBancaria(numeroCuenta , usuario.getId()).orElse(null);
        }else {

            ahorro = ahorroRepository.findByTransferenciaInternacional(numeroCuenta , usuario.getId()).orElse(null);
        }

        if(ahorro == null){
            
            response.put("mensaje", "No se encontro la cuenta");
            return new ResponseEntity<Map<String , Object>>(response , HttpStatus.BAD_REQUEST);
        
        }else {

           
            
            class Cuenta{

                public String nombre;
                public String numero_cuenta;
                public String dni;
            }


            Cuenta cuenta = new Cuenta();

            cuenta.nombre = ahorro.getSolicitud().getNombreTarjeta();
            cuenta.numero_cuenta = ahorro.getNumero_cuenta();
            cuenta.dni = ahorro.getSolicitud().getUsuario().getDni();


            response.put("mensaje", "Cuenta encontrada");
            response.put("cuenta", cuenta);
            return new ResponseEntity<Map<String , Object>>(response , HttpStatus.OK);
        }
        
      
    }

    @PostMapping(path = "/consulta/codigo" , consumes = "application/json")
    public ResponseEntity<?> consultaCodigo(@RequestBody Map<String , String> body , Authentication authentication){

        System.out.println("HOla muhdo");

        String codigo = body.get("codigo");
        
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName());

        Map<String , Object> response = new HashMap<>();

        Codigo codigoModel = codigoRepository.findByCodigoAndIdUsuario(codigo , usuario.getId()).orElse(null);
      
        if(codigoModel == null ){

            response.put("mensaje", "Codigo no encontrado");
            return new ResponseEntity<Map<String , Object>>(response , HttpStatus.BAD_REQUEST);
        
        }else {

            if(!codigoModel.isActivo()){
                    
                    response.put("mensaje", "Codigo no activo");
                    return new ResponseEntity<Map<String , Object>>(response , HttpStatus.BAD_REQUEST);
            }
             
            codigoModel.setActivo(false);
            codigoRepository.save(codigoModel);
            
            response.put("mensaje", "Codigo encontrado");
            return new ResponseEntity<Map<String , Object>>(response , HttpStatus.OK);
        }
    }

    @GetMapping(path = "/envio/codigo")
    public ResponseEntity<?> consultaCodigo(Authentication authentication){
     
        Map<String , Object> response = new HashMap<>();
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName());
        List<Codigo> codigos = codigoRepository.findByUsuarioId(usuario.getId());

        try {
           
            if(codigos.size() > 0){
                
                codigoRepository.deleteAll(codigos);
            }
           
            mailService.sendVerificationCode(usuario);
            response.put("mensaje", "Codigo enviado");
            return new ResponseEntity<Map<String , Object>>(response , HttpStatus.OK);
            
        } catch (Exception e) {

            response.put("mensaje", "Error al enviar el codigo");
            return new ResponseEntity<Map<String , Object>>(response , HttpStatus.BAD_REQUEST);
           
        }
    }

    @PostMapping(path = "/envio/transferencia" , consumes = "application/json")
    public ResponseEntity<?> envioTransferencia(@RequestBody Map<String , String> body , Authentication authentication){
        
        String tipo_moneda = body.get("tipo_moneda");
        String numeroCuenta = body.get("numero_cuenta");
        String monto = body.get("monto");
        String tipo_tarjeta = body.get("tipo_tarjeta");

        System.out.println(body);

        Map<String , Object> response = new HashMap<>();

        Usuario usuario = usuarioRepository.findByUsername(authentication.getName());
        CuentaAhorro ahorro = null;

        if(tipo_tarjeta.equals("Bancaria")){

            ahorro = ahorroRepository.findByTransferenciaFuxia(numeroCuenta , usuario.getId()).orElse(null);

        }else if(tipo_tarjeta.equals("Interbancaria")){
                
            ahorro = ahorroRepository.findByTransferenciaInterBancaria(numeroCuenta , usuario.getId()).orElse(null);
        }else {

            ahorro = ahorroRepository.findByTransferenciaInternacional(numeroCuenta , usuario.getId()).orElse(null);
        }

        double montoSoles = 0.0;

       if(tipo_moneda == "dolares"){
          
        TipoCambio tcActual = _dataTipoCambio.findLastTipoCambio();
        montoSoles = Double.parseDouble(monto) * tcActual.getCompra();

       }else {

        montoSoles = Double.parseDouble(monto);
       }

       CuentaAhorro cuentaAhorroOrigen = ahorroRepository.findByIdUsuario(usuario.getId()).orElse(null);

       ModelTransferencia modelTransferencia = new ModelTransferencia();

       modelTransferencia.setNroTarjetaOrigen(cuentaAhorroOrigen.getTarjeta().getNroTarjeta());
       modelTransferencia.setDueMonthOrigen(cuentaAhorroOrigen.getTarjeta().getDueMonth());
       modelTransferencia.setDueYearOrigen(cuentaAhorroOrigen.getTarjeta().getDueYear());
       modelTransferencia.setCvvOrigen(cuentaAhorroOrigen.getTarjeta().getCvv());
       modelTransferencia.setNombreOrigen(cuentaAhorroOrigen.getTarjeta().getNombre_titular());
       modelTransferencia.setMonedaOrigen(cuentaAhorroOrigen.getSolicitud().getTipo_moneda());
       modelTransferencia.setMontoOrigen(montoSoles);

       modelTransferencia.setNroTarjetaDestino(ahorro.getTarjeta().getNroTarjeta());
       modelTransferencia.setDueMonthDestino(ahorro.getTarjeta().getDueMonth());
       modelTransferencia.setDueYearDestino(ahorro.getTarjeta().getDueYear());
       modelTransferencia.setCvvDestino(ahorro.getTarjeta().getCvv());
       modelTransferencia.setNombreDestino(ahorro.getTarjeta().getNombre_titular());
       modelTransferencia.setMonedaDestino(ahorro.getSolicitud().getTipo_moneda());
       modelTransferencia.setMontoDestino(montoSoles);

       modelTransferencia.setDescripcion("Transferencia " + tipo_tarjeta + " a " + ahorro.getTarjeta().getNombre_titular());

       RespuestaTransferencia respuesta = pasarelasApi.transferir(modelTransferencia);

       if(respuesta == null){

        response.put("mensaje", "Error al realizar la transferencia");
        return new ResponseEntity<Map<String , Object>>(response , HttpStatus.BAD_REQUEST);

       }

       if(respuesta.getStatus().equals("error")){
           
        response.put("mensaje", respuesta.getMensaje());
        return new ResponseEntity<Map<String , Object>>(response , HttpStatus.BAD_REQUEST);
       }

       response.put("mensaje", respuesta.getMensaje());
       return new ResponseEntity<Map<String , Object>>(response , HttpStatus.OK);
        
    }

}
