
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Apuesta;
import com.apuestasamistosas.app.entities.Usuario;
import com.apuestasamistosas.app.enums.ResultadoApuesta;

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
        String url = "http://betbuy.ddns.net/user/confirm/" + usuario.getCodConfirmacion();
        String body = "Hola, " + usuario.getNombre() + "\nClickea el siguiente enlace para activar tu cuenta:" + "\n\n" + url;
        String subject = "Confirma tu cuenta";
        
        mail.setFrom("soporte.betbuy@gmail.com");
        mail.setTo(usuario.getEmail());
        mail.setSubject(subject);
        mail.setText(body);
        mailSender.send(mail);

    }
    

	@Async
	public void betConfirmationToUserA(Apuesta apuesta) {

		SimpleMailMessage mail = new SimpleMailMessage();
		String url = "http://betbuy.ddns.net/bets/summary/" + apuesta.getId();
		String body = "Hola, " + apuesta.getUsuarioA().getNombre() + "\n" + "Tu amigo "
				+ apuesta.getUsuarioB().getNombre() + " acepto tu apuesta."
				+ "\n\n Podes revisar la condiciones en el siguiente enlace: \n" + url;
		String subject = "¡" + apuesta.getUsuarioB().getNombre() + " acepto tu apuesta!";
		String toReceipt = apuesta.getUsuarioA().getEmail();

		mail.setFrom("soporte.betbuy@gmail.com");
		mail.setTo(toReceipt);
		mail.setSubject(subject);
		mail.setText(body);
		mailSender.send(mail);
	}
	
	@Async
	public void betConfirmationToUserB(Apuesta apuesta) {

		SimpleMailMessage mail = new SimpleMailMessage();
		String url = "http://betbuy.ddns.net/bets/summary/" + apuesta.getId();
		String body = "Hola, " + apuesta.getUsuarioB().getNombre() + "\n" + "Aceptaste la apuesta de " + apuesta.getUsuarioA().getNombre() + "!"
				+ "\n\n Podes revisar la condiciones en el siguiente enlace: \n" + url;
		String subject = "¡Aceptaste la apuesta de " + apuesta.getUsuarioA().getNombre() + "!";
		String toReceipt = apuesta.getUsuarioB().getEmail();

		mail.setFrom("soporte.betbuy@gmail.com");
		mail.setTo(toReceipt);
		mail.setSubject(subject);
		mail.setText(body);
		mailSender.send(mail);
	}
	
	@Async
	public void betResultToUserA(Apuesta apuesta) {
		SimpleMailMessage mail = new SimpleMailMessage();
		String toReceipt = apuesta.getUsuarioA().getEmail();
		String body, subject;
		
		if(apuesta.getResultadoApuesta() == ResultadoApuesta.USUARIO_A) {
			body = "Hola, " + apuesta.getUsuarioA().getNombre() + "\n" + "Ganaste la apuesta contra " + apuesta.getUsuarioB().getNombre();
			subject = "¡Ganaste la apuesta contra " + apuesta.getUsuarioB().getNombre() + "!";
		}else if(apuesta.getResultadoApuesta() == ResultadoApuesta.USUARIO_B){
			body = "Hola, " + apuesta.getUsuarioA().getNombre() + "\n" + "Perdiste la apuesta contra " + apuesta.getUsuarioB().getNombre();
			subject = "¡Perdiste la apuesta contra " + apuesta.getUsuarioB().getNombre() + "!";
		}else {
			body = "Hola, " + apuesta.getUsuarioA().getNombre() + "\n" + "Empataste la apuesta contra " + apuesta.getUsuarioB().getNombre();
			subject = "¡Empataste la apuesta contra " + apuesta.getUsuarioB().getNombre() + "!";
		}

		mail.setFrom("soporte.betbuy@gmail.com");
		mail.setTo(toReceipt);
		mail.setSubject(subject);
		mail.setText(body);
		mailSender.send(mail);
	}
	
	@Async
	public void betResultToUserB(Apuesta apuesta) {
		SimpleMailMessage mail = new SimpleMailMessage();
		String toReceipt = apuesta.getUsuarioB().getEmail();
		String body, subject;
		
		if(apuesta.getResultadoApuesta() == ResultadoApuesta.USUARIO_B) {
			body = "Hola, " + apuesta.getUsuarioB().getNombre() + "\n" + "Ganaste la apuesta contra " + apuesta.getUsuarioA().getNombre();
			subject = "¡Ganaste la apuesta contra " + apuesta.getUsuarioA().getNombre() + "!";
		}else if(apuesta.getResultadoApuesta() == ResultadoApuesta.USUARIO_A){
			body = "Hola, " + apuesta.getUsuarioB().getNombre() + "\n" + "Perdiste la apuesta contra " + apuesta.getUsuarioA().getNombre();
			subject = "¡Perdiste la apuesta contra " + apuesta.getUsuarioA().getNombre() + "!";
		}else {
			body = "Hola, " + apuesta.getUsuarioB().getNombre() + "\n" + "Empataste la apuesta contra " + apuesta.getUsuarioA().getNombre();
			subject = "¡Empataste la apuesta contra " + apuesta.getUsuarioA().getNombre() + "!";
		}

		mail.setFrom("soporte.betbuy@gmail.com");
		mail.setTo(toReceipt);
		mail.setSubject(subject);
		mail.setText(body);
		mailSender.send(mail);
	}
	
	@Async
	public void betRejectToUserA(Apuesta apuesta) {

		SimpleMailMessage mail = new SimpleMailMessage();
		String body = "Hola, " + apuesta.getUsuarioA().getNombre() + "\n" + "Tu apuesta fue rechazada por " + apuesta.getUsuarioB().getNombre();
		String subject = apuesta.getUsuarioB().getNombre() + " rechazo tu apuesta.";
		String toReceipt = apuesta.getUsuarioA().getEmail();

		mail.setFrom("soporte.betbuy@gmail.com");
		mail.setTo(toReceipt);
		mail.setSubject(subject);
		mail.setText(body);
		mailSender.send(mail);
	}
    
    
    /*   */
}
