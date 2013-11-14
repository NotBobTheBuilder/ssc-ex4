package ex3.gui;

import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class MetaPane extends JPanel {

  public static final int   SENT = 1;
  public static final int   RECV = 2;

  private final JLabel        L_ADDRESS = new JLabel();
  private final JTextField    ADDRESS   = new JTextField();
  private final JLabel        L_CC      = new JLabel("CC:");
  private final JTextField    CC        = new JTextField();
  private final JLabel        L_BCC     = new JLabel("BCC:");
  private final JTextField    BCC       = new JTextField();
  private final JLabel        L_SUBJECT = new JLabel("Subject:");
  private final JTextField    SUBJECT   = new JTextField();
  private final SpringLayout  LAYOUT    = new SpringLayout();
  private final int           MODE;

  public MetaPane(int _mode) {
    this.setLayout(LAYOUT);
    MODE = _mode;

    if (MODE == MetaPane.RECV) {
      ADDRESS.setEditable(false);
      CC.setEditable(false);
      SUBJECT.setEditable(false);
    }
    
    L_ADDRESS.setText((MODE == MetaPane.RECV) ? "Sender:" : "To:");

    LAYOUT.putConstraint(SpringLayout.WEST,   ADDRESS,     80,   SpringLayout.WEST,  this);
    LAYOUT.putConstraint(SpringLayout.EAST,   ADDRESS,    -10,   SpringLayout.EAST,  this);
    LAYOUT.putConstraint(SpringLayout.EAST,   L_ADDRESS,  -10,   SpringLayout.WEST,  ADDRESS);
    LAYOUT.putConstraint(SpringLayout.NORTH,  L_ADDRESS,   4,    SpringLayout.NORTH, ADDRESS);
    this.add(L_ADDRESS);
    this.add(ADDRESS);

    LAYOUT.putConstraint(SpringLayout.NORTH,  CC,          5,    SpringLayout.SOUTH, ADDRESS);

    LAYOUT.putConstraint(SpringLayout.WEST,   CC,          80,   SpringLayout.WEST,  this);
    LAYOUT.putConstraint(SpringLayout.EAST,   CC,         -10,   SpringLayout.EAST,  this);
    LAYOUT.putConstraint(SpringLayout.EAST,   L_CC,       -10,   SpringLayout.WEST,  CC);
    LAYOUT.putConstraint(SpringLayout.NORTH,  L_CC,        4,    SpringLayout.NORTH, CC);
    this.add(L_CC);
    this.add(CC);
    
    if (MODE == MetaPane.SENT) {
      LAYOUT.putConstraint(SpringLayout.NORTH,  BCC,          5,    SpringLayout.SOUTH, CC);

      LAYOUT.putConstraint(SpringLayout.WEST,   BCC,          80,   SpringLayout.WEST,  this);
      LAYOUT.putConstraint(SpringLayout.EAST,   BCC,         -10,   SpringLayout.EAST,  this);
      LAYOUT.putConstraint(SpringLayout.EAST,   L_BCC,       -10,   SpringLayout.WEST,  BCC);
      LAYOUT.putConstraint(SpringLayout.NORTH,  L_BCC,        4,    SpringLayout.NORTH, BCC);
      this.add(L_BCC);
      this.add(BCC);
    
      LAYOUT.putConstraint(SpringLayout.NORTH,  SUBJECT,     5,    SpringLayout.SOUTH, BCC);
    } else {
      LAYOUT.putConstraint(SpringLayout.NORTH,  SUBJECT,     5,    SpringLayout.SOUTH, CC);
    }

    LAYOUT.putConstraint(SpringLayout.WEST,   SUBJECT,     80,   SpringLayout.WEST,  this);
    LAYOUT.putConstraint(SpringLayout.EAST,   SUBJECT,    -10,   SpringLayout.EAST,  this);
    LAYOUT.putConstraint(SpringLayout.EAST,   L_SUBJECT,  -10,   SpringLayout.WEST,  SUBJECT);
    LAYOUT.putConstraint(SpringLayout.NORTH,  L_SUBJECT,   4,    SpringLayout.NORTH, SUBJECT);
    this.add(L_SUBJECT);
    this.add(SUBJECT);
  }

  public void setMessage(Properties _msg) {
    String name = _msg.getProperty("sender.name", "");

    if (name.length() > 1) {
      name += " (" + _msg.getProperty("sender.email") + ")";
    } else {
      name = _msg.getProperty("sender.email");
    }

    ADDRESS.setText(name);
    SUBJECT.setText(_msg.getProperty("email.subject"));
  }

  public void setAddress(String _address) {
    if (MODE == MetaPane.SENT)
      ADDRESS.setText(_address);
  }

}
