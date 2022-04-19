package gui;

import javax.swing.*;
import java.awt.*;

public class LabeledComponent extends JPanel {

    /*
    *   Encapsle the component in a panel with a label
    * */
    public LabeledComponent(String text, JComponent component) {
        setLayout(new GridLayout(1,2));
        add(new JLabel(text));
        add(component);
    }

}
