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
import reto1libraries.exception.ClientServerConnectionException;
import reto1libraries.exception.CredentialErrorException;
import reto1libraries.exception.DBConnectionException;
import reto1libraries.exception.LoginOnUseException;
import reto1libraries.logic.Encapsulation;
import reto1libraries.logic.Method;
import reto1libraries.logic.Signable;
import reto1libraries.logic.Status;
import reto1libraries.object.User;
import reto1server.application.ServerApplication;

/**
 * This Class Extends From Thread and Connect Server Socket with Client Socket
 * @author Jaime Sansebastian , Enaitz Izagirre
 */
public class MyThread extends Thread {

    private final Socket clientSocket;
    private Signable dao;
    private DaoSignableFactory factoria;
    private static final Logger LOGGER = Logger.getLogger("package.class");

    /**
     * Assign the client socket to the one of this class
     * @param client Grab the client socket
     */
    public MyThread(Socket client) {
        this.clientSocket = client;
    }

    /**
     * Initialize a new Thread to attend the request 
     * Depending on the method or the state it works differently
     * When it finishes with the request, it proceeds to close the socket
     * Finally the thread is closed
     */
    @Override
    public void run() {

        LOGGER.info("Initiating new Thread for incoming request");

        this.factoria = new DaoSignableFactory();
        this.dao = factoria.getDAO();
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Encapsulation enc = null;
        User user = null;

        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

            LOGGER.info("Reading incoming request");
            enc = (Encapsulation) ois.readObject();
            user = enc.getUser();

            if (enc.getMethod() == Method.SIGNIN) {
                LOGGER.info("Requesting SignIn");
                user = (User) dao.signIn(enc.getUser());
            } else {
                LOGGER.info("Requesting SignUp");
                user = (User) dao.signUp(enc.getUser());
            }
            if (user == null) {
                enc.setStatus(Status.FAIL);
            } else {
                enc.setStatus(Status.CORRECT);
            }
        } catch (ClassNotFoundException | IOException | ClientServerConnectionException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DBConnectionException | LoginOnUseException | CredentialErrorException ex) {
            enc.setStatus(Status.ERROR);
        }

        try {
            enc.setUser(user);
            oos.writeObject(enc);
            ServerApplication.closeSc();
        } catch (IOException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                LOGGER.info("Closing Socket Connection");
                oos.close();
                ois.close();
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        LOGGER.info("Finishing Thread");
        this.interrupt();
    }
}
