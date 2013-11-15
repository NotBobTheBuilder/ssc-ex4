package ex3.gui;

import java.util.Properties;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * The Class MessageView.
 * parent for metadata and email content views
 */
public class MessageView extends JSplitPane {

  private final MetaPane      METAPANE;
  private final JTextPane     MSG_PANE    = new JTextPane();
  private final JScrollPane   MSG_VIEW    = new JScrollPane(MSG_PANE);

  /**
   * Instantiates a new message view.
   * MessageViews default to non-editable (meaning the reading state)
   */
  public MessageView() {
    this(false);
  }

  /**
   * Instantiates a new message view - the parent for both metadata and content views.
   *
   * @param _editable the _editable
   */
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

  /**
   * Sets the message state (Message content, recipient, subject etc).
   *
   * @param _msg the new message
   */
  public void setMessage(Properties _msg) {
    MSG_PANE.setText(_msg.getProperty("email.content"));
    METAPANE.setMessage(_msg);
  }

  /**
   * Sets the address.
   *
   * @param _address the new address
   */
  public void setAddress(String _address) {
    METAPANE.setAddress(_address);
  }

  /**
   * Gets the recipient's email address.
   *
   * @return the email address
   */
  public String getAddress() {
    return METAPANE.getAddress();
  }

  /**
   * Gets the cc.
   *
   * @return cc'd emails
   */
  public String getCC() {
    return METAPANE.getCC();
  }

  /**
   * Gets the bcc addresses;
   *
   * @return bcc'd emails
   */
  public String getBCC() {
    return METAPANE.getBCC();
  }

  /**
   * Gets the subject.
   *
   * @return the subject
   */
  public String getSubject() {
    return METAPANE.getSubject();
  }

  /**
   * Gets the path to attachments.
   *
   * @return the path to attachments
   */
  public String getFile() {
    return "";
  }

  /**
   * Gets the message content.
   *
   * @return message content
   */
  public String getMessage() {
    return MSG_PANE.getText();
  }

}
