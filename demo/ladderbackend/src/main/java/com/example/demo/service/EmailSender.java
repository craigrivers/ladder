import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
public class EmailSender {

    private final String fromEmail;
    private final String appPassword;
    private final Session session;

    // Constructor sets up session with Gmail SMTP
    public EmailSender(String fromEmail, String appPassword) {
        this.fromEmail = fromEmail;
        this.appPassword = appPassword;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        this.session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });
    }

    // Method to send plain text email to multiple recipients
    public void sendEmail(List<String> recipients, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));

            // Convert recipient list to InternetAddress array
            Address[] toAddresses = recipients.stream()
                    .map(r -> {
                        try {
                            return new InternetAddress(r);
                        } catch (AddressException e) {
                            throw new RuntimeException("Invalid email: " + r, e);
                        }
                    })
                    .toArray(Address[]::new);

            message.setRecipients(Message.RecipientType.TO, toAddresses);
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent to " + recipients.size() + " recipient(s).");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
