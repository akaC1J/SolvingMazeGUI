package com;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws  ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        //������ ����������
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        java.awt.EventQueue.invokeLater(() -> new FieldGUI().setVisible(true));

    }
}
