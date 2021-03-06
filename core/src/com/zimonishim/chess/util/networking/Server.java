package com.zimonishim.chess.util.networking;

import com.zimonishim.chess.gameObjects.ChessField;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static ObjectOutputStream objectOutputStreamGameSocketOne;
    private static ObjectOutputStream objectOutputStreamGameSocketTwo;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Client.PORT);

            Socket gameSocketOne = serverSocket.accept();
            System.out.println("Accepted game socket1.");
            Socket gameSocketTwo = serverSocket.accept();
            System.out.println("Accepted game socket2.");

            objectOutputStreamGameSocketOne = new ObjectOutputStream(gameSocketOne.getOutputStream());
            ObjectInputStream objectInputStream1 = new ObjectInputStream(gameSocketOne.getInputStream());
            objectOutputStreamGameSocketTwo = new ObjectOutputStream(gameSocketTwo.getOutputStream());
            ObjectInputStream objectInputStream2 = new ObjectInputStream(gameSocketTwo.getInputStream());
            System.out.println("Initialised game input and output streams.");

            //Here we write the integers representing the player's color (white or black).
            objectOutputStreamGameSocketOne.writeInt(0);
            objectOutputStreamGameSocketOne.flush();
            objectOutputStreamGameSocketTwo.writeInt(1);
            objectOutputStreamGameSocketTwo.flush();

            //Create threads for relaying game data from the clients.
            new Thread(() -> {
                while (true) {
                    List<ChessField> chessFields;
                    //Read from socket 2.
                    try {
                        chessFields = (List<ChessField>) objectInputStream2.readObject();
                        System.out.println("Read input from socket 2.");
                    } catch (ClassNotFoundException | IOException classNotFoundException) {
                        //One of the sockets has been closed.
                        System.out.println("Connection with socket 2 broken.");
                        connectionBrokenWithSocketTwo();
                        break;
                    }

                    //Write to socket one.
                    try {
                        objectOutputStreamGameSocketOne.writeObject(chessFields);
                        System.out.println("Sent output to socket 1.");
                    } catch (IOException e) {
                        //One of the sockets has been closed.
                        System.out.println("Connection with socket 1 broken.");
                        connectionBrokenWithSocketOne();
                        break;
                    }
                }
            }).start();

            new Thread(() -> {
                while (true) {
                    List<ChessField> chessFields;

                    //Read from socket 1.
                    try {
                        chessFields = (List<ChessField>) objectInputStream1.readObject();
                        System.out.println("Read input from socket 1.");
                    } catch (ClassNotFoundException | IOException classNotFoundException) {
                        //One of the sockets has been closed.
                        System.out.println("Connection with socket 1 broken.");
                        connectionBrokenWithSocketOne();
                        break;
                    }

                    //Write to socket 2.
                    try {
                        objectOutputStreamGameSocketTwo.writeObject(chessFields);
                        System.out.println("Sent output to socket 2.");
                    } catch (IOException e) {
                        //One of the sockets has been closed.
                        System.out.println("Connection with socket 2 broken.");
                        connectionBrokenWithSocketTwo();
                        break;
                    }
                }
            }).start();

            Socket socketChat1 = serverSocket.accept();
            System.out.println("Accepted socketChat1.");
            Socket socketChat2 = serverSocket.accept();
            System.out.println("Accepted socketChat2.");

            DataOutputStream dataOutputStream1 = new DataOutputStream(socketChat1.getOutputStream());
            DataInputStream dataInputStream1 = new DataInputStream(socketChat1.getInputStream());
            DataOutputStream dataOutputStream2 = new DataOutputStream(socketChat2.getOutputStream());
            DataInputStream dataInputStream2 = new DataInputStream(socketChat2.getInputStream());
            System.out.println("Initialised input and output streams for UTF.");

            chatThread(dataOutputStream1, dataInputStream2).start();
            chatThread(dataOutputStream2, dataInputStream1).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Thread chatThread(DataOutputStream outputStream, DataInputStream inputStream) {
        return new Thread(() -> {
            while (true) {
                try {
                    outputStream.writeUTF(inputStream.readUTF());
                    System.out.println("Sent UTF from socket A to socket B.");
                } catch (IOException classNotFoundException) {
                    //Socket lost connection so stop trying to relay text.
                    System.out.println("Chat connection broken.");
                    break;
                }
            }
        });
    }

    /**
     * Is called when socket one loses connection, and makes socket two win.
     */
    private static synchronized void connectionBrokenWithSocketOne() {
        try {
            System.out.println("Trying to make socket 1 win.");
            objectOutputStreamGameSocketTwo.writeObject(new ArrayList<ChessField>());
            objectOutputStreamGameSocketTwo.flush();
            System.out.println("Successfully sent message to socket two to win.");
        } catch (IOException e) {
            System.out.println("Failed in sending message to socket two to win.");
        }
    }

    /**
     * Is called when socket two loses connection, and makes socket one win.
     */
    private static synchronized void connectionBrokenWithSocketTwo() {
        try {
            System.out.println("Trying to make socket 2 win.");
            objectOutputStreamGameSocketOne.writeObject(new ArrayList<ChessField>());
            objectOutputStreamGameSocketOne.flush();
            System.out.println("Successfully sent message to socket one to win.");
        } catch (IOException e) {
            System.out.println("Failed in sending message to socket one to win.");
        }
    }
}