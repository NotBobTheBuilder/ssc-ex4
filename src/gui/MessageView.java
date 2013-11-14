package ex3.gui;

import java.util.Properties;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class MessageView extends JSplitPane {

  private final MetaPane      METAPANE;
  private final JTextPane     MSG_PANE    = new JTextPane();
  private final JScrollPane   MSG_VIEW    = new JScrollPane(MSG_PANE);

  public MessageView() {
    this(false);
  }

  public MessageView(boolean _editable) {
    super();
    
    METAPANE = new MetaPane((_editable) ? MetaPane.SENT : MetaPane.RECV);

    this.setOrientation(JSplitPane.VERTICAL_SPLIT);
    this.setLeftComponent(METAPANE);
    this.setRightComponent(MSG_VIEW);
    this.setDividerLocation((_editable) ? 130 : 100);

    MSG_PANE.setContentType((_editable) ? "text/plain" : "text/html");
    MSG_PANE.setEditable(_editable);
    MSG_PANE.getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);

    MSG_PANE.setMinimumSize(new Dimension(300, 500));
    MSG_PANE.setBackground(Color.white);
  }

  public void setMessage(Properties _msg) {
    MSG_PANE.setText(_msg.getProperty("email.content"));
    METAPANE.setMessage(_msg);
  }

  public void setAddress(String _address) {
    METAPANE.setAddress(_address);
  }

}
