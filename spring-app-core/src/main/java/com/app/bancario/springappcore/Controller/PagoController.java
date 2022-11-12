package com.app.bancario.springappcore.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bancario.springappcore.integration.sunat.SunatApi;
import com.app.bancario.springappcore.model.CuentaAhorro;
import com.app.bancario.springappcore.model.Cuota;
import com.app.bancario.springappcore.model.Prestamo;
import com.app.bancario.springappcore.model.Solicitud;
import com.app.bancario.springappcore.model.TarjetaAhorro;
import com.app.bancario.springappcore.model.TipoCambio;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.model.dto.FormPago;
import com.app.bancario.springappcore.model.dto.RespuestaPago;
import com.app.bancario.springappcore.repository.CuentaAhorroRepository;
import com.app.bancario.springappcore.repository.CuotaRepository;
import com.app.bancario.springappcore.repository.PrestamoRepository;
import com.app.bancario.springappcore.repository.SolicitudRepository;
import com.app.bancario.springappcore.repository.TipoCambioRepository;
import com.app.bancario.springappcore.repository.UsuarioRepository;

@Controller
@RequestMapping("/pago")
@SessionAttributes({"cuota" , "pago" , "numCuota" , "textoPago" , "cuenta" , "tarjeta"})
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

    @Autowired
    private SunatApi sunatApi;

    @Autowired
    private CuentaAhorroRepository cuentaAhorroRepository;

    @Autowired
    private CuentaAhorroRepository ahorroRepository;
    
    @GetMapping(path = "/cancelar")
    public String pagarCuota(Authentication authentication , Model model , HttpSession session, RedirectAttributes redirectAttributes){
        
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
        pago.setDescripcion("Pago de cuota de préstamo con tarjeta");

        session.setAttribute("moneda", pago.getMoneda());
        session.setAttribute("monto", pago.getMonto());
        session.setAttribute("tcCompra", pago.getTcCompra());
        session.setAttribute("tcVenta", pago.getTcVenta());
        session.setAttribute("descripcion", pago.getDescripcion());
        session.setAttribute("tarjetaValidacion", null);
        session.setAttribute("cuentaValidacion", null);
        session.setAttribute("status", "");

        String textoPago = "";

        if(pago.getMoneda().equals("PEN")){
            textoPago += "S/. ";
        }else{
            textoPago += "$ ";
        }

        textoPago += pago.getMonto() + "";

        CuentaAhorro cuentaAhorro = ahorroRepository.findByIdUsuario(usuario.getId()).orElse(null);

        TarjetaAhorro tarjetaAhorro = null;

        if(cuentaAhorro != null){

            tarjetaAhorro = cuentaAhorro.getTarjeta();
        }
       
        
        redirectAttributes.addFlashAttribute("cuenta", cuentaAhorro);
        redirectAttributes.addFlashAttribute("tarjeta", tarjetaAhorro);
        redirectAttributes.addFlashAttribute("pago", pago);
        redirectAttributes.addFlashAttribute("textoPago", textoPago);
        redirectAttributes.addFlashAttribute("numCuota", numCuota);
        redirectAttributes.addFlashAttribute("cuota", cuota);
        

        return "redirect:/pago/pagar";
        
    }

    @GetMapping("/pagar")
    public String pasarela(Model model , HttpSession session){
         
        String tarjetaValidacion = (String) session.getAttribute("tarjetaValidacion");
        String cuentaValidacion = (String) session.getAttribute("cuentaValidacion");
        String status = (String) session.getAttribute("status");

        model.addAttribute("tarjetaValidacion", tarjetaValidacion);
        model.addAttribute("cuentaValidacion", cuentaValidacion);
        model.addAttribute("status", status);
        session.setAttribute("status", "");
        return "pago/pasarela";
    }

    @GetMapping("/pagar/cuenta")
    public String pagarCuenta(RedirectAttributes attributes , HttpSession session){
        
        session.setAttribute("tarjetaValidacion", null);
        session.setAttribute("cuentaValidacion", "Pagar con cuenta");
        session.setAttribute("status", "");
        return "redirect:/pago/pagar";
    }

    @GetMapping("/pagar/tarjeta")
    public String pagarTarjeta(RedirectAttributes attributes , HttpSession session){
        
        session.setAttribute("cuentaValidacion", null);
        session.setAttribute("tarjetaValidacion", "Pagar con tarjeta");

        session.setAttribute("status", "");
        return "redirect:/pago/pagar";
    }

    @GetMapping("/pagar/cuenta/cuota")
    public String pagarCuotoConcuenta(Authentication authentication ,HttpSession session , Model model , RedirectAttributes redirectAttributes , SessionStatus status){
        
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName());

        CuentaAhorro cuenta = cuentaAhorroRepository.findByIdUsuario(usuario.getId()).orElse(null);

        if(cuenta == null){
           
            redirectAttributes.addFlashAttribute("tarjetaValidacion", null);
            redirectAttributes.addFlashAttribute("cuentaValidacion", null);

            session.setAttribute("status", "error");
            redirectAttributes.addFlashAttribute("mensajeError", "Usted No tiene cuenta de ahorro");

            return "redirect:/pago/pagar";

        }

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
        boolean flag = false;

        for (Cuota cuota2 : cuotas) {

            if(numCuota == cuotas.size()){
                  
                flag = true; 
            }

            if(cuota2.getEstado().equals("Pendiente")){

                cuota = cuota2;
                break;
            }
           numCuota+=1;
        }
     
        FormPago pago = new FormPago();
     
        pago.setNroTarjeta(cuenta.getTarjeta().getNroTarjeta());
        pago.setDueMonth(cuenta.getTarjeta().getDueMonth());
        pago.setDueYear(cuenta.getTarjeta().getDueYear());
        pago.setCvv(cuenta.getTarjeta().getCvv());
        pago.setNombre(cuenta.getTarjeta().getNombre_titular());
        pago.setMoneda(session.getAttribute("moneda").toString());
        pago.setMonto((Double)session.getAttribute("monto"));
        pago.setTcCompra((Double)session.getAttribute("tcCompra"));
        pago.setTcVenta((Double)session.getAttribute("tcVenta"));
        pago.setDescripcion("Pago de cuota con cuenta propia");

        String apiKey = System.getenv("API_KEY_PASARELAS");
     
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", apiKey);

        HttpEntity<Object> entity = new HttpEntity<Object>(pago , headers);
        ResponseEntity<RespuestaPago> responseEntity;

        try{
            
            responseEntity = _restTemplate.exchange("https://fuxia-pass.herokuapp.com/api/tarjeta/pagar", HttpMethod.POST, entity, RespuestaPago.class);

            RespuestaPago respuesta = responseEntity.getBody();

            if(respuesta.getStatus().equals("reload")){
                session.setAttribute("status", respuesta.getStatus());
                redirectAttributes.addFlashAttribute("mensajeRecarga", respuesta.getMensaje());
                redirectAttributes.addFlashAttribute("textoPago", getMontoText(respuesta.getTarjeta().getMoneda(), respuesta.getTarjeta().getMonto()));
                session.setAttribute("moneda", respuesta.getTarjeta().getMoneda());
                session.setAttribute("monto", respuesta.getTarjeta().getMonto());
                pago.setMoneda(session.getAttribute("moneda").toString());
                pago.setMonto((Double)session.getAttribute("monto"));
                pago.setTcCompra((Double)session.getAttribute("tcCompra"));
                pago.setTcVenta((Double)session.getAttribute("tcVenta"));
                pago.setDescripcion("Pago de cuota con cuenta propia");

                session.setAttribute("tarjetaValidacion", null);
                session.setAttribute("cuentaValidacion", "Pagar con cuenta");
                return "redirect:/pago/pagar";
            }

            if(respuesta.getStatus().equals("error")){
                session.setAttribute("status", respuesta.getStatus());
                redirectAttributes.addFlashAttribute("mensajeError", respuesta.getMensaje());
                redirectAttributes.addFlashAttribute("textoPago", getMontoText(respuesta.getTarjeta().getMoneda(), respuesta.getTarjeta().getMonto()));
                pago.setMoneda(session.getAttribute("moneda").toString());
                pago.setMonto((Double)session.getAttribute("monto"));
                pago.setTcCompra((Double)session.getAttribute("tcCompra"));
                pago.setTcVenta((Double)session.getAttribute("tcVenta"));
                pago.setDescripcion("Pago de cuota con cuenta propia");

                session.setAttribute("tarjetaValidacion", null);
                session.setAttribute("cuentaValidacion", "Pagar con cuenta");
                return "redirect:/pago/pagar";
            }

            if(respuesta.getStatus().equals("success")){
                redirectAttributes.addFlashAttribute("status", respuesta.getStatus());
                redirectAttributes.addFlashAttribute("mensaje", respuesta.getMensaje());
                cuota.setEstado("Pagado");
                cuotaRepository.save(cuota);
                cuotaRepository.flush();
                
                double sacarItf = cuota.getMonto_total() - (Math.round(cuota.getMonto_total() / 1.00005 * 100.0)/100.0);
                double itf = Math.round(sacarItf * 100.0)/100.0;

                int dniSunat = Integer.parseInt(usuario.getDni());

                sunatApi.saveFacturaInSunat(dniSunat , cuota , itf);

                if(flag){
                   
                    solicitud.setEstado("Finalizado");
                    solicitudRepository.save(solicitud);
                    solicitudRepository.flush();

                    prestamo.setEstado("Finalizado");
                    prestamo.setSolicitud(solicitud);
                    prestamo.setCuotas(cuotaRepository.findByIdPrestamo(prestamo.getId()));
                    prestamoRepository.save(prestamo);
                    prestamoRepository.flush();
                     
                    status.setComplete();
                    return "redirect:/prestamo/index";
                }

                status.setComplete();
                return "redirect:/prestamo/cronograma";
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }


        redirectAttributes.addFlashAttribute("status", "error");
        redirectAttributes.addFlashAttribute("mensaje", "Error desconocido");
        status.setComplete();
        return "redirect:/";

    }

    @PostMapping("/pagar")
    public String pagar(Model model, @Valid FormPago pago, HttpSession session, BindingResult result, Authentication authentication ,RedirectAttributes redirectAttributes){

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
        boolean flag = false;

        for (Cuota cuota2 : cuotas) {

            if(numCuota == cuotas.size()){
                  
                flag = true; 
            }

            if(cuota2.getEstado().equals("Pendiente")){

                cuota = cuota2;
                break;
            }
           numCuota+=1;
        }

        pago.setMoneda(session.getAttribute("moneda").toString());
        pago.setMonto((Double)session.getAttribute("monto"));
        pago.setTcCompra((Double)session.getAttribute("tcCompra"));
        pago.setTcVenta((Double)session.getAttribute("tcVenta"));
        pago.setDescripcion((String)session.getAttribute("descripcion"));

        String apiKey = System.getenv("API_KEY_PASARELAS");
  
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", apiKey);

        HttpEntity<Object> entity = new HttpEntity<Object>(pago , headers);
        ResponseEntity<RespuestaPago> responseEntity;

        try{
            
            responseEntity = _restTemplate.exchange("https://fuxia-pass.herokuapp.com/api/tarjeta/pagar", HttpMethod.POST, entity, RespuestaPago.class);

            RespuestaPago respuesta = responseEntity.getBody();

            if(respuesta.getStatus().equals("reload")){
                model.addAttribute("status", respuesta.getStatus());
                model.addAttribute("mensajeRecarga", respuesta.getMensaje());
                model.addAttribute("textoPago", getMontoText(respuesta.getTarjeta().getMoneda(), respuesta.getTarjeta().getMonto()));
                session.setAttribute("moneda", respuesta.getTarjeta().getMoneda());
                session.setAttribute("monto", respuesta.getTarjeta().getMonto());
                pago.setMoneda(session.getAttribute("moneda").toString());
                pago.setMonto((Double)session.getAttribute("monto"));
                pago.setTcCompra((Double)session.getAttribute("tcCompra"));
                pago.setTcVenta((Double)session.getAttribute("tcVenta"));
                pago.setDescripcion((String)session.getAttribute("descripcion"));

                model.addAttribute("pago", pago);
                model.addAttribute("numCuota", numCuota);
                model.addAttribute("cuota", cuota);
                String tarjetaValidacion = (String) session.getAttribute("tarjetaValidacion");
                String cuentaValidacion = (String) session.getAttribute("cuentaValidacion");
              
                model.addAttribute("tarjetaValidacion", tarjetaValidacion);
                model.addAttribute("cuentaValidacion", cuentaValidacion);
                return "pago/pasarela";
            }

            if(respuesta.getStatus().equals("error")){
                model.addAttribute("status", respuesta.getStatus());
                model.addAttribute("mensajeError", respuesta.getMensaje());
                model.addAttribute("pago", respuesta.getTarjeta());
                model.addAttribute("textoPago", getMontoText(respuesta.getTarjeta().getMoneda(), respuesta.getTarjeta().getMonto()));
                pago.setMoneda(session.getAttribute("moneda").toString());
                pago.setMonto((Double)session.getAttribute("monto"));
                pago.setTcCompra((Double)session.getAttribute("tcCompra"));
                pago.setTcVenta((Double)session.getAttribute("tcVenta"));
                pago.setDescripcion((String)session.getAttribute("descripcion"));

                model.addAttribute("pago", pago);
                model.addAttribute("numCuota", numCuota);
                model.addAttribute("cuota", cuota);
                String tarjetaValidacion = (String) session.getAttribute("tarjetaValidacion");
                String cuentaValidacion = (String) session.getAttribute("cuentaValidacion");
              
                model.addAttribute("tarjetaValidacion", tarjetaValidacion);
                model.addAttribute("cuentaValidacion", cuentaValidacion);
                return "pago/pasarela";
            }

            if(respuesta.getStatus().equals("success")){
                redirectAttributes.addFlashAttribute("status", respuesta.getStatus());
                redirectAttributes.addFlashAttribute("mensaje", respuesta.getMensaje());
                cuota.setEstado("Pagado");
                cuotaRepository.save(cuota);
                cuotaRepository.flush();
                
                double sacarItf = cuota.getMonto_total() - (Math.round(cuota.getMonto_total() / 1.00005 * 100.0)/100.0);
                double itf = Math.round(sacarItf * 100.0)/100.0;

                int dniSunat = Integer.parseInt(usuario.getDni());

                sunatApi.saveFacturaInSunat(dniSunat , cuota , itf);

                if(flag){
                   
                    solicitud.setEstado("Finalizado");
                    solicitudRepository.save(solicitud);
                    solicitudRepository.flush();

                    prestamo.setEstado("Finalizado");
                    prestamo.setSolicitud(solicitud);
                    prestamo.setCuotas(cuotaRepository.findByIdPrestamo(prestamo.getId()));
                    prestamoRepository.save(prestamo);
                    prestamoRepository.flush();

                    return "redirect:/prestamo/index";
                }
                
                return "redirect:/prestamo/cronograma";
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
