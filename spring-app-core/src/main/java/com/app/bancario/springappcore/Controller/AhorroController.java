package com.app.bancario.springappcore.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ahorro")
public class AhorroController {
    @GetMapping(path = {"/", "", "/index"})
    public String index(Model model){
        return "ahorro/form_ahorros";
    }
}
