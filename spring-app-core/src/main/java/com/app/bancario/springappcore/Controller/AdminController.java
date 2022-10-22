package com.app.bancario.springappcore.Controller;


import java.util.Base64;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.app.bancario.springappcore.integration.pasarelas.PasarelasApi;
import com.app.bancario.springappcore.integration.pasarelas.dto.RespuestaPasarela;
import com.app.bancario.springappcore.integration.pasarelas.dto.UsuarioPasarela;
import com.app.bancario.springappcore.model.CuentaAhorro;
import com.app.bancario.springappcore.model.Cuota;
import com.app.bancario.springappcore.model.Prestamo;
import com.app.bancario.springappcore.model.Solicitud;
import com.app.bancario.springappcore.model.SolicitudCuenta;
import com.app.bancario.springappcore.model.Tarifa;
import com.app.bancario.springappcore.model.TarjetaAhorro;
import com.app.bancario.springappcore.repository.CuentaAhorroRepository;
import com.app.bancario.springappcore.repository.PrestamoRepository;
import com.app.bancario.springappcore.repository.SolicitudCuentaRepository;
import com.app.bancario.springappcore.repository.SolicitudRepository;
import com.app.bancario.springappcore.repository.TarifaRepository;
import com.app.bancario.springappcore.repository.TarjetaAhorroRepository;
import com.app.bancario.springappcore.service.LogicaCuotasService;

@Controller
@RequestMapping("/admin")
@SessionAttributes("tarifa")
@Secured("ROLE_ADMIN")
public class AdminController {
    
    @Autowired
    private TarifaRepository repository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private LogicaCuotasService cuotasService;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private SolicitudCuentaRepository solicitudCuentaRepository;

    @Autowired
    private PasarelasApi pasarelasApi;

    @Autowired
    private TarjetaAhorroRepository tarjetaAhorroRepository;

    @Autowired
    private CuentaAhorroRepository cuentaAhorroRepository;
    
    
    @RequestMapping(path = "/interes" , method = RequestMethod.GET)
    public String Tarifa(Model model){
        
        Tarifa tarifa = new Tarifa();
        model.addAttribute("tarifa", tarifa);

        return "admin/interes";
    }

    @RequestMapping(path = "/interes/guardar" , method = RequestMethod.POST)
    public String guardarTarifa(@Valid Tarifa tarifa , BindingResult result , Model model){

        if(result.hasErrors()){

            return "admin/interes";
        }

        repository.save(tarifa);

        return "redirect:/admin/tasa";
    }

    @RequestMapping(path = "/tasa" , method = RequestMethod.GET)
    public String tasa(Model model){

        model.addAttribute("tarifario", repository.findByMaxFecha());

        return "admin/tasa";
    }

    @RequestMapping(path = "/solicitudes" , method = RequestMethod.GET)
    public String solicitudes(Model model){

        model.addAttribute("lista_solicitudes", solicitudRepository.findByEstado());

        return "admin/solicitudes";
    }

    @RequestMapping(path = "/solicitudes/cuenta" , method = RequestMethod.GET)
    public String solicitudesCuenta(Model model){
     
        model.addAttribute("lista_solicitudes", solicitudCuentaRepository.findByEstado());

        return "admin/solicitudes_cuenta";
    }

    @RequestMapping(path = "/solicitud/detalle/cuenta/{id}" , method = RequestMethod.GET)
    public String detalleSolicitudCuenta(@PathVariable(value = "id") Long id , Model model){
       
        SolicitudCuenta cuenta = solicitudCuentaRepository.findByIdUsuario(id).orElse(null);
        
        if(cuenta == null){
            return "redirect:/admin/solicitudes/cuenta";
        }


        model.addAttribute("cuenta", cuenta);
        model.addAttribute("idCuenta", cuenta.getId());

        return "admin/detalle_solicitud_cuenta";
    }


