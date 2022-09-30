package org.example;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.*;

public class ClientFrame extends JFrame{
    int port = 3124;
    int id;
    Point point = new Point(0, 0, 0);
    InetAddress ip;
    DataInputStream dis;
    DataOutputStream dos;
    Socket cs;
    Gson convert = new Gson();

    private JPanel mainPanel;
    private JTextArea textArea1;
    private JTextField xTextField;
    private JButton отправитьButton;
    private JButton подключисьButton;
    private JLabel label;
    private JLabel coord;
    private JPanel field;
    private JTextField yTextField;



    public ClientFrame() {
        setContentPane(mainPanel);
        setTitle("Client");
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                xTextField.setText(String.valueOf(e.getX()));
                yTextField.setText(String.valueOf(e.getY()));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });



        подключисьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ip = InetAddress.getLocalHost();
                    cs = new Socket(ip, port);
                    dos = new DataOutputStream(cs.getOutputStream());
                    Thread t = new Thread(
                            ()->{
                                try {
                                    dis = new DataInputStream(cs.getInputStream());
                                    String obj = dis.readUTF();
                                    Point pt;
                                    pt = convert.fromJson(obj, Point.class);
                                    id = pt.getId();
                                    label.setText("Есть подключение к серверу (" + id + ")");
                                    addCoord(pt);

                                    while(true){
                                        obj = dis.readUTF();

                                        pt = convert.fromJson(obj, Point.class);
                                        clearPoint(point);
                                        point = pt;
                                        drawPoint(point);
                                        String s = "Координаты : " + pt.x + ' ' + pt.y + '\n';
                                        coord.setText(s);
                                    }
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                    );
                    t.start();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                label.setText("Есть подключение к серверу");
            }
        });
        отправитьButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dos!=null){
                    clearPoint(point);
                    Point pt = new Point();
                    pt.setId(id);
                    int x =Integer.parseInt(xTextField.getText());
                    int y = Integer.parseInt(yTextField.getText());
                    pt.setCoord(x, y);
                    point = pt;
                    drawPoint(point);
                    String str = convert.toJson(pt);
                    try {
                        dos.writeUTF(str);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }


    void addCoord(Point pt){
        point = pt;
        drawPoint(point);
        String s = "Координаты : " + pt.x + ' ' + pt.y + '\n';
        coord.setText(s);
    }

    void clearPoint(Point p){
        getGraphics().setColor(Color.WHITE);
        getGraphics().fillOval(p.getX(), p.getY(), 5, 5);
    }

    void drawPoint(Point p){
        getGraphics().setColor(Color.BLACK);
        getGraphics().fillOval(p.getX(), p.getY(), 5, 5);
    }


    public static void main(String[] args) {
        ClientFrame myFrame = new ClientFrame();
    }
}
