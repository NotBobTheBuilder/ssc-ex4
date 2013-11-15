package ex3.gui;

import java.lang.Runnable;

import java.util.Properties;

import java.awt.Dimension;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.KeyStroke;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import javax.mail.MessagingException;

import ex3.mx.IMAP;

public class MainWindow extends JFrame 
                        implements ActionListener,
                                   ListSelectionListener {

  private final Properties    CONFIG;
  private final IMAP          IMAP;
  private final JMenuBar      MENU        = new JMenuBar();
  private final JMenu         MENU_FILE   = new JMenu("File");
  private final JMenuItem     MENU_F_NEW  = new JMenuItem("New Message", KeyEvent.VK_N);
  private final MessageList   MSG_LIST    = new MessageList();
  private final MessageView   MSG_PANE    = new MessageView();
  private final JSplitPane    SPLIT_PANE  = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                                           MSG_LIST, MSG_PANE);
 
  public MainWindow(Properties _p) {
    super();
    this.setTitle("Email Client");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    CONFIG = _p;

    IMAP i;
    try {
      i = new IMAP(_p);
    } catch (Exception e) {
      i = null;
    }
    IMAP = i;

    SPLIT_PANE.setDividerLocation(300);
    SPLIT_PANE.setContinuousLayout(true);

    Dimension minimum = new Dimension(300, 500);

    MSG_LIST.setMinimumSize(minimum);
    MSG_PANE.setMinimumSize(minimum);

    SPLIT_PANE.setPreferredSize(new Dimension(800, 500));

    MENU_F_NEW.addActionListener(this);
    MENU_F_NEW.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_N, ActionEvent.CTRL_MASK));

    MENU_FILE.add(MENU_F_NEW);
    MENU.add(MENU_FILE);
    this.setJMenuBar(MENU);

    this.getContentPane().add(SPLIT_PANE);
    this.pack();
    this.setVisible(true);

    MSG_LIST.addListSelectionListener(this);
 
    IMAP.getMessages(MSG_LIST);
  }

  @Override
  public void valueChanged(final ListSelectionEvent e) {
    if (e.getValueIsAdjusting()) return;

    JList list = (JList) e.getSource();
    Properties msg = (Properties) list.getSelectedValue();

    MSG_PANE.setMessage(msg);
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    new SendView(CONFIG);
  }

}
