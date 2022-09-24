package com.app.bancario.springappcore.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.bancario.springappcore.model.TipoCambio;
import com.app.bancario.springappcore.repository.TipoCambioRepository;

@Controller
public class HomeController {
    
    @Autowired
    private TipoCambioRepository _dataTipoCambio;

    @GetMapping(path = {"" , "/index" , "/home" , "/"})
    public String home(Model model){
        
        TipoCambio tcActual = _dataTipoCambio.findLastTipoCambio();

        model.addAttribute("compra", tcActual.getCompra());
        model.addAttribute("venta", tcActual.getVenta());
        model.addAttribute("fechaHora", tcActual.getFechaHora());

        return "index";

    } 
    
    @GetMapping(path = "/nosotros")
    public String nosotros(){

        return "nosotros";
    }

    @GetMapping(path = {"/register/register"})
    public String register(){
    
        return "register/register";

    } 
    @GetMapping(path = {"/usuario/login"})
    public String login(){
    
        return "usuario/login";

    } 

}