    @RequestMapping(path = "/solicitud/detalle/{id}" , method = RequestMethod.GET)
    public String detalleSolicitud(@PathVariable(value = "id") Long id , Model model){
       
        Solicitud solicitud = solicitudRepository.findById(id).orElse(null);
        
        if(solicitud == null){
            return "redirect:/admin/solicitudes";
        }


        model.addAttribute("solicitud", solicitud);
        model.addAttribute("idSolicitud", solicitud.getId());

        return "admin/detalle_solicitud";
    }

   
    @GetMapping(value="/aprobar/{id}")
    public String aprobarPrestamo(@PathVariable Long id) {
         
        Solicitud solicitud = solicitudRepository.findById(id).orElse(null);
        solicitud.setEstado("Aprobado");
        solicitudRepository.save(solicitud);
        solicitudRepository.flush();

        Prestamo prestamo = new Prestamo();
        prestamo.setSolicitud(solicitud);
        prestamo.setEstado("Activo");
        List<Cuota> cuotas = cuotasService.getCuotas(solicitud);
        System.out.println(cuotas.size());
        prestamo.setCuotas(cuotas);

        prestamoRepository.save(prestamo);
        prestamoRepository.flush();

        return "redirect:/admin/solicitudes";
      
    }

    @GetMapping(value = "/aprobar/cuenta/{id}")
    public String aprobarCuenta(@PathVariable Long id){
     
        System.out.println(id.toString());
        SolicitudCuenta cuenta = solicitudCuentaRepository.findByIdUsuario(id).orElse(null);
        System.out.println("Llegue aqui");

        if(cuenta == null){
           
            return "redirect:/admin/solicitudes/cuenta";
        }

        UsuarioPasarela usuarioPasarela = new UsuarioPasarela();

        usuarioPasarela.setDni(cuenta.getUsuario().getDni());
        usuarioPasarela.setNombre(cuenta.getNombreTarjeta());
        usuarioPasarela.setMoneda(cuenta.getTipo_moneda());
        usuarioPasarela.setTipo(cuenta.getTipo_tarjeta());

        RespuestaPasarela respuestaPasarela = pasarelasApi.crearTarjetaUsuario(usuarioPasarela);

        if(respuestaPasarela == null){
                
                return "redirect:/admin/solicitudes/cuenta";
        }

       
        cuenta.setEstado("Aprobado");
        solicitudCuentaRepository.save(cuenta);
        solicitudCuentaRepository.flush();

        TarjetaAhorro tarjetaAhorro = new TarjetaAhorro();
        
        String credencialesEncode = respuestaPasarela.getTarjeta().getCredenciales();
        String credencialesDecode = new String(Base64.getDecoder().decode(credencialesEncode));

        String[] credenciales = credencialesDecode.split(",");
        String[] dueDate = credenciales[1].split("/");

        tarjetaAhorro.setNroTarjeta(credenciales[0]);
        tarjetaAhorro.setDueMonth(dueDate[0]);
        tarjetaAhorro.setDueYear(dueDate[1]);
        tarjetaAhorro.setCvv(credenciales[2]);
        tarjetaAhorro.setNombre_titular(credenciales[3]);

        tarjetaAhorroRepository.save(tarjetaAhorro);
        tarjetaAhorroRepository.flush();

        String numCuenta = ((int)(Math.random() * (999999 - 000000 + 1) + 000000)) + "" + ((int)(Math.random() * (999999 - 000000 + 1) + 000000)) + "" + ((int)(Math.random() * (999999 - 000000 + 1) + 000000));

        int numFinal = (int) (Math.random() * (99 - 10 + 1) + 10);

        CuentaAhorro ahorro = new CuentaAhorro();
        ahorro.setSolicitud(cuenta);
        ahorro.setTarjeta(tarjetaAhorro);
        ahorro.setNumero_cuenta(numCuenta);
        ahorro.setCuenta_interbancaria( (numCuenta + "" + numFinal) );
        ahorro.setCuenta_internacional(("FUXIABANK" + numFinal));

        cuentaAhorroRepository.save(ahorro);
        cuentaAhorroRepository.flush();

        return "redirect:/admin/solicitudes/cuenta";
    
    }

    @GetMapping(value="/desaprobar/{id}")
    public String desaprobarPrestamo(@PathVariable Long id) {
         
        Solicitud solicitud = solicitudRepository.findById(id).orElse(null);
        solicitud.setEstado("Cancelado");
        solicitudRepository.save(solicitud);

        return "redirect:/admin/solicitudes";
      
    }


}
