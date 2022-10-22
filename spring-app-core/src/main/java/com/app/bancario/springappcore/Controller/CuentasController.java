package com.app.bancario.springappcore.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.bancario.springappcore.integration.pasarelas.PasarelasApi;
import com.app.bancario.springappcore.integration.pasarelas.dto.ActivarTarjeta;
import com.app.bancario.springappcore.integration.pasarelas.dto.ModelBuscarTarjeta;
import com.app.bancario.springappcore.integration.pasarelas.dto.ModelPagoAbono;
import com.app.bancario.springappcore.integration.pasarelas.dto.ModelRespuestaSaldo;
import com.app.bancario.springappcore.model.CuentaAhorro;
import com.app.bancario.springappcore.model.TarjetaAhorro;
import com.app.bancario.springappcore.model.TipoCambio;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.repository.CuentaAhorroRepository;
import com.app.bancario.springappcore.repository.TipoCambioRepository;
import com.app.bancario.springappcore.repository.UsuarioRepository;

@Controller
@RequestMapping("/cuenta")
public class CuentasController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CuentaAhorroRepository ahorroRepository;

    @Autowired
    private PasarelasApi pasarelasApi;

    @Autowired
    private TipoCambioRepository _dataTipoCambio;
     
      @RequestMapping(path = "/tarjeta"  , method = RequestMethod.GET)
      public String getTarjeta(Authentication authentication , Model model){
      
           Usuario usuario = usuarioRepository.findByUsername(authentication.getName());

           CuentaAhorro cuentaAhorro = ahorroRepository.findByIdUsuario(usuario.getId()).orElse(null);

           if(cuentaAhorro == null){
              
              model.addAttribute("cuenta", cuentaAhorro);
              return "cuenta/tarjeta";

           }

           TarjetaAhorro tarjetaAhorro = cuentaAhorro.getTarjeta();

           ModelBuscarTarjeta buscarTarjeta = new ModelBuscarTarjeta();
           buscarTarjeta.setNroTarjeta(tarjetaAhorro.getNroTarjeta());
           buscarTarjeta.setDueMonth(tarjetaAhorro.getDueMonth());
           buscarTarjeta.setDueYear(tarjetaAhorro.getDueYear());
           buscarTarjeta.setCvv(tarjetaAhorro.getCvv());
           buscarTarjeta.setNombre(tarjetaAhorro.getNombre_titular());
   
           ModelRespuestaSaldo respuesta = pasarelasApi.getSaldoTarjeta(buscarTarjeta);

           model.addAttribute("saldo", respuesta.getSaldo());
           model.addAttribute("status", respuesta.isActive());
           model.addAttribute("cuenta", cuentaAhorro);
           model.addAttribute("tarjeta", tarjetaAhorro);
           return "cuenta/tarjeta";
      }

      @RequestMapping(path = "/activar"  , method = RequestMethod.GET)
      public String activarTarjeta(Authentication authentication , Model model){
   
         Usuario usuario = usuarioRepository.findByUsername(authentication.getName());

         CuentaAhorro cuentaAhorro = ahorroRepository.findByIdUsuario(usuario.getId()).orElse(null);

         if(cuentaAhorro == null){
            
            model.addAttribute("cuenta", cuentaAhorro);
            return "cuenta/tarjeta";

         }

         TarjetaAhorro tarjetaAhorro = cuentaAhorro.getTarjeta();

         ActivarTarjeta activarTarjeta = new ActivarTarjeta();
         activarTarjeta.setNroTarjeta(tarjetaAhorro.getNroTarjeta());
         activarTarjeta.setCvv(tarjetaAhorro.getCvv());
         activarTarjeta.setDueMonth(tarjetaAhorro.getDueMonth());
         activarTarjeta.setDueYear(tarjetaAhorro.getDueYear());
         activarTarjeta.setNombre(tarjetaAhorro.getNombre_titular());
         activarTarjeta.setActive(true);
 
         pasarelasApi.activarTarjeta(activarTarjeta);

         
         return "redirect:/cuenta/tarjeta";

      }

      @RequestMapping(path = "/desactivar"  , method = RequestMethod.GET)
      public String desactivarTarjeta(Authentication authentication , Model model){
   
         Usuario usuario = usuarioRepository.findByUsername(authentication.getName());

         CuentaAhorro cuentaAhorro = ahorroRepository.findByIdUsuario(usuario.getId()).orElse(null);

         if(cuentaAhorro == null){
            
            model.addAttribute("cuenta", cuentaAhorro);
            return "cuenta/tarjeta";

         }

         TarjetaAhorro tarjetaAhorro = cuentaAhorro.getTarjeta();

         ActivarTarjeta activarTarjeta = new ActivarTarjeta();
         activarTarjeta.setNroTarjeta(tarjetaAhorro.getNroTarjeta());
         activarTarjeta.setCvv(tarjetaAhorro.getCvv());
         activarTarjeta.setDueMonth(tarjetaAhorro.getDueMonth());
         activarTarjeta.setDueYear(tarjetaAhorro.getDueYear());
         activarTarjeta.setNombre(tarjetaAhorro.getNombre_titular());
         activarTarjeta.setActive(false);
 
         pasarelasApi.activarTarjeta(activarTarjeta);

         
         return "redirect:/cuenta/tarjeta";

      }

      @RequestMapping(path = "/abonar"  , method = RequestMethod.POST)
      public String abonarTarjetaUsuario(@RequestParam double monto , Authentication authentication , Model model){
          
         Usuario usuario = usuarioRepository.findByUsername(authentication.getName());

         CuentaAhorro cuentaAhorro = ahorroRepository.findByIdUsuario(usuario.getId()).orElse(null);

         if(cuentaAhorro == null){
            
            model.addAttribute("cuenta", cuentaAhorro);
            return "cuenta/tarjeta";

         }

         TarjetaAhorro tarjetaAhorro = cuentaAhorro.getTarjeta();
         TipoCambio tcActual = _dataTipoCambio.findLastTipoCambio();

         ModelPagoAbono pagoAbono = new ModelPagoAbono();
         pagoAbono.setNroTarjeta(tarjetaAhorro.getNroTarjeta());
         pagoAbono.setCvv(tarjetaAhorro.getCvv());
         pagoAbono.setDueMonth(tarjetaAhorro.getDueMonth());
         pagoAbono.setDueYear(tarjetaAhorro.getDueYear());
         pagoAbono.setNombre(tarjetaAhorro.getNombre_titular());
         pagoAbono.setMoneda(cuentaAhorro.getSolicitud().getTipo_moneda());
         pagoAbono.setMonto(monto);
         pagoAbono.setTcCompra(tcActual.getCompra());
         pagoAbono.setTcVenta(tcActual.getVenta());

         pasarelasApi.abonarTarjeta(pagoAbono);

         return "redirect:/cuenta/tarjeta";

      }

}
