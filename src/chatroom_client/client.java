package chatroom_client;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame {
    private static final int PORT = 8000;
    private PrintWriter writer;
    private BufferedReader reader;
    private JTextPane chatPane;
    private JTextField messageField;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;

    public Client() {
        setupUI();
        connectToServer();
    }

    private void setupUI() {
        setTitle("Chat Client");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatPane);

        messageField = new JTextField(30);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(this::sendMessage);

        JPanel inputPanel = new JPanel();
        inputPanel.add(messageField);
        inputPanel.add(sendButton);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane userListScrollPane = new JScrollPane(userList);
        userListScrollPane.setPreferredSize(new Dimension(150, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userListScrollPane, chatScrollPane);
        splitPane.setDividerLocation(150);

        add(splitPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String username;
            do {
                username = JOptionPane.showInputDialog(this, "Enter a username:", "Login", JOptionPane.PLAIN_MESSAGE);
                if (username == null || username.trim().isEmpty()) continue;
                writer.println(username);
            } while (!reader.readLine().equals("USERNAME_ACCEPTED"));
            System.out.println("Connected to server with username: " + username);

            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                if (message.startsWith("USER_LIST_UPDATE:")) {
                    updateUserList(message.substring(17));
                } else {
                    appendToChat(message);
                }
                System.out.println("Message received: " + message);
            }
        } catch (IOException e) {
            appendToChat("Connection lost.");
            System.out.println("Connection lost.");
        }
    }

    private void sendMessage(ActionEvent e) {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            writer.println(message);
            System.out.println("Message sent: " + message);
            messageField.setText("");
        }
    }

    private void appendToChat(String message) {
        try {
            StyledDocument doc = chatPane.getStyledDocument();
            doc.insertString(doc.getLength(), message + "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void updateUserList(String userListString) {
        userListModel.clear();
        for (String user : userListString.split(",")) {
            userListModel.addElement(user);
        }
        System.out.println("User list updated: " + userListString);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}