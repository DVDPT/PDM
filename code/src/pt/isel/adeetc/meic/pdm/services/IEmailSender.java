package pt.isel.adeetc.meic.pdm.services;

public interface IEmailSender
{
    void sendEmail(String to, String subject, String message);
}
