/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto1server.logic;

import reto1libraries.logic.Signable;
import reto1server.dataaccess.DaoSignableImplementation;

/**
 * This class creates the factory for the dao
 * @author Enaitz Izagirre
 */
public class DaoSignableFactory {
    
    /**
     * This method is used for the creation of the Dao 
     * @return It returns a dato from the Dao Implementation
     */
    public static Signable getDAO (){ 

        Signable dao = new DaoSignableImplementation();
        
        return dao;
    }
}
