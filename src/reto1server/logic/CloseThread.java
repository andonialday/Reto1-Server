/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto1server.logic;

import java.util.Scanner;
import java.util.logging.Logger;
import reto1server.application.ServerApplication;

/**
 * Thread designed to close the server from an input ofthe console
 *
 * It runs on Server start, and when an input of "exit" happens on the console,
 * sends an order close the application andthe connections to the DB
 *
 * @author Andoni Alday
 */
public class CloseThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger("package.class");

    ;
    
    @Override
    public void run() {
        while (true) {

            Scanner sc = new Scanner(System.in);
            String cl = sc.next();
            if (cl.equalsIgnoreCase("exit")) {
                LOGGER.info("Server closing request received, closing the server");
                ServerApplication.finish();

            }
        }
    }

}
