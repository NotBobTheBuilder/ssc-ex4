package ex3.gui;

import java.util.Properties;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JPanel;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import ex3.utils.Pushable;

public class MessageList  extends JScrollPane 
                          implements Pushable<String> {

  private final DefaultListModel  MSG_LIST_MODEL  = new DefaultListModel();
  private final JList             MSG_LIST        = new JList(MSG_LIST_MODEL);
  private final JPanel            PANEL_BACKING;

  public MessageList() {
    super(new JPanel());
    PANEL_BACKING = ((JPanel) this.getViewport().getView());
    PANEL_BACKING.setBackground(Color.white);
    PANEL_BACKING.add(MSG_LIST);

    MSG_LIST.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  public void push(String _subject) {
    MSG_LIST_MODEL.addElement(_subject);
  }
}
