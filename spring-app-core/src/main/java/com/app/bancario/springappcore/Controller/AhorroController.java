package com.app.bancario.springappcore.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bancario.springappcore.integration.equifax.EquifaxApi;
import com.app.bancario.springappcore.integration.reniec.ReniecApi;
import com.app.bancario.springappcore.integration.reniec.UserReniec;
import com.app.bancario.springappcore.model.SolicitudCuenta;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.repository.SolicitudCuentaRepository;
import com.app.bancario.springappcore.repository.UsuarioRepository;

@Controller
@RequestMapping("/ahorro")
@SessionAttributes("solicitudCuenta")
public class AhorroController {

    @Autowired
    private ReniecApi reniecApi;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SolicitudCuentaRepository cuentaRepository;

    @Autowired
    private EquifaxApi equifaxApi;

    @GetMapping(path = {"/", "", "/index"})
    public String index(Model model , Principal principal , RedirectAttributes attributes){
        
        if(principal == null){

            attributes.addFlashAttribute("autorizacion", "Para poder solicitar la apertura de una cuenta de ahorro necesita ser cliente de nuestro banco asi que por favor inicie sesion o cree una nueva cuenta");
            return "redirect:/usuario/login";
       }

       SolicitudCuenta solicitudCuenta = new SolicitudCuenta();
       model.addAttribute("solicitudCuenta", solicitudCuenta);

        return "ahorro/form_ahorros";
    }

    @PostMapping(path = "/solicitud")
    public String guardarSolicitud(@Valid SolicitudCuenta solicitudCuenta , BindingResult result , Authentication authentication , Principal principal , RedirectAttributes attributes ,Model model){
      
        if(principal == null){

            attributes.addFlashAttribute("autorizacion", "Para poder solicitar un prestamo necesita ser cliente de nuestro banco asi que por favor inicie sesion o cree una nueva cuenta");
            return "redirect:/usuario/login";
        }
     
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName());

        if(cuentaRepository.findByUsuarioId(usuario.getId()).isPresent()){
         
            attributes.addFlashAttribute("error", "Ya tiene una solicitud de cuenta de ahorro pendiente o aprobada y solo se puede crear una cuenta de ahorro por usuario");
            return "redirect:/ahorro/";
        }

        if(result.hasErrors()){

            return "ahorro/form_ahorros";
        }
    
       
      solicitudCuenta.setUsuario(usuario);

        
       String lista_negra = "El usuario no se encuentra en la lista negra";
       String equifax = equifaxApi.findExitsUserInEquifaxByDni(usuario.getDni());

       equifax = equifax != null ? equifax : "El usuario no registra deudas en Equifax";

       solicitudCuenta.setEquifax(equifax);
       solicitudCuenta.setListas_negras(lista_negra);
       solicitudCuenta.setEstado("Pendiente");
      
       cuentaRepository.save(solicitudCuenta);

       return "redirect:/ahorro/";

    }
    
    @ModelAttribute("tipoNombre")
    public List<String> getTipoNombre(Authentication authentication , Principal principal){

         if(principal == null){
            return new ArrayList<>();
         }

         Usuario usuario = usuarioRepository.findByUsername(authentication.getName());
         List<String> tipoNombre = new ArrayList<>();

         if(usuario != null){
           
            System.out.println(usuario.getDni());
            UserReniec reniec = reniecApi.findExitsUserByDni(usuario.getDni());

            if(reniec != null){
                 
                tipoNombre.add(reniec.getNombres() + " " + reniec.getApePat() + " " + reniec.getApeMat());

                String[] nombres = reniec.getNombres().split(" ");
    
                if(nombres.length > 1){
    
                    tipoNombre.add(nombres[0] + " " + reniec.getApePat() + " " + reniec.getApeMat());
                    tipoNombre.add(nombres[0] + " " + reniec.getApePat());
    
                }else{
    
                    tipoNombre.add(reniec.getNombres() + " " + reniec.getApePat() + " " + reniec.getApeMat());
                    tipoNombre.add(reniec.getNombres() + " " + reniec.getApePat());
                }

            }else {

                tipoNombre = null;
            }
           

         }else {
            
            tipoNombre = null;

         }

       
        return tipoNombre;
        
    }
}
