package chatroom_server;

import java.io.*;
import java.net.*;
import java.util.*;

public class server {
    private static final int PORT = 8000;
    private static final Map<String, PrintWriter> clients = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        System.out.println("Server started on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection: " + clientSocket);
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                writer = new PrintWriter(socket.getOutputStream(), true);

                System.out.println("Waiting for username from " + socket);

                while (true) {
                    username = reader.readLine();
                    synchronized (clients) {
                        if (username == null || username.trim().isEmpty() || clients.containsKey(username)) {
                            writer.println("USERNAME_TAKEN");
                            System.out.println("Username rejected: " + username);
                        } else {
                            clients.put(username, writer);
                            writer.println("USERNAME_ACCEPTED");
                            System.out.println("Username accepted: " + username);
                            broadcast("* " + username + " joined the chat.");
                            updateUserList();
                            break;
                        }
                    }
                }
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Message received from " + username + ": " + message);
                    if (message.startsWith("(To:")) sendPrivateMessage(message);
                    else if (message.contains("@")) handleMentions(message);
                    else if (message.startsWith("(Broadcast)")) broadcast(username + ": " + message.substring(11).trim());
                    else broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                System.out.println("Connection lost with " + username + ".");
            } finally {
                disconnectUser();
            }
        }

        private void sendPrivateMessage(String message) {
            int endIndex = message.indexOf(")");
            if (endIndex > 4) {
                String recipient = message.substring(4, endIndex).trim();
                String privateMessage = message.substring(endIndex + 1).trim();
                synchronized (clients) {
                    PrintWriter recipientWriter = clients.get(recipient);
                    if (recipientWriter != null) {
                        recipientWriter.println("(Private) " + username + ": " + privateMessage);
                        System.out.println("Private message from " + username + " to " + recipient + ": " + privateMessage);
                    } else {
                        writer.println("Error: User " + recipient + " not found.");
                        System.out.println("Private message failed: User " + recipient + " not found.");
                    }
                }
            } else {
                writer.println("Error: Invalid private message format.");
                System.out.println("Private message error: Invalid format.");
            }
        }

        private void handleMentions(String message) {
            String[] words = message.split("\\s+");
            Set<String> mentionedUsers = new HashSet<>();
            for (String word : words) {
                if (word.startsWith("@") && word.length() > 1) mentionedUsers.add(word.substring(1));
            }
            mentionedUsers.forEach(user -> {
                synchronized (clients) {
                    PrintWriter mentionedWriter = clients.get(user);
                    if (mentionedWriter != null) {
                        mentionedWriter.println("(Mentioned by " + username + ") " + message);
                        System.out.println("User @" + user + " mentioned by " + username);
                    } else {
                        writer.println("Error: User @" + user + " not found.");
                        System.out.println("Mention failed: User @" + user + " not found.");
                    }
                }
            });
        }

        private void broadcast(String message) {
            System.out.println("Broadcast message: " + message);
            synchronized (clients) {
                for (PrintWriter clientWriter : clients.values()) {
                    clientWriter.println(message);
                }
            }
        }

        private void updateUserList() {
            String userList = String.join(",", clients.keySet());
            broadcast("USER_LIST_UPDATE:" + userList);
            System.out.println("User list updated: " + userList);
        }

        private void disconnectUser() {
            if (username != null) {
                synchronized (clients) {
                    clients.remove(username);
                }
                broadcast("* " + username + " left the chat.");
                updateUserList();
                System.out.println("User disconnected: " + username);
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}