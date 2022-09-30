package org.example;

import com.google.gson.Gson;
import com.sun.tools.javac.Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerForClient {
    int id;
    int x, y;
    Socket cs;
    DataInputStream dis;
    DataOutputStream dos;
    Gson convert = new Gson();
    MainServer ms;

    Thread t;

    public ServerForClient(int id, Socket cs, MainServer ms, Integer x, Integer y) throws  IOException{
        this.id = id;
        this.cs = cs;
        this.ms = ms;
        this.x = x;
        this.y = y;

        System.out.println("Подключился клиент " + id + '\n');

        try {
            dos = new DataOutputStream(cs.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        t = new Thread(
                ()->{
                    try {
                        dis = new DataInputStream(cs.getInputStream());
                        while(true){
                            String obj;
                            obj = dis.readUTF();

                            Point pt = convert.fromJson(obj, Point.class);
                            System.out.println("Получил координаты (x: " + pt.getX() + ", y: " + pt.getY() + " ) (" + pt.getId() + ")");
                            ms.addCoord(pt.getX(), pt.getY());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        t.start();
        sendID();
    }

    void sendID(){
        Point pt = new Point(id, x, y);

        String sendStr = convert.toJson(pt);
        try {
            dos.writeUTF(sendStr);
            System.out.println("Отправил ID " + id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void sendCoord(Integer x, Integer y){
        Point pt = new Point(id, x, y);
        String sendStr = convert.toJson(pt);
        try {
            dos.writeUTF(sendStr);
            System.out.println("Отправил координаты " + x + ' ' + y + " клиенту (" + id + ")");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
