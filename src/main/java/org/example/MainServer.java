package org.example;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServer {
    int count = 0;
    int port = 3124;
    ServerSocket ss;
    InetAddress ip;
    Point pt = new Point(-1, 0, 0);
    ArrayList<ServerForClient> allClient = new ArrayList<>();

    public void addCoord(Integer x, Integer y){
        pt.setCoord(x, y);
        for(ServerForClient serverForClient : allClient){
            serverForClient.sendCoord(x, y);
        }
    }

    public MainServer(){
        try {
            ip = InetAddress.getLocalHost();

            ServerSocket ss = new ServerSocket(port, 0, ip);
            System.out.println("Сервер запущен!");


            while (true) {
                Socket cs = ss.accept();
                count++;
                ServerForClient sc = new ServerForClient(count, cs, this, pt.getX(), pt.getY());
                allClient.add(sc);
            }


        } catch (IOException ex) {
            System.out.println("Не могу создать сервер");
        }
    }

    public static void main(String[] args) {
        new MainServer();
    }
}
