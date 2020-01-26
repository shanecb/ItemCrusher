package com.spaghetti.item_crusher.gui;

import com.spaghetti.item_crusher.UIInteractionDelegate;
import com.spaghetti.item_crusher.entities.Profile;
import com.spaghetti.item_crusher.gui.panels.MuleSpecificOptionsPanel;
import com.spaghetti.item_crusher.gui.panels.NonMuleSpecificOptionsPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MuleTraderUI {

    /**
     * Provides data for UI and handles user interaction.
     */
    private UIInteractionDelegate uiInteractionDelegate;

    private JFrame frame;

    private JPanel mainContentPanel;
    private JButton startButton;
    private JRadioButton useAsMuleRadioButton;
    private JRadioButton doNotUseAsMuleRadioButton;
    private JPanel containerPanel;

    private MuleSpecificOptionsPanel muleOptionsPanel = new MuleSpecificOptionsPanel();
    private NonMuleSpecificOptionsPanel nonMuleOptionsPanel = new NonMuleSpecificOptionsPanel();

    public MuleTraderUI(String title, Profile profile, UIInteractionDelegate uiInteractionDelegate) {
        this.uiInteractionDelegate = uiInteractionDelegate;

        frame = new JFrame(title);

        // set up ui
        /// UI Components
        frame.setContentPane(mainContentPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        profile.getMuleList().forEach(mule -> nonMuleOptionsPanel.getMuleAccountNameDropdown().addItem(mule));

        String lastUsedMule = profile.getLastUsedMule();
        nonMuleOptionsPanel.getMuleAccountNameDropdown().setSelectedItem(lastUsedMule);

        String itemNames = String.join("\n", profile.getItemNames());
        nonMuleOptionsPanel.getItemNamesTextfield().setText(itemNames);

        containerPanel.add(muleOptionsPanel.getContentPane(), "mule");
        containerPanel.add(nonMuleOptionsPanel.getContentPane(), "nonMule");

        frame.pack();

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uiInteractionDelegate.handleStartButtonClicked();
            }
        });

        useAsMuleRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                uiInteractionDelegate.handleUseAsMuleToggleChanged(useAsMuleRadioButton.isSelected());
            }
        });

        doNotUseAsMuleRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                uiInteractionDelegate.handleUseAsMuleToggleChanged(useAsMuleRadioButton.isSelected());
            }
        });
    }

    public String getMuleName() {
        return (String) nonMuleOptionsPanel.getMuleAccountNameDropdown().getSelectedItem();
    }

    public String getItemsString() {
        return nonMuleOptionsPanel.getItemNamesTextfield().getText();
    }

    public void showMuleOptionsPanel() {
        ((CardLayout) containerPanel.getLayout()).show(containerPanel, "mule");
    }

    public void showNonMuleOptionsPanel() {
        ((CardLayout) containerPanel.getLayout()).show(containerPanel, "nonMule");
    }

    public void close() {
        frame.dispose();
    }

}
