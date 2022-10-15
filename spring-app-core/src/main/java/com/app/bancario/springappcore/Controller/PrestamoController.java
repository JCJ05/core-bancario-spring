package com.app.bancario.springappcore.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bancario.springappcore.integration.equifax.EquifaxApi;
import com.app.bancario.springappcore.model.Cuota;
import com.app.bancario.springappcore.model.Prestamo;
import com.app.bancario.springappcore.model.Solicitud;
import com.app.bancario.springappcore.model.Tarifa;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.repository.CuotaRepository;
import com.app.bancario.springappcore.repository.PrestamoRepository;
import com.app.bancario.springappcore.repository.SolicitudRepository;
import com.app.bancario.springappcore.repository.TarifaRepository;
import com.app.bancario.springappcore.repository.UsuarioRepository;
import com.app.bancario.springappcore.validation.SolicitudValidador;

@Controller
@RequestMapping("/prestamo")
@SessionAttributes("solicitud")
public class PrestamoController {

  @Autowired
  private TarifaRepository repository;
  
  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private SolicitudRepository solicitudRepository;

  @Autowired
  private PrestamoRepository prestamoRepository;

  @Autowired 
  private CuotaRepository  cuotaRepository;

  @Autowired
  private EquifaxApi equifaxApi;

  @Autowired
  private SolicitudValidador solicitudValidador;

  @InitBinder
  public void initBinder(WebDataBinder binder){

    binder.addValidators(solicitudValidador);

  }

  @ModelAttribute("fecha_hoy")
	public String fechaHoy() {
		
		Date date = new Date();
		String fecha;
		
		fecha = new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
		
		return  fecha;
		
	}

    
    @GetMapping(path = {"/", "", "/index"})
    public String index(RedirectAttributes attributes, Principal principal , Model model){
       
      if(principal == null){

        attributes.addFlashAttribute("autorizacion", "Para poder solicitar un prestamo necesita ser cliente de nuestro banco asi que por favor inicie sesion o cree una nueva cuenta");
           return "redirect:/usuario/login";
      }
      
      Usuario usuario = usuarioRepository.findByUsername(principal.getName());
      model.addAttribute("pendientes", solicitudRepository.findSolicitudPendienteByUsuarioId(usuario.getId()));


        return "prestamo/index";
    }

    @GetMapping(path = {"solicitar"})
    public String solicitarPrestamo(RedirectAttributes attributes , Principal principal , Model model){

        if(principal == null){

            attributes.addFlashAttribute("autorizacion", "Para poder solicitar un prestamo necesita ser cliente de nuestro banco asi que por favor inicie sesion o cree una nueva cuenta");
            return "redirect:/usuario/login";
       }

       Usuario usuario = usuarioRepository.findByUsername(principal.getName());

       if(solicitudRepository.findByIdUsuarioAndStatusForNotAcceptDuplicates(usuario.getId()).isPresent() || solicitudRepository.findByIdUsuarioAndStatus(usuario.getId()).isPresent()){

           attributes.addFlashAttribute("autorizacion", "Ya tiene una solicitud pendiente de aprobacion o cuenta con un prestamo activo");
           return "redirect:/prestamo/";

       }

       Solicitud solicitud = new Solicitud();
       model.addAttribute("solicitud", solicitud);
 
        return "prestamo/solicitud";
    }

    @PostMapping(path = "/guardar")
    public String guardarSolicitud(@Valid Solicitud solicitud , BindingResult result , Authentication authentication  ,RedirectAttributes attributes , Principal principal , Model model){
   
      if(principal == null){

        attributes.addFlashAttribute("autorizacion", "Para poder solicitar un prestamo necesita ser cliente de nuestro banco asi que por favor inicie sesion o cree una nueva cuenta");
        return "redirect:/usuario/login";
      }

      if(result.hasErrors()){

          return "prestamo/solicitud";

      }

       Usuario usuario = usuarioRepository.findByUsername(authentication.getName());
       Tarifa tarifa = repository.findByMaxFecha();

       String lista_negra = "El usuario no se encuentra en la lista negra";
       String equifax = equifaxApi.findExitsUserInEquifaxByDni(usuario.getDni());

       equifax = equifax != null ? equifax : "El usuario no registra deudas en Equifax";

       solicitud.setUsuario(usuario);
       solicitud.setTarifa(tarifa);
       solicitud.setEstado("Pendiente");
       solicitud.setLista_negra(lista_negra);
       solicitud.setEquifax(equifax);
       
       solicitudRepository.save(solicitud);

        return "redirect:/prestamo/index";
    }

    @GetMapping(path = "/cronograma")
    public String cronograma(RedirectAttributes attributes , Principal principal , Model model , Authentication authentication){

        if(principal == null){

            attributes.addFlashAttribute("autorizacion", "Para poder solicitar un prestamo necesita ser cliente de nuestro banco asi que por favor inicie sesion o cree una nueva cuenta");
            return "redirect:/usuario/login";
        }
        
        Usuario usuario = usuarioRepository.findByUsername(principal.getName());
        Solicitud solicitud = solicitudRepository.findByIdUsuarioAndStatus(usuario.getId()).orElse(null);

        if(solicitud == null){

            return "redirect:/prestamo/index";
        }
        

        Prestamo prestamo = prestamoRepository.findByIdSolicitud(solicitud.getId()).orElse(null);

        if(prestamo == null){

          return "redirect:/prestamo/index";
      }

        List<Cuota> cuotas = cuotaRepository.findByIdPrestamo(prestamo.getId());
      
        model.addAttribute("cuotas", cuotas);

        String monedaSymbol;

        if(solicitud.getTipo_moneda().equals("PEN")){
          monedaSymbol = "S/.";
        }else{
          monedaSymbol = "$";
        }

        model.addAttribute("monedaSymbol", monedaSymbol);

        return "prestamo/cronograma";
    }

}
