package ex3.mx;

import java.util.Properties;

import java.io.IOException;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Part;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.sun.mail.imap.IMAPFolder;

import ex3.utils.Pushable;

public class IMAP {

  private final Properties CONFIG;  
  private final Session SESSION;
  private final Store STORE;

  public IMAP(Properties _p) throws NoSuchProviderException, MessagingException {
    CONFIG = _p;
    SESSION = Session.getDefaultInstance(_p);
    STORE = SESSION.getStore(_p.getProperty("mail.store.protocol"));

    STORE.connect(CONFIG.getProperty("mail.host"), 
                  CONFIG.getProperty("mail.user"),
                  CONFIG.getProperty("mail.password"));
  }

  public Properties getMessage(int _msgnum) throws MessagingException {
    return getMessage(_msgnum, "inbox");
  }

  public Properties getMessage(int _msgnum, String _folder) throws MessagingException {
    IMAPFolder folder = (IMAPFolder) STORE.getFolder(_folder);
    if (!folder.isOpen()) folder.open(Folder.READ_WRITE);

    Properties msg = getMessage(folder.getMessage(_msgnum));

    folder.close(true);
    return msg;
  }

  public Properties getMessage(Message _message) throws MessagingException {
    InternetAddress sender = (InternetAddress) _message.getFrom()[0];

    String name = (sender.getPersonal() != null) ? sender.getPersonal() 
                                                 : sender.getAddress().split("@")[0];

    String subject = (_message.getSubject() != null) ? _message.getSubject()
                                                     : "<NO SUBJECT>";

    String content;
    try {
      content = getText(_message);
    } catch (IOException e) {
      content = "<ERROR READING SUBJECT>";
    }

    Properties p = new Properties();
    p.setProperty("sender.name", name);
    p.setProperty("sender.email", sender.getAddress());
    p.setProperty("email.subject", subject);
    p.setProperty("email.contentType", _message.getContentType());
    p.setProperty("email.content", content);
    p.setProperty("email.id", Integer.toString(_message.getMessageNumber()));
    return p;
  }

  public void getMessages(Pushable<Properties> _to) throws MessagingException {
    getMessages(_to, "inbox", 20);
  }

  public void getMessages(Pushable<Properties> _to, String _folder, int _count) throws MessagingException {
    IMAPFolder folder = (IMAPFolder) STORE.getFolder(_folder);
    if (!folder.isOpen()) folder.open(Folder.READ_WRITE);

    Message[] messages = folder.getMessages(folder.getMessageCount() - _count, folder.getMessageCount());
    Properties[] msgs = new Properties[messages.length];
    for (int i = messages.length - 1; i > -1; i--)
      _to.push(getMessage(messages[i]));

    folder.close(true);
  }

  /**
   * This taken from JavaMail FAQs
   * http://www.oracle.com/technetwork/java/javamail/faq/index.html#mainbody
   */
  private String getText(Part p) throws
              MessagingException, IOException {
    boolean textIsHtml = false;
    if (p.isMimeType("text/*")) {
      String s = (String)p.getContent();
      textIsHtml = p.isMimeType("text/html");
      return s;
    }

    if (p.isMimeType("multipart/alternative")) {
      // prefer html text over plain text
      Multipart mp = (Multipart)p.getContent();
      String text = null;
      for (int i = 0; i < mp.getCount(); i++) {
          Part bp = mp.getBodyPart(i);
          if (bp.isMimeType("text/plain")) {
            if (text == null)
              text = getText(bp);
            continue;
          } else if (bp.isMimeType("text/html")) {
            String s = getText(bp);
            if (s != null)
              return s;
          } else {
            return getText(bp);
          }
      }
      return text;
    } else if (p.isMimeType("multipart/*")) {
      Multipart mp = (Multipart)p.getContent();
      for (int i = 0; i < mp.getCount(); i++) {
        String s = getText(mp.getBodyPart(i));
        if (s != null)
          return s;
      }
    }

    return null;
  }

  public void close() {
    try {
      if (STORE != null) STORE.close();
    } catch (Exception e) {
      // Don't worry, be happy
    }
  }
}
