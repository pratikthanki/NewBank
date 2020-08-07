package newbank.server;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;

public class EmailHandler {

    /**
     * Utility method to send simple HTML email
     * @param out
     * @param toEmail
     * @param subject
     * @param body
     */

    public static void sendEmail(PrintWriter out, String toEmail, String subject, String body){
        String smtpHostServer = "smtp-mail.outlook.com";
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpHostServer);
        props.put("mail.smtp.starttls.enable","true");

        Session session = Session.getInstance(props, null);

            try
            {
                final String fromEmail = "sa2688@bath.ac.uk"; //requires valid gmail id
                MimeMessage msg = new MimeMessage(session);
                //set message headers
                msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
                msg.addHeader("format", "flowed");
                msg.addHeader("Content-Transfer-Encoding", "8bit");

                msg.setFrom(new InternetAddress(fromEmail, "NoReply-JD"));

                msg.setReplyTo(InternetAddress.parse(fromEmail, false));

                msg.setSubject(subject, "UTF-8");

                msg.setText(body, "UTF-8");

                msg.setSentDate(new Date());

                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
                out.println("Message is ready");
                Transport.send(msg);
                out.println("EMail Sent Successfully!!");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }
}