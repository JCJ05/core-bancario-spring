package com.app.bancario.springappcore.Controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bancario.springappcore.model.Cuota;
import com.app.bancario.springappcore.model.Prestamo;
import com.app.bancario.springappcore.model.Solicitud;
import com.app.bancario.springappcore.model.TipoCambio;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.model.dto.FormPago;
import com.app.bancario.springappcore.model.dto.RespuestaPago;
import com.app.bancario.springappcore.repository.CuotaRepository;
import com.app.bancario.springappcore.repository.PrestamoRepository;
import com.app.bancario.springappcore.repository.SolicitudRepository;
import com.app.bancario.springappcore.repository.TipoCambioRepository;
import com.app.bancario.springappcore.repository.UsuarioRepository;

@Controller
@RequestMapping("/pago")
public class PagoController {
    
    @Autowired
    private RestTemplate _restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;
  
    @Autowired
    private SolicitudRepository solicitudRepository;
  
    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private TipoCambioRepository _dataTipoCambio;

    @Autowired
    private CuotaRepository cuotaRepository;
    
    @GetMapping(path = "/cancelar")
    public String pagarCuota(Authentication authentication , Model model , RedirectAttributes redirectAttributes){
        
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName());
        Solicitud solicitud = solicitudRepository.findByIdUsuarioAndStatus(usuario.getId()).orElse(null);

        if(solicitud == null){

            return "redirect:/prestamo/index";
        }
        

        Prestamo prestamo = prestamoRepository.findByIdSolicitud(solicitud.getId()).orElse(null);

        if(prestamo == null){

            return "redirect:/prestamo/index";
        }

        List<Cuota> cuotas = cuotaRepository.findByIdPrestamo(prestamo.getId());

        Cuota cuota = new Cuota();
        int numCuota = 1;

        for (Cuota cuota2 : cuotas) {

            if(cuota2.getEstado().equals("Pendiente")){

                cuota = cuota2;
                break;
            }

            numCuota+=1;
           
        }

        TipoCambio tcActual = _dataTipoCambio.findLastTipoCambio();
        FormPago pago = new FormPago();

        pago.setNroTarjeta("");
        pago.setMoneda(solicitud.getTipo_moneda());
        pago.setMonto(cuota.getMonto_total());
        pago.setTcCompra(tcActual.getCompra());
        pago.setTcVenta(tcActual.getVenta());

        String textoPago = "";

        if(pago.getMoneda().equals("PEN")){
            textoPago += "S/. ";
        }else{
            textoPago += "$ ";
        }

        textoPago += pago.getMonto() + "";

        redirectAttributes.addFlashAttribute("pago", pago);
        redirectAttributes.addFlashAttribute("textoPago", textoPago);
        redirectAttributes.addFlashAttribute("numCuota", numCuota);
        redirectAttributes.addFlashAttribute("cuota", cuota);

        return "redirect:/pago/pagar";
        
    }

    @GetMapping("/pagar")
    public String pasarela(Model model){
        return "pago/pasarela";
    }

    @PostMapping("/pagar")
    public String pagar(Model model, @Valid FormPago pago, BindingResult result, Authentication authentication ,RedirectAttributes redirectAttributes){

        Usuario usuario = usuarioRepository.findByUsername(authentication.getName());
        Solicitud solicitud = solicitudRepository.findByIdUsuarioAndStatus(usuario.getId()).orElse(null);

        if(solicitud == null){

            return "redirect:/prestamo/index";
        }
        

        Prestamo prestamo = prestamoRepository.findByIdSolicitud(solicitud.getId()).orElse(null);

        if(prestamo == null){

            return "redirect:/prestamo/index";
        }

        List<Cuota> cuotas = cuotaRepository.findByIdPrestamo(prestamo.getId());

        Cuota cuota = new Cuota();
        int numCuota = 1;

        for (Cuota cuota2 : cuotas) {

            if(cuota2.getEstado().equals("Pendiente")){

                cuota = cuota2;
                break;
            }
           numCuota+=1;
        }

        if(pago.getMoneda().equals("PEN")){
          
            pago.setMoneda(solicitud.getTipo_moneda());
        }

       
        pago.setMonto(cuota.getMonto_total());

      

        HttpEntity<Object> entity = new HttpEntity<Object>(pago);
        ResponseEntity<RespuestaPago> responseEntity;

        try{
            
            responseEntity = _restTemplate.exchange("https://fuxia-pass.herokuapp.com/api/tarjeta/pagar", HttpMethod.POST, entity, RespuestaPago.class);

            RespuestaPago respuesta = responseEntity.getBody();

            if(respuesta.getStatus().equals("reload")){
                
                model.addAttribute("mensajeRecarga", respuesta.getMensaje());
                model.addAttribute("pago", respuesta.getTarjeta());
                model.addAttribute("textoPago", getMontoText(respuesta.getTarjeta().getMoneda(), respuesta.getTarjeta().getMonto()));
                model.addAttribute("pago", pago);
                model.addAttribute("numCuota", numCuota);
                model.addAttribute("cuota", cuota);
                return "pago/pasarela";
            }

            if(respuesta.getStatus().equals("error")){
                model.addAttribute("mensajeError", respuesta.getMensaje());
                model.addAttribute("pago", respuesta.getTarjeta());
                model.addAttribute("textoPago", getMontoText(respuesta.getTarjeta().getMoneda(), respuesta.getTarjeta().getMonto()));
                model.addAttribute("pago", pago);
                model.addAttribute("numCuota", numCuota);
                model.addAttribute("cuota", cuota);
                return "pago/pasarela";
            }

            if(respuesta.getStatus().equals("success")){
                redirectAttributes.addFlashAttribute("status", respuesta.getStatus());
                redirectAttributes.addFlashAttribute("mensaje", respuesta.getMensaje());
                cuota.setEstado("Pagado");
                cuotaRepository.save(cuota);
                cuotaRepository.flush();
                
                return "redirect:/";
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }


        redirectAttributes.addFlashAttribute("status", "error");
        redirectAttributes.addFlashAttribute("mensaje", "Error desconocido");
        return "redirect:/";

    }

    public String getMontoText(String moneda, Double monto){

        String montoText = "";

        if(moneda.equals("PEN")){
            montoText = "S/. ";
        }else{
            montoText = "$ ";
        }

        montoText += monto + "";

        return montoText;

    }

}