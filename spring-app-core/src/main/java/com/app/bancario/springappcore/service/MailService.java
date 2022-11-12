package com.app.bancario.springappcore.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bancario.springappcore.model.Codigo;
import com.app.bancario.springappcore.model.Usuario;
import com.app.bancario.springappcore.repository.CodigoRepository;
import com.app.bancario.springappcore.repository.UsuarioRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import net.bytebuddy.utility.RandomString;

@Service
public class MailService {
    
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private CodigoRepository codigoRepository;


    public void register(Usuario usuario, String siteURL) {
       
        String randomCode = RandomString.make(64);

        usuario.setVerificationCode(randomCode);  
        repository.save(usuario);
         
        sendVerificationEmail(usuario, siteURL);

    }
     
    private void sendVerificationEmail(Usuario usuario, String siteURL)   {

        Email from = new Email("julio_aguero0501@outlook.com");
        String subject = "Verificacion de correo";
        Email to = new Email(usuario.getUsername());
      
        String contenido = "Querido Usuario,<br>"
            + "Por favor haz click aqui para verificar correctamente tu correo:<br>"
            + "<h3><a href=\"[[URL]]\" target=\"_self\">Verificar</a></h3>"
            + "Muchas Gracias<br>"
            + "Te saluda FuxiaBank";

            String verifyURL = siteURL + "/verify?code=" + usuario.getVerificationCode();
            contenido = contenido.replace("[[URL]]", verifyURL);

        Content content = new Content("text/html", contenido);
        Mail mail = new Mail(from, subject, to, content);

      
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        int statusCode = 0;

        try {

          request.setMethod(Method.POST);
          request.setEndpoint("mail/send");
          request.setBody(mail.build());
          Response response = sg.api(request);

          System.out.println(response.getStatusCode());
          System.out.println(response.getBody());
          System.out.println(response.getHeaders());

         

        } catch (IOException ex) {
           
            ex.printStackTrace();
            statusCode=500;
        }
        
     
    }

    public void sendVerificationCode(Usuario usuario)   {

        Email from = new Email("julio_aguero0501@outlook.com");
        String subject = "Verificacion de correo";
        Email to = new Email(usuario.getUsername());
      
        String contenido = "Querido Usuario,<br>"
            + "Le estamos mandando un codigo de autorizacion para que autorice cualquier accion en su cuenta de ahorro:<br>"
            + "<h3>\"[[CODIGO]]\"</h3>"
            + "Muchas Gracias<br>"
            + "Te saluda FuxiaBank";

            String codigo = RandomString.make(4);

            contenido = contenido.replace("[[CODIGO]]", codigo);

        Content content = new Content("text/html", contenido);
        Mail mail = new Mail(from, subject, to, content);

     
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        int statusCode = 0;

        try {

          request.setMethod(Method.POST);
          request.setEndpoint("mail/send");
          request.setBody(mail.build());
          Response response = sg.api(request);

          System.out.println(response.getStatusCode());
          System.out.println(response.getBody());
          System.out.println(response.getHeaders());

          Codigo codigoSave = new Codigo();
          codigoSave.setCodigo(codigo);
          codigoSave.setUsuario(usuario);
          codigoSave.setActivo(true);

          codigoRepository.save(codigoSave);


        } catch (IOException ex) {
           
            ex.printStackTrace();
            statusCode=500;
        }
        
     
    }

    public boolean verificar(String verificationCode) {
        Usuario user = repository.findByVerificationCode(verificationCode);
         
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            repository.save(user);
             
            return true;
        }
         
    }

}
