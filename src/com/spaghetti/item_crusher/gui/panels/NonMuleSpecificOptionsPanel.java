package com.spaghetti.item_crusher.gui.panels;

import javax.swing.*;

public class NonMuleSpecificOptionsPanel {

    private JPanel contentPane;
    private JComboBox<String> muleAccountNameDropdown;
    private JTextArea itemNamesTextfield;

    public JPanel getContentPane() {
        return contentPane;
    }

    public JComboBox<String> getMuleAccountNameDropdown() {
        return muleAccountNameDropdown;
    }

    public JTextArea getItemNamesTextfield() {
        return itemNamesTextfield;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
