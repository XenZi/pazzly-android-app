package com.example.pazzly.domain.manager;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIOManager {
    private static SocketIOManager instance;
    private Socket socket;

    public SocketIOManager() {
    }

    public static SocketIOManager getInstance() {
        if (instance == null) {
            instance = new SocketIOManager();
        }
        return instance;
    }

    public void connect() {
        try {
            socket = IO.socket("http://192.168.0.12:3000");
            socket.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
