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
 *
 * @author aitor
 */
public class CloseThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger("package.class");;
    
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
