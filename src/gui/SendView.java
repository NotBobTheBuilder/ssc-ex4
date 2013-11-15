package ex3.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class SendView extends JFrame {

  public SendView() {
    this("");
  }

  public SendView(String _to) {
    MessageView m  = new MessageView(true);

    m.setAddress(_to);
    m.setPreferredSize(new Dimension(500, 400));

    this.setTitle("New Message");
    this.getContentPane().add(m);
    this.pack();
    this.setVisible(true);
  }

}
