package gui;

import com.company.MessageReceiver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputField extends JTextField implements ActionListener {

    private MessageReceiver bus;
    public InputField(MessageReceiver out) {
        bus = out;
        addActionListener(this);
    }

    /*
    *   Remove text from text field
    * */
    public void clear() {
        setText("");
    }

    /*
    *   Send text to message receiver, then clear text, when enter is pressed
    * */
    @Override
    public void actionPerformed(ActionEvent e) {
        bus.onMessage(this.getText());
        clear();
    }
}
