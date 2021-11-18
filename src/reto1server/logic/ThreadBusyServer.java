/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto1server.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import reto1libraries.logic.Encapsulation;

/**
 * This Class Extends From Thread and Connect Server Socket with Client Socket.
 *
 * @author Jaime Sansebastian , Enaitz Izagirre
 */
public class ThreadBusyServer extends Thread {

    private final Socket clientSocket;
    private static final Logger LOGGER = Logger.getLogger("package.class");

    /**
     * Assign the client socket to the one of this class.
     *
     * @param client Grab the client socket
     */
    public ThreadBusyServer(Socket client) {
        this.clientSocket = client;
    }

    /**
     * Initialize a new Thread to attend the request. Depending on the method or
     * the state it works differently. When it finishes with the request, it
     * proceeds to close the socket. Finally the thread is closed.
     */
    @Override
    public void run() {

        LOGGER.info("Initiating new Thread for incoming request");

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Encapsulation enc = null;

        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

            LOGGER.info("Server Busy . . . Answering tothe client with connection error");
            enc = (Encapsulation) ois.readObject();
            oos.writeObject(enc);
        } catch (IOException ex) {
            Logger.getLogger(ThreadBusyServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ThreadBusyServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                LOGGER.info("Closing Socket Connection");
                oos.close();
                ois.close();
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadBusyServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        LOGGER.info("Finishing Thread");
        this.interrupt();
    }
}
