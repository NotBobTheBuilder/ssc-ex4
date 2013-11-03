package ex3.gui;

import java.util.Properties;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JPanel;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class MessageList extends JScrollPane {

  private final DefaultListModel  MSG_LIST_MODEL  = new DefaultListModel();
  private final JList             MSG_LIST        = new JList(MSG_LIST_MODEL);

  public MessageList() {
    super(new JPanel());
    ((JPanel) this.getViewport().getView()).add(MSG_LIST);
    MSG_LIST.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  public void push(String _subject) {
    MSG_LIST_MODEL.addElement(_subject);
  }
}
