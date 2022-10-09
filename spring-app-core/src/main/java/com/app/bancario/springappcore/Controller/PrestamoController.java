package com.app.bancario.springappcore.Controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {
    
    @GetMapping(path = {"/", "", "/index"})
    public String index(RedirectAttributes attributes, Principal principal){
       
      if(principal == null){

        attributes.addFlashAttribute("autorizacion", "Para poder solicitar un prestamo necesita ser cliente de nuestro banco asi que por favor inicie sesion o cree una nueva cuenta");
           return "redirect:/usuario/login";
      }

        return "prestamo/index";
    }

    @GetMapping(path = {"solicitar"})
    public String solicitarPrestamo(RedirectAttributes attributes , Principal principal){

        if(principal == null){

            attributes.addFlashAttribute("autorizacion", "Para poder solicitar un prestamo necesita ser cliente de nuestro banco asi que por favor inicie sesion o cree una nueva cuenta");
            return "redirect:/usuario/login";
       }
 
        return "prestamo/solicitud";
    }

}
