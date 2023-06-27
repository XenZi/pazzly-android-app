package com.example.pazzly.config;

import android.util.Log;

import com.example.pazzly.BuildConfig;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketHandler {
    static Socket socket;

    public static void setSocket(){
        try {
            socket = IO.socket("http://192.168.1.70:3000");
        }catch (Exception e){
            Log.d("SOCKET ERROR", e.getMessage().toString());
        }
    }

    public static Socket getSocket(){
        return socket;
    }

}
