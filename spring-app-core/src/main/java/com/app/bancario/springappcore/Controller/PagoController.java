package com.app.bancario.springappcore.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bancario.springappcore.model.dto.FormPago;
import com.app.bancario.springappcore.model.dto.RespuestaPago;

@Controller
@RequestMapping("/pago")
public class PagoController {
    
    @Autowired
    private RestTemplate _restTemplate;

    @GetMapping("/pagar")
    public String pasarela(Model model){
        return "pago/pasarela";
    }

    @PostMapping("/pagar")
    public String pagar(Model model, @Valid FormPago pago, BindingResult result, RedirectAttributes redirectAttributes){

        HttpEntity<Object> entity = new HttpEntity<Object>(pago);
        ResponseEntity<RespuestaPago> responseEntity;

        try{
            
            responseEntity = _restTemplate.exchange("https://fuxia-pass.herokuapp.com/api/tarjeta/pagar", HttpMethod.POST, entity, RespuestaPago.class);

            RespuestaPago respuesta = responseEntity.getBody();

            if(respuesta.getStatus().equals("reload")){
                
                model.addAttribute("mensajeRecarga", respuesta.getMensaje());
                model.addAttribute("pago", respuesta.getTarjeta());
                model.addAttribute("textoPago", getMontoText(respuesta.getTarjeta().getMoneda(), respuesta.getTarjeta().getMonto()));
                return "pago/pasarela";
            }

            if(respuesta.getStatus().equals("error")){
                model.addAttribute("mensajeError", respuesta.getMensaje());
                model.addAttribute("pago", respuesta.getTarjeta());
                model.addAttribute("textoPago", getMontoText(respuesta.getTarjeta().getMoneda(), respuesta.getTarjeta().getMonto()));
                return "pago/pasarela";
            }

            if(respuesta.getStatus().equals("success")){
                redirectAttributes.addFlashAttribute("status", respuesta.getStatus());
                redirectAttributes.addFlashAttribute("mensaje", respuesta.getMensaje());
                return "redirect:/";
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }


        redirectAttributes.addFlashAttribute("status", "error");
        redirectAttributes.addFlashAttribute("mensaje", "Error desconocido");
        return "redirect:/";

    }

    public String getMontoText(String moneda, Double monto){

        String montoText = "";

        if(moneda.equals("PEN")){
            montoText = "S/. ";
        }else{
            montoText = "$ ";
        }

        montoText += monto + "";

        return montoText;

    }

}
