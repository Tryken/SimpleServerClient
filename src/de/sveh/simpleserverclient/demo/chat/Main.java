package de.sveh.simpleserverclient.demo.chat;

import java.util.Scanner;

public class Main {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        if (new Scanner(System.in).nextLine().equals("s")) {

            ChatServer server = new ChatServer(25565, "", 32);
            server.start();
        } else {

            ChatClient client = new ChatClient("localhost", 25565);
            client.connect();
        }
    }
}
