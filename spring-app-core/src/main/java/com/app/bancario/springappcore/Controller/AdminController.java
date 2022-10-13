package com.app.bancario.springappcore.Controller;


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

import com.app.bancario.springappcore.model.Cuota;
import com.app.bancario.springappcore.model.Prestamo;
import com.app.bancario.springappcore.model.Solicitud;
import com.app.bancario.springappcore.model.Tarifa;
import com.app.bancario.springappcore.repository.PrestamoRepository;
import com.app.bancario.springappcore.repository.SolicitudRepository;
import com.app.bancario.springappcore.repository.TarifaRepository;
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

    @GetMapping(value="/desaprobar/{id}")
    public String desaprobarPrestamo(@PathVariable Long id) {
         
        Solicitud solicitud = solicitudRepository.findById(id).orElse(null);
        solicitud.setEstado("Cancelado");
        solicitudRepository.save(solicitud);

        return "redirect:/admin/solicitudes";
      
    }


}
