package com.amdudda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by amdudda on 12/1/15.
 */
public class MainMenuGUI extends JFrame {
    private JPanel mainMenuRootPanel;
    private JButton browseInstrumentsButton;
    private JButton browseContactsButton;
    private JButton addNewInstrumentButton;
    private JButton addNewContactButton;
    private JButton quitButton;
    private JButton whatsOnExhibitButton;

    public MainMenuGUI() {
        setContentPane(mainMenuRootPanel);
        pack();
        setTitle("Browse MI Database");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        browseInstrumentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // feed in the relevant resultset
                BrowseDatabaseScreen browseWindow = new BrowseDatabaseScreen(Instrument.getBrowsingData());
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Database.closeConnStatement();
                System.exit(0);
            }
        });

        browseContactsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowseDatabaseScreen bds = new BrowseDatabaseScreen(Contact.getBrowsingData());
            }
        });

        addNewInstrumentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateInstrument addInst = new UpdateInstrument(null);
            }
        });

        addNewContactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateContact addContact = new UpdateContact(null);
            }
        });

        whatsOnExhibitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: a screen to pull up just what's on exhibit right now.
                //JOptionPane.showMessageDialog(mainMenuRootPanel,"This doesn't do anything yet.");
                WhatsOnExhibitScreen woe = new WhatsOnExhibitScreen(Exhibit.getBrowsingData());
            }
        });
    }
}
