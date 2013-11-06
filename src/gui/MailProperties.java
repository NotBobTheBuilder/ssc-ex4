package ex3.gui;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import java.awt.Container;

/** Configuration dialog for account settings.
 *  Allows user to set up config once and forget about it.
 *  Stores data in an XML file (specified by {@link EmailClient})
 *
 *  Currently falls back to hard coded settings (GUI not a Priority)
 */
public class MailProperties extends JFrame {

  /** Store config variables for accounts
   */
  public MailProperties(File _propFile) {
    Properties _p = new Properties();
    _p.setProperty("mail.user", "username");
    _p.setProperty("mail.password", "password");
    _p.setProperty("mail.store.protocol", "imaps");
    _p.setProperty("mail.host", "mail.bham.ac.uk");

    try {
      if (!(_propFile.isFile()))
        _propFile.createNewFile();

        _p.storeToXML(new FileOutputStream(_propFile), "", "utf-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
