/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ninhhoangcuong.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ninhh
 */
public class Server803 {

    private static final String SPACE_STRING = "abcdefgh   ijklmnopqrst  uvw   xyzABCDE    FGHIJ   KLMNO   PQRSTUVW    XYZ";
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    private static final boolean SPACE = true;
    private static Random ran;

    //generaten string not include special charecters
    public static String RandomString() {
        ran = new Random();
        //size of stringBuilder
        int n = ran.nextInt(50);
        n = (n > 0) ? n : 10;
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            /* generate a random number between 
            0 to AlphaNumericString variable length */
            int index = (int) (ALPHABET.length() * Math.random());
            // add Character one by one in end of sb 
            sb.append(ALPHABET.charAt(index));
        }
        return sb.toString();
    }

    //generaten string include space
    public static String RandomString(boolean flag) {
        ran = new Random();
        //size of stringBuilder
        int n = ran.nextInt(50);
        n = (n > 0) ? n : 10;
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            /* generate a random number between 
            0 to AlphaNumericString variable length */
            int index = (int) (SPACE_STRING.length() * Math.random());
            // add Character one by one in end of sb 
            sb.append(SPACE_STRING.charAt(index));
        }
        return sb.toString();
    }

    public static boolean Check(String requestIdSV, String dataSV, String requestIdCL, String dataCL) {
        return requestIdSV.equals(requestIdCL) == true && dataSV.equals(dataCL) == true;
    }

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
            DatagramSocket socketServer = null;
            DatagramPacket sendPacket, receivePacket;
            int port = 1108;
            byte[] send, receive;
            String requestId, data;
            //open
            socketServer = new DatagramSocket(port);
            while (true) {
                //nhan du lieu tu client
                receive = new byte[1024];
                receivePacket = new DatagramPacket(receive, receive.length);
                socketServer.receive(receivePacket);
                String arg1 = new String(receivePacket.getData()).trim();
                //split
                String[] result = arg1.split(";");
                for (String i : result) {
                    System.out.println(i);
                }
                //gui du lieu cho client
                send = new byte[1024];
                requestId = RandomString();
                data = RandomString(SPACE);
                String sendString = requestId + "; " + data;
                send = sendString.getBytes();
                sendPacket = new DatagramPacket(send, send.length, receivePacket.getSocketAddress());
                socketServer.send(sendPacket);
                System.out.println("Send to client : " + sendString);
                //nhan lai tu phia client
                //nhan lai du lieu va so sanh ket qua
                receive = new byte[1024];
                receivePacket = new DatagramPacket(receive, receive.length);
                socketServer.receive(receivePacket);
                String recieveString = new String(receivePacket.getData()).trim();
                String[] result2 = recieveString.split(";");
                System.out.println("Receive from client : ");
                for (String string : result2) {
                    System.out.println(string);
                }
                System.out.println("Result : " + Check(requestId, Solve(data.trim()), result2[0], result2[1]));
            }
        } catch (SocketException ex) {
            Logger.getLogger(Server803.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server803.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
