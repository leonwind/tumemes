package auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

  private static final Logger log = LoggerFactory.getLogger(EmailSender.class);

  private static final String DOMAIN = "http://localhost:3000";

  private static final String FROM = "no-reply@tumemes.de";
  private static final String FROM_NAME = "no-reply";

  // Send over server in Frankfurt, Germany
  private static final String HOSTNAME = "email-smtp.eu-central-1.amazonaws.com";
  // PORT needs to be a string
  // See https://stackoverflow.com/a/58749463
  private static final String PORT = "587";

  private static void sendEmail(
      String to, String smtpUsername, String smtpPassword, String subject, String body)
      throws Exception {

    // Create a Properties object to contain connection configuration information
    Properties props = System.getProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.port", PORT);
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");

    // Represent mail session
    Session session = Session.getInstance(props);

    MimeMessage msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress(FROM, FROM_NAME));
    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
    msg.setSubject(subject);
    msg.setContent(body, "text/html");

    Transport transport = session.getTransport();

    try {
      log.info("Sending email");
      // Connect to AWS SES using SMTP username and password
      transport.connect(HOSTNAME, smtpUsername, smtpPassword);

      transport.sendMessage(msg, msg.getAllRecipients());
    } catch (Exception ex) {
      log.info("Error sending email");
      log.info("Error Message: " + ex.getMessage());
    } finally {
      transport.close();
    }
  }

  public static void sendVerificationEmail(
      String to, String smtpUsername, String smtpPassword, String token) throws Exception {

    String subject = "TUMemes Email Verification";
    String url = "http://localhost:8080/api/account/verification/" + token;
    String link = "<a href='" + url + "'>link</a>";
    String body =
        String.join(
            System.getProperty("line.separator"),
            "Please verify your TUMemes account by clicking on this " + link,
            "or by copy-pasting the following into your browser:<br/> <br/>",
            url);

    to = "leon.windheuser@gmail.com";
    sendEmail(to, smtpUsername, smtpPassword, subject, body);
  }

  public static void sendPasswordResetEmail(
      String to, String smtpUsername, String smtpPassword, String token) throws Exception {

    String subject = "TUMemes Password reset";
    String url = DOMAIN + "/password_reset/" + token;
    String link = "<a href='" + url + "'>link</a>";
    String body =
        String.join(
            System.getProperty("line.separator"),
            "To reset your password please click on this " + link,
            "or copy-paste the following into your browser:<br/> <br/>",
            url,
            "<br/> <br/>",
            "If you do not have requested to reset your password, please ignore this email."
        );

    to = "leon.windheuser@gmail.com";
    System.out.println(smtpUsername);
    System.out.println(smtpPassword);
    sendEmail(to, smtpUsername, smtpPassword, subject, body);
  }
}
