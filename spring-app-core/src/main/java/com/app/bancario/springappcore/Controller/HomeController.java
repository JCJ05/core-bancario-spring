package com.app.bancario.springappcore.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping(path = {"" , "/index" , "/home" , "/"})
    public String home(){
    
        return "index";

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
