package com.app.bancario.springappcore.Controller;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.app.bancario.springappcore.model.Rol;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.service.MailService;
import com.app.bancario.springappcore.validation.UserValidador;

@Controller
@RequestMapping("/register")
@SessionAttributes("usuario")
public class RegistroController {

    @Autowired
	private BCryptPasswordEncoder paswordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserValidador validador;
    
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.addValidators(validador);
    }

    @GetMapping(path = "/register")
    public String registrar(Model model , Principal principal){
  
        if(principal != null){
   
            return "redirect:/index";

        }

        Usuario usuario = new Usuario();
		
        model.addAttribute("titulo", "Registro de Usuario");
		model.addAttribute("usuario", usuario);

        return "register/register";

    } 
   
    @PostMapping(path = "/guardar")
    public String registrarUsuario(@Valid Usuario usuario , BindingResult result , @RequestParam(name = "celular" , defaultValue = "")String celular , Model model , SessionStatus status , HttpServletRequest request){
    

        if(celular.isBlank() || celular.length() == 0){

            ObjectError error = new ObjectError("celular", "El campo celular es obligatorio");
            result.addError(error);
        }


        if(result.hasErrors()){
           
            model.addAttribute("titulo", "Registro de Usuario");
            return "register/register";

        }

       
        List<Rol> roles = new ArrayList<>();

        Rol rol = new Rol();
        rol.setAuthoritie("ROLE_USER");
        
        roles.add(rol);

        String password = usuario.getPassword();

        usuario.setPassword(paswordEncoder.encode(password));
        usuario.setRoles(roles);
        usuario.setEnabled(false);
        
        mailService.register(usuario, obtnerUrl(request));
        status.setComplete();

        return "redirect:/register/success";
    }

    @GetMapping(path = "/success")
    public String registroExitoso(){

        return "register/exito";
    }

    public String obtnerUrl(HttpServletRequest request){
     
        String url = request.getRequestURL().toString();
        return url.replace(request.getServletPath(), "");

    }
}
