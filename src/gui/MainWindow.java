package ex3.gui;

import java.lang.Runnable;

import java.util.Properties;

import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import javax.mail.MessagingException;

import ex3.mx.IMAP;

public class MainWindow extends JFrame 
                        implements ListSelectionListener {

  private final IMAP          IMAP;
  private final MessageList   MSG_LIST    = new MessageList();
  private final MessageView   MSG_PANE    = new MessageView();
  private final JSplitPane    SPLIT_PANE  = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                                           MSG_LIST, MSG_PANE);
 
  public MainWindow(Properties _p) {
    super();
    this.setTitle("Email Client");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

}
