package pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils.interfaces.EmailSenderLocal;

import javax.ejb.Stateless;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Klasa implementująca mechanizm wysyłania wiadomości email.
 *
 * @author Karolina Kowalczyk
 */
@Stateless
public class EmailSender implements EmailSenderLocal {

    private static final Properties prop = new Properties();

    public void sendRegistrationEmail(String recipientName, String recipientEmailAddress, String link) {
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("mail.properties")) {

            prop.load(input);

            String htmlText = prop.getProperty("mail.registration.text").replace("CONFIRMATION_LINK", link);
            String subject = prop.getProperty("mail.registration.subject");
            sendEmail(recipientName, recipientEmailAddress, subject, htmlText);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendChangedActivityEmail(String recipientName, String recipientEmailAddress, boolean active) {
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("mail.properties")) {

            prop.load(input);

            String htmlText;

            if(active){
                htmlText = prop.getProperty("mail.activity.text").replace("AKTUALNA_AKTYWNOSC", "AKTYWNE");
            }
            else{
                htmlText = prop.getProperty("mail.activity.text").replace("AKTUALNA_AKTYWNOSC", "ZABLOKOWANE");
            }
            String subject = prop.getProperty("mail.activity.subject");
            sendEmail(recipientName, recipientEmailAddress, subject, htmlText);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendModificationEmail(String recipientName, String recipientEmailAddress) {
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("mail.properties")) {

            prop.load(input);

            String htmlText = prop.getProperty("mail.info.modification.text");
            String subject = prop.getProperty("mail.info.modification.subject");
            sendEmail(recipientName, recipientEmailAddress, subject, htmlText);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendAddAccessLevelEmail(String recipientName, String recipientEmailAddress, String accessLevel) {
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("mail.properties")) {

            prop.load(input);

            String htmlText = prop.getProperty("mail.modification.add.access.level.text").replace("ACCESS_LEVEL", accessLevel);
            String subject = prop.getProperty("mail.info.modification.subject");
            sendEmail(recipientName, recipientEmailAddress, subject, htmlText);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendRemoveAccessLevelEmail(String recipientName, String recipientEmailAddress, String accessLevel) {
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("mail.properties")) {

            prop.load(input);

            String htmlText = prop.getProperty("mail.modification.remove.access.level.text").replace("ACCESS_LEVEL", accessLevel);
            String subject = prop.getProperty("mail.info.modification.subject");
            sendEmail(recipientName, recipientEmailAddress, subject, htmlText);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendRemovalEmail(String recipientName, String recipientEmailAddress) {
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("mail.properties")) {

            prop.load(input);

            String htmlText = prop.getProperty("mail.info.removal.text");
            String subject = prop.getProperty("mail.info.removal.subject");
            sendEmail(recipientName, recipientEmailAddress, subject, htmlText);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendAdminAuthenticationEmail(String firstName, String email, String clientAddress) {
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("mail.properties")) {

            prop.load(input);
            String date = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(new Date());
            String htmlText = prop.getProperty("mail.info.admin.auth.text")
                    .replace("IP_ADDRESS", clientAddress)
                    .replace("TIME", date);
            String subject = prop.getProperty("mail.info.admin.auth.subject");

            sendEmail(firstName, email, subject, htmlText);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metoda wysyłająca wiadomość email.
     *
     * @param recipientName         Imię odbiorcy wiadomości.
     * @param recipientEmailAddress Adres email odbiorcy wiadomości.
     * @param subject               Temat wiadomości.
     * @param text                  Treść wiadomości.
     */
    private void sendEmail(String recipientName, String recipientEmailAddress, String subject, String text) {
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("mail.properties")) {

            if (prop.getProperty("mail.send.flag").equals("false")) {   // Depending on profile dont send mails
                return;
            }

            prop.load(input);

            String username = prop.getProperty("mail.ferrytales.username");
            String password = prop.getProperty("mail.ferrytales.password");
            String emailAddress = prop.getProperty("mail.ferrytales.address");

            Email email = EmailBuilder.startingBlank()
                    .from("Ferrytales", emailAddress)
                    .to(recipientName, recipientEmailAddress)
                    .withSubject(subject)
                    .withHTMLText(text)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer("in-v3.mailjet.com", 587, username, password)
                    .withTransportStrategy(TransportStrategy.SMTP)
                    .buildMailer();

            mailer.sendMail(email);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
