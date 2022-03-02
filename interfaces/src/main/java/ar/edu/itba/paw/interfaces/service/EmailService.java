package ar.edu.itba.paw.interfaces.service;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String text);
}
