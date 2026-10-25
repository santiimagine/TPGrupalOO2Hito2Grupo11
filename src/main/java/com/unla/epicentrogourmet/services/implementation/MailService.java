package com.unla.epicentrogourmet.services.implementation;

import com.unla.epicentrogourmet.services.IMailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailService implements IMailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void enviarMailInicial(String mailDestino, String nombre, String contrasenia) {

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(mailDestino);
        mensaje.setSubject("Bienvenido/a a Epicentro Gourmet!");
        mensaje.setText(
                "Hola " + nombre + ":\n\n"
                + "Te dieron de alta como empleado/a en Epicentro Gourmet.\n\n"
                + "Tus datos para ingresar al sistema son:\n"
                + "Usuario: " + mailDestino + "\n"
                + "Contraseña: " + contrasenia + "\n"
                + "Saludos, \nEpicentro Gourmet"
        );

        javaMailSender.send(mensaje);

    }
}
