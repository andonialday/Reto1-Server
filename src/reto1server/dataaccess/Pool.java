/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto1server.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import reto1libraries.exception.*;

/**
 * Method that manages the amount of connections to the server 
 * @author Aitor Perez , Enaitz Izagirre
 */
 public class Pool {
    
    private static final Logger LOGGER = Logger.getLogger("package.class");
  
    private static ArrayList<Connection> cons = new ArrayList();   
    private final ResourceBundle configFile = ResourceBundle.getBundle("reto1server.dataaccess.config");
    private final String url = configFile.getString("URL");
    private final String user = configFile.getString("USER");
    private final String pass = configFile.getString("PASSWORD");
    private final Integer cant = Integer.valueOf(configFile.getString("CANT"));
    
    /**
     * Constructor that picks up from a configuration file
     * the number of connections it can handle. 
     */
    public Pool()  {
       
    }

    /**
     * Getter for the cons ArrayList that contains the Connections
     * @return the cons ArrayList
     */
    public ArrayList<Connection> getCons() {
        return cons;
    }
       
    /**
     * Method that collects the connection and checks if we have connections 
     * @return returns the connection con.
     */
    public Connection getConnection() {
        Connection con = null;
        if (cons.size() > 0) {
            con = cons.get(cons.size() - 1);
            cons.remove(cons.size() - 1);
        }
        return con;
    }

    /**
     *  Method that receives a connection and adds it to the ArrayList cons.
     * @param con Connection you receive
     */
    public void returnConnection(Connection con) {
        cons.add(con);
    }
    
    /**
     * Method to create connections up to the established limit (cant)
     * picking up the connection with Driver manager. 
     * @throws reto1libraries.exception.DBConnectionException Error processing feedback from the Database
     */
    public void createConnections() throws DBConnectionException{
        //LOGGER.info("Create Conection in Pool");
        for (int i = 0; i < cant; i++) {
            Connection con;
            try {
                con = DriverManager.getConnection(url, user, pass);
                cons.add(con);
            } catch (SQLException ex) {
                // extra???
               throw new DBConnectionException("Error processing feedback from the Database");
            }
            
        }
    }
    
    /**
     * Method to close the connections so as not to leave them open 
     * @throws reto1libraries.exception.DBConnectionException Error processing feedback from the Database
     */
    public static void closeConnections() throws DBConnectionException {
        LOGGER.info("Close Conexction in Pool");
        for (Connection con : cons) {
            try {
                con.close();
            } catch (SQLException ex) {
                 // extra???
               throw new DBConnectionException("Error processing feedback from the Database");
            }
        }
    }

}