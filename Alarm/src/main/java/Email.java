import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Email {
    private Session session;
    private String recipient;
    private String sender;

    public Email(String recipient){
        this.recipient = recipient;
        this.sender = "gr.gvr.2021@gmail.com";
        init();
    }

    public static void main(String[] args) throws MessagingException {
        Email e = new Email("fmtfg99@gmail.com");
        e.send("hello","world!");
    }

    private void init() {
        String host = "localhost";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465");

        this.session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gr.gvr.2021@gmail.com",
                        "a7$%^$`5+&Q^_NJT");
            }
        });

    }

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