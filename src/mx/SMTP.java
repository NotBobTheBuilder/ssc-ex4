package ex3.mx;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

public class SMTP {

  private final Properties  CONFIG;
  private final Session     SESSION;
  private final Transport   TRANSPORT;

  public SMTP(Properties _p) throws NoSuchProviderException, MessagingException {
    CONFIG = _p;
    SESSION = Session.getDefaultInstance(CONFIG);
    TRANSPORT = SESSION.getTransport("smtp");
  }

  public void send(String _to, String _cc, String _bcc, String _subject,
                   String _file, String _message) throws MessagingException {

        MimeMessage message = new MimeMessage(SESSION);

        message.setFrom(new InternetAddress(CONFIG.getProperty("mail.address")));

        message.setRecipients(Message.RecipientType.TO, _to);
        if (_cc.length()  > 0) message.setRecipients(Message.RecipientType.CC, _cc);
        if (_bcc.length() > 0) message.setRecipients(Message.RecipientType.BCC, _bcc);

        message.setSubject(_subject);
        message.setText(_message);

        message.saveChanges();
        send(message);
  }

  public void send(MimeMessage _m) throws MessagingException {
    TRANSPORT.connect(CONFIG.getProperty("mail.host"),
                      CONFIG.getProperty("mail.user"),
                      CONFIG.getProperty("mail.password"));
    System.out.println("here");
    TRANSPORT.sendMessage(_m, _m.getAllRecipients());
  }
}
