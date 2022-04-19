package gui;

import com.company.MessageReceiver;

import javax.swing.*;
import java.awt.*;

public class Console extends JPanel implements MessageReceiver {

    protected JTextArea area;
    protected JScrollPane scrollPane;

    public Console() {
        area = new JTextArea(15, 50);
        area.setEditable(false);
        scrollPane = new JScrollPane(area);
        setLayout(new GridLayout(1,1));
        add(scrollPane);
    }

    /*
    *   Add message and scroll to bottom on message received
    * */
    public void onMessage(String str) {
        area.append(str+"\n");
        area.setCaretPosition(area.getDocument().getLength());
    }

    /*
    *   Clear console text
    * */
    public void clear() {
        area.setText("");
    }
}
