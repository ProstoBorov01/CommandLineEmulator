package org.program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShellGUI extends JFrame {
    private final CommandLineEmulator emulator = new CommandLineEmulator();
    private final JTextArea outputArea;
    private final JTextField inputField;

    public ShellGUI() {
        setTitle("Simple Terminal Emulator");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        inputField = new JTextField(30);
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.WHITE);

        JButton sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                executeCommand(command);
                inputField.setText("");
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                executeCommand(command);
                inputField.setText("");
            }
        });
    }
    private void executeCommand(String command) {
        this.emulator.execute(command).run();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShellGUI().setVisible(true);
            }
        });
    }
}
