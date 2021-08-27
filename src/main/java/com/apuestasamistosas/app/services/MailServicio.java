
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailServicio {

    @Autowired
    JavaMailSender mailSender;

    
    /*  Metodo para enviar correo de confirmacion de cuenta, se le enviara al user
        un link de confirmacion que incluye un String de confirmacion a un endpoint del controlador
        usuario.
    */
    
    @Async
    public void accountConfirmation(Usuario usuario){
        
        SimpleMailMessage mail = new SimpleMailMessage();
        String url = "http://localhost:8080/user/confirm/" + usuario.getCodConfirmacion();
        String body = "Hola, " + usuario.getNombre() + "\nClickea el siguiente enlace para activar tu cuenta:" + "\n\n" + url;
        String subject = "Confirma tu cuenta";
        
        mail.setFrom("apuestasamistosas@outlook.com");
        mail.setTo(usuario.getEmail());
        mail.setSubject(subject);
        mail.setText(body);
        mailSender.send(mail);

    }
    
    
    /*   */
}
