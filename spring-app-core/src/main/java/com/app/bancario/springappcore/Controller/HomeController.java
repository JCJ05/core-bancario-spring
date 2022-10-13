package com.app.bancario.springappcore.Controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.bancario.springappcore.integration.exchangerates.ExchangeRatesApi;
import com.app.bancario.springappcore.integration.exchangerates.dto.ExchangeRatesResponse;
import com.app.bancario.springappcore.model.TipoCambio;
import com.app.bancario.springappcore.model.dto.FormPago;
import com.app.bancario.springappcore.repository.TipoCambioRepository;
import com.app.bancario.springappcore.service.MailService;

@Controller
public class HomeController {
    
    @Autowired
    private TipoCambioRepository _dataTipoCambio;

    @Autowired
    private MailService mailService;

    @Autowired
    private ExchangeRatesApi exchangeRatesApi;

    @GetMapping(path = {"/index" , "/home" , "/"})
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
 
    @GetMapping(path = "/sesion/expirada")
    public String sesionExpirada(RedirectAttributes attributes , HttpServletRequest request){
        
        attributes.addFlashAttribute("sesion", "Estuvo inactivo por varios minutos en nuestra aplicacion y por motivos de seggurdidad hemos expirado su sesion");
        return "redirect:/usuario/login";

    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam(value = "code" , defaultValue = "") String code) {

        if (mailService.verificar(code)) {

            return "verifica_exitoso";

        } else {

            return "verifica_error";
        }
    }
    
    @GetMapping("/getTipoCambioActual")
    public String getTipoCambioActual(Model model){
        
        ExchangeRatesResponse respuesta = exchangeRatesApi.getTipoCambioActual();

        if(respuesta != null){

            System.out.println(respuesta.getBase());
            System.out.println(respuesta.getDate());
            System.out.println(respuesta.isSuccess());
            System.out.println(respuesta.getTimestamp());
            System.out.println(respuesta.getRates());
            
            Double valorTCActual = 0.0;

            for(Map.Entry<String, Object> rate : respuesta.getRates().entrySet()){
                if(rate.getKey().equals("PEN")){
                    valorTCActual = (Double)rate.getValue();
                    System.out.println(valorTCActual);
                }
            }

            TipoCambio tc = new TipoCambio();

            tc.setFechaHora(new Date(respuesta.getTimestamp()));
            tc.setCompra(valorTCActual * 0.99);
            tc.setVenta(valorTCActual * 1.01);
            _dataTipoCambio.save(tc);

        }

        return "redirect:/";
    }

    @GetMapping(path = "/testPago")
    public String testPago(Model model, RedirectAttributes redirectAttributes){

        TipoCambio tcActual = _dataTipoCambio.findLastTipoCambio();
        FormPago pago = new FormPago();

        pago.setNroTarjeta("");
        pago.setMoneda("PEN");
        pago.setMonto(48.25);
        pago.setTcCompra(tcActual.getCompra());
        pago.setTcVenta(tcActual.getVenta());

        String textoPago = "";

        if(pago.getMoneda().equals("PEN")){
            textoPago += "S/. ";
        }else{
            textoPago += "$ ";
        }

        textoPago += pago.getMonto() + "";

        redirectAttributes.addFlashAttribute("pago", pago);
        redirectAttributes.addFlashAttribute("textoPago", textoPago);

        return "redirect:/pago/pagar";

    }

}
