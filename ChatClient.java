package ChatApplication;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient() {
        super("Client Chat");

        // Create and set up the panel
        JPanel panel = new JPanel(new BorderLayout());

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Label
        JLabel label = new JLabel("Client Box");
        panel.add(label, BorderLayout.NORTH);

        // Message field and send button
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");

        // Action listener for send button
        ActionListener sendListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(messageField.getText());
                messageField.setText("");
            }
        };
        messageField.addActionListener(sendListener);
        sendButton.addActionListener(sendListener);

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);

        // Menu
        setJMenuBar(createMenuBar());

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        new Thread(new Runnable() {
            public void run() {
                startClient();
            }
        }).start();
    }

    private void startClient() {
        try {
            socket = new Socket("localhost", 5000);
            chatArea.append("Connected to server...\n");

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                chatArea.append("Server: " + message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        chatArea.append("You: " + message + "\n");
        out.println(message);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        JMenuItem changeFontItem = new JMenuItem("Change Font");
        changeFontItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeFont();
            }
        });
        optionsMenu.add(changeFontItem);

        JMenuItem changeBackgroundItem = new JMenuItem("Change Background");
        changeBackgroundItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeBackground();
            }
        });
        optionsMenu.add(changeBackgroundItem);

        JMenuItem changeFontColorItem = new JMenuItem("Change Font Color");
        changeFontColorItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeFontColor();
            }
        });
        optionsMenu.add(changeFontColorItem);

        menuBar.add(optionsMenu);
        return menuBar;
    }

    private void changeFont() {
        Font currentFont = chatArea.getFont();
        Font newFont = new Font("Serif", Font.BOLD, 16);
        chatArea.setFont(newFont);
    }

    private void changeBackground() {
        chatArea.setBackground(Color.LIGHT_GRAY);
    }

    private void changeFontColor() {
        chatArea.setForeground(Color.BLUE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatClient();
            }
        });
    }
}