package com.app.bancario.springappcore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {
    
    @GetMapping(path = {"/", "", "/index"})
    public String index(Model model){
        return "prestamo/index";
    }

    @GetMapping(path = {"solicitar"})
    public String solicitarPrestamo(Model model){
        return "prestamo/solicitud";
    }

}
