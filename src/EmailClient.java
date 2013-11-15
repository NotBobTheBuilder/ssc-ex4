package ex3;

import java.util.Properties;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import javax.swing.UIManager;

import ex3.gui.MainWindow;
import ex3.gui.MailProperties;

/**
 * EmailClient.
 */
public class EmailClient {

  /**
   * Instantiates a new email client.
   * Checks user's preferences before starting a new window
   * @param args the args
   */
  public EmailClient(String[] args) {
    File propFile = new File(System.getProperty("user.home")
                             + "/.mailProps.xml");

    //First time account config
    while (!propFile.isFile())
      new MailProperties(propFile);
   
    //Load config
    Properties props = new Properties();
    try {
      props.loadFromXML(new FileInputStream(propFile));
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    new MainWindow(props);
  }

  /**
   * The main method - set look and feel before starting new object
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      // Carry on, it'll just look funny.
    }
    new EmailClient(args);
  }

}
