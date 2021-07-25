package Model;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * Responsável pela criação e envio de emails.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class Email {
    /**
     * Email de destino
     */
    private final String recipient;
    /**
     * Email de origem
     */
    private final String sender;
    /**
     * Sessão de email.
     */
    private Session session;

    /**
     * Construtor de classe.
     * Determina o email de origem e inicia o servidor de email.
     *
     * @param recipient Email de destino.
     */
    public Email(String recipient) {
        this.recipient = recipient;
        this.sender = "gr.gvr.2021@gmail.com";
        init();
    }

    /**
     * Inicia o servidor gmail e autentica o email fornecido com a respetiva password.
     */
    private void init() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465");

        // Caso fosse disponibilizado comercialmente estes campos teriam de ser ocultados
        this.session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gr.gvr.2021@gmail.com",
                        "password");
            }
        });

    }

    /**
     * Envia um email com os dados anteriormente definidos.
     *
     * @param subject Titulo do email.
     * @param msg     Mensagem do email.
     * @throws MessagingException Caso não consiga enviar um email.
     */
    public void send(String subject, String msg) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);


        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }


}
