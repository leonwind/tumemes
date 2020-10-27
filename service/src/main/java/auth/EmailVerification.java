package auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailVerification {

  private static final Logger log = LoggerFactory.getLogger(EmailVerification.class);
  private static final String URL = "http://localhost:8080/api/verification/";

  private static final String FROM = "no-reply@tumemes.de";
  private static final String FROM_NAME = "no-reply";

  // send over server in Frankfurt, DE
  private static final String HOSTNAME = "email-smtp.eu-central-1.amazonaws.com";
  private static final int PORT = 25;

  private static final String SUBJECT = "TUMemes Email Verification";

  public static void sendVerificationEmail(
      String to, String smtpUsername, String smtpPassword, String urlToken) throws Exception {

    String link = "<a href='" + URL + urlToken + "'>link</a>";
    String body = String.join(System.getProperty("line.separator"),
        "Please verify your TUMemes account by clicking on this " + link,
        "or by copy-pasting the following into your browser:\n",
        URL + urlToken);

    to = "leon.windheuser@gmail.com";
    // Create a Properties object to contain connection configuration information.
    Properties props = System.getProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.port", PORT);
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");

    // Create a Session object to represent a mail session with the specified properties.
    Session session = Session.getDefaultInstance(props);

    MimeMessage msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress(FROM, FROM_NAME));
    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
    msg.setSubject(SUBJECT);
    msg.setContent(body, "text/html");

    Transport transport = session.getTransport();

    try {
      log.info("Sending email");
      // Connect to Amazon SES using the SMTP username and password you specified above.
      transport.connect(HOSTNAME, smtpUsername, smtpPassword);

      // Send the email.
      transport.sendMessage(msg, msg.getAllRecipients());
    } catch (Exception ex) {
      log.info("Error sending email");
      log.info("Error Message: " + ex.getMessage());
    } finally {
      // Close and terminate the connection.
      transport.close();
    }
  }
}
