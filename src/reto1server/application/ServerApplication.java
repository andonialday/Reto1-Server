/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto1server.application;

import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import reto1libraries.exception.ClientServerConnectionException;
import reto1server.logic.MyThread;

/**
 * Client Main class to Launch Server App
 *
 * @author Aitor Perez
 */
public class ServerApplication {

    private final static int CANT = Integer.valueOf(ResourceBundle.getBundle("reto1server.dataaccess.config").getString("CANT"));
    private final static int PORT = Integer.valueOf(ResourceBundle.getBundle("reto1server.dataaccess.config").getString("PORT"));
    private static final Logger LOGGER = Logger.getLogger("package.class");
    private static int i = 0;

    /**
     * Method to Launch Server
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClientServerConnectionException {

        ServerSocket server = null;

        try {

            server = new ServerSocket(PORT);
            Socket client = null;

            while (true) {

                if (i < CANT) {
                    client = server.accept();
                    MyThread hilo = new MyThread(client);
                    hilo.start();

                    i++;
                } else {
                    //LOGGER.info("Excesive connections detected");   
                    Logger.getLogger(ServerApplication.class.getName()).log(Level.SEVERE, null, "Excesive connections detected");
                }
            }
        } catch (IOException e) {
            throw new ClientServerConnectionException("Failed to connect to server");
        }
    }

    /**
     * Synchronized method to subtract a connection before it closes
     */
    public static synchronized void closeSc() {
        i--;
    }

}
