package org.program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ShellGUI extends JFrame {
    private final CommandLineEmulator emulator = new CommandLineEmulator("C:\\Users\\SavvinPC\\Documents\\TAIGA.zip");
    private final JTextArea outputArea;
    private final JTextField inputField;

    public ShellGUI() throws IOException {
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
                String[] data = command.split(" ");
                if (data.length > 1){
                    executeCommand(data[0], data[1]);
                }
                else {
                    executeCommand(data[0], "");
                }
                inputField.setText("");
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                String[] data = command.split(" ");
                if (data.length == 2) executeCommand(data[0], data[1]);
                else executeCommand(data[0], "");
                inputField.setText("");
            }
        });
    }

    private void executeCommand(String command, String arg) {
        outputArea.append(
                "> " +
                this.emulator.execute("pwd", "") + " " + command
                        + " " + arg + " \n" + this.emulator.execute(command, arg)
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ShellGUI().setVisible(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
