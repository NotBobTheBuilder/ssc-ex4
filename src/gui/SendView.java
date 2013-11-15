package ex3.gui;

import java.util.Properties;

import java.awt.Dimension;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;

import ex3.mx.SMTP;

/**
 * SendView - JFrame wrapper for an editable message view
 */
public class SendView extends JFrame
                      implements ActionListener {

  private final Properties    CONFIG;
  private final MessageView   MSGVIEW     = new MessageView(true);
  private final JMenuBar      MENU        = new JMenuBar();
  private final JMenu         MENU_FILE   = new JMenu("File");
  private final JMenuItem     MENU_F_SEND = new JMenuItem("Send", KeyEvent.VK_S);

  /**
   * Instantiates a new send view by calling main constructor with empty recipient address.
   *
   * @param _config the _config
   */
  public SendView(Properties _config) {
    this("", _config);
  }

  /**
   * Instantiates a new send view.
   *
   * @param _to the message recipient
   * @param _config the email client config
   */
  public SendView(String _to, Properties _config) {
    CONFIG = _config;
    MSGVIEW.setAddress(_to);
    MSGVIEW.setPreferredSize(new Dimension(500, 400));

    MENU_F_SEND.addActionListener(this);
    //CTRL+S is a shortcut key for sending
    MENU_F_SEND.setAccelerator(KeyStroke.getKeyStroke(
      KeyEvent.VK_S, ActionEvent.CTRL_MASK));

    MENU_FILE.add(MENU_F_SEND);
    MENU.add(MENU_FILE);

    this.setJMenuBar(MENU);
    this.setTitle("New Message");
    this.getContentPane().add(MSGVIEW);
    this.pack();
    this.setVisible(true);
  }

  public void messageSent() {
    JOptionPane.showMessageDialog(this, "Message Sent!");
    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  @Override
  public void actionPerformed(ActionEvent e) {
	  // Send the message in a new thread
      new Runnable() {
        public void run() {
          try {
            new SMTP(CONFIG).send(
              MSGVIEW.getAddress(),
              MSGVIEW.getCC(),
              MSGVIEW.getBCC(),
              MSGVIEW.getSubject(),
              MSGVIEW.getFile(),
              MSGVIEW.getMessage()
            );
            messageSent();
          } catch (Exception _) {
            System.out.println("Error Sending Message");
            System.out.println(_);
          }
        }
      }.run();
  }

}
