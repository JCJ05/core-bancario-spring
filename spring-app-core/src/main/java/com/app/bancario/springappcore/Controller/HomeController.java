package com.app.bancario.springappcore.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping(path = {"" , "/index" , "/home" , "/"})
    public String home(){
    
        return "index";

    } 

}
