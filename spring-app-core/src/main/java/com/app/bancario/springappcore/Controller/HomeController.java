package com.app.bancario.springappcore.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.bancario.springappcore.model.TipoCambio;
import com.app.bancario.springappcore.repository.TipoCambioRepository;
import com.app.bancario.springappcore.service.MailService;

@Controller
public class HomeController {
    
    @Autowired
    private TipoCambioRepository _dataTipoCambio;

    @Autowired
    private MailService mailService;

    @GetMapping(path = {"" , "/index" , "/home" , "/"})
    public String home(Model model){
        
       /*  TipoCambio tcActual = _dataTipoCambio.findLastTipoCambio();

        model.addAttribute("compra", tcActual.getCompra());
        model.addAttribute("venta", tcActual.getVenta());
        model.addAttribute("fechaHora", tcActual.getFechaHora());*/

        return "index";

    } 
    
    @GetMapping(path = "/nosotros")
    public String nosotros(){

        return "nosotros";
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam(value = "code" , defaultValue = "") String code) {

        if (mailService.verificar(code)) {

            return "verifica_exitoso";

        } else {

            return "verifica_error";
        }
    }
    

}
