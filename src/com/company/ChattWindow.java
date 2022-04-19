package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ChattWindow extends JFrame {

    protected ChattView view;


    public ChattWindow(String title) {
        super(title);
        view = new ChattView();
        add(view);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        view.setBorder(new EmptyBorder( 5, 10, 5, 10));
        pack();
        setVisible(true);
    }


}
