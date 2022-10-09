package com.app.bancario.springappcore.Controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ahorro")
public class AhorroController {
    @GetMapping(path = {"/", "", "/index"})
    public String index(Model model , Principal principal , RedirectAttributes attributes){
        
        if(principal == null){

            attributes.addFlashAttribute("autorizacion", "Para poder solicitar la apertura de una cuenta de ahorro necesita ser cliente de nuestro banco asi que por favor inicie sesion o cree una nueva cuenta");
            return "redirect:/usuario/login";
       }

        return "ahorro/form_ahorros";
    }
}
