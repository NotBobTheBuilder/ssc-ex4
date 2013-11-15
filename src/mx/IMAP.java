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

/**
 * Wrapper for IMAP operations
 */
public class IMAP {

  private final Properties CONFIG;  
  private final Session SESSION;
  private final Store STORE;

  /**
   * Instantiates a new IMAP session.
   *
   * @param _p the _p
   * @throws NoSuchProviderException the no such provider exception
   * @throws MessagingException the messaging exception
   */
  public IMAP(Properties _p) throws NoSuchProviderException, MessagingException {
    CONFIG = _p;
    SESSION = Session.getDefaultInstance(_p);
    STORE = SESSION.getStore(_p.getProperty("mail.store.protocol"));

    STORE.connect(CONFIG.getProperty("mail.imap.host"), 
                  CONFIG.getProperty("mail.user"),
                  CONFIG.getProperty("mail.password"));
  }

  /**
   * Gets the message specified by _msgnum
   *
   * @param _msgnum the number of the message to fetch
   * @return the message (Properties repr)
   */
  public Properties getMessage(int _msgnum) {
    return getMessage(_msgnum, "inbox");
  }

  /**
   * Gets the message.
   *
   * @param _msgnum the _msgnum
   * @param _folder the _folder
   * @return the message
   */
  public Properties getMessage(int _msgnum, String _folder) {
    try {
      IMAPFolder folder = (IMAPFolder) STORE.getFolder(_folder);
      if (!folder.isOpen()) folder.open(Folder.READ_WRITE);

      Properties msg = getMessage(folder.getMessage(_msgnum));

      folder.close(true);
      return msg;
    } catch (MessagingException e) {
      return null;
    }
  }

  /**
   * Get a Properties representation of a message from a Message object.
   *
   * @param _message the _message
   * @return the message
   */
  public Properties getMessage(Message _message) {
    try {
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
    } catch (MessagingException e) {
      return null;
    }
  }

  /**
   * Gets 20 messages and pushes them asynchronously onto a pushable object.
   *
   * @param _to the _to
   * @return the messages
   */
  public void getMessages(Pushable<Properties> _to) {
    getMessages(_to, "inbox", 20);
  }

  /**
   * Fetches messages and pushes them asynchronously onto a pushable object.
   *
   * @param _to where to push the messages
   * @param _folder which IMAP folder to get messages from
   * @param _count the number of messages to fetch
   */
  public void getMessages(Pushable<Properties> _to, String _folder, int _count) {
    IMAPFolder folder;
    try {
      folder = (IMAPFolder) STORE.getFolder(_folder);
    } catch (MessagingException e) {
      folder = null;
    }
    try {
      if (!folder.isOpen()) folder.open(Folder.READ_WRITE);

      Message[] messages = folder.getMessages(folder.getMessageCount() - _count, folder.getMessageCount());
      Properties[] msgs = new Properties[messages.length];
      for (int i = messages.length - 1; i > -1; i--)
        _to.push(getMessage(messages[i]));
    } catch (MessagingException e) {
    
    } finally {
      try {
        if (folder != null) folder.close(true);
      } catch (MessagingException e) {

      }
    }
  }

  /**
   * This taken from JavaMail FAQs
   * http://www.oracle.com/technetwork/java/javamail/faq/index.html#mainbody
   *
   * @param p a message part
   * @return the text contained within the message
   * @throws MessagingException the messaging exception
   * @throws IOException Signals that an I/O exception has occurred.
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

  /**
   * Close.
   */
  public void close() {
    try {
      if (STORE != null) STORE.close();
    } catch (Exception e) {
      // Don't worry, be happy
    }
  }
}
