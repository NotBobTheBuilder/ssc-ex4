package ex3.gui;

import java.util.Properties;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import ex3.utils.Pushable;

public class MessageList  extends JScrollPane 
                          implements Pushable<Properties> {

  private final DefaultListModel  MSG_LIST_MODEL  = new DefaultListModel();
  private final JList             MSG_LIST        = new JList(MSG_LIST_MODEL);
  private final JPanel            PANEL_BACKING;

  public MessageList() {
    super(new JPanel());
    PANEL_BACKING = ((JPanel) this.getViewport().getView());
    PANEL_BACKING.setBackground(Color.white);
    PANEL_BACKING.add(MSG_LIST);

    MSG_LIST.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    //This from: http://stackoverflow.com/questions/12478661/how-to-dd-an-object-to-the-jlist-and-show-member-of-the-object-on-the-list-inter
    MSG_LIST.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, 
                                                    Object value,
                                                    int index,
                                                    boolean isSelected,
                                                    boolean cellHasFocus) {
        Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        ((JLabel) renderer).setText(((Properties) value).getProperty("email.subject"));
        return renderer;
      }
    });
  }

  public void push(Properties _subject) {
    MSG_LIST_MODEL.addElement(_subject);
  }
}
