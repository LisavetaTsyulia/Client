package client;

import connection.ConnectionHandler;
import gui.GUIHandler;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();
        client.go();
    }

    public void go() {
        setGUI();
        setNetwork();
    }

    private void setNetwork() {
        new ConnectionHandler();
    }

    private void setGUI() {
        new GUIHandler();
    }
}
