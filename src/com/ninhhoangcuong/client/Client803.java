/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ninhhoangcuong.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ninhh
 */
public class Client803 {

    public static String Solve(String data) {
        boolean CapsLock = true;
        StringBuilder dataBuilder = new StringBuilder();
        data = data.replaceAll("\\s+", " ");
        for (int i = 0; i < data.length(); i++) {
            if (CapsLock == true) {
                dataBuilder.append(Character.toUpperCase(data.charAt(i)));
                CapsLock = false;
            } else {
                Character c1 = data.charAt(i);
                if (c1.equals(' ') == true) {
                    dataBuilder.append(data.charAt(i));
                    CapsLock = true;
                } else {
                    dataBuilder.append(Character.toLowerCase(data.charAt(i)));
                }
            }
        }
        return dataBuilder.toString();
    }

    public static void main(String[] args) {
        try {
            DatagramSocket socketClient = null;
            DatagramPacket sendPacket, receivePacket;
            int port = 1108;
            String hostName = "localhost";
            byte[] send, receive;
            String requestId = null, data = null, studentCode = "B16DCCN046", qCode = "803";
            //open
            socketClient = new DatagramSocket();
            //gui cho server
            String sendString = ";" + studentCode + ";" + qCode;
            send = new byte[1024];
            send = sendString.getBytes();
            sendPacket = new DatagramPacket(send, send.length, InetAddress.getByName(hostName), port);
            socketClient.send(sendPacket);
            System.out.println("Send to server : " + sendString);
            System.out.println("Send succesful.....");
            //nhan du lieu tu server
            receive = new byte[1024];
            receivePacket = new DatagramPacket(receive, receive.length);
            socketClient.receive(receivePacket);
            String receiveString = new String(receivePacket.getData()).trim();
            String[] result = receiveString.split(";");
            requestId = result[0];
            data = result[1];
            System.out.println("Receive from server : ");
            System.out.println("RequestId : "+requestId);
            System.out.println("data: "+data);
            //chuan hoa
            data = Solve(data.trim());
            //gui lai server
            send = new byte[1024];
            sendString = requestId + ";" + data;
            send = sendString.getBytes();
            sendPacket = new DatagramPacket(send, send.length, receivePacket.getSocketAddress());
            socketClient.send(sendPacket);
            System.out.println("Send to server : " + sendString);
            System.out.println("Send succesful.....");
            socketClient.close();
        } catch (SocketException ex) {
            Logger.getLogger(Client803.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client803.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client803.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
